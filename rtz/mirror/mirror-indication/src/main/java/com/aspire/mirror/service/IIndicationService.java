package com.aspire.mirror.service;

import com.aspire.mirror.entity.IndicationEntity;

import java.util.List;

public interface IIndicationService {

    /**
     * 获取所有的指标项
     * @return
     */
    List<IndicationEntity> getAllIndication();

    /**
     * 根据指标ID 获取指标详细信息
     * @param indicationId
     * @return
     */
    IndicationEntity getIndicationByIndicationId(Integer indicationId);

    /**
     * 根据指标分类和指标计算周期获取指标集合
     * @param indicationOwner 所属系统
     * @param catalogBox 指标分类
     * @param group 分组
     * @param indicationFrequency 计算频率
     * @param indicationPosition 显示位置
     * @return
     */
    List<IndicationEntity> getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(String indicationOwner,
                                                                              String catalogBox,
                                                                              String indicationFrequency,
                                                                              String group,
                                                                              String indicationPosition);
}
