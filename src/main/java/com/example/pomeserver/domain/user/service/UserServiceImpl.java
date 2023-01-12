package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.domain.user.DTO.request.UserNicknameRequest;
import com.example.pomeserver.domain.user.DTO.response.FriendSearchResponse;
import com.example.pomeserver.domain.user.DTO.response.UserResponse;
import com.example.pomeserver.domain.user.exception.excute.UserAlreadyNickName;
import com.example.pomeserver.domain.user.exception.excute.UserAlreadyPhoneNum;
import com.example.pomeserver.domain.user.exception.excute.UserNotFoundException;
import com.example.pomeserver.domain.user.DTO.request.UserSignInRequest;
import com.example.pomeserver.domain.user.DTO.request.UserSignUpRequest;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.util.jwtToken.TokenUtils;
import com.example.pomeserver.global.util.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final TokenUtils tokenUtils;
    private final RedisService redisService;

    //TODO 확인
    @Transactional
    @Override
    public UserResponse signUp(HttpServletResponse response, UserSignUpRequest userSignUpRequest){
        userRepository.findByPhoneNum(userSignUpRequest.getPhoneNum()).orElseThrow(UserAlreadyPhoneNum::new);
        userRepository.findByNickname(userSignUpRequest.getNickname()).orElseThrow(UserAlreadyNickName::new);
        User user = userRepository.save(userSignUpRequest.toEntity());
        return UserResponse.toDto(user, getSaveToken(user));
    }



    @Override
    public UserResponse signIn(UserSignInRequest userSignInRequest) {
        User user = userRepository.findByPhoneNum(userSignInRequest.getPhoneNum()).orElseThrow(UserNotFoundException::new);
        return UserResponse.toDto(user, getSaveToken(user));
    }

    @Override
    public Boolean checkNickname(UserNicknameRequest userNicknameRequest) {
        userRepository.findByNickname(userNicknameRequest.getNickName()).orElseThrow(UserAlreadyNickName::new);
        return true;
    }

    @Override
    public List<FriendSearchResponse> searchFriends(String friendId, String userId, Pageable pageable) {
        List<User> users = userRepository.findByNicknameStartsWithAndUserIdNot(friendId,userId);
        return users.stream().map(user -> FriendSearchResponse.builder()
                .friendId(user.getNickname())
                .imageKey(user.getImage())
                .build()).collect(Collectors.toList());
    }

    private String getSaveToken(User user) {
        String accessToken = tokenUtils.createAccessToken(user.getUserId(), user.getNickname()); // 클라
        String refreshToken = tokenUtils.createRefreshToken(user.getUserId(), user.getNickname()); // 레디스
        tokenUtils.getUserIdFromFullToken(accessToken);
        redisService.saveUserRefreshToken(user.getUserId(), refreshToken);
        setHeaderToken(accessToken, refreshToken);
        return accessToken;
    }

    public void setHeaderToken(String accessToken, String refreshToken){
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
        response.setHeader("ACCESS-TOKEN",accessToken);
        response.setHeader("REFRESH-TOKEN",refreshToken);

    }
}