package com.example.pomeserver.domain.record.service;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
import com.example.pomeserver.domain.record.dto.assembler.EmotionRecordAssembler;
import com.example.pomeserver.domain.record.dto.assembler.RecordAssembler;
import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.response.record.RecordResponse;
import com.example.pomeserver.domain.record.entity.Emotion;
import com.example.pomeserver.domain.record.entity.EmotionRecord;
import com.example.pomeserver.domain.record.entity.vo.EmotionType;
import com.example.pomeserver.domain.record.repository.emotion.EmotionRecordRepository;
import com.example.pomeserver.domain.record.repository.emotion.EmotionRepository;
import com.example.pomeserver.domain.record.repository.hide.HideRecordRepository;
import com.example.pomeserver.domain.record.repository.record.RecordRepository;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.repository.FollowRepository;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.event.publisher.UserActivityEventPublisher;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import com.example.pomeserver.domain.record.entity.Record;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
public class RecordTest {
    @InjectMocks
    RecordServiceImpl recordService ;

    @Mock
    RecordRepository recordRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    GoalRepository goalRepository;
    @Mock
    EmotionRepository emotionRepository;
    @Mock
    EmotionRecordRepository emotionRecordRepository;
    @Mock
    RecordAssembler recordAssembler;
    @Mock
    EmotionRecordAssembler emotionRecordAssembler;
    @Mock
    FollowRepository followRepository;
    @Mock
    HideRecordRepository hideRecordRepository;
    @Mock
    UserActivityEventPublisher userActivityEventPublisher;


    @Test
    void 기록_생성_성공() {
        //given
        RecordCreateRequest request = getRecordCreateRequest();
        User user = getTestUser1();
        String userId = user.getUserId();
        Goal goal = getTestGoal1();
        Emotion emotion = getTestEmotion1();
        Record record = toEntity(request, goal, user);
        EmotionRecord emotionRecord = getTestMyFirstEmotionRecord(user, emotion, record);
        RecordResponse recordResponse = getRecordResponse();

        //when
        lenient().when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));
        lenient().when(goalRepository.findById(any())).thenReturn(Optional.of(goal));
        lenient().when(emotionRepository.findById(request.getEmotionId())).thenReturn(Optional.of(emotion));
        lenient().when(recordAssembler.toEntity(any(), any(), any())).thenReturn(record);
        lenient().when(emotionRecordAssembler.toEntity(any(), any(), any(), any())).thenReturn(emotionRecord);
        lenient().when(recordRepository.save(any())).thenReturn(record);
        ApplicationResponse<RecordResponse> response = recordService.create(request, userId);

        //then
        RecordResponse result = response.getData();
        assertAll(
                () -> assertEquals(request.getUseComment(), result.getUseComment()),
                () -> assertEquals(request.getUsePrice(), result.getUsePrice())
        );
    }

    private RecordResponse getRecordResponse() {
        return RecordResponse.builder()
                .id(1L)
                .nickname("peace")
                .usePrice(1000)
                .useDate("2022.03.08")
                .useComment("테스트용 코멘트")
                .oneLineMind("testOneLineMind")
                .createdAt("2022.03.08").emotionResponse(null).build();

    }

    private EmotionRecord getTestMyFirstEmotionRecord(User user, Emotion emotion, Record record){
        return EmotionRecord.builder().emotionType(EmotionType.MY_FIRST)
                .user(user)
                .emotion(emotion)
                .record(record).build();
    }

    private Record toEntity(RecordCreateRequest request, Goal goal, User user) {
        Record record = Record.builder()
                .goal(goal)
                .user(user)
                .usePrice(request.getUsePrice())
                .useDate(request.getUseDate())
                .useComment(request.getUseComment()).build();
        record.setCreatedAt(LocalDateTime.now());
        return record;
    }

    private Emotion getTestEmotion1() {
        return Emotion.builder()
                .id(1L)
                .emotionName("hi").
                image("no").build();
    }


    private Goal getTestGoal1() {
        return Goal.builder()
                .name("testGoalName")
                .startDate("2023.03.01")
                .endDate("2023.03.25")
                .oneLineMind("testOneLineMind")
                .price(50000)
                .isPublic(true)
                .user(getTestUser1()).build();
    }

    private RecordCreateRequest getRecordCreateRequest() {
        return RecordCreateRequest.builder()
                .goalId(1L)
                .emotionId(1L)
                .usePrice(1000)
                .useDate("2023.03.08")
                .useComment("테스트용 코멘트").build();
    }

    private User getTestUser1() {
        return User.builder()
                .userId("userId1")
                .nickname("peace")
                .phoneNum("01011112222")
                .image("myImage").build();
    }



}
