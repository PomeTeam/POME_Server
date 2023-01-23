package com.example.pomeserver.domain.goal.entity;

import static javax.persistence.CascadeType.ALL;

import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.global.entity.DateBaseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String startDate;
    private String endDate;
    private String oneLineMind;
    private int price;
    private boolean isPublic;

    public void addRecord(Record record) {
        this.records.add(record);
    }

    @Builder
    public Goal(GoalCategory goalCategory,
                String startDate,
                String endDate,
                String oneLineMind,
                int price,
                boolean isPublic){
        this.addGoalCategory(goalCategory);
        this.startDate = startDate;
        this.endDate = endDate;
        this.oneLineMind = oneLineMind;
        this.price = price;
        this.isPublic = isPublic;
    }

    public static Goal toUpdateEntity(
            GoalCategory goalCategory,
            String startDate,
            String endDate,
            String oneLineMind,
            int price,
            boolean isPublic
    )
    {
        Goal goal = new Goal();
        goal.goalCategory = goalCategory;
        goal.startDate = startDate;
        goal.endDate = endDate;
        goal.oneLineMind = oneLineMind;
        goal.price = price;
        goal.isPublic = isPublic;
        return goal;
    }

    private void addGoalCategory(GoalCategory goalCategory) {
        this.goalCategory = goalCategory;
        goalCategory.addGoal(this);
    }

    public void edit(Goal editGoal) {
        this.startDate = editGoal.getStartDate();
        this.endDate = editGoal.getEndDate();
        this.oneLineMind = editGoal.getOneLineMind();
        this.price = editGoal.getPrice();
        this.isPublic = editGoal.isPublic();
        if(!Objects.equals(this.getGoalCategory().getId(), editGoal.getGoalCategory().getId())) editGoalCategory(editGoal.getGoalCategory());
    }

    private void editGoalCategory(GoalCategory goalCategory){
        this.goalCategory.removeGoal(this);
        this.addGoalCategory(goalCategory);
    }
}
