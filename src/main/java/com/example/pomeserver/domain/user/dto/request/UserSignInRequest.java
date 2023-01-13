package com.example.pomeserver.domain.user.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserSignInRequest {

    @NotNull(message = "전화번호를 입력해주세요")
    @NotBlank(message ="전화번호를 입력해주세요")
    private String phoneNum;


}
