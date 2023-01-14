package com.example.pomeserver.domain.record.service;

import com.example.pomeserver.domain.record.DTO.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.DTO.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.DTO.response.RecordResponse;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecordService{
    ApplicationResponse<RecordResponse> create(RecordCreateRequest request, String userId);
    ApplicationResponse<RecordResponse> findById(Long recordId);
    ApplicationResponse<Page<RecordResponse>> findAllByUserAndGoal(Long goalId, String userId, Pageable pageable);
    ApplicationResponse<RecordResponse> update(RecordUpdateRequest request, Long recordId, String userId);
    ApplicationResponse<Void> delete(Long recordId, String userId);
}
