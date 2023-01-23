package com.example.pomeserver.domain.user.repository;

import com.example.pomeserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByNickname(String nickName);
    Optional<User> findByPhoneNum(String phoneNum);
    List<User> findByNicknameStartsWithAndUserIdNot(String nickName,String userId);
}
