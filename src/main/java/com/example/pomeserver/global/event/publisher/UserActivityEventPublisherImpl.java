package com.example.pomeserver.global.event.publisher;

import com.example.pomeserver.global.event.Activity;
import com.example.pomeserver.global.event.ActivityType;
import com.example.pomeserver.global.event.events.AddEmotion;
import com.example.pomeserver.global.event.events.ChangePositiveToNegative;
import com.example.pomeserver.global.event.events.FinishRecord;
import com.example.pomeserver.global.event.events.SuccessGoal;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserActivityEventPublisherImpl implements UserActivityEventPublisher{
    private final ApplicationEventPublisher publisher;

    @Override
    public void execute(Activity activity) {
        if(activity.getActivityType().equals(ActivityType.FINISH_RECORD))
            publisher.publishEvent(new FinishRecord(activity.getUser()));
        else if(activity.getActivityType().equals(ActivityType.SUCCESS_GOAL))
            publisher.publishEvent(new SuccessGoal(activity.getUser()));
        else if(activity.getActivityType().equals(ActivityType.ADD_EMOTION))
            publisher.publishEvent(new AddEmotion(activity.getUser()));
        else if(activity.getActivityType().equals(ActivityType.CHANGE_POSITIVE_TO_NEGATIVE))
            publisher.publishEvent(new ChangePositiveToNegative(activity.getUser()));
    }
}

