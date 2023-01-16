package com.example.pomeserver.domain.goal.dto.response;

import com.example.pomeserver.domain.goal.entity.GoalCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GoalCategoryResponse {

    private Long id;
    private String name;

    public static GoalCategoryResponse toDto(GoalCategory goalCategory) {
        GoalCategoryResponse goalCategoryResponse = new GoalCategoryResponse();
        goalCategoryResponse.id = goalCategory.getId();
        goalCategoryResponse.name = goalCategory.getName();
        return goalCategoryResponse;
    }
}
