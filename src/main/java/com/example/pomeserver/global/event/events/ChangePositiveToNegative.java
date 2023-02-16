package com.example.pomeserver.global.event.events;

import com.example.pomeserver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChangePositiveToNegative {
    private User user;
}
