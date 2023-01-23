package com.example.pomeserver.domain.record.exception.emotion;

import com.example.pomeserver.global.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public class EmotionException extends ApplicationException {
    protected EmotionException(String errorCode, HttpStatus httpStatus, String message) {
        super(errorCode, httpStatus, message);
    }
}