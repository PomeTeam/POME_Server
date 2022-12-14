package com.example.pomeserver.domain.user.entity;

import com.example.pomeserver.global.entity.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@IdClass(FollowPK.class)
public class Follow extends DateBaseEntity {

    @Id @Column(name = "to_user_id", insertable = false, updatable = false)
    private Long toUserId;

    @Id @Column(name = "from_user_id", insertable = false, updatable = false)
    private Long fromUserId;
}
