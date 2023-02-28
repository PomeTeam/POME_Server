package com.example.pomeserver.domain.record.exception.emotion;

import com.example.pomeserver.domain.record.exception.emotion.excute.LeaveEmotionToMyRecordException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum EmotionExceptionList {
    EMOTION_NOT_FOUND("E0001", HttpStatus.NOT_FOUND, "해당 id의 emotion을 찾을 수 없습니다."),
    LEAVE_EMOTION_TO_MT_RECORD("E0002", HttpStatus.BAD_REQUEST, "자신의 기록에 감정을 남길 수 없습니다.");

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
