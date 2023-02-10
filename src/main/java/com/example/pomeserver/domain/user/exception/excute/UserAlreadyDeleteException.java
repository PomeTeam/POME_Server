package com.example.pomeserver.domain.user.exception.excute;

import com.example.pomeserver.domain.user.exception.UserException;
import org.springframework.http.HttpStatus;

import static com.example.pomeserver.domain.user.exception.UserExceptionList.ALREADY_DELETE;

public class UserAlreadyDeleteException extends UserException {
    public UserAlreadyDeleteException() {
        super(ALREADY_DELETE.getCODE(), ALREADY_DELETE.getHttpStatus(), ALREADY_DELETE.getMESSAGE());
    }
}
