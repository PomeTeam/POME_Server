package com.example.pomeserver.domain.record.repository.hide;

import java.util.List;

public interface HideRecordRepositoryCustom {
    List<Long> findAllRecordIdByUserId(String userId);
}
