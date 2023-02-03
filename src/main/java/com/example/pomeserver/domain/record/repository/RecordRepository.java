package com.example.pomeserver.domain.record.repository;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.record.dto.response.record.RecordResponse;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> , RecordRepositoryCustom {
    Page<Record> findAllByUserAndGoal(User user, Goal goal, Pageable pageable);


}
