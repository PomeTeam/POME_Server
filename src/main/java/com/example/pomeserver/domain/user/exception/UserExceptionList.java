package com.example.pomeserver.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum UserExceptionList {
    ALREADY_USER_NICKNAME("U0001",HttpStatus.ALREADY_REPORTED, "해당 닉네임은 이미 사용중입니다."),
    ALREADY_USER_PHONENUM("U0002",HttpStatus.ALREADY_REPORTED, "해당 번호는 이미 사용중입니다.");

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
