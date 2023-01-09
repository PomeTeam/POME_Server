package com.example.pomeserver.domain.record.repository;

import com.example.pomeserver.domain.record.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}
