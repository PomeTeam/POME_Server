package com.example.pomeserver.domain.record.dto.paramResolver.param;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class RecordFilteringParam {
    private final Long firstEmotion;
    private final Long secondEmotion;

}
