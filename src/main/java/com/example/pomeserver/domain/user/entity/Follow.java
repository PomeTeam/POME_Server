package com.example.pomeserver.domain.user.entity;

import com.example.pomeserver.global.entity.DateBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Follow extends DateBaseEntity{

    @Id
    @GeneratedValue
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private User toUser; //팔로운 받은 사람

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private User fromUser; //팔로우 건 사람

    @Builder
    public Follow(User toUser, User fromUser) {
        this.addToUser(toUser);
        this.addFromUser(fromUser);
    }

    public void addToUser(User toUser){
        this.toUser = toUser;
        toUser.addToUser(this);
    }

    public void addFromUser(User fromUser){
        this.fromUser = fromUser;
        fromUser.addFromUser(this);
    }
}
