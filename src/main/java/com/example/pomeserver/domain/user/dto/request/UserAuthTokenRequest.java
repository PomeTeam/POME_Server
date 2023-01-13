package com.example.pomeserver.domain.user.DTO.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserAuthTokenRequest {

    private String userId;
    private String userNickname;
    private String accessToken;

}
