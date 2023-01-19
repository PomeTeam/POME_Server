package com.example.pomeserver.domain.goal.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class GoalCategoryCreateRequest {
    private String name;
}
