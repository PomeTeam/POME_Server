package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.domain.global.dto.response.ApplicationResponse;
import com.example.pomeserver.domain.user.dto.request.UserLoginRequest;
import com.example.pomeserver.domain.user.dto.request.UserSaveRequest;
import com.example.pomeserver.domain.user.dto.response.UserResponse;

public interface UserService {
    ApplicationResponse<UserResponse> create(UserSaveRequest userSaveRequest);
    ApplicationResponse<UserResponse> login(UserLoginRequest userLoginRequest);
}
