package com.aspire.mirror.dao;

import com.aspire.mirror.entity.IndicationItemConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IIndicationItemConfigDAO {

    /**
     * 根据配置ID,获取指标与指标项的配置信息
     * @param configId 配置ID
     * @return
     */
    IndicationItemConfigEntity getIndicationConfigByConfigId(@Param("configId") Integer configId);

    /**
     * 根据指标ID, 查询所有的指标配置项集合
     * @param indicationId 指标ID
     * @return
     */
    List<IndicationItemConfigEntity> getIndicationConfigByIndicationId(@Param("indicationId") Integer indicationId);
}
