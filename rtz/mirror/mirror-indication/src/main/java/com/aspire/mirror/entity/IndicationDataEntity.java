package com.aspire.mirror.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/24
 * 软探针异常指标监控系统
 * com.aspire.mirror.entity.IndicationDataEntity
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IndicationDataEntity {
    /**
     * 指标ID
     */
    private Integer indicationId;
    /**
     * 指标对象
     */
    private IndicationEntity indicationEntity;

    /**
     * 指标项ID
     */
    private Integer indicationItemId;

    /**
     * 指标项对象
     */
    private IndicationAllItemEntity indicationAllItemEntity;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 省份对象
     */
    private IndicationProvinceEntity provinceEntity;
    /**
     * 计算日期
     */
    private String calcDate;
    /**
     * 保存使用单位计算后的值
     */
    private String calcValue;

    /**
     * 保存计算后的原始值
     */
    private String origCalcValue;

    /**
     * 修改日期
     */
    private Date modifyDate;
}
