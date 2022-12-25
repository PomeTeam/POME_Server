package com.example.pomeserver.global.util.redis.repository;

import com.example.pomeserver.global.util.redis.entity.UserAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface UserAccessTokenRepository extends CrudRepository<UserAccessToken, Long> {
}
