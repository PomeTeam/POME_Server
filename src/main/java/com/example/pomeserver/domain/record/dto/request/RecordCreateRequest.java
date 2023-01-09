package com.example.pomeserver.domain.record.dto.request;

import com.example.pomeserver.domain.record.entity.Emotion;
import lombok.Data;


@Data
public class RecordCreateRequest {
    private Long goalId;
    private Long emotionId;
    private Integer usePrice;
    private String useDate;   //"2023-01-01"
    private String useComment;
}

