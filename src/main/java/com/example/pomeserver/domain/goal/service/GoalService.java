package com.example.pomeserver.domain.goal.service;

import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalTerminateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalUpdateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalResponse;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoalService {
    ApplicationResponse<GoalResponse> create(GoalCreateRequest request, String userId);

    ApplicationResponse<GoalResponse> findById(Long goalId);

//    ApplicationResponse<Page<GoalResponse>> findAllByUserCategory(String userId, Long goalCategoryId,
//        Pageable pageable);

    ApplicationResponse<GoalResponse> update(GoalUpdateRequest request, Long goalId, String userId);

    ApplicationResponse<Void> delete(Long goalId, String userId);

    ApplicationResponse<Page<GoalResponse>> findAllByUser(String userId, Pageable pageable);

  ApplicationResponse<GoalResponse> terminate(Long goalId, GoalTerminateRequest request, String userId);

  ApplicationResponse<Page<GoalResponse>> findByUserAndEnd(String userId, Pageable pageable);
}
