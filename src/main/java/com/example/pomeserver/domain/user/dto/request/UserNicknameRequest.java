package com.example.pomeserver.domain.user.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserNicknameRequest {

    @NotNull(message = "닉네임을 입력해주세요")
    @NotBlank(message ="닉네임을 입력해주세요")
    @Pattern(regexp = "^[a-z가-힣A-Z]{1,10}$", message = "닉네임은 1~10글자의 영문, 한글만 입력할 수 입력할 수 있어요.")
    private String nickName;

}
