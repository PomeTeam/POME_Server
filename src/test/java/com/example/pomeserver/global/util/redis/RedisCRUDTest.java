package com.example.pomeserver.global.util.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

@Rollback(false)
@SpringBootTest
public class RedisCRUDTest {


    @Autowired
    private RedisService redisService;

    @Test
    void userIdTest() {

//        String userId = "userId";
//        Long dbId = 10L;
//
//        //dbId
//        //save - get
//        redisService.saveUserDbId(userId, dbId);
//        Long userDbId1 = redisService.getUserDbId(userId);
//        assertEquals(userDbId1, dbId);
//
//        //dbId
//        //update
//        redisService.updateUserDbId(userId, dbId + 1L);
//        Long userDbId2 = redisService.getUserDbId(userId);
//        assertEquals(userDbId2, dbId + 1L);

        //dbId
        //delete
//        redisService.deleteUserDbId(userId);
//        Long userDbId = redisService.getUserDbId(userId);
//        assertNull(userDbId);
    }

    @Test
    void refreshTokenTest() {

        String userId = "userId";  //key
        String refreshToken = "Bearer eyi812jfheu3....";  //value

        //refreshToken
        //save - get
        redisService.saveUserRefreshToken(userId, refreshToken);
        String  refreshToken1 = redisService.getUserRefreshToken(userId);
        assertEquals(refreshToken, refreshToken1);

        //refreshToken
        //update
        redisService.updateUserRefreshToken(userId, refreshToken+"a");
        String refreshToken2 = redisService.getUserRefreshToken(userId);
        assertEquals(refreshToken+"a", refreshToken2);

        //refreshToken
        //delete
//        redisService.deleteUserRefreshToken(userId);
//        String userRefreshToken = redisService.getUserRefreshToken(userId);
//        assertNull(userRefreshToken);
    }
}
