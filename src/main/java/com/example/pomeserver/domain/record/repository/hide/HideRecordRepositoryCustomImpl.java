package com.example.pomeserver.domain.record.repository.hide;

import com.example.pomeserver.domain.record.entity.HideRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class HideRecordRepositoryCustomImpl implements  HideRecordRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Long> findAllRecordIdByUserId(String userId) {
        String query = "select h from HideRecord h " +
                "join fetch h.user u " +
                "where u.userId=:userId";

        List<HideRecord> hideRecords = em.createQuery(query, HideRecord.class)
                .setParameter("userId", userId)
                .getResultList();

        return hideRecords.stream()
                .map((hr)->hr.getRecord().getId())
                .collect(Collectors.toList());
    }
}
