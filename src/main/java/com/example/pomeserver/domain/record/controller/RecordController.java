package com.example.pomeserver.domain.record.controller;

import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.dto.response.RecordResponse;
import com.example.pomeserver.domain.record.service.RecordService;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
@RestController
@Api(tags = "Record 관련 API")
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    public ApplicationResponse<RecordResponse> create(RecordCreateRequest request, String userId)
    {
        return recordService.create(request, userId);
    }

    @GetMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> findById(@PathVariable Long recordId)
    {
        return recordService.findById(recordId);
    }

    @PutMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> update(
            RecordUpdateRequest request,
            @PathVariable Long recordId,
            String userId)
    {
        return recordService.update(request, recordId, userId);
    }

    @DeleteMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> delete(
            @PathVariable Long recordId,
            String userId)
    {
        return recordService.delete(recordId, userId);
    }
}
