package com.example.pomeserver.global.util.redis;

import com.example.pomeserver.global.util.redis.template.RedisTemplateService;
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

    @Autowired
    private RedisTemplateService redisTemplateService;

//    @Test
//    void refreshTokenTest() {
//
//        String userId = "userId";  //key
//        String refreshToken = "Bearer eyi812jfheu3....";  //value
//
//        //refreshToken
//        //save - get
//        redisService.saveUserRefreshToken(userId, refreshToken);
//        String  refreshToken1 = redisService.getUserRefreshToken(userId);
//        assertEquals(refreshToken, refreshToken1);
//
//        //refreshToken
//        //update
//        redisService.updateUserRefreshToken(userId, refreshToken+"a");
//        String refreshToken2 = redisService.getUserRefreshToken(userId);
//        assertEquals(refreshToken+"a", refreshToken2);
//
//        //refreshToken
//        //delete
//        redisService.deleteUserRefreshToken(userId);
//        String userRefreshToken = redisService.getUserRefreshToken(userId);
//        assertNull(userRefreshToken);
//    }

    @Test
    void refreshTokenTest2() {

        String userId = "userId2";  //key
        String refreshToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBY2Nlc3MtSGVhZGVyIiwiaWF0IjoxNjczNTA5Mjg5LCJ1c2VySWQiOiJmYjNlMzU2OC05NTRiLTRiMGItODc4Mi1lZWY0NTg2OWJkZTYiLCJuaWNrbmFtZSI6Iuq3nOuylO2CueyZlTEyIiwiZXhwIjoxNjczNTEwNDg5fQ.8nORDi0rWjCkRWBtF0EqS6o9Fhd-SgaswCNOLCeJOsI";  //value

        //refreshToken
        //save - get
        redisTemplateService.saveUserRefreshToken(userId, refreshToken);
        String  refreshToken1 = redisTemplateService.getUserRefreshToken(userId);
        assertEquals(refreshToken, refreshToken1);

        //refreshToken
        //update
        redisTemplateService.updateUserRefreshToken(userId, refreshToken+"a");
        String refreshToken2 = redisTemplateService.getUserRefreshToken(userId);
        assertEquals(refreshToken+"a", refreshToken2);

        //refreshToken
        //delete
//        redisTemplateService.deleteUserRefreshToken(userId);
//        String userRefreshToken = redisTemplateService.getUserRefreshToken(userId);
//        assertNull(userRefreshToken);
    }
}
