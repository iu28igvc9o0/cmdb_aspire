package com.aspire.ums.cmdb.collect.service.impl;

import com.aspire.ums.cmdb.collect.entity.CollectEmployeeEntity;
import com.aspire.ums.cmdb.collect.mapper.CollectEmployeeMapper;
import com.aspire.ums.cmdb.collect.service.CollectEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectEmployeeServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/3/12 15:08
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class CollectEmployeeServiceImpl implements CollectEmployeeService {

    @Autowired
    private CollectEmployeeMapper collectEmployeeMapper;

    @Override
    public List<CollectEmployeeEntity> getCollectEmployee(String collectId, String operType) {
        return collectEmployeeMapper.getCollectEmployee(collectId, operType);
    }

    @Override
    public void insertVO(CollectEmployeeEntity employeeEntity) {
        collectEmployeeMapper.insertVO(employeeEntity);
    }

    @Override
    public void deleteVOByCollectId(String collectId) {
        collectEmployeeMapper.deleteVOByCollectId(collectId);
    }
}
