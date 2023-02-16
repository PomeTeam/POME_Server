package com.example.pomeserver.domain.user.entity.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter(AccessLevel.PROTECTED)
public class ActivityCount {
    private int successRecordCount;
    private int finishRecordCount;
    private int addEmotionCount;
    private int changePositiveToNegativeCount;

    public ActivityCount(){
        this.successRecordCount = 0;
        this.finishRecordCount = 0;
        this.addEmotionCount = 0;
        this.changePositiveToNegativeCount = 0;
    }

    public void addSuccessRecordCount() {
        this.successRecordCount += 1;
    }
    public void addFinishRecordCount() {
        this.finishRecordCount += 1;
    }
    public void addAddEmotionCount() {
        this.addEmotionCount += 1;
    }
    public void addChangePositiveToNegativeCount() {
        this.changePositiveToNegativeCount += 1;
    }
}
