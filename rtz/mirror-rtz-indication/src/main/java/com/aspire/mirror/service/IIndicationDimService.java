package com.aspire.mirror.service;

import com.aspire.mirror.entity.IndicationDimEntity;

import java.util.List;

public interface IIndicationDimService {
    /**
     * 获取所有的指标项
     * @return
     */
    List<IndicationDimEntity> getAllDim();

    /**
     * 根据主题编码获取维度列表
     * @param themeCode
     * @return
     */
    List<IndicationDimEntity> getDimListByThemeCode(String themeCode);

    /**
     * 批量新增/更新
     * @param dimList
     */
    void batchInsert(List<IndicationDimEntity> dimList);

    /**
     * 删除dim
     * @param dimEntity
     */
    void deleteDim(IndicationDimEntity dimEntity);
}
