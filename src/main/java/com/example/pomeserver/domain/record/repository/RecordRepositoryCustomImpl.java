package com.example.pomeserver.domain.record.repository;

import com.example.pomeserver.domain.record.entity.Record;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class RecordRepositoryCustomImpl implements RecordRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Record> findAllByUserCustom(String userId, Pageable pageable) {
        String query = "select r from Record r join fetch r.user u where u.userId=:userId order by r.useDate desc";
        return em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<Record> findAllByFriends(
            ArrayList<String> friendIds,
            Pageable pageable)
    {
        String query = "select r from Record r join fetch r.user u where u.userId in (:friendIds) order by r.useDate desc";

        System.out.println("pageable = " + pageable.getOffset());
        return em.createQuery(query, Record.class)
                .setParameter("friendIds", friendIds)
                .setFirstResult((int) (pageable.getOffset()))
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<Record> findAllOneWeek(String userId, String beforeDate, Pageable pageable) {

        String query = "select r from Record r" +
                " join fetch r.user u" +
                " where u.userId=:userId and" +
                " r.useDate <= :beforeDate and" +
                " r.hasSecond = false" +
                " order by r.useDate desc";
        System.out.println("userId = " + userId);
        return em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("beforeDate", beforeDate)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
