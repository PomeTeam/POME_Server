package com.example.pomeserver.domain.record.entity;

import com.example.pomeserver.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HideRecord {
    @Id @Column(name = "hide_id")
    @GeneratedValue
    private Long id;

    // record를 숨김으로 한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    // 숨져진 record
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;


    @Builder
    public HideRecord(User user, Record record) {
        addUser(user);
        addRecord(record);
    }

    private void addUser(User user) {
        this.user = user;
        user.addHideRecord(this);
    }

    private void addRecord(Record record) {
        this.record = record;
        record.addHideRecord(this);
    }
}
