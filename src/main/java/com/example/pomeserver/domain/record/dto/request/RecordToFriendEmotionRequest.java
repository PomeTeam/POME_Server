package com.example.pomeserver.domain.record.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class RecordToFriendEmotionRequest {

    @NotNull(message = "감정 ID는 필수값입니다.")
    private Long emotionId;

    private String toUserId;
}

