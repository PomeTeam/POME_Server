package com.example.pomeserver.domain.record.controller;

import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.dto.response.RecordResponse;
import com.example.pomeserver.domain.record.service.RecordService;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.util.authResolver.Auth;
import com.example.pomeserver.global.util.authResolver.UserId;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/records")
@RestController
@Api(tags = "Record 관련 API")
public class RecordController {

    private final RecordService recordService;

    @Auth
    @PostMapping
    public ApplicationResponse<RecordResponse> create(
            RecordCreateRequest request,
            @UserId String userId)
    {
        return recordService.create(request, userId);
    }

    @GetMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> findById(@PathVariable Long recordId)
    {
        return recordService.findById(recordId);
    }

    @Auth
    @PutMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> update(
            RecordUpdateRequest request,
            @PathVariable Long recordId,
            @UserId String userId)
    {
        return recordService.update(request, recordId, userId);
    }

    @Auth
    @DeleteMapping("/{recordId}")
    public ApplicationResponse<RecordResponse> delete(
            @PathVariable Long recordId,
            @UserId String userId)
    {
        return recordService.delete(recordId, userId);
    }
}
