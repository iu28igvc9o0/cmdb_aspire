package com.aspire.ums.cmdb.maintain.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.FormValue;

public interface FormValueMapper {
	
    @SuppressWarnings("rawtypes")
    List<FormValue> getFormValues(Map map);
    
    @SuppressWarnings("rawtypes")
    List<FormValue> getDynmaicFormValues(Map map);
    
    void update(FormValue formValue);

    void update2(FormValue formValue);

    void insert(List<FormValue> formValues);

    void insertSingle(FormValue formValue);
    
    @SuppressWarnings("rawtypes")
    List<FormValue> getFormValuesByModuleId(Map map);
    
    List<Map<String,String>> getFormValueMapByInstanceId(String instanceId);
    
    void deleteByInstanceId(String instanceId);
}
