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
public class MyEmotionResponse {

    @ApiModelProperty(value = "첫번째 감정 id", example = "1", required = true)
    private Long firstEmotion;

    @ApiModelProperty(value = "두번째 감정 id", example = "2", required = true)
    private Long secondEmotion;

    public static MyEmotionResponse toDto(Record record) {
        List<EmotionRecord> emotionRecords = record.getEmotionRecords();
        MyEmotionResponse response = new MyEmotionResponse();
        if(emotionRecords.isEmpty()) return null;
        for (EmotionRecord er : emotionRecords) {
            if(er.getEmotionType().equals(EmotionType.MY_FIRST))
                response.firstEmotion = er.getEmotion().getId();
            else if(er.getEmotionType().equals(EmotionType.MY_SECOND))
                response.secondEmotion = er.getEmotion().getId();
        }
        return response;
    }
}
