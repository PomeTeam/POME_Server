package com.example.pomeserver.domain.goal.repository;

import com.example.pomeserver.domain.goal.entity.GoalCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalCategoryRepository extends JpaRepository<GoalCategory, Long> {
}
