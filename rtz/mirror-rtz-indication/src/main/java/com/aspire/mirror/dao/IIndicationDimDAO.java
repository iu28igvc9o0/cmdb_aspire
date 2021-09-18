package com.aspire.mirror.dao;

import com.aspire.mirror.entity.IndicationDimEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IIndicationDimDAO {

    /**
     * 获取所有的指标项
     * @return
     */
    List<IndicationDimEntity> getAllDim();

    /**
     * 批量新增/更新
     * @param dimList
     */
    void batchInsert(List<IndicationDimEntity> dimList);

    /**
     * 根据主题编码获取维度列表
     * @param themeCode
     * @return
     */
    List<IndicationDimEntity> getDimListByThemeCode(@Param("themeCode") String themeCode);

    /**
     * 删除dim
     * @param dimEntity
     */
    void deleteDim(IndicationDimEntity dimEntity);
}
