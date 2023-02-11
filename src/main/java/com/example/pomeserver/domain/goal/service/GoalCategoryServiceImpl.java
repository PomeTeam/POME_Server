package com.example.pomeserver.domain.goal.service;

import com.example.pomeserver.domain.goal.dto.assembler.GoalCategoryAssembler;
import com.example.pomeserver.domain.goal.dto.request.GoalCategoryCreateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalCategoryResponse;
import com.example.pomeserver.domain.goal.entity.GoalCategory;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryDuplicationException;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryListSizeException;
import com.example.pomeserver.domain.goal.exception.excute.GoalCategoryNotFoundException;
import com.example.pomeserver.domain.goal.exception.excute.ThisGoalIsNotByThisUserException;
import com.example.pomeserver.domain.goal.repository.GoalCategoryRepository;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.exception.excute.UserNotFoundException;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GoalCategoryServiceImpl implements GoalCategoryService {

    private final GoalCategoryRepository goalCategoryRepository;
    private final UserRepository userRepository;

    private final GoalCategoryAssembler goalCategoryAssembler;


    @Override
    public ApplicationResponse<List<GoalCategoryResponse>> findAll(String userId) {
        // (1) 유저가 보유하고 있는 카테고리 리스트 조회
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);

        List<GoalCategoryResponse> lists = goalCategoryRepository.findAllByUser(user)
                .stream()
                .map(GoalCategoryResponse::toDto)
                .collect(Collectors.toList());
        return ApplicationResponse.ok(lists);
    }

    @Transactional
    @Override
    public ApplicationResponse<Void> delete(Long goalCategoryId, String userId) {
        // (1) 삭제할 카테고리 ID를 가진 목표 카테고리 조회
        GoalCategory goalCategory = goalCategoryRepository.findById(goalCategoryId).orElseThrow(
            GoalCategoryNotFoundException::new);

        // (2) 유저가 보유하고 있는 지 확인
        if (!goalCategory.getUser().getUserId().equals(userId))
            throw new ThisGoalIsNotByThisUserException();

        // (3) 목표 카테고리 삭제
        goalCategoryRepository.deleteById(goalCategoryId);
        return ApplicationResponse.ok();
    }
}
