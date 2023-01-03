package com.example.pomeserver.domain.user.exception.excute;

import com.example.pomeserver.domain.user.exception.UserException;
import org.springframework.http.HttpStatus;

public class UserIdAlreadyExistException extends UserException {

    protected UserIdAlreadyExistException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}
