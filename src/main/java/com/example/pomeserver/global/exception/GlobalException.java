package com.example.pomeserver.global.exception;

import org.springframework.http.HttpStatus;

public class GlobalException extends ApplicationException {
    protected GlobalException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
