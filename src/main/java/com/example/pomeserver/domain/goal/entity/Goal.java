package com.example.pomeserver.domain.goal.entity;

import com.example.pomeserver.global.entity.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String oneLineMind;
    private int price;
    private boolean isPublic;
}
