package com.aspire.ums.cmdb.maintain.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.InstanceBaseColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.maintain.entity.DynamicInstanceColumn;
import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.Instance;
import com.aspire.ums.cmdb.maintain.mapper.FormValueMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceMapper;
import com.aspire.ums.cmdb.maintain.service.InstanceService;
import com.aspire.ums.cmdb.util.UUIDUtil;

@Service
@Transactional
public class InstanceServiceImpl implements InstanceService {
	
    @Autowired
    private InstanceMapper instanceMapper;
    
	@Autowired
	private FormValueMapper formValueMapper;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Instance addInstance(List<FormValue> formValues, String instanceName, String moduleId, String circleId) {
        Instance instance = new Instance();
        instance.setId(UUIDUtil.getUUID());
        instance.setModuleId(moduleId);
        instance.setName(instanceName);
        
        Map inMap = new HashMap();
        inMap.put("circleId", circleId);
        inMap.put("instanceId", instance.getId());
        instanceMapper.addInstanceCirlce(inMap);
        
        instanceMapper.addInstance(instance);
        // TODO Auto-generated method stub
        if(formValues != null && formValues.size()>0){
            for(FormValue f: formValues){
                f.setId(UUIDUtil.getUUID());
                f.setInstanceId(instance.getId());
                if("Y_name".equals(f.getForm().getCode())){
                    f.setFormValue(instanceName);
                }
            }
            formValueMapper.insert(formValues);
        }
        return instance;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> checkInstanceName(Map map) {
        // TODO Auto-generated method stub
        return instanceMapper.checkInstanceName(map);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void handUpInstance(String circleId, String[] instanceIds) {
        
        for(String instanceId:instanceIds){
            Map inMap = new HashMap();
            inMap.put("instanceId", instanceId);
            instanceMapper.deleteInstanceCirlce(inMap);
            inMap.put("circleId", circleId);
            instanceMapper.addInstanceCirlce(inMap);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<DynamicInstanceColumn> getDynamicInstanceColumn(Map map) {
        // TODO Auto-generated method stub
        return instanceMapper.getDynamicInstanceColumn(map);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public List<DynamicInstanceColumn> getDynamicViewInstanceColumn(Map map) {
        // TODO Auto-generated method stub
        return instanceMapper.getDynamicViewInstanceColumn(map);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getInstanceInfoById(Map map) {
        // TODO Auto-generated method stub
        return instanceMapper.getInstanceInfoById(map);
    }
    @SuppressWarnings("rawtypes")
    @Override
    public List<InstanceBaseColumn> getInstanceBaseInfoList(Map<String, Object> map) {
        List<InstanceBaseColumn> list = instanceMapper.getInstanceBaseInfoList(map);
        return list;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<Instance> getInstanceByIdArrays(String[] idArrays) {
        // TODO Auto-generated method stub
        return instanceMapper.getInstanceByIdArrays(idArrays);
    }


    @Override
    public int getCount(Map<String, Object> map) {
        return instanceMapper.getCount(map);
    }
}
