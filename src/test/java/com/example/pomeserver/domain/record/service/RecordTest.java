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
import static org.mockito.Mockito.*;

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
    @DisplayName("기록에 두번째 감정남기기 (성공 - 일반적인 경우)")
    void 기록에_두번째_감정_남기기1()
    {
        // given
        Emotion firstEmotion = getTestSmileEmotion();
        Emotion secondEmotion = getTestSmileEmotion();

        RecordSecondEmotionRequest request = new RecordSecondEmotionRequest();
        request.setEmotionId(secondEmotion.getId());

        User user = getTestUser1();
        Goal goal = getTestGoal1();

        Record record = getTestRecord1(user, goal);
        EmotionRecord firstEmotionRecord = addMyFirstEmotionRecordToRecord(user, firstEmotion, record);
        record.addEmotionRecord(firstEmotionRecord);


        // when
        lenient().when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));
        lenient().when(recordRepository.findById(any())).thenReturn(Optional.of(record));
        lenient().when(emotionRepository.findById(any())).thenReturn(Optional.of(getTestSoSoEmotion()));
        EmotionRecord secondEmotionRecord = addMySecondEmotionRecordToRecord(user, secondEmotion, record);
        lenient().when(emotionRecordAssembler.toEntity(any(), any(), any(), any())).thenReturn(secondEmotionRecord);
        lenient().when(emotionRecordRepository.save(any())).thenAnswer(invocation -> {
            record.addEmotionRecord(secondEmotionRecord);
            return secondEmotionRecord;
        });
        ApplicationResponse<RecordResponse> response = recordService.writeSecondEmotion(request, record.getId(), user.getUserId());

        // then
        RecordResponse result = response.getData();
        assertAll(
                () -> assertEquals(record.getId(), result.getId()),
                () -> assertEquals(secondEmotion.getId(), result.getEmotionResponse().getSecondEmotion()),
                () -> assertEquals(firstEmotion.getId(), result.getEmotionResponse().getFirstEmotion()),
                () -> assertEquals(user.getNickname(), result.getNickname()),
                () -> assertEquals(1, user.getActivityCount().getFinishRecordCount())
        );
    }

    @Test
    @DisplayName("기록에 두번째 감정 남기기 (성공 - 첫번째는 긍정감정 두번째는 부정감정인 경우. 이때 마시멜로 솔직 카운트 증가)")
    void 기록에_두번째_감정_남기기2()
    {
        // given
        UserActivityEventPublisher userActivityEventPublisher = mock(UserActivityEventPublisher.class);

        Emotion firstEmotion = getTestSmileEmotion();
        Emotion secondEmotion = getTestBadEmotion();


        RecordSecondEmotionRequest request = new RecordSecondEmotionRequest();
        request.setEmotionId(secondEmotion.getId());

        User user = getTestUser1();
        Goal goal = getTestGoal1();
        Record record = getTestRecord1(user, goal);
        EmotionRecord firstEmotionRecord = addMyFirstEmotionRecordToRecord(user, getTestSmileEmotion(), record);
        record.addEmotionRecord(firstEmotionRecord);


        // when
        lenient().when(userRepository.findByUserId(any())).thenReturn(Optional.of(user));
        lenient().when(recordRepository.findById(any())).thenReturn(Optional.of(record));
        lenient().when(emotionRepository.findById(any())).thenReturn(Optional.of(getTestBadEmotion()));
        EmotionRecord secondEmotionRecord = addMySecondEmotionRecordToRecord(user, getTestBadEmotion(), record);
        lenient().when(emotionRecordAssembler.toEntity(any(), any(), any(), any())).thenReturn(secondEmotionRecord);
        lenient().when(emotionRecordRepository.save(any())).thenAnswer(invocation -> {
            record.addEmotionRecord(secondEmotionRecord);
            return secondEmotionRecord;
        });

        ApplicationResponse<RecordResponse> response = recordService.writeSecondEmotion(request, record.getId(), user.getUserId());

        // then
        RecordResponse result = response.getData();
        assertAll(
                () -> assertEquals(record.getId(), result.getId()),
                () -> assertEquals(secondEmotion.getId(), result.getEmotionResponse().getSecondEmotion()),
                () -> assertEquals(firstEmotion.getId(), result.getEmotionResponse().getFirstEmotion()),
                () -> assertEquals(user.getNickname(), result.getNickname()),
                () -> assertEquals(1, user.getActivityCount().getFinishRecordCount()),
                () -> assertEquals(1, user.getActivityCount().getChangePositiveToNegativeCount())
        );
    }

    @Test
    @DisplayName("친구의 기록에 감정 남기기 (성공 - 감정을 처음 남기는 경우)")
    void 친구의_기록에_감정_남기기()
    {
        // given
        User sender = getTestUser2();

        User recordOwner = getTestUser1();
        Goal goal = getTestGoal1();
        Record record = getTestRecord1(recordOwner, goal);
        Emotion emotion = getTestBadEmotion();

        RecordToFriendEmotionRequest request = new RecordToFriendEmotionRequest();
        request.setEmotionId(emotion.getId());

        // when
        lenient().when(userRepository.findByUserId(any())).thenReturn(Optional.of(sender));
        lenient().when(recordRepository.findById(any())).thenReturn(Optional.of(record));
        lenient().when(emotionRepository.findById(any())).thenReturn(Optional.of(emotion));
        lenient().when(emotionRecordRepository.save(any())).thenAnswer( invocation -> {
            return addFriendEmotionRecordToRecord(sender, emotion, record);
        });
        ApplicationResponse<RecordResponse> response = recordService.writeEmotionToFriend(request, record.getId(), sender.getUserId());

        // then
        RecordResponse result = response.getData();
        assertAll(
                () -> assertEquals(record.getId(), result.getId()),
                () -> assertEquals(1, (long) result.getEmotionResponse().getFriendEmotions().size()),
                () -> assertEquals(emotion.getId(),  result.getEmotionResponse().getFriendEmotions().get(0).getEmotionId()),
                () -> assertEquals(1,  sender.getActivityCount().getAddEmotionCount())
        );
    }

    @Test
    @DisplayName("친구의 기록에 감정 남기기2 (성공 - 이미 기존에 감정을 남겼던 경우는 기존의 감정만 수정시킨다 <-- 마시멜로 공감 카운트 증가X)")
    void 친구의_기록에_감정_남기기2()
    {
        // given
        User sender = getTestUser2();
        User recordOwner = getTestUser1();
        Goal goal = getTestGoal1();
        Record record = getTestRecord1(recordOwner, goal);
        Emotion emotion1 = getTestBadEmotion();
        Emotion emotion2 = getTestSmileEmotion();

        RecordToFriendEmotionRequest request = new RecordToFriendEmotionRequest();
        request.setEmotionId(emotion2.getId());

        EmotionRecord emotionRecord = addFriendEmotionRecordToRecord(sender, emotion1, record); //record에 이미 sender의 emotion 존재
        sender.getActivityCount().addAddEmotionCount();

        // when
        lenient().when(userRepository.findByUserId(any())).thenReturn(Optional.of(sender));
        lenient().when(recordRepository.findById(any())).thenReturn(Optional.of(record));
        lenient().when(emotionRepository.findById(any())).thenReturn(Optional.of(emotion2));
        lenient().when(emotionRecordRepository.save(any())).thenAnswer( invocation -> {
            return addFriendEmotionRecordToRecord(sender, emotion2, record);
        });
        ApplicationResponse<RecordResponse> response = recordService.writeEmotionToFriend(request, record.getId(), sender.getUserId());

        // then
        RecordResponse result = response.getData();
        assertAll(
                () -> assertEquals(record.getId(), result.getId()),
                () -> assertEquals(1, (long) result.getEmotionResponse().getFriendEmotions().size()),
                () -> assertEquals(emotion2.getId(),  result.getEmotionResponse().getFriendEmotions().get(0).getEmotionId()),
                () -> assertEquals(1,  sender.getActivityCount().getAddEmotionCount())
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

    private EmotionRecord addMyFirstEmotionRecordToRecord(User user,
                                                          Emotion emotion,
                                                          Record record)
    {
        return EmotionRecord.builder().emotionType(EmotionType.MY_FIRST)
                .user(user)
                .emotion(emotion)
                .record(record).build();
    }

    private EmotionRecord addMySecondEmotionRecordToRecord(User user,
                                                           Emotion emotion,
                                                           Record record)
    {
        return EmotionRecord.builder().emotionType(EmotionType.MY_SECOND)
                .user(user)
                .emotion(emotion)
                .record(record).build();
    }

    private EmotionRecord addFriendEmotionRecordToRecord(User user,
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
                .id(smileEmotionId)
                .emotionName(smileEmotionName).
                image(smileEmotionImage).build();
    }

    private Emotion getTestSoSoEmotion()
    {
        return Emotion.builder()
                .id(sosoEmotionId)
                .emotionName(sosoEmotionName).
                image(sosoEmotionImage).build();
    }

    private Emotion getTestBadEmotion()
    {
        return Emotion.builder()
                .id(badEmotionId)
                .emotionName(badEmotionName).
                image(badEmotionImage).build();
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
                .emotionId(smileEmotionId)
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
        Long smileEmotionId = 0L;
        String smileEmotionName = "smile";
        String smileEmotionImage = "emoji1";

        Long sosoEmotionId = 1L;
        String sosoEmotionName = "soso";
        String sosoEmotionImage = "emoji2";

        Long badEmotionId = 2L;
        String badEmotionName = "bad";
        String badEmotionImage = "emoji3";

        /* record */
        Long record1Id = 1L;
        int record1UsePrice = 1000;
        String record1UseDate = "2022.03.08";
        String record1UseComment = "테스트1용 코멘트";
        String record1CreatedAt = "2022.03.08";

        LocalDateTime time1 = LocalDateTime.of(2023, 3, 10, 13, 15, 40);
    }
}
