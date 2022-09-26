package com.extend.domain.exception;

import lombok.Data;

@Data
public class CardTransactionNotFoundException extends Exception {
    private String errorMsg;

    public CardTransactionNotFoundException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }
}
