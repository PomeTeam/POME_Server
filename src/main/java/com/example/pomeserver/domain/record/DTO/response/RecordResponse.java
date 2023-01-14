package com.example.pomeserver.domain.record.DTO.response;

import com.example.pomeserver.domain.record.entity.Record;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RecordResponse{

    private Long id;
    private int usePrice;
    private String useDate; //2023-02-11
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
