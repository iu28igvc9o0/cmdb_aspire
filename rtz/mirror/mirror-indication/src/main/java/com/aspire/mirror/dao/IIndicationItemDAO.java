package com.aspire.mirror.dao;

import com.aspire.mirror.entity.IndicationAllItemEntity;
import com.aspire.mirror.entity.IndicationItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IIndicationItemDAO {

    /**
     * 根据指标项ID 获取指标信息
     * @param indicationItemId
     * @return
     */
    IndicationItemEntity getIndicationItemByItemId(@Param("indicationItemId") Integer indicationItemId);

    /**
     * 根据指标ID, 获取指标所有的指标项.
     * @param indicationId
     * @return
     */
    List<IndicationAllItemEntity> getIndicationAllItemsByIndicationId(@Param("indicationId") Integer indicationId);
}
