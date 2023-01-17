package com.example.pomeserver.domain.goal.exception.excute;

import static com.example.pomeserver.domain.goal.exception.GoalExceptionList.GOAL_CATEGORY_DUPLICATION;

import com.example.pomeserver.domain.goal.exception.GoalException;

public class GoalCategoryDuplicationException extends GoalException {

  public GoalCategoryDuplicationException() {
    super(GOAL_CATEGORY_DUPLICATION.getCODE(), GOAL_CATEGORY_DUPLICATION.httpStatus, GOAL_CATEGORY_DUPLICATION.getMESSAGE());
  }
}
