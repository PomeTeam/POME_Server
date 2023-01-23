package com.example.pomeserver.domain.record.exception.emotion;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum EmotionExceptionList {
    EMOTION_NOT_FOUND("U0001", HttpStatus.NOT_FOUND, "해당 id의 emotion을 찾을 수 없습니다.");

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
