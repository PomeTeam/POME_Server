package com.example.pomeserver.domain.goal.repository;

import com.example.pomeserver.domain.goal.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
