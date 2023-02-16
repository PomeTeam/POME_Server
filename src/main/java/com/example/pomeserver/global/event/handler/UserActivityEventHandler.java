package com.example.pomeserver.global.event.handler;

import com.example.pomeserver.domain.marshmello.service.MarshmelloService;
import com.example.pomeserver.global.event.events.AddEmotion;
import com.example.pomeserver.global.event.events.ChangePositiveToNegative;
import com.example.pomeserver.global.event.events.FinishRecord;
import com.example.pomeserver.global.event.events.SuccessGoal;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserActivityEventHandler {
    private final MarshmelloService marshmelloService;
    @Async
    @EventListener
    public void execute(FinishRecord event){
        marshmelloService.levelUpRecordMarshmello(event.getUser());
    }
    @Async
    @EventListener
    public void execute(SuccessGoal event){
        event.getUser().getActivityCount().addSuccessRecordCount();
        marshmelloService.levelUpGrowthMarshmello(event.getUser());
    }
    @Async
    @EventListener
    public void execute(AddEmotion event){
        marshmelloService.levelUpEmotionMarshmello(event.getUser());
    }
    @Async
    @EventListener
    public void execute(ChangePositiveToNegative event){
        event.getUser().getActivityCount().addChangePositiveToNegativeCount();
        marshmelloService.levelUpHonestMarshmello(event.getUser());
    }
}
