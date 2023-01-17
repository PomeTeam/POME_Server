package com.example.pomeserver.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.pomeserver.domain.user.dto.request.UserNicknameRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignInRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignUpRequest;
import com.example.pomeserver.domain.user.dto.response.FriendSearchResponse;
import com.example.pomeserver.domain.user.dto.response.UserResponse;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.service.UserService;
import com.example.pomeserver.global.util.jwtToken.TokenUtils;
import com.example.pomeserver.global.util.redis.template.RedisTemplateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  UserService userService;
  @MockBean
  TokenUtils tokenUtils;

  @MockBean
  RedisTemplateService redisTemplateService;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  @DisplayName("회원가입 성공")
  void 회원가입() throws Exception {
    // Request
    UserSignUpRequest request =
        new UserSignUpRequest("우릉비", "01012345678", "image001");

    // Domain
    User user = request.toEntity();
    // Token
    String accessToken = tokenUtils.createAccessToken(user.getUserId(), user.getNickname());

    // given
    given(userService.signUp(request)).willReturn(UserResponse.toDto(user, accessToken));

    String dto = objectMapper.writeValueAsString(request);

    // when-then
    mvc.perform(post("/api/v1/users/sign-up")
            .characterEncoding("utf-8")
            .content(dto)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("로그인 성공")
  void 로그인() throws Exception{ // access-token이 헤더에 담겨져 있지 않음
    // Request
    UserSignInRequest request = new UserSignInRequest("01012345678");

    // Domain
    User user = User.builder().userId("1").nickname("우릉비").phoneNum("01012345678").build();

    // Token
    String accessToken = "access-token";

    given(userService.signIn(any())).willReturn(UserResponse.toDto(user, accessToken));

    String dto = objectMapper.writeValueAsString(request);

    // when-then
    MvcResult result = mvc.perform(post("/api/v1/users/sign-in")
        .content(dto)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8"))
        .andExpect(status().isOk())
        .andReturn();

    System.out.println(result.getResponse().getHeader("ACCESS-TOKEN"));
  }

  @Test
  @DisplayName("닉네임 중복 확인 성공")
  void 닉네임중복() throws Exception{
    // Request
    UserNicknameRequest request = new UserNicknameRequest("우릉비");

    given(userService.checkNickname(request)).willReturn(true);

    String dto = objectMapper.writeValueAsString(request);

    mvc.perform(post("/api/v1/users/check-nickname")
        .content(dto)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding("utf-8"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("친구 찾기 성공")
  void 친구찾기() throws Exception{
    // Request Param
    String friendId = "우릉비의 친구";
    String userId = "우릉비";

    String accessToken = "access-token";

    // Response
    List<FriendSearchResponse> response = new ArrayList<>();
    response.add(FriendSearchResponse.builder().friendId(friendId).imageKey("image002").build());

    given(userService.searchFriends(friendId, userId, Pageable.ofSize(8))).willReturn(response);

    // when-then
    mvc.perform(get("/api/v1/users/friend/{friendId}", friendId)
            .header("ACCESS-TOKEN", accessToken)
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.[0].friendId").value("우릉비의 친구"));
  }

}