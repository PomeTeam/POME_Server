package com.example.pomeserver.domain.record.service;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.exception.excute.GoalNotFoundException;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
import com.example.pomeserver.domain.record.dto.assembler.EmotionRecordAssembler;
import com.example.pomeserver.domain.record.dto.request.RecordSecondEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordToFriendEmotionRequest;
import com.example.pomeserver.domain.record.dto.response.record.MyRecordResponse;
import com.example.pomeserver.domain.record.dto.response.record.RecordResponse;
import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.entity.Emotion;
import com.example.pomeserver.domain.record.entity.EmotionRecord;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.record.entity.vo.EmotionType;
import com.example.pomeserver.domain.record.exception.emotion.excute.EmotionNotFoundException;
import com.example.pomeserver.domain.record.exception.record.excute.RecordNotFoundException;
import com.example.pomeserver.domain.record.exception.record.excute.ThisRecordIsNotByThisUserException;
import com.example.pomeserver.domain.record.repository.EmotionRecordRepository;
import com.example.pomeserver.domain.record.repository.EmotionRepository;
import com.example.pomeserver.domain.record.repository.RecordRepository;
import com.example.pomeserver.domain.record.dto.assembler.RecordAssembler;
import com.example.pomeserver.domain.user.entity.Follow;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.exception.excute.UserNotFoundException;
import com.example.pomeserver.domain.user.repository.FollowRepository;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService{

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final EmotionRepository emotionRepository;
    private final EmotionRecordRepository emotionRecordRepository;
    private final RecordAssembler recordAssembler;
    private final EmotionRecordAssembler emotionRecordAssembler;
    private final FollowRepository followRepository;

    @Transactional
    @Override
    public ApplicationResponse<MyRecordResponse> create(
            RecordCreateRequest request,
            String userId)
    {
        // 연관관계 매핑을 위해 user, goal, emotion 객체를 찾아온다.
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        Goal goal = goalRepository.findById(request.getGoalId()).orElseThrow(GoalNotFoundException::new);
        Emotion emotion = emotionRepository.findById(request.getEmotionId()).orElseThrow(EmotionNotFoundException::new);
        // 기록 생성 및 저장
        Record record = recordAssembler.toEntity(request, goal, user);
        recordRepository.save(record);
        // 감정기록 엔티티 생성 및 저장
        EmotionRecord emotionRecord = emotionRecordAssembler.toEntity(record, user, emotion, EmotionType.MY_FIRST);
        emotionRecordRepository.save(emotionRecord);
        return ApplicationResponse.create(MyRecordResponse.toDto(record));
    }

    @Override
    public ApplicationResponse<MyRecordResponse> writeSecondEmotion(
            RecordSecondEmotionRequest request,
            Long recordId,
            String userId)
    {
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        Emotion emotion = emotionRepository.findById(request.getEmotionId()).orElseThrow(EmotionNotFoundException::new);
        EmotionRecord emotionRecord = emotionRecordAssembler.toEntity(record, user, emotion, EmotionType.MY_SECOND);
        emotionRecordRepository.save(emotionRecord);
        return ApplicationResponse.create(MyRecordResponse.toDto(record));
    }

    @Override
    public ApplicationResponse<RecordResponse> writeEmotionToFriend(
            RecordToFriendEmotionRequest request,
            Long recordId,
            String senderId)
    {
        User sender = userRepository.findByUserId(senderId).orElseThrow(UserNotFoundException::new);
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        Emotion emotion = emotionRepository.findById(request.getEmotionId()).orElseThrow(EmotionNotFoundException::new);
        EmotionRecord emotionRecord = emotionRecordAssembler.toEntity(record, sender, emotion, EmotionType.FRIEND);
        emotionRecordRepository.save(emotionRecord);
        return ApplicationResponse.create(RecordResponse.toDto(record, senderId));
    }

    @Override
    public ApplicationResponse<List<RecordResponse>> findAllByUser(
            String userId,
            Pageable pageable)
    {
        ArrayList<RecordResponse> result = new ArrayList<>();
        recordRepository.findAllByUserCustom(userId, pageable).forEach(r ->result.add(RecordResponse.toDto(r, userId)));
        return ApplicationResponse.ok(result);
    }

    @Override
    public ApplicationResponse<List<RecordResponse>> findByFriends(
            String userId,
            Pageable pageable
    )
    {
        List<Follow> friends = followRepository.findByFromUserId(
                userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new).getId());

        ArrayList<String> friendIds = new ArrayList<>();
        friends.forEach((f)->friendIds.add(f.getToUser().getUserId()));

        ArrayList<RecordResponse> result = new ArrayList<>();
        recordRepository.findAllByFriends(friendIds, pageable).forEach(r -> result.add(RecordResponse.toDto(r, userId)));
        return ApplicationResponse.ok(result);
    }

    @Override
    public ApplicationResponse<MyRecordResponse> findById(Long recordId)
    {
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        return ApplicationResponse.ok(MyRecordResponse.toDto(record));
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
                recordRepository.findAllByUserAndGoal(user, goal, pageable).map(r->RecordResponse.toDto(r,userId)));
    }

    @Transactional
    @Override
    public ApplicationResponse<MyRecordResponse> update(
            RecordUpdateRequest request,
            Long recordId,
            String userId)
    {
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        if(!record.getUser().getUserId().equals(userId)) throw new ThisRecordIsNotByThisUserException();
        record.edit(recordAssembler.toEntity(request));
        return ApplicationResponse.ok(MyRecordResponse.toDto(record));
    }

    @Transactional
    @Override
    public ApplicationResponse<Void> delete(
            Long recordId,
            String userId)
    {
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        if(!record.getUser().getUserId().equals(userId)) throw new ThisRecordIsNotByThisUserException();
        recordRepository.delete(record);
        return ApplicationResponse.ok();
    }


}
