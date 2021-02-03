package com.github.thiagolocatelli.processor.client;

import com.github.thiagolocatelli.processor.client.domain.Transfer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("transfer-service")
public interface TransferServiceClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/api/v1/transfer", consumes = "application/json")
    Transfer updateTransfer(Transfer transfer);

}
