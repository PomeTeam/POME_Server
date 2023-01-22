package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.domain.user.dto.request.UserNicknameRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignInRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignUpRequest;
import com.example.pomeserver.domain.user.dto.response.FriendSearchResponse;
import com.example.pomeserver.domain.user.dto.response.UserResponse;
import com.example.pomeserver.domain.user.entity.Follow;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.exception.excute.FollowAlreadyException;
import com.example.pomeserver.domain.user.exception.excute.UserAlreadyNickName;
import com.example.pomeserver.domain.user.exception.excute.UserAlreadyPhoneNum;
import com.example.pomeserver.domain.user.exception.excute.UserNotFoundException;
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
        userRepository.findByNickname(userNicknameRequest.getNickName()).orElseThrow(UserAlreadyNickName::new);
        return true;
    }

    @Override
    public List<FriendSearchResponse> searchFriends(String friendId, String userId, Pageable pageable) {
        List<User> users = userRepository.findByNicknameStartsWithAndUserIdNot(friendId,userId);
        return users.stream().map(user -> FriendSearchResponse.builder()
                .friendNickname(user.getNickname())
                .imageKey(user.getImage())
                .build()).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Boolean addFriend(String friendId, String userId) {
        User fromUser = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        User toUser = userRepository.findByNickname(friendId).orElseThrow(UserNotFoundException::new);

        Optional<Follow> byToUserAndFromUser = followRepository.findByToUserAndFromUser(toUser, fromUser);

        if (followRepository.findByToUserAndFromUser(toUser,fromUser).isPresent()) throw new FollowAlreadyException();

        Follow save = followRepository.save(Follow.builder()
                .toUser(toUser)
                .fromUser(fromUser)
                .build());

        System.out.println(save.getFromUser().getUserId());
        return true;
    }

    @Override
    public List<FriendSearchResponse> myFriends(String userId, Pageable pageable) {
        Long fromUserId = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new).getId();
        List<Follow> follows = followRepository.findByFromUserId(fromUserId);
        FriendSearchResponse friendSearchResponse = new FriendSearchResponse();
//        for (Follow follow : follows) {
//
//        }
//        follows.stream().map(follows -> FriendSearchResponse.builder()
//            .friendNickname())
        return null;
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