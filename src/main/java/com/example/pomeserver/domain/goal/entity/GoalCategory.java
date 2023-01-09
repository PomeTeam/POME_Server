package com.example.pomeserver.domain.goal.entity;

import com.example.pomeserver.global.entity.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalCategory extends DateBaseEntity {

    @Id @GeneratedValue
    @Column(name = "goal_category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "goalCategory", cascade = ALL)
    private List<Goal> goals = new ArrayList<>();
}
