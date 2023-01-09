package com.example.pomeserver.domain.user.repository;

import com.example.pomeserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByNickname(String nickName);
    Optional<User> findByPhoneNum(String phoneNum);
}
