package com.example.pomeserver.domain.user.exception.excute;

import com.example.pomeserver.domain.user.exception.UserException;
import com.example.pomeserver.domain.user.exception.UserExceptionList;

public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super(UserExceptionList.USERID_NOT_FOUND.getCODE(),UserExceptionList.USERID_NOT_FOUND.httpStatus,UserExceptionList.USERID_NOT_FOUND.getMESSAGE());
    }
}
