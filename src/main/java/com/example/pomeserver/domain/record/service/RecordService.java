package com.example.pomeserver.domain.record.service;

import com.example.pomeserver.domain.record.dto.paramResolver.param.RecordFilteringParam;
import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordSecondEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordToFriendEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.dto.response.record.RecordResponse;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecordService{
    ApplicationResponse<RecordResponse> create(RecordCreateRequest request, String userId);
    ApplicationResponse<RecordResponse> findById(Long recordId, String userId);
    ApplicationResponse<Page<RecordResponse>> findAllRetrospectionByUserAndGoal(Long goalId, String userId, RecordFilteringParam emotionParam, Pageable pageable);
    ApplicationResponse<RecordResponse> update(RecordUpdateRequest request, Long recordId, String userId);
    ApplicationResponse<Void> delete(Long recordId, String userId);
    ApplicationResponse<RecordResponse> writeSecondEmotion(RecordSecondEmotionRequest request, Long recordId, String userId);
    ApplicationResponse<RecordResponse> writeEmotionToFriend(RecordToFriendEmotionRequest request, Long recordId, String senderId);
    ApplicationResponse<Page<RecordResponse>> findAllByUser(String userId, String viewerId, Pageable pageable);
    ApplicationResponse<Page<RecordResponse>> findAllByFriends(String userId, Pageable pageable);
    ApplicationResponse<Page<RecordResponse>> findAllOneWeekByUserAndGoal(String userId, Long goalId, Pageable pageable);
    ApplicationResponse<Page<RecordResponse>> findAllEmotionAllByGoalAndUser(String userId, Long goalId, Pageable pageable);
    ApplicationResponse<Page<RecordResponse>> findAllRecordTabByUserAndGoal(Long goalId, String userId, Pageable pageable);

}
