package com.example.pomeserver.domain.record.dto.assembler;

import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.entity.Emotion;
import com.example.pomeserver.domain.record.entity.EmotionRecord;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.record.entity.vo.EmotionType;
import com.example.pomeserver.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class EmotionRecordAssembler {

    public EmotionRecord toEntity(Record record,
                                  User user,
                                  Emotion emotion,
                                  EmotionType emotionType)
    {
        return EmotionRecord.builder()
                .emotionType(emotionType)
                .emotion(emotion)
                .record(record)
                .user(user)
                .build();
    }

    public Record toEntity(RecordUpdateRequest request){
        return Record.toUpdateEntity(request.getUsePrice(), request.getUseDate(), request.getUseComment());
    }

}
