package com.extend.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransactionResponse {
    private List<Transaction> transactions;
}
