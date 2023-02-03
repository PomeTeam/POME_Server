package com.example.pomeserver.domain.goal.dto.request;

import io.swagger.annotations.ApiModel;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("목표 종료 요청 객체")
@NoArgsConstructor
@Data
public class GoalTerminateRequest {

  @NotNull(message = "한 줄 코멘트는 필수값입니다.")
  private String oneLineComment;
}
