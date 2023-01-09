package com.example.pomeserver.domain.user.exception;

import com.example.pomeserver.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserException extends ApplicationException {
    protected UserException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
