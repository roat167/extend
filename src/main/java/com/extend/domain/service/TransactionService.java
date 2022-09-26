package com.extend.domain.service;

import com.extend.domain.dto.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction getTransactionDetails(String accessToken, String transactionId);

    List<Transaction> getAllTransactions(String accessToken, String virtualCardId);
}
