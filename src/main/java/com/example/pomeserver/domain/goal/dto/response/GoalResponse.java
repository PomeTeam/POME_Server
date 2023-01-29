package com.example.pomeserver.domain.goal.dto.response;

import com.example.pomeserver.domain.goal.entity.Goal;
import java.sql.Date;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GoalResponse {
    private Long id;
    private GoalCategoryResponse goalCategoryResponse;
    private String startDate; //2023.02.23
    private String endDate;   //2023.02.23
    private String oneLineMind;
    private int price;
    private Boolean isPublic;

    private Boolean isEnd; // endDate가 지났거나 소비기록이 없거나
    private String nickname;

    public static GoalResponse toDto(Goal goal){
        GoalResponse response = new GoalResponse();
        response.id = goal.getId();
        response.goalCategoryResponse = GoalCategoryResponse.toDto(goal.getGoalCategory());
        response.startDate = goal.getStartDate();
        response.endDate = goal.getEndDate();
        response.oneLineMind = goal.getOneLineMind();
        response.price = goal.getPrice();
        response.isPublic = goal.isPublic();

        if (LocalDate.parse(goal.getEndDate()).isBefore(LocalDate.now())) {
            response.isEnd = true;
        } else
            response.isEnd = goal.getRecords().isEmpty();

        response.nickname = goal.getGoalCategory().getUser().getNickname();
        return response;
    }
}
