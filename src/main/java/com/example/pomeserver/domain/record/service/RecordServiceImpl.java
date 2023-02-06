package com.example.pomeserver.domain.record.service;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.exception.excute.GoalNotFoundException;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
import com.example.pomeserver.domain.record.dto.assembler.EmotionRecordAssembler;
import com.example.pomeserver.domain.record.dto.request.RecordSecondEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordToFriendEmotionRequest;
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
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

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
    public ApplicationResponse<RecordResponse> create(
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
        return ApplicationResponse.create(RecordResponse.toDto(record, userId));
    }
    @Transactional
    @Override
    public ApplicationResponse<RecordResponse> writeSecondEmotion(
            RecordSecondEmotionRequest request,
            Long recordId,
            String userId)
    {
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        Emotion emotion = emotionRepository.findById(request.getEmotionId()).orElseThrow(EmotionNotFoundException::new);
        EmotionRecord emotionRecord = emotionRecordAssembler.toEntity(record, user, emotion, EmotionType.MY_SECOND);
        record.hasSecond();
        emotionRecordRepository.save(emotionRecord);
        return ApplicationResponse.create(RecordResponse.toDto(record, userId));
    }
    @Transactional
    @Override
    public ApplicationResponse<RecordResponse> writeEmotionToFriend(
            RecordToFriendEmotionRequest request,
            Long recordId,
            String senderId)
    {
        User sender = userRepository.findByUserId(senderId).orElseThrow(UserNotFoundException::new);
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        Emotion emotion = emotionRepository.findById(request.getEmotionId()).orElseThrow(EmotionNotFoundException::new);
        List<EmotionRecord> emotionRecords = record.getEmotionRecords();
        if(alreadyHaveFriendEmotion(emotionRecords, senderId))
            editEmotion(senderId, emotionRecords, emotion);
        else emotionRecordRepository.save(emotionRecordAssembler.toEntity(record, sender, emotion, EmotionType.FRIEND));
        return ApplicationResponse.create(RecordResponse.toDto(record, senderId));
    }

    private void editEmotion(String senderId, List<EmotionRecord> emotionRecords, Emotion emotion){
        for (EmotionRecord er : emotionRecords)
            if(er.getUser().getUserId().equals(senderId)) er.editEmotion(emotion);
    }

    private boolean alreadyHaveFriendEmotion(List<EmotionRecord> emotionRecords, String senderId) {
        for (EmotionRecord er : emotionRecords)
            if(er.getUser().getUserId().equals(senderId) && er.getEmotionType().equals(EmotionType.FRIEND)) return true;
        return false;
    }

    @Override
    public ApplicationResponse<List<RecordResponse>> findAllByUser(
            String userId,
            Pageable pageable)
    {
        ArrayList<RecordResponse> result = new ArrayList<>();
        recordRepository.findAllByUserCustom(userId, pageable).forEach(record ->result.add(RecordResponse.toDto(record, userId)));
        return ApplicationResponse.ok(result);
    }

    @Override
    public ApplicationResponse<List<RecordResponse>> findAllByFriends(
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
    public ApplicationResponse<List<RecordResponse>> findAllOneWeekByUserAndGoal(String userId, Long goalId, Pageable pageable){
        Calendar week = Calendar.getInstance();
        week.add(Calendar.DATE , -7);
        String beforeWeek = new java.text.SimpleDateFormat("yyyy.MM.dd").format(week.getTime());
        return ApplicationResponse.ok(
                recordRepository.findAllOneWeekByUserAndGoal(userId, goalId, beforeWeek, pageable).stream()
                        .map((r)->RecordResponse.toDto(r, userId))
                        .collect(Collectors.toList())
                );
    }

    @Override
    public ApplicationResponse<List<RecordResponse>> findAllEmotionAllByGoalAndUser(String userId, Long goalId, Pageable pageable) {
        return ApplicationResponse.ok(
                recordRepository.findAllEmotionAllByGoalAndUser(userId, goalId, pageable).stream()
                        .map((r)->RecordResponse.toDto(r, userId))
                        .collect(Collectors.toList())
        );
    }



    @Override
    public ApplicationResponse<RecordResponse> findById(Long recordId, String userId)
    {
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        return ApplicationResponse.ok(RecordResponse.toDto(record, userId));
    }

    @Override
    public ApplicationResponse<Page<RecordResponse>> findAllRetrospectionByUserAndGoal(
            Long goalId,
            String userId,
            Pageable pageable)
    {
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);
        return ApplicationResponse.ok(
                recordRepository.findAllByUserAndGoal(user, goal, pageable)
                        .map((record)->RecordResponse.toDto(record, userId)));
    }

    @Override
    public ApplicationResponse<List<RecordResponse>> findAllRecordTabByUserAndGoal(Long goalId, String userId, Pageable pageable) {
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        Goal goal = goalRepository.findById(goalId).orElseThrow(GoalNotFoundException::new);
        Calendar week = Calendar.getInstance();
        week.add(Calendar.DATE , -7);
        String beforeWeek = new java.text.SimpleDateFormat("yyyy.MM.dd").format(week.getTime());
        return ApplicationResponse.ok(
                recordRepository.findAllSecondEmotionIsFalseByGoalAndUser(user.getUserId(), goal.getId(), beforeWeek, pageable).stream()
                        .map((record)->RecordResponse.toDto(record, userId))
                        .collect(Collectors.toList()));
    }

    @Transactional
    @Override
    public ApplicationResponse<RecordResponse> update(
            RecordUpdateRequest request,
            Long recordId,
            String userId)
    {
        Record record = recordRepository.findById(recordId).orElseThrow(RecordNotFoundException::new);
        if(!record.getUser().getUserId().equals(userId)) throw new ThisRecordIsNotByThisUserException();
        record.edit(recordAssembler.toEntity(request));
        return ApplicationResponse.ok(RecordResponse.toDto(record, userId));
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
