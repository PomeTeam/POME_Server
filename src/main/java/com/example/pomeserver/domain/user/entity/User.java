package com.example.pomeserver.domain.user.entity;

import com.example.pomeserver.domain.goal.entity.GoalCategory;
import javax.persistence.*;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.record.entity.EmotionRecord;
import com.example.pomeserver.domain.record.entity.HideRecord;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.user.entity.vo.UserType;
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

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy="user", cascade=ALL)
    private List<Goal> goals = new ArrayList<>();

    @OneToMany(mappedBy="user", cascade=ALL)
    private List<Record> records = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<GoalCategory> goalCategories = new ArrayList<>();

    @OneToMany(mappedBy = "toUser", cascade = ALL)
    private List<Follow> toUser = new ArrayList<>();

    @OneToMany(mappedBy = "fromUser", cascade = ALL)
    private List<Follow> fromUser = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<EmotionRecord> emotionRecords = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<HideRecord> hideRecords = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = ALL)
    private UserWithdrawal userWithdrawal;

    @Builder
    public User(String userId, String nickname, String phoneNum, String image) {
        this.userId = userId;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.image = image;
        setUserType(UserType.ACCESS);
    }

    public void setUserType(UserType userType){this.userType = userType;}

    public void addGoal(Goal goal) {
        this.goals.add(goal);
    }

    public void addGoalCategory(GoalCategory goalCategory) {
        this.goalCategories.add(goalCategory);
    }

    public void removeGoalCategory(GoalCategory goalCategory) {
        this.goalCategories.remove(goalCategory);
    }

    public void addEmotionRecord(EmotionRecord emotionRecord) {
        this.emotionRecords.add(emotionRecord);
    }

    public void addFromUser(Follow fromUser){
        this.fromUser.add(fromUser);
    }

    public void addToUser(Follow toUser){
        this.toUser.add(toUser);
    }

    public void addRecord(Record record) {
        this.records.add(record);
    }

    public void addHideRecord(HideRecord hideRecord) {
        this.hideRecords.add(hideRecord);
    }
}