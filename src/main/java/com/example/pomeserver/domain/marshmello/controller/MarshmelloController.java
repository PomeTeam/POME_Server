package com.example.pomeserver.domain.marshmello.controller;

import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalResponse;
import com.example.pomeserver.domain.marshmello.dto.response.MarshmelloResponse;
import com.example.pomeserver.domain.marshmello.service.MarshmelloService;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.authResolver.Auth;
import com.example.pomeserver.global.util.authResolver.UserId;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/v1/marshmello")
@RestController
@Api(tags = "Marshmello 관련 API")
public class MarshmelloController {

    private final MarshmelloService marshmelloService;

    @Operation(summary = "특정 유저의 마시멜로 정보 가져오기",
            description = "현재 로그인된 유저의 마시멜로 정보(마시멜로 레벨)을 불러온다.")
    @Auth
    @GetMapping
    public ApplicationResponse<MarshmelloResponse> findByUser(@ApiIgnore @UserId String userId)
    {
        return marshmelloService.findByUser(userId);
    }
}
