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
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "회원 가입 기능",description = "회원가입을 진행합니다.")
    @PostMapping("/sign-up")
    public ApplicationResponse<UserResponse> signUp(@RequestBody @Valid UserSignUpRequest request) {
        return ApplicationResponse.create("회원가입에 성공하셨습니다.", userService.signUp(request));
    }

    /**
     * 로그인
     * @Author 한규범
     */
    @Operation(summary = "로그인 기능",description = "로그인 기능을 수행합니다.")
    @PostMapping("/sign-in")
    public ApplicationResponse<UserResponse> signIn(@RequestBody @Valid UserSignInRequest request) {
        return ApplicationResponse.ok(userService.signIn(request));
    }

    /**
     * 닉네임 중복 확인
     * @Author 한규범
    */
    @Operation(summary = "닉네임 중복 확인",description = "true/false 형태로 반환합니다.")
    @PostMapping("/check-nickname")
    public ApplicationResponse<Boolean> checkNickname(@RequestBody @Valid UserNicknameRequest userNicknameRequest){
        return ApplicationResponse.create("멋진 닉네임이네요!",userService.checkNickname(userNicknameRequest));
    }

    /**
     * 친구찾기
     * @Author 한규범
     */
    @Operation(summary = "친구찾기 기능",description = "닉네임을 기반으로 검색됩니다.")
    @Auth
    @GetMapping("/friend/{friendId}")
    public ApplicationResponse<List<FriendSearchResponse>> searchFriends(
            @PathVariable("friendId") String friendId,
            @UserId String userId,
            Pageable pageable){
        return ApplicationResponse.ok(userService.searchFriends(friendId,userId,pageable));
    }

    /**
     * 친구 등록
     * @Author 한규범
     */
    @Operation(summary = "친구 등록",description = "친구 등록을 진행합니다.")
    @Auth
    @PostMapping("/friend/{friendId}")
    public ApplicationResponse<Boolean> addFriend(
            @PathVariable("friendId") String friendId,
            @UserId String userId
            ){
        return ApplicationResponse.ok(userService.addFriend(friendId,userId));
    }

    /**
     * 친구 목록 조회
     * @Author 한규범
     */
    @Operation(summary = "내 친구 목록 조회", description = "해당 유저가 추가한 친구 목록을 조회합니다.")
    @Auth
    @GetMapping("/friends")
    public ApplicationResponse<List<FriendSearchResponse>> myFriends(
            @UserId String userId,
            Pageable pageable){
        return ApplicationResponse.ok(userService.myFriends(userId,pageable));
    }

    /**
     * 친구 삭제 기능
     * @Author 한규범
     */
    @Operation(summary = "친구 삭제", description = "기존의 친구를 삭제합니다.")
    @Auth
    @DeleteMapping("/friend/{friendId}")
    public ApplicationResponse<Boolean> deleteFriend(
            @PathVariable("friendId") String friendNickName,
            @UserId String userId
            ){
        return ApplicationResponse.ok(userService.deleteFriend(friendNickName,userId));
    }

}
