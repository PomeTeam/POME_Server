package com.example.pomeserver.global.util.redis.repository;

import com.example.pomeserver.global.util.redis.entity.UserDbId;
import com.example.pomeserver.global.util.redis.entity.UserRefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface UserDbIdRepository extends CrudRepository<UserDbId, String> {
}
