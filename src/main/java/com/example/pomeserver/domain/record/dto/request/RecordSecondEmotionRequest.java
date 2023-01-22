package com.example.pomeserver.domain.record.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@ApiModel("기록에 2번째 감정 남기기 요청 객체")
@NoArgsConstructor
@Data
public class RecordSecondEmotionRequest {

    @ApiModelProperty(value = "감정 ID", example = "2", required = true, dataType = "number")
    @NotNull(message = "감정 ID는 필수값입니다.")
    private Long emotionId;
}

