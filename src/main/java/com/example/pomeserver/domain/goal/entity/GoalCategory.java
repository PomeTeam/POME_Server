package com.example.pomeserver.domain.goal.entity;

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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalCategory extends DateBaseEntity {

    @Id @GeneratedValue
    @Column(name = "goal_category_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @OneToMany(mappedBy = "goalCategory", cascade = ALL)
    private List<Goal> goals = new ArrayList<>();

    @Builder
    public GoalCategory(User user, String name) {
        this.addUser(user);
        this.name = name;
    }

    public void addGoal(Goal goal) {
        this.goals.add(goal);
    }

    public void addUser(User user) {
        this.user = user;
        user.addGoalCategory(this);
    }

    public void removeGoal(Goal goal) {
        this.goals.remove(goal);
    }
}
