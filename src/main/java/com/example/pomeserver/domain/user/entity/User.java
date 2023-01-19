package com.example.pomeserver.domain.user.entity;

import com.example.pomeserver.domain.goal.entity.GoalCategory;
import javax.persistence.*;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.record.entity.Record;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, unique = true)
    private String nickname;
    private String phoneNum;
    private String image;

    @OneToMany(mappedBy="user", cascade=ALL)
    private List<Record> records = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<GoalCategory> goalCategories = new ArrayList<>();

    @Builder
    public User(String userId, String nickname, String phoneNum, String image) {
        this.userId = userId;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.image = image;
    }

    public void addRecord(Record record) {
        this.records.add(record);
    }

    public void addGoalCategory(GoalCategory goalCategory) {
        this.goalCategories.add(goalCategory);
    }

    public void removeGoalCategory(GoalCategory goalCategory) {
        this.goalCategories.remove(goalCategory);
    }
}