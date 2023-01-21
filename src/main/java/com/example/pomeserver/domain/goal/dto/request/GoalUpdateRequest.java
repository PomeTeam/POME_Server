package com.example.pomeserver.domain.goal.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data
public class GoalUpdateRequest {
    @NotNull(message = "목표 카테고리 ID는 필수값입니다.")
    private Long goalCategoryId;

    //@Pattern(regexp = "(19|20)\\\\d{2}\\\\.((11|12)|(0?(\\\\d)))\\\\.(30|31|((0|1|2)?\\\\d))", message = "날짜의 패턴은 yyyy.mm.dd이어야 합니다.")
    @NotNull(message = "시작 날짜는 필수값입니다.")
    @NotBlank(message = "시작 날짜는 빈 문자열일 수 없습니다.")
    private String startDate;

    //@Pattern(regexp = "(19|20)\\\\d{2}\\\\.((11|12)|(0?(\\\\d)))\\\\.(30|31|((0|1|2)?\\\\d))", message = "날짜의 패턴은 yyyy.mm.dd이어야 합니다.")
    @NotNull(message = "종료 날짜는 필수값입니다.")
    @NotBlank(message = "종료 날짜는 빈 문자열일 수 없습니다.")
    private String endDate;

    @NotNull(message = "한 줄 다짐은 필수값입니다.")
    @NotBlank(message = "한 줄 다짐은 빈 문자열일 수 없습니다.")
    @Size(max = 19, message = "코멘트는 20자 이하만 가능합니다.")
    private String oneLineMind;

    @Positive(message = "금액은 0원 이하일 수 없습니다.")
    @NotNull(message = "금액은 필수값입니다.")
    private int price;

    @NotNull(message = "금액은 필수값입니다.")
    private Boolean isPublic;
}
