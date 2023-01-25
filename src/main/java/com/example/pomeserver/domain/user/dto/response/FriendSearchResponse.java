package com.example.pomeserver.domain.user.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FriendSearchResponse {

    private String friendUserId;
    private String friendNickName;
    private String imageKey;

    @Builder
    public FriendSearchResponse(String friendUserId, String friendNickname, String imageKey) {
        this.friendUserId = friendUserId;
        this.friendNickName = friendNickname;
        this.imageKey = imageKey;
    }
}
