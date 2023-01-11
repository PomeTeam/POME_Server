package com.example.pomeserver.domain.goal.exception.excute;

import com.example.pomeserver.domain.goal.exception.GoalExceptionList;
import com.example.pomeserver.domain.user.exception.UserException;
import com.example.pomeserver.domain.user.exception.UserExceptionList;

public class GoalNotFoundException extends UserException {
    public GoalNotFoundException() {
        super(GoalExceptionList.GOAL_NOT_FOUND.getCODE(), GoalExceptionList.GOAL_NOT_FOUND.httpStatus, GoalExceptionList.GOAL_NOT_FOUND.getMESSAGE());
    }
}
