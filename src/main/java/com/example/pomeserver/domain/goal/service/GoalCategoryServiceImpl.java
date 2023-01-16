package com.example.pomeserver.domain.goal.service;

import com.example.pomeserver.domain.goal.dto.request.GoalCategoryCreateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalCategoryResponse;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class GoalCategoryServiceImpl implements GoalCategoryService {

    @Transactional
    @Override
    public ApplicationResponse<GoalCategoryResponse> create(GoalCategoryCreateRequest request, String userId) {
        return null;
    }

    @Override
    public ApplicationResponse<List<GoalCategoryResponse>> findAll() {
        return null;
    }

    @Transactional
    @Override
    public ApplicationResponse<Void> delete(Long goalCategoryId, String userId) {
        return null;
    }
}
