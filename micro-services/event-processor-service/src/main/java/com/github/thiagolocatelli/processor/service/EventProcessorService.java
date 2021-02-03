package com.github.thiagolocatelli.processor.service;

import com.github.thiagolocatelli.processor.client.TransferServiceClient;
import com.github.thiagolocatelli.processor.client.domain.Transfer;
import com.github.thiagolocatelli.processor.domain.PulsarTransfer;
import io.github.majusko.pulsar.PulsarMessage;
import io.github.majusko.pulsar.annotation.PulsarConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EventProcessorService {

    private static final Logger logger = LoggerFactory.getLogger(EventProcessorService.class);

    static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    @Autowired
    private TransferServiceClient transferServiceClient;

    @PulsarConsumer(topic = "clearing-events", clazz = PulsarTransfer.class)
    public void consume(PulsarMessage<PulsarTransfer> pulsarTransfer) {
        Transfer transfer = convertToTransfer(pulsarTransfer.getValue());
        logger.info("Transfer retrieved: {}", transfer);
        transferServiceClient.updateTransfer(transfer);
    }

    private Transfer convertToTransfer(PulsarTransfer pulsarTransfer) {
        Transfer transfer = new Transfer();
        transfer.setRequestId(pulsarTransfer.getRequestId());
        transfer.setFromAccount(pulsarTransfer.getFromAccount());
        transfer.setToAccount(pulsarTransfer.getToAccount());
        transfer.setAmount(pulsarTransfer.getAmount());
        transfer.setStatus(pulsarTransfer.getStatus());
        transfer.setTimestamp(LocalDateTime.parse(pulsarTransfer.getTimestamp(), formatter));
        return transfer;
    }

}
