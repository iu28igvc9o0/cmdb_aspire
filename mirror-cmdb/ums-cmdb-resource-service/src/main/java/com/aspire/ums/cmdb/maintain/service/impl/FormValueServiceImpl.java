package com.aspire.ums.cmdb.maintain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.Instance;
import com.aspire.ums.cmdb.maintain.entity.InstanceModel;
import com.aspire.ums.cmdb.maintain.mapper.FormValueMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceMapper;
import com.aspire.ums.cmdb.maintain.service.FormValueService;
import com.aspire.ums.cmdb.util.UUIDUtil;

@Service
@Transactional
public class FormValueServiceImpl implements FormValueService {
	
	@Autowired
	private FormValueMapper formValueMapper;
	
	@Autowired
    private InstanceMapper instanceMapper;

    @SuppressWarnings({ "rawtypes"})
    @Override
    public List<FormValue> getFormValues(Map map) {
        // TODO Auto-generated method stub
        return formValueMapper.getFormValues(map);
    }

    @Override
    public void update(InstanceModel instanceModel) {
        Instance instance = new Instance();
        instance.setId(instanceModel.getId());
        instance.setName(instanceModel.getName());
        instanceMapper.update(instance);
        // TODO Auto-generated method stub
        for(FormValue f: instanceModel.getFormValues()){{
            if("Y_name".equals(f.getForm().getCode())){
                f.setFormValue(instanceModel.getName());
            }
            
            if(StringUtils.isNotBlank(f.getId())){
                formValueMapper.update(f);
            }else{
                f.setId(UUIDUtil.getUUID());
                f.setInstanceId(instance.getId());
                List<FormValue> formValues = new ArrayList<FormValue>();
                formValues.add(f);
                insert(formValues);
            }
            }
        }
        
        
    }
    
    @Override
    public void insert(List<FormValue> formValues) {
        // TODO Auto-generated method stub  
            formValueMapper.insert(formValues);
    }
    
    @SuppressWarnings({ "rawtypes"})
    @Override
    public List<FormValue> getFormValuesByModuleId(Map map) {
        // TODO Auto-generated method stub
        return formValueMapper.getFormValuesByModuleId(map);
    }

}
