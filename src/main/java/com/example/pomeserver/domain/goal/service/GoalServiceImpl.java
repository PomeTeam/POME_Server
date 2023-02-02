package com.example.pomeserver.domain.goal.service;

import com.example.pomeserver.domain.goal.dto.assembler.GoalAssembler;
import com.example.pomeserver.domain.goal.dto.assembler.GoalCategoryAssembler;
import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalTerminateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalUpdateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalResponse;
import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.entity.GoalCategory;
import com.example.pomeserver.domain.goal.exception.excute.GoalAlreadyEndException;
import com.example.pomeserver.domain.goal.exception.excute.GoalCanNotEndException;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryDuplicationException;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryListSizeException;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryNotFoundException;
import com.example.pomeserver.domain.goal.exception.excute.GoalNotFoundException;
import com.example.pomeserver.domain.goal.exception.excute.ThisGoalIsNotByThisUserException;
import com.example.pomeserver.domain.goal.repository.GoalCategoryRepository;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.exception.excute.UserNotFoundException;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GoalServiceImpl implements GoalService{

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final GoalCategoryRepository goalCategoryRepository;
    private final GoalAssembler goalAssembler;

    private final GoalCategoryAssembler goalCategoryAssembler;


    @Transactional
    @Override
    public ApplicationResponse<GoalResponse> create(
            GoalCreateRequest request,
            String userId)
    {
        // (1) 카테고리 리스트의 개수 초과 여부 확인
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        if (user.getGoalCategories().size() == 10) {
            throw new GoalCategoryListSizeException();
        }

        // (2) 유저가 보유하고 있는 카테고리명 중복 확인
        boolean distinct = user.getGoalCategories().stream()
            .anyMatch(goalCategory -> goalCategory.getName().equals(request.getName()));
        if (distinct) {
            throw new GoalCategoryDuplicationException();
        }

        // (3) 카테고리 생성
        GoalCategory goalCategory = goalCategoryAssembler.toEntity(request.getName(), user);
        GoalCategory saved = goalCategoryRepository.save(goalCategory);


        // (2) DTO <-> Entity
        Goal goal = goalAssembler.toEntity(request, saved);

        // (3) Goal 저장
        Goal savedGoal = goalRepository.save(goal);

        return ApplicationResponse.create(GoalResponse.toDto(savedGoal));
    }

    @Override
    public ApplicationResponse<GoalResponse> findById(Long goalId)
    {
        Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);
        return ApplicationResponse.ok(GoalResponse.toDto(goal));
    }

    @Override
    public ApplicationResponse<Page<GoalResponse>> findAllByUserCategory(
            String userId,
        Long goalCategoryId, Pageable pageable)
    {
        // (1) 유저 데이터 조회
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        // (2) 유저가 보유한 목표 카테고리 리스트에서 goalCategoryId를 가진 데이터 조회
        GoalCategory goalCategory = user.getGoalCategories().stream()
            .filter(category -> category.getId().equals(goalCategoryId))
            .findFirst()
            .orElseThrow(GoalCategoryNotFoundException::new);
        // (3) GoalCategory가 갖는 Goal 리스트 조회
        return ApplicationResponse.ok(goalRepository.findAllByGoalCategory(goalCategory, pageable).map(GoalResponse::toDto));
    }

    @Transactional
    @Override
    public ApplicationResponse<GoalResponse> update(
            GoalUpdateRequest request,
            Long goalId,
            String userId)
    {
        // (1) GoalCategory 조회
        GoalCategory goalCategory = goalCategoryRepository.findById(request.getGoalCategoryId()).orElseThrow(GoalCategoryNotFoundException::new);

        // (2) Goal 조회
        Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);

        // (3) 유저가 보유한 Goal Category 인지 확인
        if(!goalCategory.getUser().getUserId().equals(userId)) throw new ThisGoalIsNotByThisUserException();

        // (4) Goal 수정
        goal.edit(goalAssembler.toEntity(request, goalCategory));

        // (5) Goal 저장
        Goal saved = goalRepository.save(goal);

        return ApplicationResponse.ok(GoalResponse.toDto(saved));
    }
    @Transactional
    @Override
    public ApplicationResponse<Void> delete(
            Long goalId,
            String userId)
    {
        // (1) Goal 조회
        Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);

        // (2) 유저의 삭제 권한 확인
        if(!goal.getGoalCategory().getUser().getUserId().equals(userId)) throw new ThisGoalIsNotByThisUserException();

        // (3) Goal 삭제
        goalRepository.deleteById(goalId);

        return ApplicationResponse.ok();
    }

    @Override
    public ApplicationResponse<Page<GoalResponse>> findAllByUser(String userId, Pageable pageable) {

        // (1) User 데이터 조회
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        // (2) User가 갖는 목표 카테고리로부터 목표 전체 조회
        return ApplicationResponse.ok(goalRepository.findAllByUser(user, pageable).map(GoalResponse::toDto));
    }

    @Transactional
    @Override
    public ApplicationResponse<GoalResponse> terminate(Long goalId, GoalTerminateRequest request,
        String userId) {
        // (1) Goal 데이터 조회
        Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);

        // (2) User 권한 조회
        if(!goal.getGoalCategory().getUser().getUserId().equals(userId)) throw new ThisGoalIsNotByThisUserException();

        // (3) 종료 상태여부 확인
        if(goal.isEnd()) {
            throw new GoalAlreadyEndException();
        }

        // (4) 종료일자가 오늘보다 이전인지 확인 -> 소비 기록이 없어도 종료 가능하기에 해당 로직 제외

        // (5) 목표가 보유한 모든 기록이 2번째 감정까지 남겼는지 확인
        for (Record record : goal.getRecords()){
            if (record.getEmotionRecords().size() == 1) {
                throw new GoalCanNotEndException();
            }
        }

        // (6) 목표 종료 상태 수정 및 저장
        goal.terminate(request);
        Goal saved = goalRepository.save(goal);

        return ApplicationResponse.ok(GoalResponse.toDto(saved));
    }

}
