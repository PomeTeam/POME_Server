package com.example.pomeserver.domain.goal.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum GoalExceptionList {
    GOAL_NOT_FOUND("G0001",HttpStatus.NOT_FOUND, "해당 id를 가진 Goal을 찾을 수 없습니다.");

    public final String CODE;
    public final HttpStatus httpStatus;
    public final String MESSAGE;
}
