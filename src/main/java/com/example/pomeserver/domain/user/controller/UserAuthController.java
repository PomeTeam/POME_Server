package com.example.pomeserver.domain.user.controller;

import com.example.pomeserver.domain.user.dto.request.UserAuthTokenRequest;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.jwtToken.TokenUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
@Api(tags = "User Auth 관련 API")
public class UserAuthController {

    private final TokenUtils tokenUtils;

    @PostMapping("/renew")
    public ApplicationResponse<String> accessToken(@RequestBody @Valid UserAuthTokenRequest userAuthTokenRequest){
        return ApplicationResponse.ok(tokenUtils.accessExpiration(userAuthTokenRequest));
    }


}
