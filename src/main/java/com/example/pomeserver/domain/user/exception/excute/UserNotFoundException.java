package com.example.pomeserver.domain.user.exception.excute;

import com.example.pomeserver.domain.user.exception.UserException;
import com.example.pomeserver.domain.user.exception.UserExceptionList;

public class UserNotFoundException extends UserException {
    public UserNotFoundException() {
        super(UserExceptionList.ALREADY_USER_NICKNAME.getCODE(),UserExceptionList.ALREADY_USER_NICKNAME.httpStatus,UserExceptionList.ALREADY_USER_NICKNAME.getMESSAGE());
    }
}
