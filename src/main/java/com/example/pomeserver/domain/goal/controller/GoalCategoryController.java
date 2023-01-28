package com.example.pomeserver.domain.goal.controller;

import com.example.pomeserver.domain.goal.dto.request.GoalCategoryCreateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalCategoryResponse;
import com.example.pomeserver.domain.goal.service.GoalCategoryService;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.authResolver.Auth;
import com.example.pomeserver.global.util.authResolver.UserId;
import io.swagger.annotations.Api;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/goal-category")
@RestController
@Api(tags = "Goal Category 관련 API")
public class GoalCategoryController {

    private final GoalCategoryService goalCategoryService;

    /**
     * Goal Category 생성 ( 한 번에 1개만 생성 가능 )
     * 단, 최대 개수는 10개이다.
     *
     * @author 이은비
     */
    @Auth
    @PostMapping
    public ApplicationResponse<GoalCategoryResponse> create(
            @RequestBody @Valid GoalCategoryCreateRequest request,
            @UserId String userId)
    {
        return goalCategoryService.create(request, userId);
    }

    /**
     * 유저의 Goal Category 전체 조회.
     *
     * @author 이은비
     */
    @Auth
    @GetMapping
    public ApplicationResponse<List<GoalCategoryResponse>> findAll(@UserId String userId)
    {
        return goalCategoryService.findAll(userId);
    }

    /**
     * Goal Category 단일 삭제.
     *
     * @author 이은비
     */
    @Auth
    @DeleteMapping("/{goalCategoryId}")
    public ApplicationResponse<Void> delete(
            @PathVariable Long goalCategoryId,
            @UserId String userId)
    {
        return goalCategoryService.delete(goalCategoryId, userId);
    }
}
