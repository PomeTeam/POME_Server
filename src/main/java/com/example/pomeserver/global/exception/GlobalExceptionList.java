package com.example.pomeserver.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum GlobalExceptionList {
    TOKEN_NOT_VALID("G0001", HttpStatus.NOT_ACCEPTABLE, "해당 토큰은 유효하지 않습니다.");

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
