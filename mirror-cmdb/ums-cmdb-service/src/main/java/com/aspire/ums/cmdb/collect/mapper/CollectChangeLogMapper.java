package com.aspire.ums.cmdb.collect.mapper;

import com.aspire.ums.cmdb.collect.entity.CollectChangeLogEntity;
import com.aspire.ums.cmdb.module.entity.FormBean;
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
public interface CollectChangeLogMapper {

    /**
     * 根据模型ID 查询配置异常信息
     * @param moduleId 模型ID
     * @param queryFields 查询列列表
     * @return 异常信息列表
     */
    List<Map> getChangeLogsByModuleId(@Param("moduleId") String moduleId,
                                      @Param("queryFields") List<Map<String, String>> queryFields,
                                      @Param("innerQueryWhere") String innerQueryWhere,
                                      @Param("queryWhere") String queryWhere);

    /**
     * 根据批次号, 获取改变列表
     * @param batchId 批次号
     * @return
     */
    List<CollectChangeLogEntity> getChangeLogsByBatchId(@Param("batchId") String batchId);

    /**
     * 新增异常配置记录
     * @param changeLogEntity
     */
    void insertVO(CollectChangeLogEntity changeLogEntity);

    /**
     * 获取指定batchId及主机实例的变更信息
     * @param instanceId
     * @param batchId
     * @param instanceForms
     * @return
     */
    Map<String, String> getChangeLogByBatchId(@Param("instanceId") String instanceId,
                              @Param("batchId") String batchId,
                              @Param("instanceForms") List<FormBean> instanceForms);
}
