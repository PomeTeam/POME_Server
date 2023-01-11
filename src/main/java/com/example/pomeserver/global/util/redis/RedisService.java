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

//    @Autowired
//    private UserDbIdRepository dbIdRepository;

    @Autowired
    private UserRefreshTokenRepository refreshTokenRepository;


    /*
    * redis의 key는 항상 userId
    */


//    @Nullable
//    @Transactional(readOnly = true)
//    public Long getUserDbId(@NotNull String userId) {
//        Optional<UserDbId> optional = dbIdRepository.findById(userId);
//        if(optional.isEmpty()) return null;
//        return optional.get().getDbId();
//    }
//
//    //DB id insert
//    public void saveUserDbId(@NotNull String userId, @NotNull Long dbId){
//        UserDbId entity = new UserDbId(userId, dbId);
//        dbIdRepository.save(entity);
//    }
//
//    //DB id update
//    public void updateUserDbId(@NotNull String userId, @NotNull Long id) {
//        Optional<UserDbId> optional = dbIdRepository.findById(userId);
//        if(optional.isPresent()) deleteUserDbId(userId);
//        saveUserDbId(userId, id);
//    }
//
//    //DB id delete
//    public void deleteUserDbId(@NotNull String userId){
//        dbIdRepository.deleteById(userId);
//    }

    /**
     * redis 리프레시 토큰 조회 기능
     * @Author 이찬영
     */
    @Nullable
    @Transactional(readOnly = true)
    public String getUserRefreshToken(@NotNull String userId) {
        Optional<UserRefreshToken> optional = refreshTokenRepository.findById(userId);
        if(optional.isEmpty()) return null;
        return optional.get().getRefreshToken();
    }

    /**
     * redis 리프레시 토큰 저장 기능
     * @Author 이찬영
     */
    public void saveUserRefreshToken(@NotNull String userId, @NotNull String refreshToken) {
        UserRefreshToken entity = new UserRefreshToken(userId, refreshToken);
        refreshTokenRepository.save(entity);
    }

    /**
     * redis 리프레시 토큰 수정 기능
     * @Author 이찬영
     */
    public void updateUserRefreshToken(@NotNull String userId, @NotNull String refreshToken) {
        Optional<UserRefreshToken> optional = refreshTokenRepository.findById(userId);
        if(optional.isPresent()) deleteUserRefreshToken(userId);
        saveUserRefreshToken(userId, refreshToken);
    }

    /**
     * redis 리프레시 토큰 삭제 기능
     * @Author 이찬영
     */
    public void deleteUserRefreshToken(@NotNull String userId){
        refreshTokenRepository.deleteById(userId);
    }

}
