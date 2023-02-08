package com.example.pomeserver.domain.user.entity.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    ACCESS,
    DELETE,
    STOP;
}
