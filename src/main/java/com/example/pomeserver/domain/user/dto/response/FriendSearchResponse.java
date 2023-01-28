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
    private boolean isFriend;



    @Builder
    public FriendSearchResponse(String friendUserId, String friendNickName, String imageKey, boolean isFriend) {
        this.friendUserId = friendUserId;
        this.friendNickName = friendNickName;
        this.imageKey = imageKey;
        this.isFriend = isFriend;
    }
}
