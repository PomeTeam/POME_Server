package com.example.pomeserver.domain.user.dto.response;

import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.global.util.aws.s3.ImageService;
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
        userResponse.imageURL = ImageService.getImageURL(user.getImage()); //TO-DO
        userResponse.accessToken = AccessToken;
        return userResponse;
    }
}
