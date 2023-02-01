package com.example.pomeserver.domain.record.entity.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmotionType {
    MY_FIRST,
    MY_SECOND,
    FRIEND;

    private boolean isWriter;

    public boolean isWriter() {
        return isWriter;
    }
}
