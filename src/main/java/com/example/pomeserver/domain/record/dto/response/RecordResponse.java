package com.example.pomeserver.domain.record.dto.response;

import com.example.pomeserver.domain.record.entity.Record;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel("기록 응답 객체")
@NoArgsConstructor
@Getter
public class RecordResponse{

    @ApiModelProperty(value = "기록 ID", example = "1", required = true, dataType = "number")
    private Long id;

    @ApiModelProperty(value = "기록 소비 금액", example = "30000", required = true, dataType = "number")
    private int usePrice;

    @ApiModelProperty(value = "기록 작성 날짜", example = "2023.02.23", required = true, dataType = "string")
    private String useDate;

    @ApiModelProperty(value = "기록 코멘트", example = "이런저런 소비를 하였다. ^.^", required = true, dataType = "string")
    private String useComment;


    public static RecordResponse toDto(Record record){
        RecordResponse response = new RecordResponse();
        response.id = record.getId();
        response.usePrice = record.getUsePrice();
        response.useDate = record.getUseDate();
        response.useComment = record.getUseComment();
        return response;
    }
}
