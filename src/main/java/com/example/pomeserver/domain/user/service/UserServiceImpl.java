package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.domain.user.DTO.response.UserResponse;
import com.example.pomeserver.domain.user.exception.excute.UserAlreadyNickName;
import com.example.pomeserver.domain.user.exception.excute.UserAlreadyPhoneNum;
import com.example.pomeserver.global.util.SHA256Util;
import com.example.pomeserver.domain.user.DTO.request.UserSignInRequest;
import com.example.pomeserver.domain.user.DTO.request.UserSignUpRequest;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.util.sms.service.SmsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserResponse signUp(UserSignUpRequest userSignUpRequest){
        userRepository.findByNickname(userSignUpRequest.getNickname()).orElseThrow(UserAlreadyNickName::new);
        userRepository.findByPhoneNum(userSignUpRequest.getPhoneNum()).orElseThrow(UserAlreadyPhoneNum::new);
//        userSignUpRequest.setEncPassword(SHA256Util.encrypt(userSignUpRequest.getPassword()));

        return UserResponse.toDto(userRepository.save(userSignUpRequest.toEntity()));
    }

    @Transactional
    @Override
    public UserResponse signIn(UserSignInRequest userSignInRequest) {
//        User user = userRepository.findByUserId(userSignInRequest.getEmail())
//                .orElseThrow(() -> new UserIdNotFound();
//
//        if(!user.getPassword().equals(SHA256Util.encrypt(userSignInRequest.getPassword())))
//            throw new PasswordIsNotValid();

        return UserResponse.toDto(null);
    }
}