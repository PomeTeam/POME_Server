package com.example.pomeserver.domain.user.exception.excute;

import com.example.pomeserver.domain.user.exception.UserException;
import org.springframework.http.HttpStatus;

import static com.example.pomeserver.domain.user.exception.UserExceptionList.NOTFOUND_FOLLOW;

public class FollowNotFoundException extends UserException {
    public FollowNotFoundException() {
        super(NOTFOUND_FOLLOW.getCODE(), NOTFOUND_FOLLOW.getHttpStatus(), NOTFOUND_FOLLOW.getMESSAGE());
    }
}
