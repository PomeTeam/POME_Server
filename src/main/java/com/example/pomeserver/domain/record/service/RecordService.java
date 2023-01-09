package com.example.pomeserver.domain.record.service;

import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.dto.response.RecordResponse;
import com.example.pomeserver.global.dto.response.ApplicationResponse;

public interface RecordService{
    ApplicationResponse<RecordResponse> create(RecordCreateRequest request, String userId);
    ApplicationResponse<RecordResponse> findById(Long recordId);
    ApplicationResponse<RecordResponse> update(RecordUpdateRequest request, Long recordId, String userId);
    ApplicationResponse<RecordResponse> delete(Long recordId, String userId);
}
