package com.aspire.ums.cmdb.collect.mapper;

import com.aspire.ums.cmdb.collect.entity.CollectOriginalRecordEntity;
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
public interface CollectOriginalRecordMapper {

    /**
     * 根据采集配置ID, 查询采集的历史记录
     * @param collectId 采集ID
     * @return 采集历史记录集合
     */
    List<Map> getCollectRecordsByCollectId(@Param("collectId") String collectId,
                                           @Param("pageNo") Integer pageNo,
                                           @Param("pageSize") Integer pageSize);

    /**
     * 根据collectId 删除对象信息
     * @param collectId 采集配置ID
     */
    void deleteVOByCollectId(String collectId);

    /**
     * 新增采集记录
     * @param recordEntity 采集记录信息
     * @return
     */
    void insertVO(CollectOriginalRecordEntity recordEntity);

    /**
     * 根据taskId 获取所有的配置信息
     * @param taskId 任务ID
     * @return 返回配置信息列表
     */
    List<Map<String, String>> getCollectInfoByTaskId(@Param("taskId") String taskId);

    /**
     * 根据任务ID 更新采集任务数据
     * @param recordEntity 采集任务数据
     */
    void updateVOByTaskId(Map<String, Object> recordEntity);
}
