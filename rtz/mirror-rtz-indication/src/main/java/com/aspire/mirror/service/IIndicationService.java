package com.aspire.mirror.service;

import com.aspire.mirror.entity.IndicationEntity;

import java.util.List;
import java.util.Map;

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
     * @param params
     * @return
     */
    List<IndicationEntity> filterList(Map<String, String> params);
}
