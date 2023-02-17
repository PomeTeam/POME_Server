package com.example.pomeserver.domain.marshmello.dto.response;

import com.example.pomeserver.domain.marshmello.entity.Marshmello;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MarshmelloResponse {

    @ApiModelProperty(value = "기록 말랑", example = "1", required = true)
    private int recordMarshmelloLv;

    @ApiModelProperty(value = "발전 말랑", example = "1", required = true)
    private int growthMarshmelloLv;

    @ApiModelProperty(value = "공감 말랑", example = "1", required = true)
    private int emotionMarshmelloLv;

    @ApiModelProperty(value = "솔직 말랑", example = "1", required = true)
    private int honestMarshmelloLv;

    public static MarshmelloResponse toDto(Marshmello marshmello){
        MarshmelloResponse marshmelloResponse = new MarshmelloResponse();
        marshmelloResponse.recordMarshmelloLv = marshmello.getRecordMarshmelloLv();
        marshmelloResponse.growthMarshmelloLv = marshmello.getGrowthMarshmelloLv();
        marshmelloResponse.emotionMarshmelloLv = marshmello.getEmotionMarshmelloLv();
        marshmelloResponse.honestMarshmelloLv = marshmello.getHonestMarshmelloLv();
        return marshmelloResponse;

    }
}
