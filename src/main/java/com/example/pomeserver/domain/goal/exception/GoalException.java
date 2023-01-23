package com.example.pomeserver.domain.goal.exception;

import com.example.pomeserver.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class GoalException extends ApplicationException {
    protected GoalException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
