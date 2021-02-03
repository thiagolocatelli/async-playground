package com.github.thiagolocatelli.transfer.client;

import com.github.thiagolocatelli.transfer.model.Transfer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("clearing-service")
public interface ClearingServiceClient {

    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/transfer", consumes = "application/json")
    Transfer submitTransfer(Transfer transfer);

}
