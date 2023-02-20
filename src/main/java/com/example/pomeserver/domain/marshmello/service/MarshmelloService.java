package com.example.pomeserver.domain.marshmello.service;

import com.example.pomeserver.domain.goal.dto.response.GoalResponse;
import com.example.pomeserver.domain.marshmello.dto.response.MarshmelloResponse;
import com.example.pomeserver.domain.marshmello.dto.response.MarshmelloSmallResponse;
import com.example.pomeserver.domain.marshmello.entity.Marshmello;
import com.example.pomeserver.domain.marshmello.exception.execute.MarshmelloNotFoundException;
import com.example.pomeserver.domain.marshmello.repository.MarshmelloRepository;
import com.example.pomeserver.domain.user.entity.User;
import com.example.pomeserver.domain.user.exception.excute.UserNotFoundException;
import com.example.pomeserver.domain.user.repository.UserRepository;
import com.example.pomeserver.global.dto.response.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MarshmelloService {

    private final MarshmelloRepository marshmelloRepository;
    private final UserRepository userRepository;


    public ApplicationResponse<MarshmelloResponse> findByUser(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        Marshmello marshmello = marshmelloRepository.findByUser(user).orElseThrow(MarshmelloNotFoundException::new);
        return ApplicationResponse.ok(MarshmelloResponse.toDto(marshmello));
    }

    public ApplicationResponse<List<MarshmelloSmallResponse>> findMarshmelloListByUser(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new);
        Marshmello marshmello = marshmelloRepository.findByUser(user).orElseThrow(MarshmelloNotFoundException::new);
        return ApplicationResponse.ok(MarshmelloSmallResponse.toListDto(marshmello));
    }

    @Transactional
    public void levelUpRecordMarshmello(User user) { // 1 3 7 10 15
        Marshmello marshmello = marshmelloRepository.findByUser(user).orElseThrow(MarshmelloNotFoundException::new);
        int recordMarshmelloLv = marshmello.getRecordMarshmelloLv();
        int finishRecordCount = user.getActivityCount().getFinishRecordCount();
        if(
           (recordMarshmelloLv == 0 && finishRecordCount == 1)  ||
           (recordMarshmelloLv == 1 && finishRecordCount == 3)  ||
           (recordMarshmelloLv == 2 && finishRecordCount == 7)  ||
           (recordMarshmelloLv == 3 && finishRecordCount == 10) ||
           (recordMarshmelloLv == 4 && finishRecordCount == 15)
        )
        {
            marshmello.levelUpRecordMarshmello();
        }
    }
    @Transactional
    public void levelUpGrowthMarshmello(User user) {
        Marshmello marshmello = marshmelloRepository.findByUser(user).orElseThrow(MarshmelloNotFoundException::new);
        int growthMarshmelloLv = marshmello.getGrowthMarshmelloLv();
        int successRecordCount = user.getActivityCount().getSuccessRecordCount();
        if(
           (growthMarshmelloLv == 0 && successRecordCount == 1) ||
           (growthMarshmelloLv == 1 && successRecordCount == 2) ||
           (growthMarshmelloLv == 2 && successRecordCount == 3) ||
           (growthMarshmelloLv == 3 && successRecordCount == 4) ||
           (growthMarshmelloLv == 4 && successRecordCount == 5)
        )
        {
            marshmello.levelUpGrowthMarshmello();
        }
    }

    @Transactional
    public void levelUpEmotionMarshmello(User user) { // 1 5 15 25 35
        Marshmello marshmello = marshmelloRepository.findByUser(user).orElseThrow(MarshmelloNotFoundException::new);
        int emotionMarshmelloLv = marshmello.getEmotionMarshmelloLv();
        int addEmotionCount = user.getActivityCount().getAddEmotionCount();
        if(
           (emotionMarshmelloLv == 0 && addEmotionCount == 1)  ||
           (emotionMarshmelloLv == 1 && addEmotionCount == 5)  ||
           (emotionMarshmelloLv == 2 && addEmotionCount == 15) ||
           (emotionMarshmelloLv == 3 && addEmotionCount == 25) ||
           (emotionMarshmelloLv == 4 && addEmotionCount == 35)
        )
        {
            marshmello.levelUpEmotionMarshmello();
        }
    }
    @Transactional
    public void levelUpHonestMarshmello(User user) { // 1 3 7 10 15
        Marshmello marshmello = marshmelloRepository.findByUser(user).orElseThrow(MarshmelloNotFoundException::new);
        int honestMarshmelloLv = marshmello.getHonestMarshmelloLv();
        int changePositiveToNegativeCount = user.getActivityCount().getChangePositiveToNegativeCount();
        if(
           (honestMarshmelloLv == 0 && changePositiveToNegativeCount == 1)  ||
           (honestMarshmelloLv == 1 && changePositiveToNegativeCount == 3)  ||
           (honestMarshmelloLv == 2 && changePositiveToNegativeCount == 7)  ||
           (honestMarshmelloLv == 3 && changePositiveToNegativeCount == 10) ||
           (honestMarshmelloLv == 4 && changePositiveToNegativeCount == 15)
        )
        {
            marshmello.levelUpHonestMarshmello();
        }
    }


}
