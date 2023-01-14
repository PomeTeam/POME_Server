package com.example.pomeserver.domain.record.DTO.request;

import lombok.Data;


@Data
public class RecordUpdateRequest {
    private Long goalId;
    private Long emotionId;
    private Integer usePrice;
    private String useDate;   //"2023-01-01"
    private String useComment;
}
