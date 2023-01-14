package com.example.pomeserver.domain.user.DTO.response;

import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.global.util.jwtToken.TokenUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserResponse {

    private String userId;
    private String nickName;
    private String imageURL;
    private String accessToken;


    public static UserResponse toDto(User user, String AccessToken) {
        UserResponse userResponse = new UserResponse();
        userResponse.userId = user.getUserId();
        userResponse.nickName = user.getNickname();
        userResponse.imageURL = user.getImage(); //TO-DO
        userResponse.accessToken = AccessToken;
        return userResponse;
    }
}
