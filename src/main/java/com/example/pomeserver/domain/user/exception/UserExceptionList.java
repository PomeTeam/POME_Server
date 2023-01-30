package com.example.pomeserver.domain.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum UserExceptionList {
    ALREADY_USER_NICKNAME("U0001",HttpStatus.BAD_REQUEST, "해당 닉네임은 이미 사용중입니다."),
    ALREADY_USER_PHONENUM("U0002",HttpStatus.BAD_REQUEST, "해당 번호는 이미 사용중입니다."),
    USERID_NOT_FOUND("U0003",HttpStatus.NOT_FOUND, "해당하는 userId를 가진 사용자를 찾을 수 없습니다."),
    ALREADY_FOLLOW("U0004",HttpStatus.BAD_REQUEST,"이미 팔로우를 한 유저입니다."),
    NOTFOUND_FOLLOW("U0005",HttpStatus.NOT_FOUND,"해당 팔로우 정보를 찾을 수 없습니다.");

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
