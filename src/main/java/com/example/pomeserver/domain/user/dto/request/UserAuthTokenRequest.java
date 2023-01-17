package com.example.pomeserver.domain.user.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
public class UserAuthTokenRequest {

    @NotNull(message = "유저 ID를 입력해주세요")
    @NotBlank(message = "유저 ID를 입력해주세요")
    private String userId;

    @NotNull(message = "유저 닉네임을 입력해주세요")
    @NotBlank(message = "유저 닉네임을 입력해주세요")
    private String userNickname;


}
