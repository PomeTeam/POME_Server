package com.example.pomeserver.domain.user.service;

import com.example.pomeserver.domain.user.dto.request.UserNicknameRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignInRequest;
import com.example.pomeserver.domain.user.dto.request.UserSignUpRequest;
import com.example.pomeserver.domain.user.dto.response.FriendSearchResponse;
import com.example.pomeserver.domain.user.dto.response.UserResponse;
import com.example.pomeserver.domain.user.entity.Follow;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.entity.vo.UserType;
import com.example.pomeserver.domain.user.exception.excute.*;
import com.example.pomeserver.domain.user.repository.FollowRepository;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.domain.user.repository.UserWithdrawalRepository;
import com.example.pomeserver.global.util.jwtToken.TokenUtils;
import com.example.pomeserver.global.util.redis.template.RedisTemplateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;
    @Mock
    TokenUtils tokenUtils;
    @Mock
    RedisTemplateService redisTemplateService;
    @Mock
    FollowRepository followRepository;
    @Mock
    UserWithdrawalRepository userWithdrawalRepository;

    @DisplayName("유저_회원가입 성공")
    @Test
    void 유저생성_성공(){
        //given
        UserSignUpRequest request = createUserSignUpRequest();
        User actualUser = request.toEntity();

        //stub
        lenient().when(userRepository.save(any())).thenReturn(actualUser);

        //when
        UserResponse userResponse = userService.signUp(request);

        //then
        assertAll(
                () -> assertEquals(actualUser.getNickname(),userResponse.getNickName()),
                () -> assertEquals(actualUser.getUserId(),userResponse.getUserId())
        );
     }

    @DisplayName("유저_회원가입 닉네임중복")
    @Test
    void 유저생성_닉네임중복(){
        //given
        UserSignUpRequest request = createUserSignUpRequest();
        User actualUser = request.toEntity();

        //when
        lenient().when(userRepository.findByNickname(request.getNickname())).thenReturn(Optional.of(actualUser));

        //then
        assertThrows(UserAlreadyNickName.class, () -> {
            userService.signUp(request);
        });
     }

    @DisplayName("유저_회원가입 전화번호중복")
    @Test
    public void 유저생성_전화번호중복(){
        //given
        UserSignUpRequest request = createUserSignUpRequest();
        User actualUser = request.toEntity();

        lenient().when(userRepository.findByPhoneNum(request.getPhoneNum())).thenReturn(Optional.of(actualUser));

        //then
        assertThrows(UserAlreadyPhoneNum.class, () -> {
            userService.signUp(request);
        });
     }

    @DisplayName("로그인_성공")
    @Test
    public void 로그인_성공(){
        //given
        UserSignInRequest request = UserSignInRequest.builder().phoneNum("01012341234").build();
        User actualUser = User.builder().userId(UUID.randomUUID().toString()).image("userprof847.png").nickname("testNick").phoneNum(request.getPhoneNum()).build();

        //when
        lenient().when(userRepository.findByPhoneNum(request.getPhoneNum())).thenReturn(Optional.of(actualUser));
        UserResponse userResponse = userService.signIn(request);

        //then
        assertAll(
                () -> assertEquals(actualUser.getNickname(),userResponse.getNickName()),
                () -> assertEquals(actualUser.getUserId(),userResponse.getUserId())
        );
    }

    @DisplayName("로그인_존재하지 않는 유저 접근")
    @Test
    public void 로그인_실패_존재하지않는유저(){
        //given
        UserSignInRequest request = UserSignInRequest.builder().phoneNum("01012341234").build();

        lenient().when(userRepository.findByPhoneNum(request.getPhoneNum())).thenReturn(Optional.ofNullable(null));

        //then
        assertThrows(UserNotFoundException.class, () -> {
            userService.signIn(request);
        });
    }

    @DisplayName("로그인_탈퇴한 유저 접근")
    @Test
    public void 로그인_실패_권한없는유저(){
        //given
        UserSignInRequest request = UserSignInRequest.builder().phoneNum("01012341234").build();

        User actualUser = User.builder().userId(UUID.randomUUID().toString()).image("userprof847.png").nickname("testNick").phoneNum(request.getPhoneNum()).build();
        actualUser.setUserType(UserType.DELETE);

        lenient().when(userRepository.findByPhoneNum(request.getPhoneNum())).thenReturn(Optional.of(actualUser));

        //then
        assertThrows(UserNotAllowedException.class, () -> {
            userService.signIn(request);
        });
    }

    @DisplayName("유저_닉네임_중복체크_성공(중복되지않음)")
    @Test
    public void 유저_닉네임_중복체크_성공(){
        //given
        UserNicknameRequest request = UserNicknameRequest.builder().nickName("dupNick").build();

        //stub
        lenient().when(userRepository.findByNickname(request.getNickName())).thenReturn(Optional.empty());

        //then
        assertEquals(userService.checkNickname(request),true);
    }

    @DisplayName("유저_닉네임_중복체크_실패(중복됨)")
    @Test
    public void 유저_닉네임_중복체크_실패(){
        //given
        UserNicknameRequest request = UserNicknameRequest.builder().nickName("dupNick").build();
        User actualUser = User.builder().userId(UUID.randomUUID().toString()).image("userprof847.png").nickname("testNick").phoneNum("01012341234").build();

        //when
        lenient().when(userRepository.findByNickname(request.getNickName())).thenReturn(Optional.of(actualUser));

        //then
        assertThrows(UserAlreadyNickName.class, () -> {
            userService.checkNickname(request);
        });
    }

    @DisplayName("유저_회원탈퇴_성공")
    @Test
    public void 유저_회원탈퇴_성공(){
        //given
        User actualUser = createUSer();
        String reason = "알림이 너무 많이 와요.";

        //when
        lenient().when(userRepository.findByUserId(actualUser.getUserId())).thenReturn(Optional.of(actualUser));

        //then
        assertEquals(userService.deleteUser(actualUser.getUserId(),reason),true);
    }

    @DisplayName("유저_회원탈퇴_실패_이미 탈퇴한 유저")
    @Test
    public void 유저_회원탈퇴_실패(){
        //given
        User actualUser = createUSer();
        String reason = "알림이 너무 많이 와요.";
        actualUser.setUserType(UserType.DELETE);

        //when
        lenient().when(userRepository.findByUserId(actualUser.getUserId())).thenReturn(Optional.of(actualUser));

        //then
        assertThrows(UserAlreadyDeleteException.class, () -> {
            userService.deleteUser(actualUser.getUserId(),reason);
        });
    }


    @DisplayName("유저_회원탈퇴_실패_존재하지 않는 유저")
    @Test
    public void 유저_회원탈퇴_실패_존재하지않는유저(){
        //given
        User actualUser = createUSer();
        String reason = "알림이 너무 많이 와요.";

        //when
        lenient().when(userRepository.findByUserId(actualUser.getUserId())).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(actualUser.getUserId(),reason);
        });
    }

    @DisplayName("유저_로그아웃_성공")
    @Test
    public void 유저_로그아웃_성공(){
        //given
        User actualUser = createUSer();

        //when
        lenient().when(userRepository.findByUserId(actualUser.getUserId())).thenReturn(Optional.of(actualUser));

        //then
        assertEquals(userService.logout(actualUser.getUserId()),true);
    }

    @DisplayName("유저_로그아웃_실패_존재하지 않는 유저의 접근")
    @Test
    public void 유저_로그아웃_실패(){
        //given
        User actualUser = createUSer();

        //when
        lenient().when(userRepository.findByUserId(actualUser.getUserId())).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class, () -> {
            userService.logout(actualUser.getUserId());
        });
    }

    @DisplayName("친구찾기(검색)_성공")
    @Test
    public void 친구찾기_성공(){
        //given
        User user1 = createUSer("테스트1");
        User friendUser = createUSer("테스트2");
        User unFriendUser = createUSer("테스트3");
        List<User> friends = List.of(friendUser,unFriendUser);
        Follow follow = Follow.builder().fromUser(user1).toUser(friendUser).build();

        //when
        lenient().when(userRepository.findByUserId(user1.getUserId())).thenReturn(Optional.of(user1));
        lenient().when(userRepository.findByNicknameStartsWithAndUserIdNot("테스트",user1.getUserId())).thenReturn(friends);
        lenient().when(followRepository.findByToUserAndFromUser(friends.get(0),user1)).thenReturn(Optional.of(follow));
        lenient().when(followRepository.findByToUserAndFromUser(friends.get(1),user1)).thenReturn(Optional.empty());

        List<FriendSearchResponse> friendSearchResponses = userService.searchFriends("테스트", user1.getUserId(),Pageable.ofSize(10));

        //then
        assertAll(
                () -> assertEquals(friendSearchResponses.get(0).isFriend(),true),
                () -> assertEquals(friendSearchResponses.get(0).getFriendUserId(),friendUser.getUserId()),
                () -> assertEquals(friendSearchResponses.get(0).getFriendNickName(),friendUser.getNickname()),
                () -> assertEquals(friendSearchResponses.get(1).isFriend(),false),
                () -> assertEquals(friendSearchResponses.get(1).getFriendUserId(),unFriendUser.getUserId()),
                () -> assertEquals(friendSearchResponses.get(1).getFriendNickName(),unFriendUser.getNickname())
        );
    }

    @DisplayName("친구_추가_성공")
    @Test
    public void 친구_추가_성공(){
        //given
        User fromUser = createUSer("테스트1");
        User toUser = createUSer("테스트2");
        Follow follow = Follow.builder().toUser(toUser).fromUser(fromUser).build();

        //when
        when(userRepository.findByUserId(fromUser.getUserId())).thenReturn(Optional.of(fromUser));
        when(userRepository.findByNickname(toUser.getNickname())).thenReturn(Optional.of(toUser));
        lenient().when(followRepository.save(follow)).thenReturn(follow);

        //then
        assertTrue(userService.addFriend(toUser.getNickname(), fromUser.getUserId()));
    }

    @DisplayName("친구_추가_실패_존재 하지 않는 유저")
    @Test
    public void 친구_추가_실패_존재하지않는유저(){
        //given
        User toUser = createUSer("테스트2");

        //when
        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class, () -> {
            userService.addFriend(toUser.getNickname(), null);
        });
    }

    @DisplayName("친구_추가_실패_존재 하지 않는 친구")
    @Test
    public void 친구_추가_실패_존재하지않는친구(){
        //given
        User fromUser = createUSer("테스트1");

        //when
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(fromUser));
        when(userRepository.findByNickname(any())).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class, () -> {
            userService.addFriend(null, fromUser.getUserId());
        });
    }

    @DisplayName("친구_추가_실패_이미 친구")
    @Test
    public void 친구_추가_실패_이미친구(){
        //given
        User fromUser = createUSer("테스트1");
        User toUser = createUSer("테스트2");
        Follow follow = Follow.builder().toUser(toUser).fromUser(fromUser).build();

        //when
        when(userRepository.findByUserId(fromUser.getUserId())).thenReturn(Optional.of(fromUser));
        when(userRepository.findByNickname(toUser.getNickname())).thenReturn(Optional.of(toUser));
        when(followRepository.findByToUserAndFromUser(toUser,fromUser)).thenReturn(Optional.of(follow));

        //then
        assertThrows(FollowAlreadyException.class, () -> {
            userService.addFriend(toUser.getNickname(), fromUser.getUserId());
        });
    }

    @DisplayName("친구_내 친구 목록 조회")
    @Test
    public void 친구_내친구목록조회(){
        //given
        User fromUser = createUSer("테스트1");
        User friendUser = createUSer("테스트2");
        User friendUser2 = createUSer("테스트3");
        Follow follow1 = Follow.builder().toUser(friendUser).fromUser(fromUser).build();
        Follow follow2 = Follow.builder().toUser(friendUser2).fromUser(fromUser).build();
        List<Follow> followList = List.of(follow1, follow2);

        //when
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(fromUser));
        when(followRepository.findByFromUserId(any())).thenReturn(followList);

        //then
        List<FriendSearchResponse> friendSearchResponses = userService.myFriends(fromUser.getUserId(), Pageable.ofSize(10));
        assertAll(
                () -> assertEquals(friendSearchResponses.get(0).isFriend(),false),
                () -> assertEquals(friendSearchResponses.get(0).getFriendUserId(),friendUser.getUserId()),
                () -> assertEquals(friendSearchResponses.get(0).getFriendNickName(),friendUser.getNickname()),
                () -> assertEquals(friendSearchResponses.get(1).isFriend(),false),
                () -> assertEquals(friendSearchResponses.get(1).getFriendUserId(),friendUser2.getUserId()),
                () -> assertEquals(friendSearchResponses.get(1).getFriendNickName(),friendUser2.getNickname())
        );
    }

    @DisplayName("친구삭제_성공")
    @Test
    public void 친구삭제_성공(){
        //given
        User fromUser = createUSer("테스트1");
        User toUser = createUSer("테스트2");
        Follow follow = Follow.builder().toUser(toUser).fromUser(fromUser).build();

        //when
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(fromUser));
        when(userRepository.findByNickname(any())).thenReturn(Optional.of(toUser));
        when(followRepository.findByToUserAndFromUser(any(),any())).thenReturn(Optional.of(follow));

        //then
        assertTrue(userService.deleteFriend(toUser.getNickname(), fromUser.getUserId()));
    }

    @DisplayName("친구삭제_실패_존재 하지 않는 유저")
    @Test
    public void 친구삭제_실패_존재하지않는유저(){
        //given
        User toUser = createUSer("테스트2");

        //when
        when(userRepository.findByUserId(any())).thenReturn(Optional.empty());
        lenient().when(userRepository.findByNickname(any())).thenReturn(Optional.of(toUser));

        //then
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteFriend(toUser.getNickname(),null);
        });
    }

    @DisplayName("친구삭제_실패_친구가 아닌 경우")
    @Test
    public void 친구삭제_실패_친구가아닌경우(){
        //given
        User fromUser = createUSer("테스트1");
        User toUser = createUSer("테스트2");

        //when
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(fromUser));
        when(userRepository.findByNickname(any())).thenReturn(Optional.of(toUser));
        when(followRepository.findByToUserAndFromUser(any(),any())).thenReturn(Optional.empty());

        //then
        assertThrows(FollowNotFoundException.class, () -> {
            userService.deleteFriend(toUser.getNickname(),fromUser.getUserId());
        });
    }

    @DisplayName("친구삭제_실패_존재 하지 않는 친구")
    @Test
    public void 친구삭제_실패_존재하지않는친구(){
        //given
        User fromUser = createUSer("테스트2");

        //when
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(fromUser));
        lenient().when(userRepository.findByNickname(any())).thenReturn(Optional.empty());

        //then
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteFriend(null,fromUser.getUserId());
        });
    }


    UserSignUpRequest createUserSignUpRequest(){
        return UserSignUpRequest.builder()
                .imageKey("default")
                .nickname("testNick")
                .phoneNum("01012341231").build();
    }

    User createUSer(){
        return User.builder()
                .userId(UUID.randomUUID().toString())
                .image("userprof847.png")
                .nickname("testNick")
                .phoneNum("01012341234")
                .build();
    }

    User createUSer(String nickname){
        return User.builder()
                .userId(UUID.randomUUID().toString())
                .image("userprof847.png")
                .nickname(nickname)
                .phoneNum("01012341234")
                .build();
    }
}
