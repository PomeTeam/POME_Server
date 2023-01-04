package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.domain.user.DTO.response.UserResponse;
import com.example.pomeserver.domain.user.DTO.request.UserSignInRequest;
import com.example.pomeserver.domain.user.DTO.request.UserSignUpRequest;

public interface UserService {
    UserResponse signUp(UserSignUpRequest userSignUpRequest);
    UserResponse signIn(UserSignInRequest userSignInRequest);
}
