package com.example.pomeserver.domain.user.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserSignInRequest {
    private String email;
    private String password;
}
