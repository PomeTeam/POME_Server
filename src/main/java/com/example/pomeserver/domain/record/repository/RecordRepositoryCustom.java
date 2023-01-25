package com.example.pomeserver.domain.record.repository;
import com.example.pomeserver.domain.record.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface RecordRepositoryCustom {
    List<Record> findAllByUserCustom(String userId, Pageable pageable);
}
