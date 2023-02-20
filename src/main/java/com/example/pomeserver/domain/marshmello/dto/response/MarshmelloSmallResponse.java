package com.example.pomeserver.domain.marshmello.dto.response;

import com.example.pomeserver.domain.marshmello.entity.Marshmello;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
public class MarshmelloSmallResponse {
    @ApiModelProperty(value = "마시멜로 이름", example = "record", required = true)
    private String marshmelloName;

    @ApiModelProperty(value = "마시멜로 레벨", example = "1", required = true)
    private int marshmelloLv;

    public static List<MarshmelloSmallResponse> toListDto(Marshmello marshmello) {
        List<MarshmelloSmallResponse> marshmelloSmallResponseList = new ArrayList<>();
        marshmelloSmallResponseList.add(new MarshmelloSmallResponse("record", marshmello.getRecordMarshmelloLv()));
        marshmelloSmallResponseList.add(new MarshmelloSmallResponse("emotion", marshmello.getRecordMarshmelloLv()));
        marshmelloSmallResponseList.add(new MarshmelloSmallResponse("growth", marshmello.getGrowthMarshmelloLv()));
        marshmelloSmallResponseList.add(new MarshmelloSmallResponse("honest", marshmello.getHonestMarshmelloLv()));
        return marshmelloSmallResponseList;
    }

    @Builder
    public MarshmelloSmallResponse(String marshmelloName, int marshmelloLv) {
        this.marshmelloName = marshmelloName;
        this.marshmelloLv = marshmelloLv;
    }
}
