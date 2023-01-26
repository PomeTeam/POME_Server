package com.example.pomeserver.domain.goal.dto.assembler;

import com.example.pomeserver.domain.goal.entity.GoalCategory;
import com.example.pomeserver.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class GoalCategoryAssembler {
  public GoalCategory toEntity(String name, User user) {
    return GoalCategory.builder()
        .name(name)
        .user(user)
        .build();
  }
}
