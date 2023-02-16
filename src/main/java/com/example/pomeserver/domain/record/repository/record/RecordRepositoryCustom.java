package com.example.pomeserver.domain.record.repository.record;
import com.example.pomeserver.domain.record.dto.paramResolver.param.RecordFilteringParam;
import com.example.pomeserver.domain.record.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecordRepositoryCustom {
    Page<Record> findAllByUserCustom(String userId, List<Long> hideRecordIds, Pageable pageable);
    Page<Record> findAllByFriends(List<String> friendIds, List<Long> hideRecordIds, Pageable pageable);
    Page<Record> findAllOneWeekByUserAndGoal(String userId, Long goalId, String beforeWeek, Pageable pageable);
    Page<Record> findAllEmotionAllByGoalAndUser(String userId, Long goalId, Pageable pageable);
    Page<Record> findAllSecondEmotionIsFalseByGoalAndUser(String userId, Long goalId, String beforeDate, Pageable pageable);
    Page<Record> findAllByUserAndGoalAndEmotionFiltering(String userId, Long goalId, RecordFilteringParam emotionParam, Pageable pageable);
}
