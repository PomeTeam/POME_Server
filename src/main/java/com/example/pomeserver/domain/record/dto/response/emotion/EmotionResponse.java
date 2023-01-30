package com.example.pomeserver.domain.record.dto.response.emotion;

import com.example.pomeserver.domain.record.entity.EmotionRecord;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.record.entity.vo.EmotionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

//@ApiModel("기록의 감정 응답 객체")
@NoArgsConstructor
@Getter
public class EmotionResponse {

    @ApiModelProperty(value = "첫번째 감정 id", example = "1", required = true)
    private Long firstEmotion;

    @ApiModelProperty(value = "두번째 감정 id", example = "2", required = true)
    private Long secondEmotion;

    @ApiModelProperty(value = "내가 남긴 감정 id", example = "2", required = true)
    private Long myEmotion;

    @ApiModelProperty(value = "친구들의 감정 리스트", required = true)
    private List<FriendEmotion> friendEmotions = new ArrayList<>();


    public static EmotionResponse toDto(Record record, String viewerUserId) {
        List<EmotionRecord> emotionRecords = record.getEmotionRecords();
        EmotionResponse response = new EmotionResponse();
        if(emotionRecords.isEmpty()) return null;
        for (EmotionRecord er : emotionRecords){
            if(er.getUser().getUserId().equals(viewerUserId))
                response.myEmotion = er.getEmotion().getId();
            else if(er.getEmotionType().equals(EmotionType.MY_FIRST))
                response.firstEmotion = er.getEmotion().getId();
            else if(er.getEmotionType().equals(EmotionType.MY_SECOND))
                response.secondEmotion = er.getEmotion().getId();
            else response.friendEmotions.add(FriendEmotion.toDto(er.getUser().getNickname(), er.getEmotion().getId()));
        }
        return response;
    }
}
