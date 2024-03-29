package com.example.pomeserver.domain.record.dto.response.record;

import com.example.pomeserver.domain.record.dto.response.emotion.EmotionResponse;
import com.example.pomeserver.domain.record.entity.Record;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@ApiModel("기록 응답 객체 (친구들이 조회)")
@NoArgsConstructor
@Getter
public class RecordResponse{

    @ApiModelProperty(value = "기록 ID", example = "1", required = true)
    private Long id;

    @ApiModelProperty(value = "기록자 닉네임", example = "커트코베Z", required = true)
    private String nickname;

    @ApiModelProperty(value = "기록 소비 금액", example = "30000", required = true)
    private int usePrice;

    @ApiModelProperty(value = "기록 작성 날짜", example = "2023.02.23", required = true)
    private String useDate;

    @ApiModelProperty(value = "기록 코멘트", example = "이런저런 소비를 하였다(^.^)", required = true)
    private String useComment;

    @ApiModelProperty(value = "목표 한줄 다짐", example = "하루에 커피는 1잔만", required = true)
    private String oneLineMind;

    @ApiModelProperty(value = "기록 생성일", example = "2023-01-27 20:11:04.000000", required = true)
    private String createdAt;

    @ApiModelProperty(value = "감정 응답(나의 첫번째, 두번째, 친구들 감정 포함)",
            example = "{firstEmotion:, secondEmotion:, friendEmotions:[]}", required = true)
    private EmotionResponse emotionResponse;


    public static RecordResponse toDto(Record record, String viewerUserId){
        RecordResponse response = new RecordResponse();
        response.id = record.getId();
        response.nickname = record.getUser().getNickname();
        response.usePrice = record.getUsePrice();
        response.useDate = record.getUseDate();
        response.useComment = record.getUseComment();
        response.oneLineMind = record.getGoal().getOneLineMind();
        response.createdAt = record.getCreatedAt().toString();
        response.emotionResponse = EmotionResponse.toDto(record.getEmotionRecords(), viewerUserId);
        return response;
    }

    @Builder
    public RecordResponse(Long id,
                          String nickname,
                          int usePrice,
                          String useDate,
                          String useComment,
                          String oneLineMind,
                          String createdAt,
                          EmotionResponse emotionResponse)
    {
        this.id = id;
        this.nickname = nickname;
        this.usePrice = usePrice;
        this.useDate = useDate;
        this.useComment = useComment;
        this.oneLineMind = oneLineMind;
        this.createdAt = createdAt;
        this.emotionResponse = emotionResponse;
    }
}
