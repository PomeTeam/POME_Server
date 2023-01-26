package com.example.pomeserver.domain.record.dto.response.record;

import com.example.pomeserver.domain.record.dto.response.emotion.MyEmotionResponse;
import com.example.pomeserver.domain.record.entity.Record;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel("기록 응답 객체 (기록자 자신이 조회)")
@NoArgsConstructor
@Getter
public class MyRecordResponse {

    @ApiModelProperty(value = "기록 ID", example = "1", required = true, dataType = "number")
    private Long id;

    @ApiModelProperty(value = "기록자 닉네임", example = "커트코베Z", required = true, dataType = "string")
    private String nickname;

    @ApiModelProperty(value = "기록 소비 금액", example = "30000", required = true, dataType = "number")
    private int usePrice;

    @ApiModelProperty(value = "기록 작성 날짜", example = "2023.02.23", required = true, dataType = "string")
    private String useDate;

    @ApiModelProperty(value = "기록 코멘트", example = "이런저런 소비를 하였다(^.^)", required = true, dataType = "string")
    private String useComment;

    @ApiModelProperty(value = "목표 한줄 다짐", example = "하루에 커피는 1잔만", required = true, dataType = "string")
    private String oneLineMind;

    @ApiModelProperty(value = "감정 응답(나의 첫번째, 두번째, 친구들 감정 포함)",
            example = "{firstEmotion:, secondEmotion:, friendEmotions:[]}", required = true, dataType = "json")
    private MyEmotionResponse myEmotionResponse;


    public static MyRecordResponse toDto(Record record){
        MyRecordResponse response = new MyRecordResponse();
        response.id = record.getId();
        response.nickname = record.getUser().getNickname();
        response.usePrice = record.getUsePrice();
        response.useDate = record.getUseDate();
        response.useComment = record.getUseComment();
        response.oneLineMind = record.getGoal().getOneLineMind();
        response.myEmotionResponse = MyEmotionResponse.toDto(record);
        return response;
    }
}
