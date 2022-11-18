package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.domain.global.dto.response.ApplicationResponse;
import com.example.pomeserver.domain.global.util.SHA256Util;
import com.example.pomeserver.domain.user.dto.request.UserLoginRequest;
import com.example.pomeserver.domain.user.dto.request.UserSaveRequest;
import com.example.pomeserver.domain.user.dto.response.UserResponse;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public ApplicationResponse<UserResponse> create(UserSaveRequest userSaveRequest) {
        //TODO userId 이미 존재 여부 확인하기
        userSaveRequest.setEncPassword(SHA256Util.encrypt(userSaveRequest.getPassword()));
        User user = userRepository.save(userSaveRequest.toEntity());
        return ApplicationResponse.create("회원가입 완료.", UserResponse.toDto(user));
    }

    @Override
    public ApplicationResponse<UserResponse> login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByUserId(userLoginRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("찾을 수 없는 userId"));//TODO Exception 정리
        if(!user.getPassword().equals(SHA256Util.encrypt(userLoginRequest.getPassword())))
            throw new RuntimeException("패스워드가 일치하지 않습니다.");
        return ApplicationResponse.ok(UserResponse.toDto(user));
    }
}