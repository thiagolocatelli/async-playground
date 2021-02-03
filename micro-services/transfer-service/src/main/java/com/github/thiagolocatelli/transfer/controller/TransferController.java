package com.github.thiagolocatelli.transfer.controller;

import com.github.thiagolocatelli.transfer.model.Transfer;
import com.github.thiagolocatelli.transfer.transfer.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
public class TransferController {

    @Autowired
    private TransferService transferService;

    /**
     *
     * @param requestId
     * @return
     */
    @GetMapping("/transfer/{requestId}")
    public ResponseEntity<Transfer> retrieveTransfer(@PathVariable Long requestId) {
        return transferService.retrieveTransfer(requestId)
                .map( transfer -> ResponseEntity.ok().body(transfer) )          //200 OK
                .orElseGet( () -> ResponseEntity.notFound().build() );  //404 Not found
    }

    /**
     *
     * @param transfer
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PostMapping("/transfer")
    public ResponseEntity<Transfer> save(@RequestBody Transfer transfer) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(transferService.submitTransfer(transfer));
    }

    /**
     *
     * @param transfer
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @PutMapping("/transfer")
    public ResponseEntity<Transfer> update(@RequestBody Transfer transfer) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(transferService.updateTransfer(transfer));
    }

}
