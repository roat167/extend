package com.extend.controller;

import com.extend.domain.dto.Transaction;
import com.extend.domain.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Tag(name = "Transaction")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/{transactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Transaction> getTransactionDetails(
            @RequestHeader @NotBlank String accessToken,
            @PathVariable String transactionId) {
        return new ResponseEntity<>(transactionService.getTransactionDetails(accessToken, transactionId), HttpStatus.OK);
    }
}
