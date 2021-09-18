package com.aspire.ums.cmdb.common.service;

import com.aspire.ums.cmdb.common.entity.QueryEntity;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: QueryService
 * Author:   zhu.juwang
 * Date:     2019/3/25 15:32
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface QueryService {
    /**
     * 根据模型ID和用户 获取用户定义的所有自定义查询条件
     * @param moduleId 模型ID
     * @param user 用户
     * @return 自定义条件列表
     */
    List<QueryEntity> getQueryConditionByModuleIdAndUserAndMenuType(String moduleId, String user, String menuType);

    /**
     * 新增自定义条件
     * @param queryEntity 自定义条件VO
     */
    void insertVO(QueryEntity queryEntity);

    /**
     * 修改自定义条件
     * @param queryEntity 自定义条件VO
     */
    void updateVO(QueryEntity queryEntity);

    /**
     * 删除自定义条件
     * @param queryEntity 自定义条件VO
     */
    void deleteVO(QueryEntity queryEntity);

}
