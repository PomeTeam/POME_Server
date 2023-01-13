package com.example.pomeserver.global.exception.excute;

import com.example.pomeserver.global.exception.GlobalException;
import com.example.pomeserver.global.exception.GlobalExceptionList;
import org.springframework.http.HttpStatus;

import static com.example.pomeserver.global.exception.GlobalExceptionList.TOKEN_EXPIRATION;

public class TokenExpirationException extends GlobalException {
    public TokenExpirationException() {
        super(TOKEN_EXPIRATION.getCODE(), TOKEN_EXPIRATION.getHttpStatus(), TOKEN_EXPIRATION.getMESSAGE());
    }
}
