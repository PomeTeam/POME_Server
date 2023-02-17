package com.example.pomeserver.domain.marshmello.exception.execute;

import com.example.pomeserver.domain.marshmello.exception.MarshmelloExceptionList;
import com.example.pomeserver.domain.record.exception.emotion.EmotionException;
import com.example.pomeserver.domain.record.exception.emotion.EmotionExceptionList;

public class MarshmelloNotFoundException extends EmotionException {
    public MarshmelloNotFoundException() {
        super(
                MarshmelloExceptionList.MARSHMELLO_NOT_FOUND.CODE,
                MarshmelloExceptionList.MARSHMELLO_NOT_FOUND.httpStatus,
                MarshmelloExceptionList.MARSHMELLO_NOT_FOUND.MESSAGE
        );
    }
}
