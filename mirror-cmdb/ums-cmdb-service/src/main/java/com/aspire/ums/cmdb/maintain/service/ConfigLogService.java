package com.aspire.ums.cmdb.maintain.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.ConfigLog;
import com.aspire.ums.cmdb.maintain.entity.InstanceModel;


public interface ConfigLogService {
    @SuppressWarnings("rawtypes")
    List<Map> getRelationInfoList(Map map);
    
    void insert(ConfigLog configLog);
    
    void saveInstanceLog(String instanceId, String type);
    
    void saveInstanceUpdateLog(InstanceModel instanceModel);
}
