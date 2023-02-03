package com.example.pomeserver.domain.record.repository;
import com.example.pomeserver.domain.record.dto.response.record.RecordResponse;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public interface RecordRepositoryCustom {
    List<Record> findAllByUserCustom(String userId, Pageable pageable);
    List<Record> findAllByFriends(ArrayList<String> friendIds, Pageable pageable);
    List<RecordResponse> findAllOneWeek(String userId, int offset, int size);
}
