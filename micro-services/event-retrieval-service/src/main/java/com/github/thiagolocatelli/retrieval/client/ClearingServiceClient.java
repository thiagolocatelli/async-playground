package com.github.thiagolocatelli.retrieval.client;

import com.github.thiagolocatelli.retrieval.client.domain.Transfer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("clearing-service")
public interface ClearingServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/transfers", consumes = "application/json")
    List<Transfer> retrieveTransfers();

}
