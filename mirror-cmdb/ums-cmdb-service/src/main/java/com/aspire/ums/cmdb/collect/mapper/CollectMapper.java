package com.aspire.ums.cmdb.collect.mapper;

import com.aspire.ums.cmdb.collect.entity.CollectEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectService
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface CollectMapper {

    /**
     * 根据模型ID, 获取模型下 配置的采集配置信息
     * @param moduleId 模型ID
     * @return 配置的采集配置信息列表
     */
    List<CollectEntity> getCollectsByModuleId(@Param("moduleId") String moduleId);

    /**
     * 根据模型ID, 获取模型下 配置的采集配置信息
     * @param moduleId 模型ID
     * @return 配置的采集配置信息列表
     */
    List<Map> getCollectListMapByModuleId(@Param("moduleId") String moduleId);

    /**
     * 新增采集配置
     * @param collectEntity 采集配置信息
     * @return 新增的采集配置ID
     */
    void insertVO(CollectEntity collectEntity);

    /**
     * 删除采集配置信息
     * @param deleteEntity 采集配置信息
     */
    void deleteVO(CollectEntity deleteEntity);

    /**
     * 根据采集频率 获取采集配置信息
     * @return 采集配置列表
     */
    List<CollectEntity> getCollectByFrequency(String frequency);

    /**
     * 根据CollectId 获取采集配置信息
     * @param collectId 采集配置ID
     * @return 采集配置信息
     */
    CollectEntity getCollectByCollectId(@Param("collectId") String collectId);
}
