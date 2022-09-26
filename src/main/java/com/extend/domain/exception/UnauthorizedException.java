package com.extend.domain.exception;

import lombok.Data;

@Data
public class UnauthorizedException extends Exception {
    private String errorMsg;

    public UnauthorizedException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }
}
