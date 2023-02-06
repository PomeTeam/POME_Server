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
        String query = "select r from Record r join fetch r.user u " +
                "where u.userId=:userId " +
                "order by r.useDate desc";
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
        String query = "select r from Record r join fetch r.user u " +
                "where u.userId in (:friendIds) " +
                "order by r.useDate desc";
        return em.createQuery(query, Record.class)
                .setParameter("friendIds", friendIds)
                .setFirstResult((int) (pageable.getOffset()))
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<Record> findAllOneWeekByUserAndGoal(String userId, Long goalId, String beforeDate, Pageable pageable){

        String query = "select r from Record r" +
                " join fetch r.user u" +
                " join fetch r.goal g" +
                " where u.userId=:userId and" +
                " r.useDate <= :beforeDate and" +
                " r.hasSecond = false and" +
                " g.id=:goalId" +
                " order by r.useDate desc";
        System.out.println("userId = " + userId);
        return em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setParameter("beforeDate", beforeDate)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<Record> findAllEmotionAllByGoalAndUser(String userId, Long goalId, Pageable pageable) {

        String query = "select r from Record r" +
                " join fetch r.user u" +
                " join fetch r.goal g" +
                " where u.userId=:userId and" +
                " r.hasSecond = true and" +
                " g.id=:goalId" +
                " order by r.useDate desc";

        return em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<Record> findAllSecondEmotionIsFalseByGoalAndUser(String userId, Long goalId, String beforeDate, Pageable pageable) {

        String query = "select r from Record r" +
                " join fetch r.user u" +
                " join fetch r.goal g" +
                " where u.userId=:userId and" +
                " r.useDate > :beforeDate and" +
                " r.hasSecond = false and" +
                " g.id=:goalId" +
                " order by r.useDate desc";

        return em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setParameter("beforeDate", beforeDate)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
