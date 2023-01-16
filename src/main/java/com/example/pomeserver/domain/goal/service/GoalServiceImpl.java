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
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        GoalCategory goalCategory = goalCategoryRepository.findById(request.getGoalCategoryId()).orElseThrow(GoalCategoryNotFoundException::new);
        Goal goal = goalAssembler.toEntity(request, user, goalCategory);
        goalRepository.save(goal);
        return ApplicationResponse.create(GoalResponse.toDto(goal));
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
            Pageable pageable)
    {
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        return ApplicationResponse.ok(goalRepository.findAllByUser(user, pageable).map(GoalResponse::toDto));
    }

    @Transactional
    @Override
    public ApplicationResponse<GoalResponse> update(
            GoalUpdateRequest request,
            Long goalId,
            String userId)
    {
        GoalCategory goalCategory = goalCategoryRepository.findById(request.getGoalCategoryId()).orElseThrow(GoalCategoryNotFoundException::new);
        Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);
        if(!goal.getUser().getUserId().equals(userId)) throw new ThisGoalIsNotByThisUserException();
        goal.edit(goalAssembler.toEntity(request, goalCategory));
        return null;
    }

    @Transactional
    @Override
    public ApplicationResponse<Void> delete(
            Long goalId,
            String userId)
    {
        Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);
        if(!goal.getUser().getUserId().equals(userId)) throw new ThisGoalIsNotByThisUserException();
        goalRepository.deleteById(goalId);
        return ApplicationResponse.ok();
    }
}
