package com.example.pomeserver.global.event.publisher;

import com.example.pomeserver.global.event.Activity;

public interface UserActivityEventPublisher {
    void execute(Activity activity);
}
