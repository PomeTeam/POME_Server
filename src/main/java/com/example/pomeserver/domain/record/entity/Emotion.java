package com.example.pomeserver.domain.record.entity;

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
public class Emotion extends DateBaseEntity{

    @Id @Column(name = "emotion_id")
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy="emotion", cascade=ALL)
    private List<EmotionRecord> emotionRecords = new ArrayList<>();

    private String emotionName;
    private String image;

    public void addEmotionRecord(EmotionRecord emotionRecord) {
        this.emotionRecords.add(emotionRecord);
    }

    @Builder
    public Emotion(Long id, String emotionName, String image) {
        this.id = id;
        this.emotionName = emotionName;
        this.image = image;
    }
}
