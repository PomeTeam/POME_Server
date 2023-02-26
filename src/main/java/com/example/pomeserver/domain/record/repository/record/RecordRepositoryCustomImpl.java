package com.example.pomeserver.domain.record.repository.record;

import com.example.pomeserver.domain.record.dto.paramResolver.param.RecordFilteringParam;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.global.util.supplier.MyLongSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

@RequiredArgsConstructor
@Repository
public class RecordRepositoryCustomImpl implements RecordRepositoryCustom{

    private final EntityManager em;

    @Override
    public Page<Record> findAllByUserCustom(String userId, List<Long> hideRecordIds, Pageable pageable){
        if(hideRecordIds.isEmpty()) return findAllByUserCustom(userId, pageable);
        String query =
                "select r from Record r join fetch r.user u " +
                "where u.userId=:userId and " +
                "r.id not in (:hideRecordIds) " +
                "order by r.useDate desc";
        List<Record> resultList = em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("hideRecordIds", hideRecordIds)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        String countQuery = "select count(r) from Record r join r.user u where u.userId=:userId and r.id not in (:hideRecordIds)";
        Object singleResult = em.createQuery(countQuery)
                .setParameter("userId", userId)
                .setParameter("hideRecordIds", hideRecordIds)
                .getSingleResult();

        return PageableExecutionUtils.getPage(resultList, pageable, new MyLongSupplier(singleResult));
    }

    public Page<Record> findAllByUserCustom(String userId, Pageable pageable){
        String query =
                "select r from Record r join fetch r.user u " +
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
            List<String> friendIds,
            List<Long> hideRecordIds,
            Pageable pageable)
    {
        if(hideRecordIds.isEmpty()) return findAllByFriends(friendIds, pageable);
        String query =
                "select r from Record r " +
                "join fetch r.user u " +
                "where u.userId in (:friendIds) and " +
                "r.id not in (:hideRecordIds) " +
                "order by r.useDate desc";

        List<Record> resultList = em.createQuery(query, Record.class)
                .setParameter("friendIds", friendIds)
                .setParameter("hideRecordIds", hideRecordIds)
                .setFirstResult((int) (pageable.getOffset()))
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        String countQuery = "select count(r) from Record r join r.user u where u.userId in (:friendIds) and r.id not in (:hideRecordIds)";
        Object singleResult = em.createQuery(countQuery)
                .setParameter("friendIds", friendIds)
                .setParameter("hideRecordIds", hideRecordIds)
                .getSingleResult();

        return PageableExecutionUtils.getPage(resultList, pageable, new MyLongSupplier(singleResult));
    }

    private Page<Record> findAllByFriends(List<String> friendIds, Pageable pageable) {
        String query =
                        "select r from Record r " +
                        "join fetch r.user u " +
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

        String query =
                "select r from Record r" +
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

        String query =
                "select r from Record r" +
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

    @Override // 회고탭
    public Page<Record> findAllByUserAndGoalAndEmotionFiltering(String userId,
                                                                Long goalId,
                                                                RecordFilteringParam emotionParam,
                                                                Pageable pageable)
    {
        Long firstEmotion = emotionParam.getFirstEmotion();
        Long secondEmotion = emotionParam.getSecondEmotion();
        if(!emotionParam.isNull()){ // 감정 필터링 조건이 존재하는 경우
            List<Long> recordIds = new ArrayList<>();
            if(emotionParam.hasAllEmotion())
                recordIds = getRecordIdsByUserAndGoalAndAllEmotion(userId, goalId, firstEmotion, secondEmotion);
            else if(emotionParam.hasFirstEmotion())
                recordIds = getRecordIdsByUserAndGoalAndFirstEmotion(userId, goalId, firstEmotion);
            else if(emotionParam.hasSecondEmotion())
                recordIds = getRecordIdsByUserAndGoalAndSecondEmotion(userId, goalId, secondEmotion);
            return findAllByUserAndGoalAndIn(userId, goalId, recordIds, pageable);
        }

        String query = "select r from Record r " +
                "join fetch r.user u " +
                "join fetch r.goal g " +
                "where u.userId=:userId " +
                "and g.id=:goalId " +
                "and r.hasSecond = true " +
                "order by r.useDate desc ";


        List<Record> resultList = em.createQuery(query, Record.class)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        String countQuery =
                "select count(r) from Record r " +
                "join r.user u " +
                "join r.goal g " +
                "where u.userId=:userId " +
                "and r.hasSecond = true " +
                "and g.id=:goalId";
        Object singleResult = em.createQuery(countQuery)
                .setParameter("userId", userId)
                .setParameter("goalId", goalId)
                .getSingleResult();

        return PageableExecutionUtils.getPage(resultList, pageable, new MyLongSupplier(singleResult));
    }

    // 회고탭
    /* FIRST */
    private List<Long> getRecordIdsByUserAndGoalAndFirstEmotion(String userId,
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
                "and r.hasSecond = true " +
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
    // 회고탭
    /* SECOND */
    private List<Long> getRecordIdsByUserAndGoalAndSecondEmotion(String userId,
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
                "and r.hasSecond = true " +
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
    // 회고탭
    /* FIRST + SECOND */
    private List<Long> getRecordIdsByUserAndGoalAndAllEmotion(String userId,
                                                              Long goalId,
                                                              Long firstEmotionId,
                                                              Long secondEmotionId)
    {
        List<Long> firstList = getRecordIdsByUserAndGoalAndFirstEmotion(userId, goalId, firstEmotionId);
        List<Long> secondList = getRecordIdsByUserAndGoalAndSecondEmotion(userId, goalId, secondEmotionId);
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


