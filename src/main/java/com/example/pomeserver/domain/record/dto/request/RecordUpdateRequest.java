package com.example.pomeserver.domain.record.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@Data
public class RecordUpdateRequest {

    @NotNull(message = "목표 ID는 필수값입니다.")
    private Long goalId;

    @Positive(message = "금액은 0원 이하일 수 없습니다.")
    @NotNull(message = "금액은 필수값입니다.")
    private int usePrice;

//    @Pattern(regexp = "(19|20)\\\\d{2}\\\\.((11|12)|(0?(\\\\d)))\\\\.(30|31|((0|1|2)?\\\\d))", message = "날짜의 패턴은 yyyy.mm.dd이어야 합니다.")
    @NotNull(message = "금액은 필수값입니다.")
    @NotBlank(message = "날짜는 빈 문자열일 수 없습니다.")
    private String useDate;

    @Size(max = 21, message = "코멘트는 20자 이하만 가능합니다.")
    private String useComment;
}
