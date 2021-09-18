package com.aspire.mirror.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指标限制类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IndicationLimitEntity {
    /**
     * 指标限制ID
     */
    private Integer indicationLimitId;
    /**
     * 指标ID
     */
    private Integer indicationId;
    /**
     * 上限
     */
    private String maxValue;
    /**
     * 下限
     */
    private String minValue;
    /**
     * 变动值上限
     */
    private String changeValueMax;
    /**
     * 变动值下限
     */
    private String changeValueMin;
    /**
     * 变动率上限
     */
    private String changeRateMax;
    /**
     * 变动率下限
     */
    private String changeRateMin;
}
