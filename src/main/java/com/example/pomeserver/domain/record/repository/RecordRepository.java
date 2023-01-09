package com.example.pomeserver.domain.record.repository;

import com.example.pomeserver.domain.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
