package com.example.pomeserver.domain.record.exception.record;

import com.example.pomeserver.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class RecordException extends ApplicationException {
    protected RecordException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}