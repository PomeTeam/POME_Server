package com.example.pomeserver.domain.user.DTO.request;

import com.example.pomeserver.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
@JsonInclude(Include.NON_NULL)
public class UserSignUpRequest {
    private String nickname;
    private String phoneNum;
    //private MultipartFile image; //TODO 추후 추가 예정

    public User toEntity() {
        return User.builder()
            .userId(UUID.randomUUID().toString())
            .nickname(this.nickname)
            .phoneNum(this.phoneNum).build();
    }
}
