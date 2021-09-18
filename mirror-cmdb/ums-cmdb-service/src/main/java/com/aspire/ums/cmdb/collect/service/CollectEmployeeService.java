package com.aspire.ums.cmdb.collect.service;

import com.aspire.ums.cmdb.collect.entity.CollectEmployeeEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectEmployeeService
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface CollectEmployeeService {

    /**
     * 根据采集ID 对象类型 获取对象列表
     * @param collectId 配置ID
     * @param operType 对象类型
     * @return 对象列表
     */
    List<CollectEmployeeEntity> getCollectEmployee(@Param("collectId") String collectId,
                                                   @Param("operType") String operType);

    /**
     * 新增采集配置对象
     * @param employeeEntity 对象信息
     * @return 新增的主键ID
     */
    void insertVO(CollectEmployeeEntity employeeEntity);

    /**
     * 根据collectId 删除对象信息
     * @param collectId 采集配置ID
     */
    void deleteVOByCollectId(String collectId);
}
