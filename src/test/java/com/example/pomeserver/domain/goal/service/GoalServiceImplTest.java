package com.example.pomeserver.domain.goal.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.repository.GoalCategoryRepository;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
import com.example.pomeserver.domain.user.dto.request.UserSignUpRequest;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.domain.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GoalServiceImplTest {

  @Autowired
  GoalServiceImpl goalService;
  @Autowired
  GoalRepository goalRepository;
  @Autowired
  GoalCategoryRepository goalCategoryRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  UserServiceImpl userService;

  @BeforeEach
  void 회원가입() {
    UserSignUpRequest request = new UserSignUpRequest();
    request.setNickname("POME");
    request.setPhoneNum("010-1234-9876");
    request.setImageKey("default");

    userService.signUp(request);
  }

  @Test
  @DisplayName("목표 생성하기_성공")
  void 목표생성_성공() {
    // given
    GoalCreateRequest request = new GoalCreateRequest();
    request.setName("커피");
    request.setPrice(50000);
    request.setIsPublic(false);
    request.setStartDate("2023.01.01");
    request.setEndDate("2023.03.01");
    request.setOneLineMind("방학에는 커피를 줄여보자.");

    User user = userRepository.findByNickname("POME").get();

    // when
    goalService.create(request, user.getUserId());
    Goal findGoal = goalRepository.findAllByUser(user, null).stream().findFirst().get();

    // then
    assertThat(findGoal.getUser()).isEqualTo(user);
    assertThat(findGoal.getPrice()).isEqualTo(request.getPrice());
  }
}