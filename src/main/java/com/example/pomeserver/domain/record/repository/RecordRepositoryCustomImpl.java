package com.example.pomeserver.domain.record.repository;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.record.dto.paramResolver.param.RecordFilteringParam;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.global.util.supplier.MyLongSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class RecordRepositoryCustomImpl implements RecordRepositoryCustom{

    private final EntityManager em;

    @Override
    public Page<Record> findAllByUserCustom(String userId, Pageable pageable){
        String query = "select r from Record r join fetch r.user u " +
                "where u.userId=:userId " +
                "order by r.useDate desc";
        List<Record> resultList = em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        String countQuery = "select count(r) from Record r join r.user u where u.userId=:userId";
        Object singleResult = em.createQuery(countQuery)
                .setParameter("userId", userId)
                .getSingleResult();

        return PageableExecutionUtils.getPage(resultList, pageable, new MyLongSupplier(singleResult));
    }

    @Override
    public Page<Record> findAllByFriends(
            ArrayList<String> friendIds,
            Pageable pageable)
    {
        String query = "select r from Record r join fetch r.user u " +
                "where u.userId in (:friendIds) " +
                "order by r.useDate desc";

        List<Record> resultList = em.createQuery(query, Record.class)
                .setParameter("friendIds", friendIds)
                .setFirstResult((int) (pageable.getOffset()))
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        String countQuery = "select count(r) from Record r join r.user u where u.userId in (:friendIds)";
        Object singleResult = em.createQuery(countQuery)
                .setParameter("friendIds", friendIds)
                .getSingleResult();

        return PageableExecutionUtils.getPage(resultList, pageable, new MyLongSupplier(singleResult));
    }

    @Override
    public Page<Record> findAllOneWeekByUserAndGoal(String userId,
                                                    Long goalId,
                                                    String beforeDate,
                                                    Pageable pageable){

        String query = "select r from Record r" +
                " join fetch r.user u" +
                " join fetch r.goal g" +
                " where u.userId=:userId and" +
                " r.useDate <= :beforeDate and" +
                " r.hasSecond = false and" +
                " g.id=:goalId" +
                " order by r.useDate desc";

        List<Record> resultList = em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setParameter("beforeDate", beforeDate)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        String countQuery = "select count(r) from Record r " +
                "join r.user u " +
                "join r.goal g " +
                "where u.userId=:userId and " +
                "r.useDate <= :beforeDate and " +
                "r.hasSecond = false and " +
                "g.id=:goalId";

        Object singleResult = em.createQuery(countQuery)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setParameter("beforeDate", beforeDate)
                .getSingleResult();

        return PageableExecutionUtils.getPage(resultList, pageable, new MyLongSupplier(singleResult));

    }

    @Override
    public Page<Record> findAllEmotionAllByGoalAndUser(String userId,
                                                       Long goalId,
                                                       Pageable pageable) {

        String query = "select r from Record r" +
                " join fetch r.user u" +
                " join fetch r.goal g" +
                " where u.userId=:userId and" +
                " r.hasSecond = true and" +
                " g.id=:goalId" +
                " order by r.useDate desc";

        List<Record> resultList = em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        String countQuery = "select count(r) from Record r " +
                "join r.user u " +
                "join r.goal g " +
                "where u.userId=:userId and " +
                "r.hasSecond = true and " +
                "g.id=:goalId";

        Object singleResult = em.createQuery(countQuery)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .getSingleResult();

        return PageableExecutionUtils.getPage(resultList, pageable, new MyLongSupplier(singleResult));
    }

    @Override
    public Page<Record> findAllSecondEmotionIsFalseByGoalAndUser(String userId,
                                                                 Long goalId,
                                                                 String beforeDate,
                                                                 Pageable pageable) {

        String query = "select r from Record r" +
                " join fetch r.user u" +
                " join fetch r.goal g" +
                " where u.userId=:userId and" +
                " r.useDate > :beforeDate and" +
                " r.hasSecond = false and" +
                " g.id=:goalId" +
                " order by r.useDate desc";

        List<Record> resultList = em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setParameter("beforeDate", beforeDate)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        String countQuery = "select count(r) from Record r " +
                "join r.user u " +
                "join r.goal g " +
                "where u.userId=:userId and " +
                "r.useDate > :beforeDate and " +
                "r.hasSecond = false and " +
                "g.id=:goalId";

        Object singleResult = em.createQuery(countQuery)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setParameter("beforeDate", beforeDate)
                .getSingleResult();

        return PageableExecutionUtils.getPage(resultList, pageable, new MyLongSupplier(singleResult));
    }

    @Override
    public Page<Record> findAllByUserAndGoalAndEmotionFiltering(String userId,
                                                                Long goalId,
                                                                RecordFilteringParam emotionParam,
                                                                Pageable pageable){
        Long firstEmotion = emotionParam.getFirstEmotion();
        Long secondEmotion = emotionParam.getSecondEmotion();

        if(firstEmotion != null && secondEmotion != null) {
            List<Long> recordIds = findAllByUserAndGoalAndAllEmotion(userId, goalId, firstEmotion, secondEmotion);
            return findAllByUserAndGoalAndIn(userId, goalId, recordIds, pageable);
        }
        if(firstEmotion != null) {
            List<Long> recordIds = findAllByUserAndGoalAndFirstEmotion(userId, goalId, firstEmotion);
            return findAllByUserAndGoalAndIn(userId, goalId, recordIds, pageable);
        }
        if(secondEmotion != null){
            List<Long> recordIds = findAllByUserAndGoalAndSecondEmotion(userId, goalId, secondEmotion);
            return findAllByUserAndGoalAndIn(userId, goalId, recordIds, pageable);
        }

        String query = "select r from Record r " +
                "join fetch r.user u " +
                "join fetch r.goal g " +
                "where u.userId=:userId " +
                "and g.id=:goalId " +
                "order by r.useDate desc ";


        List<Record> resultList = em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        String countQuery = "select count(r) from Record r " +
                "join r.user u " +
                "join r.goal g " +
                "where u.userId=:userId " +
                "and g.id=:goalId";
        Object singleResult = em.createQuery(countQuery)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .getSingleResult();

        return PageableExecutionUtils.getPage(resultList, pageable, new MyLongSupplier(singleResult));
    }

    /* FIRST */
    private List<Long> findAllByUserAndGoalAndFirstEmotion(String userId,
                                                           Long goalId,
                                                           Long emotionId)
    {
        String query1 = "select r from Record r " +
                "join fetch r.user u " +
                "join fetch r.goal g " +
                "join fetch r.emotionRecords er " +
                "join fetch er.emotion e " +
                "where u.userId=:userId " +
                "and g.id=:goalId " +
                "and er.emotionType = 'MY_FIRST' " +
                "and e.id =: emotionId ";

        List<Record> records = em.createQuery(query1, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setParameter("emotionId", emotionId) //FIRST
                .getResultList();

        List<Long> ids = new ArrayList<>();
        records.forEach((r)->ids.add(r.getId()));
        em.clear();

        return ids;
    }

    /* SECOND */
    private List<Long> findAllByUserAndGoalAndSecondEmotion(String userId,
                                                            Long goalId,
                                                            Long emotionId)
    {
        String query = "select r from Record r " +
                "join fetch r.user u " +
                "join fetch r.goal g " +
                "join fetch r.emotionRecords er " +
                "join fetch er.emotion e " +
                "where u.userId=:userId " +
                "and g.id=:goalId " +
                "and er.emotionType = 'MY_SECOND' " +
                "and e.id =: emotionId ";

        List<Record> records = em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setParameter("emotionId", emotionId) //FIRST
                .getResultList();

        List<Long> ids = new ArrayList<>();
        records.forEach((r)->ids.add(r.getId()));
        em.clear();
        return ids;
    }

    /* FIRST + SECOND */
    private List<Long> findAllByUserAndGoalAndAllEmotion(String userId,
                                                         Long goalId,
                                                         Long firstEmotionId,
                                                         Long secondEmotionId)
    {
        List<Long> firstList = findAllByUserAndGoalAndFirstEmotion(userId, goalId, firstEmotionId);
        List<Long> secondList = findAllByUserAndGoalAndSecondEmotion(userId, goalId, secondEmotionId);
        List<Long> common = new ArrayList<>();
        for (Long f : firstList) for (Long s : secondList) if(f.equals(s)) common.add(f);
        return common;
    }

    private Page<Record> findAllByUserAndGoalAndIn(String userId,
                                                   Long goalId,
                                                   List<Long> recordIds,
                                                   Pageable pageable)
    {
        String query =
                        "select r from Record r " +
                        "join fetch r.user u " +
                        "join fetch r.goal g " +
                        "where u.userId=:userId " +
                        "and g.id=:goalId " +
                        "and r.id in (:recordIds) " +
                        "order by r.useDate desc ";

        List<Record> resultList = em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setParameter("recordIds", recordIds)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        String countQuery = "select count(r) from Record r where r.id in (:recordIds)";

        Object singleResult = em.createQuery(countQuery)
                .setParameter("recordIds", recordIds)
                .getSingleResult();

        return PageableExecutionUtils.getPage(resultList, pageable, new MyLongSupplier(singleResult));
    }
}


