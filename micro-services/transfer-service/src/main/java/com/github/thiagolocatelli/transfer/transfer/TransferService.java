package com.github.thiagolocatelli.transfer.transfer;

import com.github.thiagolocatelli.transfer.client.ClearingServiceClient;
import com.github.thiagolocatelli.transfer.model.Transfer;
import com.github.thiagolocatelli.transfer.model.TransferStatus;
import com.github.thiagolocatelli.transfer.repository.TransferRepository;
import org.awaitility.core.ConditionTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.with;

@Service
public class TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    private ClearingServiceClient clearingServiceClient;

    @Autowired
    private TransferRepository transferRepository;

    /**
     *
     * @param transfer
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Transfer submitTransfer(Transfer transfer) throws ExecutionException, InterruptedException {

        //Save transfer request into database
        transfer.setRequestId(null);
        transfer.setStatus(TransferStatus.SUBMITTED);
        transfer.setTimestamp(LocalDateTime.now());
        CompletableFuture<Transfer> transferCompletableFuture = CompletableFuture.supplyAsync(() -> transferRepository.saveAndFlush(transfer));

        //Submit transfer request to clearing service in async mode
        Transfer savedTransfer = transferCompletableFuture.get();
        CompletableFuture.runAsync(() -> clearingServiceClient.submitTransfer(savedTransfer));

        //Wait for 4 seconds the event to be processed and status updated into the database
        //If 4 seconds passed, response will contain status SUBMITTED, otherwise
        //it will contain the status from clearing-service
        try {

            with().pollInterval(200, MILLISECONDS)
                    .and().with().pollDelay(1000, MILLISECONDS)
                    .await()
                    .atMost(4, SECONDS).until(() -> {
                        Optional<Transfer> dbTransfer = transferRepository.findById(transfer.getRequestId());
                        if(dbTransfer.get().getStatus() != transfer.getStatus()) {
                            logger.info("Status changed for transfer {}: from {} to {}", transfer.getRequestId(),
                                transfer.getStatus(), dbTransfer.get().getStatus());
                        }
                        return dbTransfer.get().getStatus() != TransferStatus.SUBMITTED;
                    });

            return retrieveTransfer(transfer.getRequestId()).get();

        }
        catch(ConditionTimeoutException e) {
            logger.warn("Event for transfer request id {} not receiving in 4 seconds", transfer.getRequestId());
        }

        return transfer;
    }

    public Optional<Transfer> retrieveTransfer(Long requestId) {
        return transferRepository.findById(requestId);
    }

    public Transfer updateTransfer(Transfer transfer) {
        logger.info("Updating transfer: {}", transfer);
        return transferRepository.saveAndFlush(transfer);
    }
}
