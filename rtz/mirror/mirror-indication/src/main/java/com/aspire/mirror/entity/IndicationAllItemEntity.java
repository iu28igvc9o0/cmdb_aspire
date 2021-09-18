package com.aspire.mirror.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/25
 * 软探针异常指标监控系统
 * com.aspire.mirror.entity.IndicationAllItemEntity
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IndicationAllItemEntity {
    /**
     * 此类 是集合mirror_indication mirror_indication_item mirror_indication_item_config关系后, 得到的一个完整的指标信息类
     */
    /**
     * 指标配置ID
     */
    private Integer configId;
    /**
     * 指标ID
     */
    private Integer indicationId;
    /**
     * 指标项ID
     */
    private Integer indicationItemId;
    /**
     * 指标名称
     */
    private String indicationName;
    /**
     * 指标项编码
     */
    private String indicationItemCode;
    /**
     * 指标项名称
     */
    private String indicationItemName;

    /**
     * 其他指标项是否根据此指标项进行计算, 1:是 0:不是  一个指标只会存在一个主指标项
     */
    private Integer primaryItem;

    /**
     * 指标项单位
     */
    private String indicationItemUnit;

}
