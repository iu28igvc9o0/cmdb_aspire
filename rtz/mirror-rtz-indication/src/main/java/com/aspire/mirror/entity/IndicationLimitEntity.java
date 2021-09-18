package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndicationLimitEntity {
    private Integer indicationLimitId;
    private String indicationId;
    private String maxValue;
    private String minValue;
    private String changeValueMax;
    private String changeValueMin;
    private String changeRateMax;
    private String changeRateMin;
    public IndicationLimitEntity(String maxValue,String minValue,String changeValueMax,String changeValueMin,
    		String changeRateMax,String changeRateMin){
    	this.maxValue = maxValue;
    	this.minValue = minValue;
    	this.changeValueMax = changeValueMax;
    	this.changeValueMin = changeValueMin;
    	this.changeRateMax = changeRateMax;
    	this.changeRateMin = changeRateMin;
    }
    public String toString() {
        return String.format("{indicationId:%s,maxValue:%s, minValue:%s, changeValueMax:%s, changeValueMin:%s, changeRateMax:%s, changeRateMin:%s}",
                getIndicationId(),getMaxValue(), getMinValue(), getChangeValueMax(), getChangeValueMin(), getChangeRateMax(), getChangeRateMin());
    }
}
