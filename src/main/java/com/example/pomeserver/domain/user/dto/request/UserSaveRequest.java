package com.example.pomeserver.domain.user.dto.request;

import com.example.pomeserver.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class UserSaveRequest {
    private String userId;
    private String password;
    private String nickname;
    private String phoneNum;
    //private MultipartFile image; //TODO 추후 추가 예정

    public void setEncPassword(String encPassword) {
        this.password = encPassword;
    }

    public User toEntity() {
        return User.builder()
                .userId(this.userId)
                .password(this.password)
                .nickname(this.nickname)
                .phoneNum(this.phoneNum).build();
    }
}
