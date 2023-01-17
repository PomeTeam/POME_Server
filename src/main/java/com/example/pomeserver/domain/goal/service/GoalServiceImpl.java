package com.example.pomeserver.domain.goal.service;

import com.example.pomeserver.domain.goal.dto.assembler.GoalAssembler;
import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalUpdateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalResponse;
import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.entity.GoalCategory;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryNotFoundException;
import com.example.pomeserver.domain.goal.exception.excute.GoalNotFoundException;
import com.example.pomeserver.domain.goal.exception.excute.ThisGoalIsNotByThisUserException;
import com.example.pomeserver.domain.goal.repository.GoalCategoryRepository;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
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

    @Transactional
    @Override
    public ApplicationResponse<GoalResponse> create(
            GoalCreateRequest request,
            String userId)
    {
        // (1) 목표 카테고리 조회
        GoalCategory goalCategory = goalCategoryRepository.findById(request.getGoalCategoryId()).orElseThrow(GoalCategoryNotFoundException::new);
        System.out.println(goalCategory.getGoals().size());
        // (2) DTO <-> Entity
        Goal goal = goalAssembler.toEntity(request, goalCategory);
        System.out.println(goal.getPrice());
        // (3) Goal 저장
        Goal saved = goalRepository.save(goal);

        return ApplicationResponse.create(GoalResponse.toDto(saved));
    }

    @Override
    public ApplicationResponse<GoalResponse> findById(Long goalId)
    {
        Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);
        return ApplicationResponse.ok(GoalResponse.toDto(goal));
    }

    @Override
    public ApplicationResponse<Page<GoalResponse>> findAllByUser(
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
}
