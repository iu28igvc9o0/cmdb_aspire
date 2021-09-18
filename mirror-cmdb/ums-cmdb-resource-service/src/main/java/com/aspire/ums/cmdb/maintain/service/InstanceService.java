package com.aspire.ums.cmdb.maintain.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.DynamicInstanceColumn;
import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.Instance;
import com.aspire.ums.cmdb.maintain.entity.InstanceBaseColumn;

public interface InstanceService {


    Instance addInstance(List<FormValue> formValues, String instanceName, String moduleId, String circleId);
    
    @SuppressWarnings("rawtypes")
    List<Map> checkInstanceName(Map map);
    
    void handUpInstance(String circleId,String[] instanceIds);
    
    @SuppressWarnings("rawtypes")
    List<DynamicInstanceColumn> getDynamicInstanceColumn(Map map);
    
    @SuppressWarnings("rawtypes")
    List<DynamicInstanceColumn> getDynamicViewInstanceColumn(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceInfoById(Map map);

    List<InstanceBaseColumn> getInstanceBaseInfoList(Map<String,Object> map);

    List<Instance> getInstanceByIdArrays(String[] idArrays);

    int getCount(Map<String,Object> map);
}
