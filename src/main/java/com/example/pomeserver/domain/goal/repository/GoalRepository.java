package com.example.pomeserver.domain.goal.repository;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    Page<Goal> findAllByUser(User user, Pageable pageable);

    Page<Goal> findAllByUserAndIsEnd(User user, boolean isEnd, Pageable pageable);
}
