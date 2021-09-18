package com.aspire.mirror.entity;

import com.aspire.mirror.bean.IndicationLimitEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class IndicationEntity {
    /**
     * 指标ID 自增长
     */
    private Integer indicationId;
    /**
     * 所属系统 全量/双送
     */
    private String indicationOwner;
    /**
     * 指标分类 机顶盒/网关
     */
    private String indicationCatalog;
    /**
     * 指标颗粒度 月/日/小时
     */
    private String indicationCycle;
    /**
     * 指标分组 各省/全国14天
     */
    private String indicationGroup;
    /**
     * 指标名称
     */
    private String indicationName;
    /**
     * 指标计算频率 月/日/小时
     */
    private String indicationUpdateFrequency;
    /**
     * 指标别名
     */
    private String indicationAlias;
    /**
     * 指标排序
     */
    private Integer indicationOrder;
    /**
     * 指标显示位置 1:日报显示 2:网站显示 0:都显示
     */
    private Integer indicationDisplay;
    /**
     * 指标单位. 在计算逐日变动值时, 需要使用
     */
    private String indicationUnit;
    /**
     * 指标项配置集合
     */
    List<IndicationAllItemEntity> indicationItemList;
    /**
     * 指标限制集合
     */
    IndicationLimitEntity limitEntity;
}
