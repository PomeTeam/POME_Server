package com.example.pomeserver.global.util.sms.web.controller;


import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.sms.service.SmsServiceImpl;
import com.example.pomeserver.global.util.sms.web.dto.SmsSendReq;
import com.example.pomeserver.global.util.sms.web.dto.SmsSendRes;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("api/v1/sms")
@RequiredArgsConstructor
@Api(tags = "SMS 관련 API")
public class SmsController {

    private final SmsServiceImpl smsService;

    @ApiOperation(value = "문자발송", notes = "문자를 발송합니다.")
    @PostMapping("/send")
    public ApplicationResponse<SmsSendRes> send(@RequestBody SmsSendReq smsSendReq) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, URISyntaxException {
        return ApplicationResponse.ok(SmsSendRes.builder()
                .value(smsService.send(smsSendReq))
                .build());
    }
}
