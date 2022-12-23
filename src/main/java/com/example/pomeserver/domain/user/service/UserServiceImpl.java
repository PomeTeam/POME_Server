package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.global.util.SHA256Util;
import com.example.pomeserver.domain.user.dto.request.UserSignInRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignUpRequest;
import com.example.pomeserver.domain.user.dto.response.UserResponse;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.exception.PasswordIsNotValid;
import com.example.pomeserver.domain.user.exception.UserIdAlreadyExistException;
import com.example.pomeserver.domain.user.exception.UserIdNotFound;
import com.example.pomeserver.domain.user.repository.UserRepository;
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
        if(userRepository.findByUserId(userSignUpRequest.getEmail()).isPresent())
            throw new UserIdAlreadyExistException(userSignUpRequest.getEmail());

        userSignUpRequest.setEncPassword(SHA256Util.encrypt(userSignUpRequest.getPassword()));

        User user = userRepository.save(userSignUpRequest.toEntity());

        return UserResponse.toDto(user);
    }

    @Transactional
    @Override
    public UserResponse signIn(UserSignInRequest userSignInRequest) {
        User user = userRepository.findByUserId(userSignInRequest.getEmail())
                .orElseThrow(() -> new UserIdNotFound(userSignInRequest.getEmail()));

        if(!user.getPassword().equals(SHA256Util.encrypt(userSignInRequest.getPassword())))
            throw new PasswordIsNotValid();

        return UserResponse.toDto(user);
    }
}