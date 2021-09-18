package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/24
 * 软探针异常指标监控系统
 * com.aspire.mirror.entity.IndicationItemEntity
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IndicationItemEntity {
    /**
     * 指标项ID
     */
    private Integer indicationItemId;
    /**
     * 指标项编码
     */
    private String indicationItemCode;
    /**
     * 指标项名称
     */
    private String indicationItemName;
    /**
     * 指标项单位
     */
    private String indicationItemUnit;
}
