package com.example.pomeserver.domain.marshmello.exception;

import com.example.pomeserver.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class MarshmelloException extends ApplicationException {
    protected MarshmelloException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}