package com.extend.controller;

import com.extend.domain.dto.ErrorResponse;
import com.extend.domain.dto.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {
    private static final String DEFAULT_ERROR_MESSAGE_FORMAT = "{} {}, error message: {}";

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ErrorResponse> exceptionUnauthorized(HttpServletRequest req, HttpMethod method, HttpClientErrorException.Unauthorized ex) {
        log.error(DEFAULT_ERROR_MESSAGE_FORMAT, method, req.getRequestURI(), ex.getMessage());
        if (req.getRequestURI().contains("login")) {
            return new ResponseEntity<>(new ErrorResponse("invalid login request"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(new ErrorResponse("Unable to authorized the request"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorResponse> exceptionNotFound(HttpServletRequest req, HttpMethod method, HttpClientErrorException.NotFound ex) {
        log.error(DEFAULT_ERROR_MESSAGE_FORMAT, method, req.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse("Unable to found result for given request"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> exceptionBadRequest(HttpServletRequest req, HttpMethod method, MethodArgumentNotValidException ex) {
        log.error(DEFAULT_ERROR_MESSAGE_FORMAT, method, req.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(new ValidationErrorResponse(ex.getBindingResult()), HttpStatus.BAD_REQUEST);
    }
}
