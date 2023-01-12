package com.example.pomeserver.domain.user.DTO.response;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FriendSearchResponse {

    private String friendId;
    private String imageKey;

    @Builder
    public FriendSearchResponse(String friendId, String imageKey) {
        this.friendId = friendId;
        this.imageKey = imageKey;
    }
}
