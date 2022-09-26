package com.extend.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidationErrorResponse extends ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Getter
    private final List<ObjectError> errors;

    public ValidationErrorResponse(Errors errors) {
        super(String.format("%d validation failures on %s", errors.getAllErrors().size(), errors.getObjectName()));
        this.errors = errors.getAllErrors();
    }
}
