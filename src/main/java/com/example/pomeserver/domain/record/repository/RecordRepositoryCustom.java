package com.example.pomeserver.domain.record.repository;
import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.record.dto.paramResolver.param.RecordFilteringParam;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.user.entity.User;
import io.lettuce.core.ScanIterator;
import io.lettuce.core.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface RecordRepositoryCustom {
    Page<Record> findAllByUserCustom(String userId, Pageable pageable);
    Page<Record> findAllByFriends(ArrayList<String> friendIds, Pageable pageable);
    Page<Record> findAllOneWeekByUserAndGoal(String userId, Long goalId, String beforeWeek, Pageable pageable);
    Page<Record> findAllEmotionAllByGoalAndUser(String userId, Long goalId, Pageable pageable);
    Page<Record> findAllSecondEmotionIsFalseByGoalAndUser(String userId, Long goalId, String beforeDate, Pageable pageable);
    Page<Record> findAllByUserAndGoalAndEmotionFiltering(String userId, Long goalId, RecordFilteringParam emotionParam, Pageable pageable);
}
