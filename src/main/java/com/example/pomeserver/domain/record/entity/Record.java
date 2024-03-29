package com.example.pomeserver.domain.record.entity;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.global.entity.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "record")
@Entity
public class Record extends DateBaseEntity {

    @Id @Column(name = "record_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @OneToMany(mappedBy="record", cascade=ALL)
    private List<EmotionRecord> emotionRecords = new ArrayList<>();

    @OneToMany(mappedBy="record", cascade=ALL)
    private List<HideRecord> hideRecords = new ArrayList<>();
    private int usePrice;
    private String useDate;
    private String useComment;
    private boolean hasSecond;

    @Builder
    public Record(Goal goal,
                  User user,
                  Integer usePrice,
                  String useDate,
                  String useComment){
        this.usePrice = usePrice;
        this.useDate = useDate;
        this.useComment = useComment;
        this.hasSecond = false;
        this.addGoal(goal);
        this.addUser(user);
    }

    public static Record toUpdateEntity(
            Integer usePrice,
            String useDate,
            String useComment){
        Record record = new Record();
        record.usePrice = usePrice;
        record.useDate = useDate;
        record.useComment = useComment;
        return record;
    }

    public void changeHasSecondToTrue(){
        this.hasSecond = true;
    }

    private void addUser(User user){
        this.user = user;
        user.addRecord(this);
    }

    public void addGoal(Goal goal) {
        this.goal = goal;
        goal.addRecord(this);
    }

    public void edit(Record editRecord) {
        this.usePrice = editRecord.getUsePrice();
        this.useDate = editRecord.getUseDate();
        this.useComment = editRecord.getUseComment();
    }

    public void addEmotionRecord(EmotionRecord emotionRecord) {
        this.emotionRecords.add(emotionRecord);
    }

    public void addHideRecord(HideRecord hideRecord) {
        this.hideRecords.add(hideRecord);
    }
}
