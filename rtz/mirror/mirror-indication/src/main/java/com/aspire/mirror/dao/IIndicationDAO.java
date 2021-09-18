package com.aspire.mirror.dao;

import com.aspire.mirror.entity.IndicationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IIndicationDAO {

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
    IndicationEntity getIndicationByIndicationId(@Param("indicationId") Integer indicationId);

    /**
     * 根据指标分类和指标计算周期获取指标集合
     * @param indicationOwner 所属系统 全量/双送
     * @param catalogBox 指标分类
     * @param indicationFrequency 指标计算频率
     * @param group 指标分组 全国/各省
     * @return
     */
    List<IndicationEntity> getIndicationListByOwnerAndCatalogAndFrequencyAndGroup(@Param("indicationOwner") String indicationOwner,
                                                                              @Param("catalogBox") String catalogBox,
                                                                              @Param("indicationFrequency") String indicationFrequency,
                                                                              @Param("indicationGroup") String group,
                                                                              @Param("indicationPosition") String indicationPosition);
}
