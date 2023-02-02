package com.example.pomeserver.domain.goal.controller;

import com.example.pomeserver.domain.goal.dto.request.GoalCreateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalTerminateRequest;
import com.example.pomeserver.domain.goal.dto.request.GoalUpdateRequest;
import com.example.pomeserver.domain.goal.dto.response.GoalResponse;
import com.example.pomeserver.domain.goal.service.GoalService;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.authResolver.Auth;
import com.example.pomeserver.global.util.authResolver.UserId;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RequiredArgsConstructor
@RequestMapping("/api/v1/goals")
@RestController
@Api(tags = "Goal 관련 API")
public class GoalController {

    private final GoalService goalService;

    /**
     * Goal 생성
     * 단, Goal Category가 존재해야 한다.
     *
     * @author 이은비
     */
    @Auth
    @PostMapping
    public ApplicationResponse<GoalResponse> create(
            @RequestBody @Valid GoalCreateRequest request,
            @ApiIgnore @UserId String userId)
    {
        return goalService.create(request, userId);
    }

    @Operation(summary = "목표 한개 조회",
            description = "사용자가 목표 하나를 조회한다.")
    /**
     * Goal 단일 조회.
     *
     * @author 이찬영
     */
    @GetMapping("/{goalId}")
    public ApplicationResponse<GoalResponse> findById(@PathVariable Long goalId)
    {
        return goalService.findById(goalId);
    }

    /**
     * 유저가 가진 Goal Category에 해당하는 Goal 전체 조회.
     *
     * @author 이은비
     */
    @Auth
    @GetMapping("/category/{goalCategoryId}")
    public ApplicationResponse<Page<GoalResponse>> findAllByUserCategory(
            @ApiIgnore @UserId String userId,
            @PathVariable Long goalCategoryId,
            Pageable pageable)
    {
        return goalService.findAllByUserCategory(userId, goalCategoryId, pageable);
    }

    /**
     * 유저가 가진 Goal 전체 조회.
     *
     * @author 이은비
     */
    @Auth
    @GetMapping("/users")
    public ApplicationResponse<Page<GoalResponse>> findAllByUser(
        @ApiIgnore @UserId String userId,
        Pageable pageable)
    {
        return goalService.findAllByUser(userId, pageable);
    }

    /**
     * Goal 수정.
     *
     * @author 이은비
     */
    @Auth
    @PutMapping("/{goalId}")
    public ApplicationResponse<GoalResponse> update(
            @RequestBody @Valid GoalUpdateRequest request,
            @PathVariable Long goalId,
            @ApiIgnore @UserId String userId)
    {
        return goalService.update(request, goalId, userId);
    }

    /**
     * Goal 삭제.
     *
     * @author 이은비
     */
    @Auth
    @DeleteMapping("/{goalId}")
    public ApplicationResponse<Void> delete(
            @PathVariable Long goalId,
            @ApiIgnore @UserId String userId)
    {
        return goalService.delete(goalId, userId);
    }

    /**
     * Goal 종료.
     *
     * @author 이은비
     * */
    @Auth
    @PutMapping("/end/{goalId}")
    public ApplicationResponse<GoalResponse> terminate(
        @PathVariable Long goalId,
        @RequestBody GoalTerminateRequest request,
        @ApiIgnore @UserId String userId
    ) {
        return goalService.terminate(goalId, request, userId);
    }

    @Auth
    @GetMapping("/users/end")
    public ApplicationResponse<Page<GoalResponse>> findByUsersAndEnd(
        @ApiIgnore @UserId String userId,
        Pageable pageable
    ) {
        return goalService.findByUserAndEnd(userId, pageable);
    }
}
