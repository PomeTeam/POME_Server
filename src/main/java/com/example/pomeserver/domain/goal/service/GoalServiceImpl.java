package com.example.pomeserver.domain.goal.service;

import com.example.pomeserver.domain.goal.dto.assembler.GoalAssembler;
import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalTerminateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalUpdateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalResponse;
import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.exception.excute.GoalAlreadyEndException;
import com.example.pomeserver.domain.goal.exception.excute.GoalCanNotEndException;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryListSizeException;
import com.example.pomeserver.domain.goal.exception.excute.GoalNotFoundException;
import com.example.pomeserver.domain.goal.exception.excute.ThisGoalIsNotByThisUserException;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.exception.excute.UserNotFoundException;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.dto.response.ApplicationResponse;

import com.example.pomeserver.global.event.Activity;
import com.example.pomeserver.global.event.ActivityType;
import com.example.pomeserver.global.event.publisher.UserActivityEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GoalServiceImpl implements GoalService {

  private final GoalRepository goalRepository;
  private final UserRepository userRepository;
  private final GoalAssembler goalAssembler;

  private final UserActivityEventPublisher userActivityEventPublisher;


  @Transactional
  @Override
  public ApplicationResponse<GoalResponse> create(
      GoalCreateRequest request,
      String userId) {
    // (1) 카테고리 리스트의 개수 초과 여부 확인
    User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

    // 활성화된 목표 + 유저가 종료 처리하지 않은 기한이 지난 목표 = 10개 제한
    // isEnd -> 유저가 종료처리한 목표
    if (user.getGoals().stream().filter(goal -> !goal.isEnd()).count() == 10) {
      throw new GoalCategoryListSizeException();
    }

    // (2) 유저가 보유하고 있는 카테고리명 중복 확인 -> 중복 허용

    // (4) DTO <-> Entity
    Goal goal = goalAssembler.toEntity(request, user);

    // (4) Goal 저장
    Goal savedGoal = goalRepository.save(goal);

    return ApplicationResponse.create(GoalResponse.toDto(savedGoal));
  }

  @Override
  public ApplicationResponse<GoalResponse> findById(Long goalId) {
    Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);
    return ApplicationResponse.ok(GoalResponse.toDto(goal));
  }


  @Transactional
  @Override
  public ApplicationResponse<GoalResponse> update(
      GoalUpdateRequest request,
      Long goalId,
      String userId) {
    // (2) Goal 조회
    Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);

    // (3) 유저가 보유한 Goal 인지 확인
    if (!goal.getUser().getUserId().equals(userId)) {
      throw new ThisGoalIsNotByThisUserException();
    }

    // (4) Goal 수정
    goal.edit(goalAssembler.toEntity(request));

    // (5) Goal 저장
    Goal saved = goalRepository.save(goal);

    return ApplicationResponse.ok(GoalResponse.toDto(saved));
  }

  @Transactional
  @Override
  public ApplicationResponse<Void> delete(
      Long goalId,
      String userId) {
    // (1) Goal 조회
    Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);

    // (2) 유저의 삭제 권한 확인
    if (!goal.getUser().getUserId().equals(userId)) {
      throw new ThisGoalIsNotByThisUserException();
    }

    // (3) Goal 삭제
    goalRepository.deleteById(goalId);

    return ApplicationResponse.ok();
  }

  @Override
  public ApplicationResponse<Page<GoalResponse>> findAllByUser(String userId, Pageable pageable) {

    // (1) User 데이터 조회
    User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

    // (2) User가 갖는 목표 카테고리로부터 목표 전체 조회
    return ApplicationResponse.ok(
        goalRepository.findAllByUser(user, pageable).map(GoalResponse::toDto));
  }

  @Transactional
  @Override
  public ApplicationResponse<GoalResponse> terminate(Long goalId, GoalTerminateRequest request,
      String userId) {
    // (1) Goal 데이터 조회
    Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);

    // (2) User 권한 조회
    User user = goal.getUser();
    if (!user.getUserId().equals(userId)) {
      throw new ThisGoalIsNotByThisUserException();
    }

    // (3) 종료 상태여부 확인
    if (goal.isEnd()) {
      throw new GoalAlreadyEndException();
    }

    // (4) 종료일자가 오늘보다 이전인지 확인 -> 소비 기록이 없어도 종료 가능하기에 해당 로직 제외

    // (5) 목표가 보유한 모든 기록이 2번째 감정까지 남겼는지 확인
    for (Record record : goal.getRecords()) {
      if (record.getEmotionRecords().size() == 1) {
        throw new GoalCanNotEndException();
      }
    }

    // (6) 목표 종료 상태 수정 및 저장 ( 종료 시, 목표의 성공 여부 저장 )
    // Goal이 갖는 Record의 usePrice의 합으로 갱신
    int usePrice = 0;
    for (Record record : goal.getRecords()) {
      usePrice += record.getUsePrice();
    }
    boolean isSuccess = usePrice <= goal.getPrice();
    goal.terminate(request, isSuccess);
    Goal saved = goalRepository.save(goal);

    // (7) 목표 성공 이벤트 publish
    if(isSuccess){
      user.getActivityCount().addSuccessRecordCount();
      userActivityEventPublisher.execute(Activity.create(user, ActivityType.SUCCESS_GOAL));
    }

    return ApplicationResponse.ok(GoalResponse.toDto(saved));
  }

  @Override
  public ApplicationResponse<Page<GoalResponse>> findByUserAndEnd(String userId,
      Pageable pageable) {
    // (1) User 데이터 조회
    User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

    // (2) 유저가 보유한 종료된 목표 조회

    return ApplicationResponse.ok(
        goalRepository.findAllByUserAndIsEnd(user, true, pageable).map(GoalResponse::toDto));
  }
}
