package com.aspire.ums.cmdb.module.service;

import java.util.List;

import com.aspire.ums.cmdb.module.entity.FormScript;
import com.aspire.ums.cmdb.module.entity.FormTag;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.util.BusinessException;

public interface ModuleService {
	Module addModule(Module module,List<String> list) throws Exception;

	List<Module> selectModule();

	void deleteModule(Module module) throws BusinessException, Exception;

	List<Module> selectSelective(Module module);

	void updateModule(Module module,List<FormTag> tags);
	
	Module getModuleByModuleName(String moduleName);
	
	List<FormTag> getModuleTag(String mid);

	List<FormScript> getScriptByTagId(String tagId);
	
}
