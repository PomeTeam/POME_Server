package com.example.pomeserver.domain.marshmello.service;

import com.example.pomeserver.domain.marshmello.entity.Marshmello;
import com.example.pomeserver.domain.marshmello.repository.MarshmelloRepository;
import com.example.pomeserver.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MarshmelloService {

    private final MarshmelloRepository marshmelloRepository;
    @Transactional
    public void levelUpRecordMarshmello(User user) { // 1 3 7 10 15

    }
    @Transactional
    public void levelUpGrowthMarshmello(User user) { // 1 2 3 5 7

    }

    @Transactional
    public void levelUpEmotionMarshmello(User user) { // 1 5 15 25 35
        Marshmello marshmello = marshmelloRepository.findByUser(user).orElseThrow(); //TODO 익셉션 추가
        int emotionMarshmelloLv = marshmello.getEmotionMarshmelloLv();
        int addEmotionCount = user.getActivityCount().getAddEmotionCount();
        if(emotionMarshmelloLv == 0 && addEmotionCount == 1){
            marshmello.levelUpEmotionMarshmello();
        } else if (emotionMarshmelloLv == 1 && addEmotionCount == 5) {
            marshmello.levelUpEmotionMarshmello();
        } else if(emotionMarshmelloLv == 2 && addEmotionCount == 15){
            marshmello.levelUpEmotionMarshmello();
        } else if (emotionMarshmelloLv == 3 && addEmotionCount == 25) {
            marshmello.levelUpEmotionMarshmello();
        } else if (emotionMarshmelloLv == 4 && addEmotionCount == 35) {
            marshmello.levelUpEmotionMarshmello();
        }
    }
    @Transactional
    public void levelUpHonestMarshmello(User user) { // 1 3 7 10 15

    }
}
