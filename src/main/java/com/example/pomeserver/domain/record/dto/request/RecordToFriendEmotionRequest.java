package com.example.pomeserver.domain.record.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel("기록에 친구가 감정 남기기 요청 객체")
@NoArgsConstructor
@Data
public class RecordToFriendEmotionRequest {

    @ApiModelProperty(value = "감정 ID", example = "2", required = true, dataType = "number")
    @NotNull(message = "감정 ID는 필수값입니다.")
    private Long emotionId;
}

