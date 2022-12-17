package com.example.pomeserver.domain.user.controller;

import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.domain.user.dto.request.UserSaveRequest;
import com.example.pomeserver.domain.user.dto.response.UserResponse;
import com.example.pomeserver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/")
    public ApplicationResponse<UserResponse> join(@ModelAttribute UserSaveRequest userSaveRequest){
        return userService.create(userSaveRequest);
    }
}
