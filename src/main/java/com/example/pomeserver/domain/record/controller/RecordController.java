package com.example.pomeserver.domain.record.controller;

import com.example.pomeserver.domain.record.dto.paramResolver.param.RecordFilteringParam;
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
    public ApplicationResponse<Page<RecordResponse>> findAllByFriends(
            @ApiIgnore @UserId String userId,
            Pageable pageable)
    {
        return recordService.findAllByFriends(userId, pageable);
    }

    /**
     * 특정 User의 기록 조회 페이징 기능
     * @Author 이찬영
     */
    @Auth
    @Operation(summary = "특정 유저의 기록들 페이징 조회 By User",
            description = "특정 사용자의 기록들을 페이징 조회한다. 이때 사용자의 userId로 기록을 불러온다. "+
            "이때 클라이언트는 반드시 쿼리스트링으로 size와 page를 명시해 주어야 한다. ex) /api/v1/records/goal/1?page=0&size=10" +
            " --> 맨 첫 페이지(0페이지)부터 10개 가져오기")
    @GetMapping("/users/{userId}")
    public ApplicationResponse<Page<RecordResponse>> findAllByUser(
            @PathVariable String userId,
            @ApiIgnore @UserId String viewerId,
            Pageable pageable)
    {
        return recordService.findAllByUser(userId, viewerId, pageable);
    }

    /**
     * Goal에 해당하는 기록들 페이징 조회 기능
     * @Author 이찬영
     */
    /* 회고탭 */
    @Operation(summary = "[회고탭] 기록 페이징 조회",
            description = "회고탭 조회. 조회 조건: User, Goal, Emotion (두번째 감정까지 존재해야한다.)" +
                    "이때 클라이언트는 반드시 쿼리스트링으로 size와 page를 명시해 주어야 한다." +
                    "< 추가적으로 해당 API는 감정 필터링이 가능합니다. 만약 첫번째 감정이 0인 기록만을 조회하고 싶으신 경우에는" +
                    "회고탭api?first_emotion=0 과 같이 보내주시면 됩니다. 만약 감정으로 필터링을 원치 않으시는 경우에는 감정 쿼리스트링(first" +
                    "_emotion 혹은 second_emotion)을 추가하지 않으시면 됩니다. / 두개의 감정을 모두 필터링 조건으로 사용하실 경우에는 마찬가지로 " +
                    "회고탭api?first_emotion=0&second_emotion 입니다.")
    @Auth
    @GetMapping("/goal/{goalId}/retrospection-tab")
    public ApplicationResponse<Page<RecordResponse>> findAllRetrospectionByUserAndGoal(
            @PathVariable Long goalId,
            @ApiIgnore @UserId String userId,
            RecordFilteringParam emotionParam,
            Pageable pageable)
    {
        return recordService.findAllRetrospectionByUserAndGoal(goalId, userId, emotionParam, pageable);
    }

    /* 기록탭: 기록탭은 첫번째 감정만 남긴(두번째 감정은 남기지 않은)기록들만 조회된다. */
    @Operation(summary = "[기록탭] 기록 페이징 조회",
            description = "기록탭 기록 조회. 조회 조건: User, Goal, 두번째 감정이 존재하지 않음, 일주일이 지나지 않음이다." +
                    "이때 클라이언트는 반드시 쿼리스트링으로 size와 page를 명시해 주어야 한다.")
    @Auth
    @GetMapping("/goal/{goalId}/record-tab")
    public ApplicationResponse<Page<RecordResponse>> findAllRecordTabByUserAndGoal(
            @PathVariable Long goalId,
            @ApiIgnore @UserId String userId,
            Pageable pageable)
    {
        return recordService.findAllRecordTabByUserAndGoal(goalId, userId, pageable);
    }

    /**
     * 기록일로부터 1주일이 지난 기록들 조회 기능(2차 감정이 없음)
     * @Author 이찬영
     */
    @Operation(summary = "특정 기록 일주일이 지난 나의 기록들 조회",
            description = "첫번째 소비 기록일(==기록 생성일)로 부터 1주가 지난 기록들(즉 2차 감정을 남겨여하는 기록들) 조회. " +
                    "조회 조건은 User, Goal, 2주가 지남, 두번째 감정 존재 여부가 false(2차 감정이 없음)이다.")
    @Auth
    @GetMapping("/one-week/goal/{goalId}")
    public ApplicationResponse<Page<RecordResponse>> findAllOneWeek(
            @ApiIgnore @UserId String userId,
            @PathVariable Long goalId,
            Pageable pageable)
    {
        return recordService.findAllOneWeekByUserAndGoal(userId, goalId, pageable);
    }


//    /**
//     * 2차 감정까지 존재하는 기록 조회 기능
//     * @Author 이찬영
//     */
//    @Operation(summary = "두번째 감정까지 존재하는 기록들 조회",
//            description = "두번째 감정까지 존재하는 기록을 조회한다. 조회 조건은 User, Goal, 두번째 감정 존재 여부가 true(2차 감점이 있음)이다.")
//    @Auth
//    @GetMapping("/goal/{goalId}/all-emotion")
//    public ApplicationResponse<List<RecordResponse>> findAllHaveSecondEmotion(
//            @ApiIgnore @UserId String userId,
//            @PathVariable Long goalId,
//            Pageable pageable)
//    {
//        return recordService.findAllEmotionAllByGoalAndUser(userId, goalId, pageable);
//    }
    /**
     * 기록 숨기기 기능
     * @Author 이찬영
     */
    @Operation(summary = "특정 게시물 숨기기 기능",
            description = "사용자가 특정 게시물을 보이지 않게 숨긴다.")
    @Auth
    @DeleteMapping("/hide/{recordId}")
    public ApplicationResponse<Void> hideRecord(
            @PathVariable Long recordId,
            @ApiIgnore @UserId String userId)
    {
        return recordService.hideRecord(recordId, userId);
    }

    /**
     * 기록 수정 기능
     * @Author 이찬영
     */
    @Operation(summary = "기록 수정",
            description = "사용자가 작성한 기록을 수정한다. 소비 순간의 감정을 제외하고 모든 것(목표, 소비 금액, 소비 날짜, 소비 코멘트)을 수정할 수 있다.")
    @Auth
    @PutMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> update(
            @RequestBody RecordUpdateRequest request,
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
