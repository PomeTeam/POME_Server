package com.example.pomeserver.domain.goal.dto.assembler;

import com.example.pomeserver.domain.goal.dto.request.GoalCategoryCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.entity.GoalCategory;
import com.example.pomeserver.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class GoalCategoryAssembler {
  public GoalCategory toEntity(GoalCategoryCreateRequest request, User user) {
    return GoalCategory.builder()
        .name(request.getName())
        .user(user)
        .build();
  }
}
