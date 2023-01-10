package com.example.pomeserver.global.util.redis;

import com.example.pomeserver.global.util.redis.repository.UserDbIdRepository;
import com.example.pomeserver.global.util.redis.repository.UserRefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

@SpringBootTest
public class RedisCRUDTest {


    @Autowired
    private RedisService redisService;

    @Test

    void test() {

        String userId = "userId";
        Long dbId = 10L;

        //dbId
        //save - get
        redisService.saveUserDbId(userId, dbId);
        Long userDbId1 = redisService.getUserDbId(userId);
        assertEquals(userDbId1, dbId);

        //dbId
        //update
        redisService.updateUserDbId(userId, dbId + 1L);
        Long userDbId2 = redisService.getUserDbId(userId);
        assertEquals(userDbId2, dbId + 1L);

        //dbId
        //delete
        redisService.deleteUserDbId(userId);
        Long userDbId = redisService.getUserDbId(userId);
        assertNull(userDbId);
    }
}
