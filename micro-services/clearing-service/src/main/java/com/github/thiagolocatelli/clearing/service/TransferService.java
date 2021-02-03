package com.github.thiagolocatelli.clearing.service;

import com.github.thiagolocatelli.clearing.domain.transfer.Transfer;
import com.github.thiagolocatelli.clearing.domain.transfer.TransferStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
public class TransferService {

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    private Queue<Transfer> transfers = new ConcurrentLinkedQueue<>();

    public synchronized Transfer createTransfer(Transfer transfer) {
        synchronized (transfers) {
            logger.info("Transfer request received: {}", transfer);
            if("789".equals(transfer.getFromAccount())) {
                try {
                    Thread.sleep(5000);
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            transfer.setStatus("000".equals(transfer.getFromAccount()) ? TransferStatus.FAILED : TransferStatus.COMPLETED);
            transfer.setTimestamp(LocalDateTime.now());
            transfers.add(transfer);
        }
        return transfer;
    }

    public  List<Transfer> retrieveTransfers() {
        List<Transfer> transferList;
        synchronized (transfers) {
            transferList = transfers.stream().collect(Collectors.toList());
            transfers.clear();
        }
        return transferList;
    }

}
