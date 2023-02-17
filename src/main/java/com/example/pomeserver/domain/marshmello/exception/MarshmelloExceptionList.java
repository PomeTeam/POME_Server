package com.example.pomeserver.domain.marshmello.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum MarshmelloExceptionList {
    MARSHMELLO_NOT_FOUND("M0001", HttpStatus.NOT_FOUND, "해당 user의 marshmello를 찾을 수 없습니다.");

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
