/**
 * 
 */
package com.aspire.ums.cmdb.sync.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.sync.entity.Form;
import com.aspire.ums.cmdb.sync.entity.FormOptions;
import com.aspire.ums.cmdb.sync.entity.FormValue;
import com.aspire.ums.cmdb.sync.entity.Instance;
import com.aspire.ums.cmdb.sync.entity.Module;

/**
 * @author lupeng
 *
 */
public interface CmdbSyncFormService {
	
	/**
	 * 
	 * @param moduleCode
	 * @return key:远程字段   value:本地字段
	 */
	Map<String,String> getFieldMap(String moduleCode);
	
	/**
	 * 实例及值同步新增
	 * @param module
	 * @param values
	 */
	public void insertData(Instance instance,List<FormValue> values,boolean exist);
	
	/**
	 * 获取模型
	 * @param code
	 * @return
	 */
	public Module getModuleByCode(String code);
	
	
	/**
	 * 获取实例
	 * @param instance
	 * @return
	 */
	public Instance getInstance(Instance instance);
	
	
	/**
	 * @param moduleId
	 * @return key:code value:id
	 */
	Map<String,String> getFormByModuleId(String moduleId);
	
	/**
	 * 根据模型获取实例的id集合
	 * @return
	 */
	List<String> getInstanceIdsByModuleId(String moduleId);
	
	/**
	 * 移除冗余的实例
	 * @return
	 */
	public void DeleteInstances(String moduleId,List<String> localList, List<String> syncList);
	
	
	/**
	 * 批量更新多选属性
	 * @param formId
	 */
	public void updateOptions(String formId,List<FormOptions> options);
}
