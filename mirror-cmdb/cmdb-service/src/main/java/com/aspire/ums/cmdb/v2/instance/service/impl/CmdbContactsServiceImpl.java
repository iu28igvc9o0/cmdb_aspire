package com.aspire.ums.cmdb.v2.instance.service.impl;

import com.aspire.ums.cmdb.instance.payload.CmdbContactsResponse;
import com.aspire.ums.cmdb.v2.instance.mapper.CmdbContactsMapper;
import com.aspire.ums.cmdb.v2.instance.service.CmdbContactsService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/2 15:37
 */
@Slf4j
@Service("CmdbContactsService")
public class CmdbContactsServiceImpl implements CmdbContactsService {

    @Autowired
    private CmdbContactsMapper contactsMapper;
    @Autowired
    private ModuleService moduleService;

    @Override
    public List<CmdbContactsResponse> findContactsById(Map<String, Object> param) {
        String moduleId = param.get("moduleId").toString();
        String instanceId = param.get("instanceId").toString();
        String tableName = moduleService.getTableNameByModuleId(moduleId);
        return contactsMapper.findContactsById(instanceId, tableName);
    }


}
