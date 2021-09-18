package com.aspire.mirror.service;

import com.aspire.mirror.entity.IndicationProvinceEntity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/25
 * 软探针异常指标监控系统
 * com.aspire.mirror.service.IIndicationProvinceService
 */
public interface IIndicationProvinceService {
    /**
     * 根据省份名称 获取编码
     * @param provinceName 省份名称
     * @return
     */
    IndicationProvinceEntity getIndicationProvinceByName(String provinceName);

    /**
     * 获取所有省份
     * @return
     */
    List<IndicationProvinceEntity> getAllProvince();
}
