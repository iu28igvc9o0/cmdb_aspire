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

    List<String> getInstanceByNameAndModuleId(Map<String, Object> map);

    int getCount(Map<String,Object> map);

    /**
     * 根据设备IP获取实例详情 --> 提供给告警模块的接口
     * @param map
     * @return
     */
    List<InstanceBaseColumn> getInstanceBaseInfoByIp(Map<String, Object> map);

    List<Map> getInstanceByAuthParam(Map<String,Object> param);
}
