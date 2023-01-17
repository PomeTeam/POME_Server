package com.example.pomeserver.domain.goal.controller;

import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalUpdateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalResponse;
import com.example.pomeserver.domain.goal.service.GoalService;
import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.dto.response.RecordResponse;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.authResolver.Auth;
import com.example.pomeserver.global.util.authResolver.UserId;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/goals")
@RestController
@Api(tags = "Goal 관련 API")
public class GoalController {

    private final GoalService goalService;

    /**
     * 목표 작성 기능
     * @Author 이찬영
     */
    @Auth
    @PostMapping
    public ApplicationResponse<GoalResponse> create(
            GoalCreateRequest request,
            @UserId String userId)
    {
        return goalService.create(request, userId);
    }

    /**
     * 목표 한개 조회 기능
     * @Author 이찬영
     */
    @GetMapping("/{goalId}")
    public ApplicationResponse<GoalResponse> findById(@PathVariable Long goalId)
    {
        return goalService.findById(goalId);
    }

    /**
     * 특정 사용자의 모든 목표들 조회 기능
     * @Author 이찬영
     */
    @Auth
    @GetMapping("/{goalCategoryId}")
    public ApplicationResponse<Page<GoalResponse>> findAllByUser(
            @UserId String userId,
            @PathVariable Long goalCategoryId,
            Pageable pageable)
    {
        return goalService.findAllByUser(userId, goalCategoryId, pageable);
    }

    /**
     * 목표 수정 기능
     * @Author 이찬영
     */
    @Auth
    @PutMapping("/{goalId}")
    public ApplicationResponse<GoalResponse> update(
            GoalUpdateRequest request,
            @PathVariable Long goalId,
            @UserId String userId)
    {
        return goalService.update(request, goalId, userId);
    }

    /**
     * 목표 삭제 기능
     * @Author 이찬영
     */
    @Auth
    @DeleteMapping("/{goalId}")
    public ApplicationResponse<Void> delete(
            @PathVariable Long goalId,
            @UserId String userId)
    {
        return goalService.delete(goalId, userId);
    }
}
