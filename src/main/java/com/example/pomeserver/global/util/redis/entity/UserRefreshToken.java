package com.example.pomeserver.global.util.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.time.LocalDateTime;

@RedisHash(value = "UserRefreshToken", timeToLive = 30)
@Getter
public class UserRefreshToken {

    @Id
    private Long id;
    private String refreshToken;
    private LocalDateTime createdAt;

    public UserRefreshToken(Long id, String refreshToken) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.createdAt = LocalDateTime.now();
    }
}
