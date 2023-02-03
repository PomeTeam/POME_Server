package com.example.pomeserver.domain.record.service;

import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordSecondEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordToFriendEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.dto.response.record.RecordResponse;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecordService{
    ApplicationResponse<RecordResponse> create(RecordCreateRequest request, String userId);
    ApplicationResponse<RecordResponse> findById(Long recordId, String userId);
    ApplicationResponse<Page<RecordResponse>> findAllByUserAndGoal(Long goalId, String userId, Pageable pageable);
    ApplicationResponse<RecordResponse> update(RecordUpdateRequest request, Long recordId, String userId);
    ApplicationResponse<Void> delete(Long recordId, String userId);
    ApplicationResponse<RecordResponse> writeSecondEmotion(RecordSecondEmotionRequest request, Long recordId, String userId);
    ApplicationResponse<RecordResponse> writeEmotionToFriend(RecordToFriendEmotionRequest request, Long recordId, String senderId);
    ApplicationResponse<List<RecordResponse>> findAllByUser(String userId, Pageable pageable);
    ApplicationResponse<List<RecordResponse>> findAllByFriends(String userId, Pageable pageable);
    ApplicationResponse<List<RecordResponse>> findAllOneWeek(String userId, int offset, int size);
}
