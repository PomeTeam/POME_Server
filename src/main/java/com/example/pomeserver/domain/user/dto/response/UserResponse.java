package com.example.pomeserver.domain.user.DTO.response;

import com.example.pomeserver.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserResponse {
    private String userId;
    private String nickName;
    private String imageURL;


    public static UserResponse toDto(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.userId = user.getUserId();
        userResponse.nickName = user.getNickname();
        userResponse.imageURL = "test"; //TO-DO
        return userResponse;
    }
}
