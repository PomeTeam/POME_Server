package com.example.pomeserver.domain.record.entity;

import com.example.pomeserver.domain.goal.entity.GoalCategory;
import com.example.pomeserver.domain.record.entity.vo.EmotionType;
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
@Entity
public class EmotionRecord extends DateBaseEntity {
    @Id
    @Column(name = "emotion_record_id")
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private EmotionType emotionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotion_id")
    private Emotion emotion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    public void addEmotion(Emotion emotion){
        this.emotion = emotion;
        emotion.addEmotionRecord(this);
    }

    public void addRecord(Record record){
        this.record = record;
        record.addEmotionRecord(this);
    }

    public void addUser(User user){
        this.user = user;
        user.addEmotionRecord(this);
    }

    @Builder
    public EmotionRecord(EmotionType emotionType,
                         User user,
                         Emotion emotion,
                         Record record)
    {
        this.emotionType = emotionType;
        this.addUser(user);
        this.addEmotion(emotion);
        this.addRecord(record);
    }
}
