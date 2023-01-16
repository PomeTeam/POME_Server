package com.example.pomeserver.domain.record.entity;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.global.entity.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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
    @JoinColumn(name = "emotion_id")
    private Emotion emotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    private int usePrice;
    private String useDate;
    private String useComment;


    @Builder
    public Record(Goal goal,
                  User user,
                  Emotion emotion,
                  Integer usePrice,
                  String useDate,
                  String useComment){
        Record record = new Record();
        record.usePrice = usePrice;
        record.useDate = useDate;
        record.useComment = useComment;
        record.addGoal(goal);
        record.addUser(user);
        record.addEmotion(emotion);
    }

    public static Record toUpdateEntity(
            Emotion emotion,
            Integer usePrice,
            String useDate,
            String useComment){
        Record record = new Record();
        record.usePrice = usePrice;
        record.useDate = useDate;
        record.useComment = useComment;
        record.emotion = emotion;
        return record;
    }

    private void addEmotion(Emotion emotion){
        this.emotion = emotion;
        emotion.addRecord(this);
    }

    private void addUser(User user){
        this.user = user;
        user.addRecord(this);
    }

    private void addGoal(Goal goal) {
        this.goal = goal;
        goal.addRecord(this);
    }

    public void edit(Record editRecord) {
        this.usePrice = editRecord.getUsePrice();
        this.useDate = editRecord.getUseDate();
        this.useComment = editRecord.getUseComment();
        if(!Objects.equals(this.getEmotion().getId(), editRecord.getEmotion().getId())) editEmotion(editRecord.getEmotion());
    }

    private void editEmotion(Emotion emotion) {
        this.emotion.removeRecord(this);
        addEmotion(emotion);
    }
}
