package com.example.pomeserver.domain.user.dto.request;

import com.example.pomeserver.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class UserSignUpRequest {
    private String nickname;
    private String phoneNum;
    @NotNull(message = "사진을 입력해주세요")
    @NotBlank(message ="사진을 입력해주세요")
    private String imageKey;
    //private MultipartFile image; //TODO 추후 추가 예정

    public User toEntity() {
        return User.builder()
            .userId(UUID.randomUUID().toString())
            .nickname(this.nickname)
            .phoneNum(this.phoneNum)
            .image(this.imageKey)
            .build();
    }

    @Builder
    public UserSignUpRequest(String nickname, String phoneNum, String imageKey) {
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.imageKey = imageKey;
    }
}
