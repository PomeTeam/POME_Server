package com.example.pomeserver.domain.record.repository;

import com.example.pomeserver.domain.record.entity.EmotionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionRecordRepository extends JpaRepository<EmotionRecord, Long> {
}
