package com.example.pomeserver.domain.user.controller;

import com.example.pomeserver.domain.user.DTO.request.UserSignInRequest;
import com.example.pomeserver.domain.user.DTO.request.UserSignUpRequest;
import com.example.pomeserver.domain.user.DTO.response.UserResponse;
import com.example.pomeserver.domain.user.service.UserService;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
@Api(tags = "User 관련 API")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입 기능
     * @Author 한규범
     */
    //TODO 사진
    @PostMapping("/sign-up")
    public ApplicationResponse<UserResponse> signUp(@RequestBody UserSignUpRequest request) {
        return ApplicationResponse.create("회원가입에 성공하셨습니다.", userService.signUp(request));
    }

    @PostMapping("/sign-in")
    public ApplicationResponse<UserResponse> signIn(@RequestBody UserSignInRequest request) {
        UserResponse data = userService.signIn(request);
        return ApplicationResponse.ok(data);
    }

    /**
     * 닉네임 중복 확인
     * @Author 한규범
  범  */
    @PostMapping("/check-nickname")
    public ApplicationResponse<Boolean> checkNickname(@RequestBody @Valid UserNicknameRequest userNicknameRequest){
        return ApplicationResponse.create("멋진 닉네임이네요!",userService.checkNickname(userNicknameRequest));
    }


}
