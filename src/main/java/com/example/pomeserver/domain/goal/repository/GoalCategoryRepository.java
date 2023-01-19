package com.example.pomeserver.domain.goal.repository;

import com.example.pomeserver.domain.goal.entity.GoalCategory;
import com.example.pomeserver.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalCategoryRepository extends JpaRepository<GoalCategory, Long> {
  List<GoalCategory> findAllByUser(User user);

  Optional<GoalCategory> findByNameAndUser(String name, User user);
}
