package com.example.pomeserver.domain.marshmello.entity;

import com.example.pomeserver.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Marshmello {

    @Id @Column(name = "marshmello_id")
    @GeneratedValue
    private Long id;
    private int recordMarshmelloLv;
    private int growthMarshmelloLv;
    private int emotionMarshmelloLv;
    private int honestMarshmelloLv;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    public Marshmello() {
        this.recordMarshmelloLv = 0;
        this.growthMarshmelloLv = 0;
        this.emotionMarshmelloLv = 0;
        this.honestMarshmelloLv = 0;
    }

    public void levelUpEmotionMarshmello() {
        this.emotionMarshmelloLv += 1;
    }

    public void levelUpGrowthMarshmello() {
        this.growthMarshmelloLv += 1;
    }

    public void levelUpRecordMarshmello() {
        this.recordMarshmelloLv += 1;
    }

    public void levelUpHonestMarshmello() {
        this.honestMarshmelloLv += 1;
    }

    public void addUser(User user) {
        this.user = user;
    }
}
