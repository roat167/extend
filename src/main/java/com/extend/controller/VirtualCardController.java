package com.extend.controller;

import com.extend.domain.dto.Transaction;
import com.extend.domain.dto.VirtualCard;
import com.extend.domain.service.TransactionService;
import com.extend.domain.service.VirtualCardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Tag(name = "VirtualCards")
@RestController
@RequestMapping("/api/virtualcards")
@Slf4j
public class VirtualCardController {
    private final VirtualCardsService virtualCardsService;
    private final TransactionService transactionService;

    public VirtualCardController(VirtualCardsService virtualCardsService, TransactionService transactionService) {
        this.virtualCardsService = virtualCardsService;
        this.transactionService = transactionService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get all virtual cards based on login user")
    public ResponseEntity<List<VirtualCard>> getAllVirtualCards(@RequestHeader @NotBlank String accessToken) {
        log.info("Execute getAllVirtualCard");
        return new ResponseEntity<>(virtualCardsService.getVirtualCards(accessToken), HttpStatus.OK);
    }

    @GetMapping(value = "/{virtualCardId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get all virtual card' transactions for given virtual card ID")
    public ResponseEntity<List<Transaction>> getVirtualCardTransactions(
            @RequestHeader @NotBlank String accessToken,
            @PathVariable String virtualCardId) {
        return new ResponseEntity<>(transactionService.getAllTransactions(accessToken, virtualCardId), HttpStatus.OK);
    }
}
