package com.aspire.mirror.service;

import com.aspire.mirror.bean.IndicationLimitEntity;

import java.util.List;

public interface IIndicationLimitService {

    /**
     * 根据limit_id查询限制信息
     * @param indicationLimitId
     * @return
     */
    IndicationLimitEntity getIndicationLimitByLimitId(Integer indicationLimitId);

    /**
     * 根据指标ID 加载限制信息
     * @param indicationId
     * @return
     */
    List<IndicationLimitEntity> getIndicationLimitByIndicationId(Integer indicationId);

    /**
     * 获取所有指标阈值
     * @return
     */
    List<IndicationLimitEntity> getAll();
    
    /**
     * 保存网关指标设置
     * @return
     */
   int saveMerits(List<IndicationLimitEntity> merits);

    /**
     * 根据指标ID 省份编码 获取指标阈值
     * @param indicationId 指标ID
     * @param provinceCode 省份编码
     * @return
     */
   IndicationLimitEntity getIndicationByIndicationIdAndProvinceCode(Integer indicationId, String provinceCode);
}
