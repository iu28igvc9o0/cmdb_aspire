package com.aspire.ums.cmdb.maintain.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.InstanceModel;

public interface FormValueService {
	
    @SuppressWarnings("rawtypes")
    List<FormValue> getFormValues(Map map);
    
    void update(InstanceModel instanceModel);
	
    void insert(List<FormValue> formValues);
    
    @SuppressWarnings("rawtypes")
    List<FormValue> getFormValuesByModuleId(Map map);
}
