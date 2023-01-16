package com.example.pomeserver.domain.goal.controller;

import com.example.pomeserver.domain.goal.dto.request.GoalCategoryCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalUpdateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalCategoryResponse;
import com.example.pomeserver.domain.goal.dto.response.GoalResponse;
import com.example.pomeserver.domain.goal.service.GoalCategoryService;
import com.example.pomeserver.domain.goal.service.GoalService;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.authResolver.Admin;
import com.example.pomeserver.global.util.authResolver.Auth;
import com.example.pomeserver.global.util.authResolver.UserId;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/goal-category")
@RestController
@Api(tags = "Goal Category 관련 API")
public class GoalCategoryController {

    private final GoalCategoryService goalCategoryService;

    /**
     * 목표 카테고리 작성 기능
     * @Author 이찬영
     */
    @Auth
    @PostMapping
    public ApplicationResponse<GoalCategoryResponse> create(
            GoalCategoryCreateRequest request,
            @Admin String userId)
    {
        return goalCategoryService.create(request, userId);
    }

    /**
     * 목표 카테고리 전체 조회
     * @Author 이찬영
     */
    @Auth
    @GetMapping
    public ApplicationResponse<List<GoalCategoryResponse>> findAll()
    {
        return goalCategoryService.findAll();
    }

    /**
     * 목표 카테고리 삭제 기능
     * @Author 이찬영
     */
    @Auth
    @DeleteMapping("/{goalCategoryId}")
    public ApplicationResponse<Void> delete(
            @PathVariable Long goalCategoryId,
            @Admin String userId)
    {
        return goalCategoryService.delete(goalCategoryId, userId);
    }
}
