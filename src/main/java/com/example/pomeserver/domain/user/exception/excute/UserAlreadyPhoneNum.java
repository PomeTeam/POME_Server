package com.example.pomeserver.domain.user.exception.excute;

import com.example.pomeserver.domain.user.exception.UserException;
import com.example.pomeserver.domain.user.exception.UserExceptionList;

public class UserAlreadyPhoneNum extends UserException {
    public UserAlreadyPhoneNum() {
        super(UserExceptionList.ALREADY_USER_PHONENUM.getCODE(),UserExceptionList.ALREADY_USER_PHONENUM.httpStatus,UserExceptionList.ALREADY_USER_PHONENUM.getMESSAGE());
    }
}
