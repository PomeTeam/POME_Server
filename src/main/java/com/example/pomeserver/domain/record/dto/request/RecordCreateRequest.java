package com.example.pomeserver.domain.record.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@ApiModel("기록 작성 요청 객체")
@NoArgsConstructor
@Data
public class RecordCreateRequest {

    @ApiModelProperty(value = "목표 ID", example = "1", required = true, dataType = "number")
    @NotNull(message = "목표 ID는 필수값입니다.")
    private Long goalId;

    @ApiModelProperty(value = "감정 ID", example = "2", required = true, dataType = "number")
    @NotNull(message = "감정 ID는 필수값입니다.")
    private Long emotionId;

    @ApiModelProperty(value = "사용 금액", example = "3000", required = true, dataType = "number")
    @Positive(message = "금액은 0원 이하일 수 없습니다.")
    @NotNull(message = "금액은 필수값입니다.")
    private int usePrice;

//    @Pattern(regexp = "(19|20)\\\\d{2}\\\\.((11|12)|(0?(\\\\d)))\\\\.(30|31|((0|1|2)?\\\\d))", message = "날짜의 패턴은 yyyy.mm.dd이어야 합니다.")
    @ApiModelProperty(value = "소비 날짜(yyyy.mm.dd)", example = "2023.02.23", required = true, dataType = "string")
    @NotNull(message = "금액은 필수값입니다.")
    @NotBlank(message = "날짜는 빈 문자열일 수 없습니다.")
    private String useDate;   // "2023.01.13"

    @ApiModelProperty(value = "기록 코멘트", example = "이런저런 소비를 하였다. ^.^", required = true, dataType = "string")
    @Size(max = 151, message = "코멘트는 150자 이하만 가능합니다.")
    private String useComment;

    @Builder
    public RecordCreateRequest(Long goalId,
                               Long emotionId,
                               int usePrice,
                               String useDate,
                               String useComment) {
        this.goalId = goalId;
        this.emotionId = emotionId;
        this.usePrice = usePrice;
        this.useDate = useDate;
        this.useComment = useComment;
    }
}

