package com.example.pomeserver.domain.user.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class FollowPK implements Serializable {

    private Long toUserId;
    private Long fromUserId;
}
