package com.example.pomeserver.domain.goal.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel("목표 카테고리 작성 요청 객체")
@NoArgsConstructor
@Data
public class GoalCategoryCreateRequest {
    @NotNull(message = "목표 카테고리는 필수값입니다.")
    @NotBlank(message = "목표 카테고리 빈 문자열일 수 없습니다.")
    @Size(max = 9, message = "목표 카테고리는 9자 이하만 가능합니다.")
    private String name;
}
