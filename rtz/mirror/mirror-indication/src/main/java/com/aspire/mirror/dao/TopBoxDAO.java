package com.aspire.mirror.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TopBoxDAO {

    /**
     * 更新机顶盒指标阙值
     * @param  updateIndicationData
     */
    void updateIndicationData(@Param("updateIndicationData") List<Map<String,String>> updateIndicationData);

    /**
     * 新增机顶盒指标阙值
     * @param insertIndicationData
     */
    void insertIndicationData(@Param("insertIndicationData") List<Map<String,String>> insertIndicationData);

    /**
     * 删除机顶盒指标阙值
     * @param deleteIndicationData
     */
    void deleteIndicationData(@Param("deleteIndicationData") List<String> deleteIndicationData);

    /**
     * 根据指标ID获取指标名称
     * @param indicationId
     * @return
     */
    Map<String,String> getIndicationNameById(@Param("indicationId") String indicationId);



}
