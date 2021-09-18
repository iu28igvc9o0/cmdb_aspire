/**
 * 
 */
package com.aspire.ums.cmdb.sync.mapper;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.Instance;
import com.aspire.ums.cmdb.module.entity.Form;
import com.aspire.ums.cmdb.module.entity.FormOptions;
import com.aspire.ums.cmdb.module.entity.Module;

/**
 * @author lupeng
 *
 */
public interface SyncMapper {
	
	/**
	 * 根据code获取模型
	 * @param code
	 * @return
	 */
	Module getModuleByCode(String code);
	
	/**
	 * 获取表单
	 * @param form
	 * @return
	 */
	Form getFormByForm(Form form);
	
	/**
	 * 获取多选
	 * @param formId
	 * @return
	 */
	List<FormOptions> getOptionsByFormId(String formId);
	
	
	/**
	 * 根据模型获取实例
	 * @param moduleId
	 * @return
	 */
	List<Instance> getInstanceByModuleId(String moduleId);
	
	/**
	 * 根据实例获取表单值
	 * @param instanceId
	 * @return
	 */
	List<FormValue> getFormValueMapByInstanceId(String instanceId);

}
