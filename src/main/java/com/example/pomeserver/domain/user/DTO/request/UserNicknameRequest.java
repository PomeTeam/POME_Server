package com.example.pomeserver.domain.user.DTO.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserNicknameRequest {

    @NotNull(message = "닉네임을 입력해주세요")
    @NotBlank(message ="닉네임을 입력해주세요")
    @Pattern(regexp = "^[a-z0-9가-힣]{6,18}$", message = "닉네임은 6~18글자의 영소문자, 숫자, 한글만 가능합니다.")
    private String nickName;

}
