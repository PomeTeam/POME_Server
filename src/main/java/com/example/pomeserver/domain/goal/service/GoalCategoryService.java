package com.example.pomeserver.domain.goal.service;

import com.example.pomeserver.domain.goal.dto.request.GoalCategoryCreateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalCategoryResponse;
import com.example.pomeserver.global.dto.response.ApplicationResponse;

import java.util.List;

public interface GoalCategoryService {
    ApplicationResponse<GoalCategoryResponse> create(GoalCategoryCreateRequest request, String userId);
    ApplicationResponse<List<GoalCategoryResponse>> findAll();
    ApplicationResponse<Void> delete(Long goalCategoryId, String userId);
}
