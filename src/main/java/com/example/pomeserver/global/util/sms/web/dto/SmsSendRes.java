package com.example.pomeserver.global.util.sms.web.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "문자인증 값 반환을 위한 객체")
public class SmsSendRes {
    private String value;
    private String message;
}
