package com.example.pomeserver.domain.record.service;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.goal.repository.GoalRepository;
import com.example.pomeserver.domain.record.dto.assembler.EmotionRecordAssembler;
import com.example.pomeserver.domain.record.dto.assembler.RecordAssembler;
import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordSecondEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordToFriendEmotionRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.dto.response.record.RecordResponse;
import com.example.pomeserver.domain.record.entity.Emotion;
import com.example.pomeserver.domain.record.entity.EmotionRecord;
import com.example.pomeserver.domain.record.entity.vo.EmotionType;
import com.example.pomeserver.domain.record.exception.record.excute.ThisRecordIsNotByThisUserException;
import com.example.pomeserver.domain.record.repository.emotion.EmotionRecordRepository;
import com.example.pomeserver.domain.record.repository.emotion.EmotionRepository;
import com.example.pomeserver.domain.record.repository.hide.HideRecordRepository;
import com.example.pomeserver.domain.record.repository.record.RecordRepository;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.repository.FollowRepository;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import com.example.pomeserver.global.event.publisher.UserActivityEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import com.example.pomeserver.domain.record.entity.Record;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.pomeserver.domain.record.service.RecordTest.staticValues.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

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
    void 기록_생성_성공()
    {
        //given
        RecordCreateRequest request = getRecordCreateRequest();
        User user = getTestUser1();
        String userId = user.getUserId();

        Goal goal = getTestGoal1();

        Emotion emotion = getTestSmileEmotion();


        //when
        lenient().when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));
        lenient().when(goalRepository.findById(any())).thenReturn(Optional.of(goal));
        lenient().when(emotionRepository.findById(any())).thenReturn(Optional.of(emotion));
        Record record = requestToRecordEntity(request, goal, user);
        lenient().when(recordAssembler.toEntity(any(), any(), any())).thenReturn(record);
        lenient().when(emotionRecordAssembler.toEntity(any(), any(), any(), any())).thenAnswer( invocation -> {
            return EmotionRecord.builder().emotionType(EmotionType.MY_FIRST)
                    .user(user)
                    .emotion(emotion)
                    .record(record).build();
        });
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

    @Test
    void 기록_단건_조회()
    {

        // given
        Record savedRecord = getTestRecord1(getTestUser1(), getTestGoal1());
        Long recordId = 1L;
        String userId = user1Id;

        // when
        lenient().when(recordRepository.findById(recordId)).thenReturn(Optional.of(savedRecord));

        ApplicationResponse<RecordResponse> response = recordService.findById(recordId, userId);

        // then
        RecordResponse result = response.getData();
        assertAll(
                () -> assertEquals(record1UsePrice, result.getUsePrice()),
                () -> assertEquals(record1UseDate, result.getUseDate())
        );
    }

    @Test
    void 기록_수정하기()
    {
        // given
        String requestUserId = user1Id;
        Long updateRecordId = record1Id;
        Record savedRecord = getTestRecord1(getTestUser1(), getTestGoal1());

        Long goalId = goal1Id;
        String editUseDate = "2023.03.09";
        int editUsePrice = 3000;
        String editUseComment = "수정된 코멘트입니다.";


        RecordUpdateRequest request = RecordUpdateRequest.builder()
                .goalId(goalId)
                .useDate(editUseDate)
                .usePrice(editUsePrice)
                .useComment(editUseComment).build();

        // when
        lenient().when(recordRepository.findById(updateRecordId)).thenReturn(Optional.of(savedRecord));
        lenient().when(recordAssembler.toEntity(request)).thenReturn(Record.toUpdateEntity(
                                                                                            request.getUsePrice(),
                                                                                            request.getUseDate(),
                                                                                            request.getUseComment())
        );
        ApplicationResponse<RecordResponse> response = recordService.update(request, updateRecordId, requestUserId);
        RecordResponse result = response.getData();

        // then
        assertAll(
                () -> assertEquals(editUseDate, result.getUseDate()),
                () -> assertEquals(editUsePrice, result.getUsePrice()),
                () -> assertEquals(editUseComment, result.getUseComment())
        );
    }

    @Test
    @DisplayName("기록_수정하기_실패(요청한 유저가 해당 기록의 주인이 아닌 경우)")
    void 기록_수정하기_실패1()
    {
        // given
        String otherUserId = "otherUserId";
        Long updateRecordId = record1Id;
        Record savedRecord = getTestRecord1(getTestUser1(), getTestGoal1());

        Long goalId = goal1Id;
        String editUseDate = "2023.03.09";
        int editUsePrice = 3000;
        String editUseComment = "수정된 코멘트입니다.";


        RecordUpdateRequest request = RecordUpdateRequest.builder()
                .goalId(goalId)
                .useDate(editUseDate)
                .usePrice(editUsePrice)
                .useComment(editUseComment).build();

        // when
        lenient().when(recordRepository.findById(updateRecordId)).thenReturn(Optional.of(savedRecord));
        lenient().when(recordAssembler.toEntity(request)).thenReturn(Record.toUpdateEntity(
                request.getUsePrice(),
                request.getUseDate(),
                request.getUseComment())
        );
        assertThrows(ThisRecordIsNotByThisUserException.class, () -> {
            recordService.update(request, updateRecordId, otherUserId);
        });
    }


    @Test
    void 기록_삭제하기()
    {
        // given
        Long recordId = 1L;
        String userId = user1Id;
        Record savedRecord = getTestRecord1(getTestUser1(), getTestGoal1());

        // when
        lenient().when(recordRepository.findById(recordId)).thenReturn(Optional.of(savedRecord));
        ApplicationResponse<Void> response = recordService.delete(recordId, userId);

        // then
        assertAll(
                () -> assertTrue(response.isSuccess())
        );
    }

    @Test
    void 기록에_두번째_감정_남기기()
    {
        // given
        Long firstEmotionId = 1L;
        Long secondEmotionId = 2L;

        RecordSecondEmotionRequest request = new RecordSecondEmotionRequest();
        request.setEmotionId(secondEmotionId);

        User user = getTestUser1();
        String userId = user.getUserId();

        Record record = getTestRecord1(getTestUser1(), getTestGoal1());
        Long targetRecordId = record.getId();
        record.addEmotionRecord(EmotionRecord.builder()
                .emotionType(EmotionType.MY_FIRST)
                .user(user)
                .emotion(getTestSmileEmotion())
                .record(record).build());

        EmotionRecord emotionRecord = getTestMySecondEmotionRecord(user, getTestSoSoEmotion(), getTestRecord1(getTestUser1(), getTestGoal1()));

        // when
        lenient().when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));
        lenient().when(recordRepository.findById(any())).thenReturn(Optional.of(record));
        lenient().when(emotionRepository.findById(any())).thenReturn(Optional.of(getTestSoSoEmotion()));
        lenient().when(emotionRecordAssembler.toEntity(any(), any(), any(), any())).thenReturn(emotionRecord);
        lenient().when(emotionRecordRepository.save(any())).thenAnswer(invocation -> {
            EmotionRecord emotionRecord2 = EmotionRecord.builder()
                    .emotionType(EmotionType.MY_SECOND)
                    .user(user)
                    .emotion(getTestSoSoEmotion())
                    .record(record).build();
            record.addEmotionRecord(emotionRecord2);
            return emotionRecord2;
        });
        ApplicationResponse<RecordResponse> response = recordService.writeSecondEmotion(request, targetRecordId, userId);

        // then
        RecordResponse result = response.getData();
        assertAll(
                () -> assertEquals(record.getId(), result.getId()),
                () -> assertEquals(secondEmotionId, result.getEmotionResponse().getSecondEmotion()),
                () -> assertEquals(firstEmotionId, result.getEmotionResponse().getFirstEmotion()),
                () -> assertEquals(user.getNickname(), result.getNickname())
        );
    }

    @Test
    @DisplayName("친구의 기록에 감정 남기기 (성공 - 감정을 처음 남기는 경우)")
    void 친구의_기록에_감정_남기기()
    {
        // given
        User user = getTestUser2();
        String userId = user.getUserId();

        Record record = getTestRecord1(getTestUser1(), getTestGoal1());
        RecordToFriendEmotionRequest request = new RecordToFriendEmotionRequest();
        Long requestEmotionId = 3L;
        request.setEmotionId(requestEmotionId);

        EmotionRecord emotionRecord = getTestFriendEmotionRecord(user, getTestBadEmotion(), record);

        // when
        lenient().when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));
        lenient().when(recordRepository.findById(any())).thenReturn(Optional.of(record));
        lenient().when(emotionRepository.findById(any())).thenReturn(Optional.of(getTestBadEmotion()));
        lenient().when(emotionRecordAssembler.toEntity(any(), any(), any(), any())).thenReturn(emotionRecord);
        lenient().when(emotionRecordRepository.save(any())).thenAnswer( invocation -> {
            record.addEmotionRecord(emotionRecord);
            return emotionRecord;
        });

        ApplicationResponse<RecordResponse> response = recordService.writeEmotionToFriend(request, record.getId(), userId);

        // then
        RecordResponse result = response.getData();
        assertAll(
                () -> assertEquals(record.getId(), result.getId()),
                () -> assertEquals(1, (long) result.getEmotionResponse().getFriendEmotions().size()),
                () -> assertEquals(requestEmotionId,  result.getEmotionResponse().getFriendEmotions().get(0).getEmotionId())
        );
    }

    private Record getTestRecord1(User user, Goal goal){
        Record record = Record.builder()
                .goal(goal)
                .user(user)
                .usePrice(record1UsePrice)
                .useDate(record1UseDate)
                .useComment(record1UseComment).build();

        record.setCreatedAt(time1);

        return record;
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

    private EmotionRecord getTestMySecondEmotionRecord(User user,
                                                      Emotion emotion,
                                                      Record record)
    {
        return EmotionRecord.builder().emotionType(EmotionType.MY_SECOND)
                .user(user)
                .emotion(emotion)
                .record(record).build();
    }

    private EmotionRecord getTestFriendEmotionRecord(User user,
                                                       Emotion emotion,
                                                       Record record)
    {
        return EmotionRecord.builder().emotionType(EmotionType.FRIEND)
                .user(user)
                .emotion(emotion)
                .record(record).build();
    }

    private Record requestToRecordEntity(RecordCreateRequest request,
                                         Goal goal,
                                         User user)
    {
        Record record = Record.builder()
                .goal(goal)
                .user(user)
                .usePrice(request.getUsePrice())
                .useDate(request.getUseDate())
                .useComment(request.getUseComment()).build();
        record.setCreatedAt(time1);
        return record;
    }

    private Emotion getTestSmileEmotion()
    {
        return Emotion.builder()
                .id(emotion1Id)
                .emotionName(emotion1Name).
                image(emotion1Image).build();
    }

    private Emotion getTestSoSoEmotion()
    {
        return Emotion.builder()
                .id(emotion2Id)
                .emotionName(emotion2Name).
                image(emotion2Image).build();
    }

    private Emotion getTestBadEmotion()
    {
        return Emotion.builder()
                .id(emotion3Id)
                .emotionName(emotion3Name).
                image(emotion3Image).build();
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

    private User getTestUser2()
    {
        return User.builder()
                .userId(user2Id)
                .nickname(user2Nickname)
                .phoneNum(user2PhoneNumber)
                .image(user2Image).build();
    }

    public interface staticValues{
        /* user */
        String user1Id = "userId1";
        String user1Nickname = "peace";
        String user1PhoneNumber = "01011112222";
        String user1Image = "user1Image";

        String user2Id = "userId2";
        String user2Nickname = "chaos";
        String user2PhoneNumber = "01033334444";
        String user2Image = "user2Image";

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

        Long emotion2Id = 2L;
        String emotion2Name = "soso";
        String emotion2Image = "emoji2";

        Long emotion3Id = 3L;
        String emotion3Name = "bad";
        String emotion3Image = "emoji3";

        /* record */
        Long record1Id = 1L;
        int record1UsePrice = 1000;
        String record1UseDate = "2022.03.08";
        String record1UseComment = "테스트1용 코멘트";
        String record1CreatedAt = "2022.03.08";

        LocalDateTime time1 = LocalDateTime.of(2023, 3, 10, 13, 15, 40);
    }
}
