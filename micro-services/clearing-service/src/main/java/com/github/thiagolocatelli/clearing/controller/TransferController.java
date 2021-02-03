package com.github.thiagolocatelli.clearing.controller;

import com.github.thiagolocatelli.clearing.domain.transfer.Transfer;
import com.github.thiagolocatelli.clearing.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/transfer")
    public ResponseEntity<Transfer> save(@RequestBody Transfer transfer) {
        return ResponseEntity.ok(transferService.createTransfer(transfer));
    }

    @GetMapping("/transfers")
    public ResponseEntity<List<Transfer>> transfers() {
        return ResponseEntity.ok(transferService.retrieveTransfers());
    }

}
