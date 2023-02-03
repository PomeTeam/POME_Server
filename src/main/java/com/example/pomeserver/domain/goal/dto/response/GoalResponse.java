package com.example.pomeserver.domain.goal.dto.response;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.record.entity.Record;
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

    private Boolean isEnd; // 종료인지 아닌지 상태
    private int usePrice; // 사용금액

    private String oneLineComment; // 한줄 코멘트
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
        response.isEnd = goal.isEnd();
        response.oneLineComment = goal.getOneLineComment();
        // Goal이 갖는 Record의 usePrice의 합으로 갱신
        for (Record record : goal.getRecords()) {
            goal.addUsePrice(record.getUsePrice());
        }
        response.usePrice = goal.getUsePrice();
        response.nickname = goal.getGoalCategory().getUser().getNickname();
        return response;
    }
}
