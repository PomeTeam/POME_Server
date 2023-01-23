package com.example.pomeserver.domain.user.exception.excute;

import com.example.pomeserver.domain.user.exception.UserException;
import com.example.pomeserver.domain.user.exception.UserExceptionList;
import org.springframework.http.HttpStatus;

import static com.example.pomeserver.domain.user.exception.UserExceptionList.ALREADY_FOLLOW;

public class FollowAlreadyException extends UserException {
    public FollowAlreadyException() {
        super(ALREADY_FOLLOW.getCODE(), ALREADY_FOLLOW.getHttpStatus(), ALREADY_FOLLOW.getMESSAGE());
    }
}
