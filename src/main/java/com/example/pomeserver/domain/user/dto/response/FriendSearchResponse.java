package com.example.pomeserver.domain.user.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FriendSearchResponse {

    private String friendNickName;
    private String imageKey;

    @Builder
    public FriendSearchResponse(String friendNickname, String imageKey) {
        this.friendNickName = friendNickname;
        this.imageKey = imageKey;
    }
}
