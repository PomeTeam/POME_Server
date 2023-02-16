package com.example.pomeserver.global.event;
import com.example.pomeserver.domain.user.entity.User;
import lombok.Getter;

@Getter
public class Activity {
    private User user;
    private ActivityType activityType;

    public static Activity create(User user, ActivityType activityType) {
        Activity activity = new Activity();
        activity.user = user;
        activity.activityType = activityType;
        return activity;
    }
}
