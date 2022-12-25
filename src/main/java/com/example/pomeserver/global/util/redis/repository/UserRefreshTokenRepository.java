package com.example.pomeserver.global.util.redis.repository;

import com.example.pomeserver.global.util.redis.entity.UserRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface UserRefreshTokenRepository extends CrudRepository<UserRefreshToken, Long> {
}
