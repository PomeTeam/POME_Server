package com.example.pomeserver.global.util.redis.template;

import com.example.pomeserver.global.util.redis.entity.UserRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class RedisTemplateService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * redis 리프레시 토큰 조회 기능
     * @Author 이찬영
     */
    @Nullable
    @Transactional(readOnly = true)
    public String getUserRefreshToken(@NotNull String userId) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(userId);
    }

    /**
     * redis 리프레시 토큰 저장 기능
     * @Author 이찬영
     */
    public void saveUserRefreshToken(@NotNull String userId, @NotNull String refreshToken){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(userId, refreshToken, Duration.ofDays(365));
//        redisTemplate.expire(userId, 60, TimeUnit.SECONDS);
//        redisTemplate.expire(userId,30, TimeUnit.DAYS);
    }

    /**
     * redis 리프레시 토큰 수정 기능
     * @Author 이찬영
     */
    public void updateUserRefreshToken(@NotNull String userId, @NotNull String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.getAndDelete(userId);
        saveUserRefreshToken(userId, refreshToken);
    }

    /**
     * redis 리프레시 토큰 삭제 기능
     * @Author 이찬영
     */
    public void deleteUserRefreshToken(@NotNull String userId){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.getAndDelete(userId);
    }
}
