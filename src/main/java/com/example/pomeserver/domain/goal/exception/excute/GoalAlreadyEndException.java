package com.example.pomeserver.domain.goal.exception.excute;

import static com.example.pomeserver.domain.goal.exception.GoalExceptionList.GOAL_ALREADY_END;

import com.example.pomeserver.domain.goal.exception.GoalException;

public class GoalAlreadyEndException extends GoalException {

  public GoalAlreadyEndException() {
    super(GOAL_ALREADY_END.getCODE(), GOAL_ALREADY_END.httpStatus, GOAL_ALREADY_END.getMESSAGE());
  }
}
