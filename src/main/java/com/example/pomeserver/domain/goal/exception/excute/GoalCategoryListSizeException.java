package com.example.pomeserver.domain.goal.exception.excute;

import static com.example.pomeserver.domain.goal.exception.GoalExceptionList.GOAL_CATEGORY_LIST_SIZE_OUT_OF_INDEX;

import com.example.pomeserver.domain.goal.exception.GoalException;

public class GoalCategoryListSizeException extends GoalException {

  public GoalCategoryListSizeException() {
    super(GOAL_CATEGORY_LIST_SIZE_OUT_OF_INDEX.getCODE(), GOAL_CATEGORY_LIST_SIZE_OUT_OF_INDEX.httpStatus, GOAL_CATEGORY_LIST_SIZE_OUT_OF_INDEX.getMESSAGE());
  }
}
