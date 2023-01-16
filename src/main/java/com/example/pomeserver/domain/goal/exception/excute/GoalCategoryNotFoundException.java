package com.example.pomeserver.domain.goal.exception.excute;

import com.example.pomeserver.domain.goal.exception.GoalException;
import com.example.pomeserver.domain.goal.exception.GoalExceptionList;

import static com.example.pomeserver.domain.goal.exception.GoalExceptionList.GOAL_CATEGORY_NOT_FOUND;

public class GoalCategoryNotFoundException extends GoalException {
    public GoalCategoryNotFoundException() {
        super(GOAL_CATEGORY_NOT_FOUND.getCODE(), GOAL_CATEGORY_NOT_FOUND.httpStatus, GOAL_CATEGORY_NOT_FOUND.getMESSAGE());
    }
}