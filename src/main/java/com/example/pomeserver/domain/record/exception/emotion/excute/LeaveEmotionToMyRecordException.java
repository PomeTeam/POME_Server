package com.example.pomeserver.domain.record.exception.emotion.excute;

import com.example.pomeserver.domain.record.exception.emotion.EmotionException;
import com.example.pomeserver.domain.record.exception.emotion.EmotionExceptionList;

public class LeaveEmotionToMyRecordException extends EmotionException {
    public LeaveEmotionToMyRecordException() {
        super(
                EmotionExceptionList.EMOTION_NOT_FOUND.CODE,
                EmotionExceptionList.EMOTION_NOT_FOUND.httpStatus,
                EmotionExceptionList.EMOTION_NOT_FOUND.MESSAGE
        );
    }
}