package com.example.pomeserver.global.exception.excute;

import com.example.pomeserver.global.exception.GlobalException;
import com.example.pomeserver.global.exception.GlobalExceptionList;

public class TokenIsNotValidException extends GlobalException {
    public TokenIsNotValidException(){
        super(GlobalExceptionList.TOKEN_NOT_VALID.getCODE(),
                GlobalExceptionList.TOKEN_NOT_VALID.httpStatus,
                GlobalExceptionList.TOKEN_NOT_VALID.MESSAGE);
    }
}
