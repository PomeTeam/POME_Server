package com.example.pomeserver.domain.record.controller;

import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordSecondEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordToFriendEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.dto.response.record.RecordResponse;
import com.example.pomeserver.domain.record.service.RecordService;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.authResolver.Auth;
import com.example.pomeserver.global.util.authResolver.UserId;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
@RestController
@Api(tags = "Record 관련 API")
public class RecordController {

    private final RecordService recordService;

    /**
     * 기록 작성 기능
     * @Author 이찬영
     */
    @Operation(summary = "기록 작성하기",
               description = "사용자가 자신의 목표에 대한 소비 기록을 작성한다.")
    @Auth
    @PostMapping
    public ApplicationResponse<RecordResponse> create(
            @RequestBody @Valid RecordCreateRequest request,
            @ApiIgnore @UserId String userId)
    {
        return recordService.create(request, userId);
    }

    /**
     * 기록에 두번째 감정 남기기 기능
     * @Author 이찬영
     */
    @Operation(summary = "기록에 두번째 감정 남기기",
               description = "사용자가 자신의 기록에 대한 두번째 감정을 남긴다.")
    @Auth
    @PostMapping("/{recordId}/second-emotion")
    public ApplicationResponse<RecordResponse> writeSecondEmotion(
            @RequestBody @Valid RecordSecondEmotionRequest request,
            @PathVariable Long recordId,
            @ApiIgnore @UserId String userId)
    {
        return recordService.writeSecondEmotion(request, recordId, userId);
    }

    /**
     * 친구의 기록에 감정 남기기 기능
     * @Author 이찬영
     */
    @Operation(summary = "친구의 기록에 감정 남기기",
               description = "사용자가 자신의 친구의 기록에 대해 감정을 남긴다.")
    @Auth
    @PostMapping("/{recordId}/friend-emotion")
    public ApplicationResponse<RecordResponse> writeEmotionToFriend(
            @RequestBody @Valid RecordToFriendEmotionRequest request,
            @PathVariable Long recordId,
            @ApiIgnore @UserId String userId)
    {
        return recordService.writeEmotionToFriend(request, recordId, userId);
    }

    /**
     * 기록 조회 기능
     * @Author 이찬영
     */
    @Operation(summary = "나의 기록 상세 조회",
            description = "기록 한개 상세 조회")
    @GetMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> findById(@PathVariable Long recordId, @ApiIgnore @UserId String userId)
    {
        return recordService.findById(recordId, userId);
    }

    /**
     * 내 친구들의 기록 조회 페이징 기능
     * @Author 이찬영
     */
    @Operation(summary = "기록 페이징 조회 By 친구들",
            description = "특정 사용자 친구들의 기록들을 페이징 조회한다."+
                    "이때 클라이언트는 반드시 쿼리스트링으로 size와 page를 명시해 주어야 한다. ex) /api/v1/records/goal/1?page=0&size=10" +
                    " --> 맨 첫 페이지(0페이지)부터 10개 가져오기")
    @Auth
    @GetMapping("/friends")
    public ApplicationResponse<List<RecordResponse>> findAllByFriends(
            @ApiIgnore @UserId String userId,
            Pageable pageable)
    {
        return recordService.findAllByFriends(userId, pageable);
    }

    /**
     * 특정 User의 기록 조회 페이징 기능
     * @Author 이찬영
     */
    @Operation(summary = "나의 기록들 페이징 조회 By User",
            description = "특정 사용자의 기록들을 페이징 조회한다. 이때 사용자의 userId로 기록을 불러온다. "+
            "이때 클라이언트는 반드시 쿼리스트링으로 size와 page를 명시해 주어야 한다. ex) /api/v1/records/goal/1?page=0&size=10" +
            " --> 맨 첫 페이지(0페이지)부터 10개 가져오기")
    @GetMapping("/users/{userId}")
    public ApplicationResponse<List<RecordResponse>> findAllByUser(
            @PathVariable String userId,
            Pageable pageable)
    {
        return recordService.findAllByUser(userId, pageable);
    }

    /**
     * Goal에 해당하는 기록들 페이징 조회 기능
     * @Author 이찬영
     */
    @Operation(summary = "기록 페이징 조회 By User, Goal",
            description = "특정 사용자와 특정 목표의 기록을 페이징 조회한다. 즉 사용자+목표의 조합으로 기록들을 불러오는 것이다." +
                    "이때 클라이언트는 반드시 쿼리스트링으로 size와 page를 명시해 주어야 한다. ex) /api/v1/records/goal/1?page=0&size=10" +
                    " --> 맨 첫 페이지(0페이지)부터 10개 가져오기")
    @Auth
    @GetMapping("/goal/{goalId}")
    public ApplicationResponse<Page<RecordResponse>> findAllByUserAndGoal(
            @PathVariable Long goalId,
            @ApiIgnore @UserId String userId,
            Pageable pageable)
    {
        return recordService.findAllByUserAndGoal(goalId, userId, pageable);
    }

    /**
     * 기록일로부터 1주일이 지난 기록들 조회 기능
     * @Author 이찬영
     */
    @Operation(summary = "일주일이 지난 나의 기록들 조회",
            description = "첫번째 소비 기록일(==기록 생성일)로 부터 1주가 지난 기록들(즉 2차 감정을 남겨여하는 기록들) 조회")
    @Auth
    @GetMapping("/one-week")
    public ApplicationResponse<List<RecordResponse>> findAllOneWeek(
            @ApiIgnore @UserId String userId)
    {
        return recordService.findAllOneWeek(userId);
    }
    /**
     * 기록 수정 기능
     * @Author 이찬영
     */
    @Operation(summary = "기록 수정",
            description = "사용자가 작성한 기록을 수정한다. 소비 순간의 감정을 제외하고 모든 것(소비 금액, 소비 날짜, 소비 코멘트)을 수정할 수 있다.")
    @Auth
    @PutMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> update(
            RecordUpdateRequest request,
            @PathVariable Long recordId,
            @ApiIgnore @UserId String userId)
    {
        return recordService.update(request, recordId, userId);
    }

    /**
     * 기록 삭제 기능
     * @Author 이찬영
     */
    @Operation(summary = "기록 삭제",
            description = "사용자가 작성한 기록을 삭제한다.")
    @Auth
    @DeleteMapping("/{recordId}")
    public ApplicationResponse<Void> delete(
            @PathVariable Long recordId,
            @ApiIgnore @UserId String userId)
    {
        return recordService.delete(recordId, userId);
    }
}
