package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.domain.user.dto.request.UserNicknameRequest;
import com.example.pomeserver.domain.user.dto.response.FriendSearchResponse;
import com.example.pomeserver.domain.user.dto.response.UserResponse;
import com.example.pomeserver.domain.user.dto.request.UserSignInRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignUpRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    UserResponse signUp(UserSignUpRequest userSignUpRequest);
    UserResponse signIn(UserSignInRequest userSignInRequest);

    Boolean checkNickname(UserNicknameRequest userNicknameRequest);

    List<FriendSearchResponse> searchFriends(String friendId, String userId, Pageable pageable);

    Boolean addFriend(String friendId, String userId);

    List<FriendSearchResponse> myFriends(String userId, Pageable pageable);

    Boolean deleteFriend(String friendNickName, String userId);

    Boolean checkUser(UserSignInRequest userSignInRequest);

    Boolean deleteUser(String userId);

    Boolean logout(String userId);
}
