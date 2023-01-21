package com.example.pomeserver.domain.record.controller;

import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordSecondEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordToFriendEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.dto.response.RecordResponse;
import com.example.pomeserver.domain.record.service.RecordService;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.authResolver.Auth;
import com.example.pomeserver.global.util.authResolver.UserId;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    @Auth
    @PostMapping
    public ApplicationResponse<RecordResponse> create(
            @RequestBody @Valid RecordCreateRequest request,
            @UserId String userId)
    {
        return recordService.create(request, userId);
    }

    /**
     * 기록에 두번째 감정 남기기 기능
     * @Author 이찬영
     */
    @Auth
    @PostMapping("/{recordId}/second-emotion")
    public ApplicationResponse<RecordResponse> writeSecondEmotion(
            @RequestBody @Valid RecordSecondEmotionRequest request,
            @PathVariable Long recordId,
            @UserId String userId)
    {
        return recordService.writeSecondEmotion(request, recordId, userId);
    }

    /**
     * 친구의 기록에 감정 남기기 기능
     * @Author 이찬영
     */
    @Auth
    @PostMapping("/{recordId}/friend-emotion")
    public ApplicationResponse<RecordResponse> writeEmotionToFriend(
            @RequestBody @Valid RecordToFriendEmotionRequest request,
            @PathVariable Long recordId,
            @UserId String userId)
    {
        return recordService.writeEmotionToFriend(request, recordId, userId);
    }

    /**
     * 기록 조회 기능
     * @Author 이찬영
     */
    @GetMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> findById(@PathVariable Long recordId)
    {
        return recordService.findById(recordId);
    }

    /**
     * Goal에 해당하는 기록들 페이징 조회 기능
     * @Author 이찬영
     */
    @Auth
    @GetMapping("/goal/{goalId}")
    public ApplicationResponse<Page<RecordResponse>> findAllByUserAndGoal(
            @PathVariable Long goalId,
            @UserId String userId,
            Pageable pageable)
    {
        return recordService.findAllByUserAndGoal(goalId, userId, pageable);
    }

    /**
     * 기록 수정 기능
     * @Author 이찬영
     */
    @Auth
    @PutMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> update(
            RecordUpdateRequest request,
            @PathVariable Long recordId,
            @UserId String userId)
    {
        return recordService.update(request, recordId, userId);
    }

    /**
     * 기록 삭제 기능
     * @Author 이찬영
     */
    @Auth
    @DeleteMapping("/{recordId}")
    public ApplicationResponse<Void> delete(
            @PathVariable Long recordId,
            @UserId String userId)
    {
        return recordService.delete(recordId, userId);
    }
}
