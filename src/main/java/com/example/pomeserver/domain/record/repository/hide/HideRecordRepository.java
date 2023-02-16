package com.example.pomeserver.domain.record.repository.hide;

import com.example.pomeserver.domain.record.entity.HideRecord;
import com.example.pomeserver.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HideRecordRepository extends JpaRepository<HideRecord, Long>, HideRecordRepositoryCustom {
    List<HideRecord> findAllByUser(User user);
}
