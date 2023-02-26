package com.example.pomeserver.domain.record.dto.paramResolver.param;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class RecordFilteringParam {
    private final Long firstEmotion;
    private final Long secondEmotion;

    public boolean hasAllEmotion() {
        return (this.firstEmotion != null) && (this.secondEmotion != null);
    }

    public boolean hasFirstEmotion() {
        return this.firstEmotion != null;
    }

    public boolean hasSecondEmotion() {
        return this.secondEmotion != null;
    }

    public boolean isNull() {
        return (this.firstEmotion == null) && (this.secondEmotion == null);
    }
}
