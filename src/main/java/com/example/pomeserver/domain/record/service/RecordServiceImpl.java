package com.example.pomeserver.domain.record.service;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.exception.excute.GoalNotFoundException;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
import com.example.pomeserver.domain.record.DTO.response.RecordResponse;
import com.example.pomeserver.domain.record.DTO.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.DTO.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.entity.Emotion;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.record.exception.emotion.excute.EmotionNotFoundException;
import com.example.pomeserver.domain.record.exception.record.excute.RecordNotFoundException;
import com.example.pomeserver.domain.record.exception.record.excute.ThisRecordIsNotByThisUser;
import com.example.pomeserver.domain.record.repository.EmotionRepository;
import com.example.pomeserver.domain.record.repository.RecordRepository;
import com.example.pomeserver.domain.user.DTO.assembler.RecordAssembler;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.exception.excute.UserNotFoundException;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final EmotionRepository emotionRepository;
    private final RecordAssembler recordAssembler;

    @Transactional
    @Override
    public ApplicationResponse<RecordResponse> create(
            RecordCreateRequest request,
            String userId)
    {
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        Goal goal = goalRepository.findById(request.getGoalId()).orElseThrow(GoalNotFoundException::new);
        Emotion emotion = emotionRepository.findById(request.getEmotionId()).orElseThrow(EmotionNotFoundException::new);
        Record record = recordAssembler.toEntity(request, goal, user, emotion);
        recordRepository.save(record);
        return ApplicationResponse.create("created", RecordResponse.toDto(record));
    }

    @Override
    public ApplicationResponse<RecordResponse> findById(Long recordId)
    {
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        return ApplicationResponse.ok(RecordResponse.toDto(record));
    }

    @Override
    public ApplicationResponse<Page<RecordResponse>> findAllByUserAndGoal(
            Long goalId,
            String userId,
            Pageable pageable)
    {
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);
        return ApplicationResponse.ok(
                recordRepository.findAllByUserAndGoal(user, goal, pageable).map(RecordResponse::toDto));
    }

    @Override
    public ApplicationResponse<RecordResponse> update(
            RecordUpdateRequest request,
            Long recordId,
            String userId)
    {
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        Emotion emotion = emotionRepository.findById(request.getEmotionId()).orElseThrow(EmotionNotFoundException::new);//TODO 감정을 변경할 수 없다면 이건 빼기
        if(!record.getUser().getUserId().equals(userId)) throw new ThisRecordIsNotByThisUser();
        record.edit(recordAssembler.toEntity(request, emotion));
        return ApplicationResponse.ok(RecordResponse.toDto(record));
    }

    @Override
    public ApplicationResponse<Void> delete(
            Long recordId,
            String userId)
    {
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        if(!record.getUser().getUserId().equals(userId)) throw new ThisRecordIsNotByThisUser();
        recordRepository.delete(record);
        return ApplicationResponse.ok();
    }
}
