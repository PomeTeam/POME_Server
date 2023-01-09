package com.example.pomeserver.global.util.redis;

import com.example.pomeserver.global.util.redis.entity.UserAccessToken;
import com.example.pomeserver.global.util.redis.repository.UserAccessTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import java.util.Optional;

@SpringBootTest
public class RedisCRUDTest {

    @Autowired
    private UserAccessTokenRepository repo;

    @Test
    void test() {

        String accessToken1 = "accesstokenOfUser1";
        String accessToken2 = "accesstokenOfUsser2";
        UserAccessToken userAccessToken1 = new UserAccessToken(1L, accessToken1);
        UserAccessToken userAccessToken2 = new UserAccessToken(2L, accessToken2);

        // 저장
        repo.save(userAccessToken1);
        repo.save(userAccessToken2);

        // `keyspace:id` 값을 가져옴
        Optional<UserAccessToken> result = repo.findById(userAccessToken1.getId());
        if(result.isPresent()) {
            UserAccessToken userAccessToken = result.get();
            assertThat(userAccessToken.getAccessToken()).isEqualTo(accessToken1);
        }
        // 삭제
        repo.delete(userAccessToken1);
        repo.delete(userAccessToken2);
    }
}
