package com.aspire.ums.cmdb.module.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.module.entity.*;
import com.aspire.ums.cmdb.util.BusinessException;

public interface FormService {
	List<FormBean> getForms(Module module) throws Exception,BusinessException;
	void updateForm(Map<String,Object> map) throws Exception,BusinessException;
	List<OptionBean> getCascaderOptions(String formId);
	List<FormRule> getFormRule();
	FormScript getScriptByformId(String formId);
	List<FormBean> getDicts();
    FormBean getDictValue(String formId);
//	List<Map<String, String>> getFirstBusiness(List<String> ids);
//	List<Map<String, String>> findBusCodeAndName(List<String> ids);
//	List<Map<String, String>> findBusCodeByName(String name);
}
