package com.example.pomeserver.domain.goal.repository;

import com.example.pomeserver.domain.goal.entity.Goal;

public interface GoalRepository {
    Goal findById(Long goalId);
}
