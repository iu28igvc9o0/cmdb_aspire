package com.aspire.ums.cmdb.maintain.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.DynamicInstanceColumn;
import com.aspire.ums.cmdb.maintain.entity.Instance;
import com.aspire.ums.cmdb.maintain.entity.InstanceBaseColumn;

public interface InstanceMapper {
	
	List<Instance> getInstanceByModuleId(String moduleId);
	
    void addInstance(Instance instance);
    
    @SuppressWarnings("rawtypes")
    void addInstanceCirlce(Map map);
    
    @SuppressWarnings("rawtypes")
    void deleteInstanceCirlce(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> checkInstanceName(Map map);
    
    Instance getInstanceByName(String name);
    
    Instance getInstanceById(String id);

    List<Instance> getInstanceByIdArrays(String[] idArrays);
    
    @SuppressWarnings("rawtypes")
    List<DynamicInstanceColumn> getDynamicInstanceColumn(Map map);
    
    @SuppressWarnings("rawtypes")
    List<DynamicInstanceColumn> getDynamicViewInstanceColumn(Map map);
    
    void update(Instance instance);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceInfoById(Map map);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceIdByModuleId(String moduleId);
    
    @SuppressWarnings("rawtypes")
    List<Map> getInstanceLogInfoById(Map map);

    @SuppressWarnings("rawtypes")
    List<InstanceBaseColumn> getInstanceBaseInfoList(Map<String,Object> map);

    int getCount(Map<String,Object> map);
}
