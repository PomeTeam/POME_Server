package com.example.pomeserver.domain.goal.entity;

import static javax.persistence.CascadeType.ALL;

import com.example.pomeserver.domain.goal.dto.request.GoalTerminateRequest;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.global.entity.DateBaseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal extends DateBaseEntity {
    @Id @Column(name = "goal_id")
    @GeneratedValue
    private Long id;

    private String name; // 목표 카테고리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    private User user;

    @OneToMany(mappedBy="goal", cascade=ALL)
    private List<Record> records = new ArrayList<>();

    private String startDate; // 시작일자
    private String endDate; // 목표일자
    private String oneLineMind; // 한줄 다짐
    private int price; // 목표 금액
    private boolean isPublic; // 공개여부

    private boolean isEnd; // 종료여부

    private String oneLineComment; // 한줄 코멘트

    private boolean isSuccess; // 목표 성공여부 ( 목표 종료 시, 갱신 )

    public void addRecord(Record record) {
        this.records.add(record);
    }
    private void addUser(User user){
        this.user = user;
        user.addGoal(this);
    }


    @Builder
    public Goal(String name,
                String startDate,
                String endDate,
                String oneLineMind,
                int price,
                boolean isPublic,
                User user){
        this.name = name;
        this.addUser(user);
        this.startDate = startDate;
        this.endDate = endDate;
        this.oneLineMind = oneLineMind;
        this.price = price;
        this.isPublic = isPublic;
        this.isSuccess = false;
    }

    public static Goal toUpdateEntity(
            String name,
            String startDate,
            String endDate,
            String oneLineMind,
            int price,
            boolean isPublic
    )
    {
        Goal goal = new Goal();
        goal.name = name;
        goal.startDate = startDate;
        goal.endDate = endDate;
        goal.oneLineMind = oneLineMind;
        goal.price = price;
        goal.isPublic = isPublic;
        return goal;
    }

    public void edit(Goal editGoal) {
        this.startDate = editGoal.getStartDate();
        this.endDate = editGoal.getEndDate();
        this.oneLineMind = editGoal.getOneLineMind();
        this.price = editGoal.getPrice();
        this.isPublic = editGoal.isPublic();
        this.name = editGoal.getName();
        this.isEnd = editGoal.isEnd;
        this.oneLineComment = editGoal.oneLineComment;
    }

    /**
    * 목표 종료 시, 데이터 변경하는 함수
    * */
    public void terminate(GoalTerminateRequest request, boolean isSuccess) {
        this.isEnd = true;
        this.oneLineComment = request.getOneLineComment();

        // 목표 성공여부 저장
        this.isSuccess = isSuccess;
    }

}
