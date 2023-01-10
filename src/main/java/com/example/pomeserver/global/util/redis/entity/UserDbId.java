package com.example.pomeserver.global.util.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash(value = "UserDbId", timeToLive = 30)
@Getter
public class UserDbId {
    @Id
    private String userId;
    private Long dbId;
    private LocalDateTime createdAt;

    public UserDbId(String userId, Long dbId) {
        this.userId = userId;
        this.dbId = dbId;
        this.createdAt = LocalDateTime.now();
    }
}
