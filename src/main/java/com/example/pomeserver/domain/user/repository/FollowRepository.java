package com.example.pomeserver.domain.user.repository;

import com.example.pomeserver.domain.user.entity.Follow;
import com.example.pomeserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByToUserId(Long toUserId);
//    Optional<Follow> findByToUserIdAndFromUserId(Long toUserId, Long fromUserId);
    Optional<Follow> findByToUserAndFromUser(User toUser, User fromUser);
    List<Follow> findByFromUserId(Long fromUserId);
}