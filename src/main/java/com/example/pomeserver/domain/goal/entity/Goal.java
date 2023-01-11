package com.example.pomeserver.domain.goal.entity;

import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.global.entity.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal extends DateBaseEntity {
    @Id @Column(name = "goal_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="goal_category_id")
    private GoalCategory goalCategory;

    @OneToMany(mappedBy="goal", cascade=ALL)
    private List<Record> records = new ArrayList<>();

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String oneLineMind;
    private int price;
    private boolean isPublic;

    public void addRecord(Record record) {
        this.records.add(record);
    }
}
