package com.example.pomeserver.domain.user.exception.excute;

import com.example.pomeserver.domain.user.entity.vo.UserType;
import com.example.pomeserver.domain.user.exception.UserException;
import com.example.pomeserver.domain.user.exception.UserExceptionList;
import org.springframework.http.HttpStatus;

import static com.example.pomeserver.domain.user.exception.UserExceptionList.NOT_ALLOWED;

public class UserNotAllowedException extends UserException {
    public UserNotAllowedException() {
        super(NOT_ALLOWED.getCODE(), NOT_ALLOWED.getHttpStatus(), NOT_ALLOWED.getMESSAGE());
    }
}
