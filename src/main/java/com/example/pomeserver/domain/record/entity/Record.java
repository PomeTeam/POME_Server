package com.example.pomeserver.domain.record.entity;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.global.entity.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "record")
@Entity
public class Record extends DateBaseEntity {

    @Id @Column(name = "record_id")
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id")
    private Emotion emotion;

    private int usePrice;
    private String useDate;
    private String useComment;


    @Builder
    public static Record create(Goal goal, User user, Emotion emotion, Integer usePrice, String useDate, String useComment) {
        Record record = new Record();
        record.addGoal(goal);
        record.addUser(user);
        record.addEmotion(emotion);
        record.usePrice = usePrice;
        record.useDate = useDate;
        record.useComment = useComment;
        return record;
    }
    //TODO
    private void addEmotion(Emotion emotion) {
    }
    //TODO
    private void addUser(User user) {
    }
    //TODO
    private void addGoal(Goal goal) {
    }
}
