package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.domain.user.dto.request.UserNicknameRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignInRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignUpRequest;
import com.example.pomeserver.domain.user.dto.response.FriendSearchResponse;
import com.example.pomeserver.domain.user.dto.response.UserResponse;
import com.example.pomeserver.domain.user.entity.Follow;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.exception.excute.*;
import com.example.pomeserver.domain.user.repository.FollowRepository;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.util.jwtToken.TokenUtils;
import com.example.pomeserver.global.util.redis.template.RedisTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final EntityManager em;
    private final TokenUtils tokenUtils;
    private final RedisTemplateService redisTemplateService;

    //TODO 확인
    @Transactional
    @Override
    public UserResponse signUp(UserSignUpRequest userSignUpRequest){
        if (userRepository.findByPhoneNum(userSignUpRequest.getPhoneNum()).isPresent()){
            throw new UserAlreadyPhoneNum();
        }
        if (userRepository.findByNickname(userSignUpRequest.getNickname()).isPresent()){
            throw new UserAlreadyNickName();
        }
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
        if(userRepository.findByNickname(userNicknameRequest.getNickName()).isPresent()) throw new UserAlreadyNickName();
//        userRepository.findByNickname(userNicknameRequest.getNickName()).orElseThrow(UserAlreadyNickName::new);
        return true;
    }

    @Override
    public List<FriendSearchResponse> searchFriends(String friendId, String userId, Pageable pageable) {
        List<User> users = userRepository.findByNicknameStartsWithAndUserIdNot(friendId,userId);
        return users.stream().map(user -> FriendSearchResponse.builder()
                .friendUserId(user.getUserId())
                .friendNickname(user.getNickname())
                .imageKey(user.getImage())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Boolean addFriend(String friendId, String userId) {
        User fromUser = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        User toUser = userRepository.findByNickname(friendId).orElseThrow(UserNotFoundException::new);

        if (followRepository.findByToUserAndFromUser(toUser,fromUser).isPresent()) throw new FollowAlreadyException();
        followRepository.save(Follow.builder()
                .toUser(toUser)
                .fromUser(fromUser)
                .build());

        return true;
    }

    @Override
    public List<FriendSearchResponse> myFriends(String userId, Pageable pageable) {
        Long fromUserId = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new).getId();
        List<Follow> followList = followRepository.findByFromUserId(fromUserId);

        return followList.stream().map(follow -> FriendSearchResponse.builder()
                .friendUserId(follow.getToUser().getUserId())
                .friendNickname(follow.getToUser().getNickname())
                .imageKey(follow.getToUser().getImage())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Boolean deleteFriend(String friendNickName, String userId) {
        User fromUser = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        User toUser = userRepository.findByNickname(friendNickName).orElseThrow(UserNotFoundException::new);
        Follow follow = followRepository.findByToUserAndFromUser(toUser, fromUser).orElseThrow(FollowNotFoundException::new);
        followRepository.delete(follow);
        return true;
    }

    private String getSaveToken(User user) {
        String accessToken = tokenUtils.createAccessToken(user.getUserId(), user.getNickname()); // 클라
        String refreshToken = tokenUtils.createRefreshToken(user.getUserId(), user.getNickname()); // 레디스
        tokenUtils.getUserIdFromFullToken(accessToken);
        redisTemplateService.saveUserRefreshToken(user.getUserId(), refreshToken);
        setHeaderToken(accessToken);
        return accessToken;
    }

    public void setHeaderToken(String accessToken){
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
        response.setHeader("ACCESS-TOKEN",accessToken);
    }

    public String getUserNickName(String userId){
        return userRepository.findByUserId(userId).get().getNickname();
    }
}