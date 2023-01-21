package com.example.pomeserver.domain.record.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class RecordSecondEmotionRequest {

    @NotNull(message = "감정 ID는 필수값입니다.")
    private Long emotionId;
}

