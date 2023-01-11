package com.example.pomeserver.domain.user.entity;

import javax.persistence.*;

import com.example.pomeserver.domain.record.entity.Record;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;
    @Column(nullable = false, unique = true)
    private String nickname;
    private String phoneNum;
    private String image;

    @OneToMany(mappedBy="emotion", cascade=ALL)
    private List<Record> records = new ArrayList<>();

    @Builder
    public User(String userId, String nickname, String phoneNum, String image) {
        this.userId = userId;
        this.nickname = nickname;
        this.phoneNum = phoneNum;
        this.image = image;
    }

    public void addRecord(Record record) {
        this.records.add(record);
    }
}