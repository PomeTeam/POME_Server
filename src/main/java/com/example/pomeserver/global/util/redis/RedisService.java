package com.example.pomeserver.global.util.redis;

import com.example.pomeserver.global.util.redis.entity.UserDbId;
import com.example.pomeserver.global.util.redis.entity.UserRefreshToken;
import com.example.pomeserver.global.util.redis.repository.UserDbIdRepository;
import com.example.pomeserver.global.util.redis.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Transactional
@Service
public class RedisService {

    @Autowired
    private UserDbIdRepository dbIdRepository;

    @Autowired
    private UserRefreshTokenRepository refreshTokenRepository;


    /*
    * redis의 key는 항상 userId
    */

//    DB id get
    @Nullable
    @Transactional(readOnly = true)
    public Long getUserDbId(@NotNull String userId) {
        Optional<UserDbId> optional = dbIdRepository.findById(userId);
        if(optional.isEmpty()) return null;
        return optional.get().getDbId();
    }

    //DB id insert
    public void saveUserDbId(@NotNull String userId, @NotNull Long dbId){
        UserDbId entity = new UserDbId(userId, dbId);
        dbIdRepository.save(entity);
    }

    //DB id update
    public void updateUserDbId(@NotNull String userId, @NotNull Long id) {
        Optional<UserDbId> optional = dbIdRepository.findById(userId);
        if(optional.isPresent()) deleteUserDbId(userId);
        saveUserDbId(userId, id);
    }

    //DB id delete
    public void deleteUserDbId(@NotNull String userId){
        dbIdRepository.deleteById(userId);
    }

    //refreshToken get
    @Nullable
    @Transactional(readOnly = true)
    public String getUserRefreshToken(@NotNull String userId) {
        Optional<UserRefreshToken> optional = refreshTokenRepository.findById(userId);
        if(optional.isEmpty()) return null;
        return optional.get().getRefreshToken();
    }

    //refreshToken insert
    public void saveUserRefreshToken(@NotNull String userId, @NotNull String refreshToken) {
        UserRefreshToken entity = new UserRefreshToken(userId, refreshToken);
        refreshTokenRepository.save(entity);
    }

    //refreshToken update
    public void updateUserRefreshToken(@NotNull String userId, @NotNull String refreshToken) {
        Optional<UserRefreshToken> optional = refreshTokenRepository.findById(userId);
        if(optional.isPresent()) deleteUserRefreshToken(userId);
        saveUserRefreshToken(userId, refreshToken);
    }

    //refreshToken delete
    public void deleteUserRefreshToken(@NotNull String userId){
        dbIdRepository.deleteById(userId);
    }

}
