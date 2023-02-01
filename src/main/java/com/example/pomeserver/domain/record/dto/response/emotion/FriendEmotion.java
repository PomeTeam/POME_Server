package com.example.pomeserver.domain.record.dto.response.emotion;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@ApiModel("감정 응답 객체 (friend들의 감정으로 닉네임과 emotionId)")
@NoArgsConstructor
@Getter
public class FriendEmotion{
    @ApiModelProperty(value = "감정을 남긴 친구의 nickname", example = "커트코베z", required = true)
    private String nickname;
    @ApiModelProperty(value = "감정 id", example = "1", required = true)
    private Long emotionId;

    public static FriendEmotion toDto(String nickname, Long emotionId) {
        FriendEmotion friendEmotion = new FriendEmotion();
        friendEmotion.nickname = nickname;
        friendEmotion.emotionId = emotionId;
        return friendEmotion;
    }
}
