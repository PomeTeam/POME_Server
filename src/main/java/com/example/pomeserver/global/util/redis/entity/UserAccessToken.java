package com.example.pomeserver.global.util.redis.entity;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDateTime;

@RedisHash(value = "UserAccessToken", timeToLive = 30)
@Getter
public class UserAccessToken {
    @Id
    private Long id;
    private String accessToken;
    private LocalDateTime createdAt;

    public UserAccessToken(Long id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.createdAt = LocalDateTime.now();
    }
}
