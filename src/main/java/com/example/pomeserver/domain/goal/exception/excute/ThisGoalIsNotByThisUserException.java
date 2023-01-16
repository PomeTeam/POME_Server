package com.example.pomeserver.domain.goal.exception.excute;

import com.example.pomeserver.domain.goal.exception.GoalException;
import com.example.pomeserver.domain.goal.exception.GoalExceptionList;

import static com.example.pomeserver.domain.goal.exception.GoalExceptionList.THIS_IS_GOAL_NOT_BY_THIS_USER;

public class ThisGoalIsNotByThisUserException extends GoalException {
    public ThisGoalIsNotByThisUserException() {
        super(THIS_IS_GOAL_NOT_BY_THIS_USER.getCODE(), THIS_IS_GOAL_NOT_BY_THIS_USER.httpStatus, THIS_IS_GOAL_NOT_BY_THIS_USER.getMESSAGE());
    }
}
