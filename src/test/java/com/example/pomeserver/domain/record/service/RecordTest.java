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

import static com.example.pomeserver.domain.record.service.RecordTest.staticValues.*;
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

        //when
        lenient().when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));
        lenient().when(goalRepository.findById(any())).thenReturn(Optional.of(goal));
        lenient().when(emotionRepository.findById(any())).thenReturn(Optional.of(emotion));
        lenient().when(recordAssembler.toEntity(any(), any(), any())).thenReturn(record);
        lenient().when(emotionRecordAssembler.toEntity(any(), any(), any(), any())).thenReturn(emotionRecord);
        lenient().when(recordRepository.save(any())).thenReturn(record);

        ApplicationResponse<RecordResponse> response = recordService.create(request, userId);

        //then
        RecordResponse result = response.getData();
        assertAll(
                () -> assertEquals(request.getUseComment(), result.getUseComment()),
                () -> assertEquals(request.getUsePrice(), result.getUsePrice()),
                () -> assertEquals(request.getUseDate(), result.getUseDate()),
                () -> assertEquals(request.getEmotionId(), result.getEmotionResponse().getFirstEmotion())
        );
    }










    private RecordResponse getRecordResponse()
    {
        return RecordResponse.builder()
                .id(record1Id)
                .nickname(user1Nickname)
                .usePrice(record1UsePrice)
                .useDate(record1UseDate)
                .useComment(record1UseComment)
                .oneLineMind(goal1OneLineMind)
                .createdAt(record1CreatedAt).emotionResponse(null).build();
    }

    private EmotionRecord getTestMyFirstEmotionRecord(User user,
                                                      Emotion emotion,
                                                      Record record)
    {
        return EmotionRecord.builder().emotionType(EmotionType.MY_FIRST)
                .user(user)
                .emotion(emotion)
                .record(record).build();
    }

    private Record toEntity(RecordCreateRequest request,
                            Goal goal,
                            User user)
    {
        Record record = Record.builder()
                .goal(goal)
                .user(user)
                .usePrice(request.getUsePrice())
                .useDate(request.getUseDate())
                .useComment(request.getUseComment()).build();
        record.setCreatedAt(LocalDateTime.now());
        return record;
    }

    private Emotion getTestEmotion1()
    {
        return Emotion.builder()
                .id(emotion1Id)
                .emotionName(emotion1Name).
                image(emotion1Image).build();
    }


    private Goal getTestGoal1()
    {
        return Goal.builder()
                .name(goal1Name)
                .startDate(goal1StartDate)
                .endDate(goal1EndDate)
                .oneLineMind(goal1OneLineMind)
                .price(goal1Price)
                .isPublic(goal1IsPublic)
                .user(getTestUser1()).build();
    }

    private RecordCreateRequest getRecordCreateRequest()
    {
        return RecordCreateRequest.builder()
                .goalId(goal1Id)
                .emotionId(emotion1Id)
                .usePrice(record1UsePrice)
                .useDate(record1UseDate)
                .useComment(record1UseComment).build();
    }

    private User getTestUser1()
    {
        return User.builder()
                .userId(user1Id)
                .nickname(user1Nickname)
                .phoneNum(user1PhoneNumber)
                .image(user1Image).build();
    }

    public interface staticValues{
        /* user */
        String user1Id = "userId1";
        String user1Nickname = "peace";
        String user1PhoneNumber = "01011112222";
        String user1Image = "user1Image";

        /* goal */
        Long goal1Id = 1L;
        String goal1Name = "goal1Name";
        String goal1StartDate = "2023.03.01";
        String goal1EndDate = "2023.03.25";
        String goal1OneLineMind = "goal1OneLineMind";
        int goal1Price = 50000;
        Boolean goal1IsPublic = true;

        /* emotion */
        Long emotion1Id = 1L;
        String emotion1Name = "smile";
        String emotion1Image = "emoji1";

        /* record */
        Long record1Id = 1L;
        int record1UsePrice = 1000;
        String record1UseDate = "2022.03.08";
        String record1UseComment = "테스트1용 코멘트";
        String record1CreatedAt = "2022.03.08";
    }
}
