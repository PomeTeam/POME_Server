package com.example.pomeserver.domain.goal.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GoalCreateRequest {

    private Long goalCategoryId;
    private String startDate;//2023.02.23
    private String endDate;//2023.02.23
    private String oneLineMind;
    private int price;
    private Boolean isPublic;
}
