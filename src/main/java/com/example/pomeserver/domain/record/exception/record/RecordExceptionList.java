package com.example.pomeserver.domain.record.exception.record;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum RecordExceptionList {
    RECORD_NOT_FOUND("U0001", HttpStatus.NOT_FOUND, "해당 id의 Record를 찾을 수 없습니다."),
    THIS_RECORD_NOT_BY_THIS_USER("U0002", HttpStatus.FORBIDDEN, "해당 기록은 api를 요청한 사용자의 기록이 아닙니다.");

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
