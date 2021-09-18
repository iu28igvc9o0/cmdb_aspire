package com.aspire.mirror.dao;

import com.aspire.mirror.entity.IndicationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IIndicationDAO {

    /**
     * 获取所有的指标项
     * @return
     */
    List<IndicationEntity> getAllIndication();

    List<IndicationEntity> getIndicationByThemeCode(@Param("themeCode") String themeCode);

    /**
     * 根据指标ID 获取指标详细信息
     * @param indicationId
     * @return
     */
    IndicationEntity getIndicationByIndicationId(@Param("indicationId") Integer indicationId);

    /**
     * 根据指标分类和指标计算周期获取指标集合
     * @param params
     * @return
     */
    List<IndicationEntity> filterList(Map<String, String> params);
}
