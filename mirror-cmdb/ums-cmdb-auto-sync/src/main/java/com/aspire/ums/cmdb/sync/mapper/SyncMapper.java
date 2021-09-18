package com.aspire.ums.cmdb.sync.mapper;

import java.util.List;

import com.aspire.ums.cmdb.sync.entity.CmdbSyncForm;
import com.aspire.ums.cmdb.sync.entity.Form;
import com.aspire.ums.cmdb.sync.entity.FormOptions;
import com.aspire.ums.cmdb.sync.entity.FormValue;
import com.aspire.ums.cmdb.sync.entity.Instance;
import com.aspire.ums.cmdb.sync.entity.Module;

/**
 * @author lupeng
 *
 */
public interface SyncMapper {
	
	/**
	 * 获取模型同步字段对应关系
	 * @param moduleCode
	 * @return
	 */
	List<CmdbSyncForm> getByModuleCode(String moduleCode);
	
	/**
	 * 获取模型
	 * @param code
	 * @return
	 */
	Module getModuleByCode(String code);
	
	/**
	 * 根据模型获取表单
	 * @param moduleId
	 * @return
	 */
	List<Form> getFormByModuleId(String moduleId);
	
	/**
	 * 获取实例
	 * @param instance
	 */
	Instance getInstance(Instance instance);
	
	
	/**
	 * 根据模型获取实例的id集合
	 * @return
	 */
	List<String> getInstanceIdsByModuleId(String moduleId);
	
	/**
	 * 新增实例
	 * @param instance
	 */
	void insertInstance(Instance instance);
	
	/**
	 * 批量新增表单值
	 * @param values
	 */
	void insertFormValue(List<FormValue> values);
	
	/**
	 * 批量删除表单值
	 * @param instanceId
	 */
	void deleteFormValue(String instanceId);
	
	
	/**
	 * 批量删除多选值
	 * @param formId
	 */
	void deleteFormOptions(String formId);
	
	
	/**
	 * 批量新增多选值
	 * @param options
	 */
	void insertFormOptions(List<FormOptions> options);
	
	/**
	 * 删除实例
	 * @param instanceId
	 */
	void deleteInstance(String instanceId);

}
