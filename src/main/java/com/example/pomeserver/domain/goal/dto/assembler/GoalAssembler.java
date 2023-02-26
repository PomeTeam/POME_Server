package com.example.pomeserver.domain.goal.dto.assembler;

import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalUpdateRequest;
import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.user.entity.User;
import org.springframework.stereotype.Component;


@Component
public class GoalAssembler {
    public Goal toEntity(GoalCreateRequest request, User user){
        return Goal.builder()
                .name(request.getName())
                .price(request.getPrice())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .oneLineMind(request.getOneLineMind())
                .isPublic(request.getIsPublic())
                .user(user)
                .build();
    }

    public Goal toEntity(GoalUpdateRequest request){
        return Goal.toUpdateEntity(
                request.getName(),
                request.getStartDate(),
                request.getEndDate(),
                request.getOneLineMind(),
                request.getPrice(),
                request.getIsPublic()
        );
    }
}
