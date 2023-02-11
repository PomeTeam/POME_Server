package com.example.pomeserver.domain.goal.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.example.pomeserver.domain.goal.dto.assembler.GoalAssembler;
import com.example.pomeserver.domain.goal.dto.assembler.GoalCategoryAssembler;
import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalUpdateRequest;
import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.entity.GoalCategory;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryDuplicationException;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryListSizeException;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryNotFoundException;
import com.example.pomeserver.domain.goal.exception.excute.GoalNotFoundException;
import com.example.pomeserver.domain.goal.exception.excute.ThisGoalIsNotByThisUserException;
import com.example.pomeserver.domain.goal.repository.GoalCategoryRepository;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.exception.excute.UserNotFoundException;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.domain.user.service.UserServiceImpl;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {

  @InjectMocks
  GoalServiceImpl goalService;
  @Mock
  GoalRepository goalRepository;
  @Mock
  GoalCategoryRepository goalCategoryRepository;
  @Mock
  UserRepository userRepository;
  @InjectMocks
  UserServiceImpl userService;

  @Test
  @DisplayName("목표 생성하기_성공")
  void 목표생성_성공() {
    // given for creating goal
    GoalCreateRequest request = new GoalCreateRequest();
    request.setName("음료");
    request.setPrice(50000);
    request.setIsPublic(false);
    request.setStartDate("2023.01.01");
    request.setEndDate("2023.03.01");
    request.setOneLineMind("방학에는 커피를 줄여보자.");
    // given for user
    String userId = UUID.randomUUID().toString();
    Optional<User> user = Optional.of(User.builder().userId(userId).nickname("POME").build());

    when(userRepository.findByUserId(anyString())).thenReturn(user);
    assertEquals(userService.getUserNickName(userId), "POME");

    GoalCategory goalCategoryEntity = GoalCategory.builder().name("음료").user(user.get()).build();
    lenient().when(goalCategoryRepository.save(any())).thenReturn(goalCategoryEntity);

    Goal goal = Goal.builder().goalCategory(goalCategoryEntity).endDate(request.getEndDate())
        .startDate(request.getStartDate()).isPublic(request.getIsPublic())
        .oneLineMind(request.getOneLineMind()).build();
    lenient().when(goalRepository.save(any())).thenReturn(goal);

    assertThrows(GoalCategoryDuplicationException.class, () -> {
      goalService.create(request, userId);
    });
  }

  @Test
  @DisplayName("유저 정보 없이 목표 생성할 경우")
  void 유저없이_목표생성() {
    // given for creating goal
    GoalCreateRequest request = new GoalCreateRequest();
    request.setName("커피");
    request.setPrice(50000);
    request.setIsPublic(false);
    request.setStartDate("2023.01.01");
    request.setEndDate("2023.03.01");
    request.setOneLineMind("방학에는 커피를 줄여보자.");

    // when-then
    assertThrows(UserNotFoundException.class, () -> {
      goalService.create(request, null);
    });
  }

  @Test
  @DisplayName("유저가 갖는 카테고리 개수 초과")
  void 카테고리개수_초과() {
    // given for user
    String userId = UUID.randomUUID().toString();
    Optional<User> user = Optional.of(User.builder().userId(userId).nickname("POME").build());

    when(userRepository.findByUserId(anyString())).thenReturn(user);

    // size of goal categories user has is 10
    for (int i = 0; i < 5; i++) {
      user.get().getGoals().add(
          Goal.builder().goalCategory(
              GoalCategory.builder().name("a" + i).user(user.get()).build()
          ).build()
      );
    }

    // given for creating goal
    GoalCreateRequest request = new GoalCreateRequest();
    request.setName("커피");
    request.setPrice(50000);
    request.setIsPublic(false);
    request.setStartDate("2023.01.01");
    request.setEndDate("2023.03.01");
    request.setOneLineMind("방학에는 커피를 줄여보자.");

    // when-then
    assertThrows(GoalCategoryListSizeException.class, () -> {
      goalService.create(request, userId);
    });
  }

  @Test
  @DisplayName("목표 ID로 데이터 조회")
  void 목표단일_조회_성공() {
    // given for user
    String userId = UUID.randomUUID().toString();
    Optional<User> user = Optional.of(User.builder().userId(userId).nickname("POME").build());

    // given for getting goal
    Optional<Goal> mockGoal = Optional.of(Goal.builder()
        .goalCategory(GoalCategory.builder().name("커피").user(user.get()).build())
        .startDate("2023.01.01")
        .endDate("2023.03.01")
        .isPublic(false)
        .price(50000)
        .oneLineMind("방학에는 커피를 줄여보자.")
        .build());

    // when-then
    when(goalRepository.findById(anyLong())).thenReturn(mockGoal);
    assertThat(goalService.findById(1L).getData().getPrice()).isEqualTo(50000);
  }

  @Test
  @DisplayName("목표 ID로 데이터 조회 실패")
  void 목표단일_조회_실패() {

    assertThrows(GoalNotFoundException.class, () -> {
      goalService.findById(1L);
    });
  }

  @Test
  @DisplayName("목표 수정 - 카테고리 없음")
  void 목표수정_실패_카테고리없음() {
    // given for user
    String userId = UUID.randomUUID().toString();
    // given for updating goal
    GoalUpdateRequest request = new GoalUpdateRequest();
    request.setPrice(50000);
    request.setIsPublic(true);
    request.setStartDate("2023.01.01");
    request.setEndDate("2023.03.01");
    request.setOneLineMind("방학에는 커피를 줄여보자.");
    request.setGoalCategoryId(1L);

    assertThrows(GoalCategoryNotFoundException.class, () -> {
      goalService.update(request, 1L, userId);
    });
  }

  @Test
  @DisplayName("목표 수정 - 목표없음")
  void 목표수정_실패_목표없음() {
    // given for user
    String userId = UUID.randomUUID().toString();
    // given for updating goal
    GoalUpdateRequest request = new GoalUpdateRequest();
    request.setPrice(50000);
    request.setIsPublic(true);
    request.setStartDate("2023.01.01");
    request.setEndDate("2023.03.01");
    request.setOneLineMind("방학에는 커피를 줄여보자.");
    request.setGoalCategoryId(1L);

    Optional<User> user = Optional.of(User.builder().build());
    Optional<GoalCategory> goalCategory = Optional.of(GoalCategory.builder().name("커피").user(user.get()).build());
    when(goalCategoryRepository.findById(anyLong())).thenReturn(goalCategory);

    assertThrows(GoalNotFoundException.class, () -> {
      goalService.update(request, 1L, userId);
    });
  }

  @Test
  @DisplayName("목표 수정 - 유저소유아님")
  void 목표수정_유저소유아님() {
    // given for user
    String userId = UUID.randomUUID().toString();
    // given for updating goal
    GoalUpdateRequest request = new GoalUpdateRequest();
    request.setPrice(50000);
    request.setIsPublic(true);
    request.setStartDate("2023.01.01");
    request.setEndDate("2023.03.01");
    request.setOneLineMind("방학에는 커피를 줄여보자.");
    request.setGoalCategoryId(1L);

    Optional<User> user = Optional.of(User.builder().userId(userId).build());
    Optional<GoalCategory> goalCategory = Optional.of(GoalCategory.builder().name("커피").user(user.get()).build());
    Optional<Goal> goal = Optional.of(Goal.builder().goalCategory(goalCategory.get()).build());

    when(goalCategoryRepository.findById(anyLong())).thenReturn(goalCategory);
    when(goalRepository.findById(anyLong())).thenReturn(goal);

    assertThrows(ThisGoalIsNotByThisUserException.class, () -> {
      goalService.update(request, 1L, "tester");
    });

  }
}