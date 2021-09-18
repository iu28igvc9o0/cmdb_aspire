package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/24
 * 软探针异常指标监控系统
 * com.aspire.mirror.entity.IndicationItemConfigEntity
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IndicationItemConfigEntity {
    /**
     * 配置项ID
     */
    private Integer configId;
    /**
     * 指标ID
     */
    private Integer indicationId;
    /**
     * 指标项ID
     */
    private IndicationAllItemEntity indicationItemEntity;
    /**
     * 指标项在指标中的排序位置
     */
    private Integer indicationItemOrder;

}
