package com.example.pomeserver.domain.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String userId;
    private String password;
    private String nickname;
    private String phoneNum;
    private String image;

    @Builder
    public User(String userId, String password, String nickname, String phoneNum, String image) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.image = image;
    }
}
