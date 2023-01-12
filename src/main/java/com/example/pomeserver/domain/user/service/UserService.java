package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.domain.user.DTO.request.UserNicknameRequest;
import com.example.pomeserver.domain.user.DTO.response.FriendSearchResponse;
import com.example.pomeserver.domain.user.DTO.response.UserResponse;
import com.example.pomeserver.domain.user.DTO.request.UserSignInRequest;
import com.example.pomeserver.domain.user.DTO.request.UserSignUpRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    UserResponse signUp(HttpServletResponse response, UserSignUpRequest userSignUpRequest);
    UserResponse signIn(UserSignInRequest userSignInRequest);

    Boolean checkNickname(UserNicknameRequest userNicknameRequest);

    List<FriendSearchResponse> searchFriends(String friendId, String userId, Pageable pageable);
}
