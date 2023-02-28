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
     *
     * @author 이은비
     */
    @Operation(summary = "목표 생성하기",
                description = "사용자가 목표를 생성한다.")
    @Auth
    @PostMapping
    public ApplicationResponse<GoalResponse> create(
            @RequestBody @Valid GoalCreateRequest request,
            @ApiIgnore @UserId String userId)
    {
        return goalService.create(request, userId);
    }

    @Operation(summary = "목표 한개 조회하기",
            description = "사용자가 목표ID를 통해 특정 목표를 조회한다.")
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
     * 유저가 가진 Goal 전체 조회.
     *
     * @author 이은비
     */
    @Operation(summary = "사용자가 보유한 목표 리스트 조회하기",
        description = "사용자가 보유한 모든 목표 리스트를 조회한다. " +
                        "이때 클라이언트는 반드시 쿼리스트링으로 sort를 명시해주어야 한다. " +
                        "ex) /api/v1/goals/users?sort=endDate,desc" +
                        " --> 목표 종료일자 최신순으로 조회하기")
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
    @Operation(summary = "목표 수정하기",
        description = "사용자가 생성할 때 작성했던 데이터를 수정한다. (종료, 한 줄 코멘트, 소비금액)을 제외하고 수정할 수 있다.")
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
    @Operation(summary = "목표 삭제하기",
        description = "사용자가 목표를 삭제한다.")
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
    @Operation(summary = "목표 종료하기",
        description = "사용자가 목표를 종료한다. 종료 시, 한줄 코멘트를 입력해야 한다." +
                        " 목표 종료일자가 오늘보다 이전이거나 소비 기록이 하나도 없을 경우 종료할 수 있다." +
                        " 목표가 가진 소비 기록에 대해 2번째 감정을 기록하지 않은 경우가 있다면, 목표를 종료할 수 없다.")
    @Auth
    @PutMapping("/end/{goalId}")
    public ApplicationResponse<GoalResponse> terminate(
        @PathVariable Long goalId,
        @RequestBody GoalTerminateRequest request,
        @ApiIgnore @UserId String userId
    ) {
        return goalService.terminate(goalId, request, userId);
    }

    /**
     * 사용자의 종료된 목표 리스트 조회.
     *
     * @author 이은비
     * */
    @Operation(summary = "종료된 목표 조회하기",
        description = "사용자가 종료된 목표를 조회한다.")
    @Auth
    @GetMapping("/users/end")
    public ApplicationResponse<Page<GoalResponse>> findByUsersAndEnd(
        @ApiIgnore @UserId String userId,
        Pageable pageable
    ) {
        return goalService.findByUserAndEnd(userId, pageable);
    }
}
