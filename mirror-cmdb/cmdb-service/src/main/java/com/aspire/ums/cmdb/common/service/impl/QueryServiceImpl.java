package com.aspire.ums.cmdb.common.service.impl;

import com.aspire.ums.cmdb.common.entity.QueryEntity;
import com.aspire.ums.cmdb.common.mapper.QueryMapper;
import com.aspire.ums.cmdb.common.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
public class QueryServiceImpl implements QueryService {

    @Autowired
    private QueryMapper queryMapper;

    @Override
    public List<QueryEntity> getQueryConditionByModuleIdAndUserAndMenuType(String moduleId, String user, String menuType) {
        return queryMapper.getQueryConditionByModuleIdAndUserAndMenuType(moduleId, user, menuType);
    }

    @Override
    public void insertVO(QueryEntity queryEntity) {
        queryMapper.insertVO(queryEntity);
    }

    @Override
    public void updateVO(QueryEntity queryEntity) {
        queryMapper.updateVO(queryEntity);
    }

    @Override
    public void deleteVO(QueryEntity queryEntity) {
        queryMapper.deleteVO(queryEntity);
    }
}
