package com.example.pomeserver.domain.record.service;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.dto.response.RecordResponse;
import com.example.pomeserver.domain.record.entity.Emotion;
import com.example.pomeserver.domain.record.repository.EmotionRepository;
import com.example.pomeserver.domain.record.repository.RecordRepository;
import com.example.pomeserver.domain.user.dto.assembler.RecordAssembler;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService{

    private RecordRepository recordRepository;
    private UserRepository userRepository;
    private GoalRepository goalRepository;
    private EmotionRepository emotionRepository;
    private RecordAssembler recordAssembler;
    @Override
    public ApplicationResponse<RecordResponse> create(RecordCreateRequest request, String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new IllegalArgumentException(userId));//TODO Exception추가
        Goal goal = goalRepository.findById(request.getGoalId());//TODO Exception추가
        Emotion emotion = emotionRepository.findById(request.getEmotionId()).orElseThrow(()-> new IllegalArgumentException());//TODO Exception추가
        recordAssembler.toEntity(request, goal, user, emotion);
        return null;
    }

    @Override
    public ApplicationResponse<RecordResponse> findById(Long recordId) {
        return null;
    }

    @Override
    public ApplicationResponse<RecordResponse> update(RecordUpdateRequest request, Long recordId, String userId) {
        return null;
    }

    @Override
    public ApplicationResponse<RecordResponse> delete(Long recordId, String userId) {
        return null;
    }
}
