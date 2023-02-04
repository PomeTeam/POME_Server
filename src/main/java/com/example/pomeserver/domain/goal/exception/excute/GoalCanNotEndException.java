package com.example.pomeserver.domain.goal.exception.excute;

import static com.example.pomeserver.domain.goal.exception.GoalExceptionList.GOAL_CAN_NOT_END;

import com.example.pomeserver.domain.goal.exception.GoalException;

public class GoalCanNotEndException extends GoalException {

  public GoalCanNotEndException() {
    super(GOAL_CAN_NOT_END.getCODE(), GOAL_CAN_NOT_END.httpStatus, GOAL_CAN_NOT_END.getMESSAGE());
  }
}
