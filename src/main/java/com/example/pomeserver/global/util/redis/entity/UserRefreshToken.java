package com.example.pomeserver.global.util.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash(value = "UserRefreshToken", timeToLive = 2600000L)
@Getter
public class UserRefreshToken {

    @Id
    private String userId;
    private String refreshToken;
    private LocalDateTime createdAt;

    public UserRefreshToken(String userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.createdAt = LocalDateTime.now();
    }
}
