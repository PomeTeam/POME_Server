package com.example.pomeserver.domain.goal.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum GoalExceptionList {
    GOAL_NOT_FOUND("G0001",HttpStatus.NOT_FOUND, "해당 id를 가진 Goal을 찾을 수 없습니다."),
    GOAL_CATEGORY_NOT_FOUND("G0002",HttpStatus.NOT_FOUND, "해당 id를 가진 Goal Category을 찾을 수 없습니다."),
    THIS_IS_GOAL_NOT_BY_THIS_USER("G0003",HttpStatus.NOT_FOUND, "해당 목표는 api를 요청한 사용자의 목표가 아닙니다.");

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
