package com.example.pomeserver.domain.record.dto.assembler;

import com.example.pomeserver.domain.goal.entity.Goal;
import com.example.pomeserver.domain.record.dto.request.RecordCreateRequest;
import com.example.pomeserver.domain.record.dto.request.RecordUpdateRequest;
import com.example.pomeserver.domain.record.entity.Record;
import com.example.pomeserver.domain.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RecordAssembler {

    public Record toEntity(RecordCreateRequest request, Goal goal, User user){
        return Record.builder()
                .goal(goal)
                .user(user)
                .usePrice(request.getUsePrice())
                .useDate(request.getUseDate())
                .useComment(request.getUseComment())
                .build();
    }

    public Record toEntity(RecordUpdateRequest request){
        return Record.toUpdateEntity(
                request.getUsePrice(),
                request.getUseDate(),
                request.getUseComment());
    }

}
