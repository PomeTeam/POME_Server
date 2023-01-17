package com.example.pomeserver.domain.user.controller;

import com.example.pomeserver.domain.user.dto.request.UserNicknameRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignInRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignUpRequest;
import com.example.pomeserver.domain.user.dto.response.FriendSearchResponse;
import com.example.pomeserver.domain.user.dto.response.UserResponse;
import com.example.pomeserver.domain.user.service.UserService;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.authResolver.Auth;
import com.example.pomeserver.global.util.authResolver.UserId;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

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
    public ApplicationResponse<UserResponse> signUp(@RequestBody @Valid UserSignUpRequest request) {
        return ApplicationResponse.create("회원가입에 성공하셨습니다.", userService.signUp(request));
    }

    /**
     * 로그인
     * @Author 한규범
     */
    @PostMapping("/sign-in")
    public ApplicationResponse<UserResponse> signIn(@RequestBody @Valid UserSignInRequest request) {
        return ApplicationResponse.ok(userService.signIn(request));
    }

    /**
     * 닉네임 중복 확인
     * @Author 한규범
    */
    @PostMapping("/check-nickname")
    public ApplicationResponse<Boolean> checkNickname(@RequestBody @Valid UserNicknameRequest userNicknameRequest){
        return ApplicationResponse.create("멋진 닉네임이네요!",userService.checkNickname(userNicknameRequest));
    }

    /**
     * 친구찾기
     * @Author 한규범
     */
    @Auth
    @GetMapping("/friend/{friendId}")
    public ApplicationResponse<List<FriendSearchResponse>> searchFriends(
            @PathVariable("friendId") String friendId,
            @UserId String userId,
            Pageable pageable){
        return ApplicationResponse.ok(userService.searchFriends(friendId,userId,pageable));
    }

}
