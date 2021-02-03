package com.github.thiagolocatelli.retrieval.service;

import com.github.thiagolocatelli.retrieval.client.ClearingServiceClient;
import com.github.thiagolocatelli.retrieval.client.domain.Transfer;
import com.github.thiagolocatelli.retrieval.domain.PulsarTransfer;
import io.github.majusko.pulsar.producer.PulsarTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.github.thiagolocatelli.retrieval.config.AppConfig.TOPIC_NAME;

@Service
public class EventRetrievalService {

    private static final Logger logger = LoggerFactory.getLogger(EventRetrievalService.class);
    
    @Autowired
    private ClearingServiceClient clearingServiceClient;

    @Autowired
    private PulsarTemplate<PulsarTransfer> producer;

    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Scheduled(fixedDelayString = "1000")
    public void retrieveTransfers() {
        List<Transfer> transfers = clearingServiceClient.retrieveTransfers();
        for(Transfer transfer : transfers) {
            logger.info("Transfer retrieved: {}", transfer);
            producer.sendAsync(TOPIC_NAME, convertToPulsarTransfer(transfer)).thenAccept(messageId -> {
                logger.info("Message id {} received for transfer {}", messageId, transfer.getRequestId());
            });
        }
    }

    private PulsarTransfer convertToPulsarTransfer(Transfer transfer) {
        PulsarTransfer pulsarTransfer = new PulsarTransfer();
        pulsarTransfer.setRequestId(transfer.getRequestId());
        pulsarTransfer.setFromAccount(transfer.getFromAccount());
        pulsarTransfer.setToAccount(transfer.getToAccount());
        pulsarTransfer.setAmount(transfer.getAmount());
        pulsarTransfer.setStatus(transfer.getStatus());
        pulsarTransfer.setTimestamp(transfer.getTimestamp().format(formatter));
        return pulsarTransfer;
    }

}
