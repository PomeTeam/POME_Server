package com.example.pomeserver.domain.user.exception;

public class UserIdAlreadyExistException extends RuntimeException{
    public UserIdAlreadyExistException(String userId) {
    }
}
