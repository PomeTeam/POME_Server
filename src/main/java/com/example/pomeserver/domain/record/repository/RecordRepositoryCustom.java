package com.example.pomeserver.domain.record.repository;
import com.example.pomeserver.domain.record.entity.Record;
import io.lettuce.core.ScanIterator;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface RecordRepositoryCustom {
    List<Record> findAllByUserCustom(String userId, Pageable pageable);
    List<Record> findAllByFriends(ArrayList<String> friendIds, Pageable pageable);
    List<Record> findAllOneWeekByUserAndGoal(String userId, Long goalId, String beforeWeek, Pageable pageable);
    List<Record> findAllEmotionAllByGoalAndUser(String userId, Long goalId, Pageable pageable);
    List<Record> findAllSecondEmotionIsFalseByGoalAndUser(String userId, Long goalId, Pageable pageable);
}
