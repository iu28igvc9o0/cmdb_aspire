package com.aspire.ums.cmdb.v2.module.service;

import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.module.payload.ModuleTag;
import com.aspire.ums.cmdb.module.payload.SimpleModule;

import java.util.List;
import java.util.Map;

public interface ModuleService {

	/**
	 * 根据模型类型, 获取默认的模型信息
	 * @param moduleType 模型类型
	 * @return
	 */
	Module getDefaultModule(String moduleType);
	/**
	 * 查询模型列表
	 * @return
	 */
	List<Module> getModuleTree(String catalogId, String moduleType);

	/**
	 * 查询模型列表
	 * @return
	 */
	List<Module> getModuleList();

	/**
	 * 根据id获取模型详情
	 */
	Module getModuleDetail(String moduleId);

	SimpleModule getSimpleModuleDetail(String moduleId);
	/**
	 * 根据信息获取模型
	 */
	List<Module>  getModuleSelective(Module module);

	/**
	 * 添加模型
	 * @return
	 */
	void addModule(String topCatalogId, Module module) throws Exception;

	/**
	 * 修改模型
	 * @return
	 */
	void updateModule(String topCatalogId, Module module) throws Exception;
	/**
	 * 修改模型顺序
	 * @return
	 */
	void updateModuleSort(String sortType, String moduleId);
	/**
	 * 删除模型
	 * @return
	 */
	void deleteModule(String moduleId) throws Exception;

	/**
	 * 获取模型标签
	 * @return
	 */
	List<ModuleTag> getModuleTag(String moduleId);

	/**
	 * 查询表数据
	 * @return
	 */
	List<Map<String, Object>> queryTableData(Map<String, Object> queryData);

	String moveModuleData();

	/**
	 * 获取模型查询SQL
	 * @param moduleId 模型ID
	 * @return
	 */
	String getModuleQuerySQL(String moduleId);

	/**
	 * 判断是否是独立模型, 独立模型不引用其他任何模型
	 * 独立模型是指 不需要依赖父模型, 自己包含id/module_id/insert...等列
	 * @param moduleId 模型ID
	 * @return
	 */
	boolean isAloneModule(String moduleId);

	String getModuleQueryCountSQL(String moduleId);

	/**
	 * 获取模型查询SQL
	 * @param moduleId 模型ID
	 * @return
	 */
	String getTempModuleQuerySQL(String moduleId);

	/**
	 * 根据模型id获取tableName(catalog.code)
	 * @param moduleId 模型ID
	 * @return
	 */
	String getTableNameByModuleId(String moduleId);

	/**
	 * 根据模型分组ID, 查询模型列表
	 * @param catalogId 模型分组ID
	 * @return
	 */
	List<Module> getChildByCatalogId(String catalogId);

	/**
	 * 根据模型分组找到对应的模型信息
	 * @param catalogId 模型分组ID
	 * @return
	 */
	Module getModuleDetailByCatalogId(String catalogId);

	/**
	 * 根据模型分组找到对应的模型信息
	 * @param catalogId 模型分组ID
	 * @return
	 */
	SimpleModule getSimpleModuleByCatalogId(String catalogId);

	/**
	 * 根据码表id从关系表中查出相关模型
	 * @param codeId 模型分组ID
	 * @return
	 */
	List<Module> getModuleByCodeId(String codeId);

	/**
	 * 根据id获取模型详情
	 */
	Module getModuleByInstanceId(String instanceId);

	/**
	 * 根据码表id从关系表中查出相关模型
	 * @param moduleId 模型分组ID
	 * @return
	 */
	List<SimpleModule> getRefModuleByModuleId(String moduleId);

	/**
	 * 根据引用模型id查出有哪些引用该模型的模型
	 * @param moduleId 模型分组ID
	 * @return
	 */
	List<SimpleModule> getModuleByRefModuleId(String moduleId);
	/**
	 * 查询当前模型的所有引用模型包括自己
	 * @param moduleId 模型分组ID
	 * @return
	 */
	List<Module> getCurRefModule(String moduleId);


	/**
	 * 根据设备类型, 获取模型的分组编码和模型编码
	 * @param deviceType 设备类型
	 * @return
	 */
	Map<String, String> getModuleCodeAndCatalogCodeByDeviceType(String deviceType);

	/**
	 * 根据模型ID, 查询模型数据
	 */
	List<Map<String, Object>> getModuleData(Map<String, Object> queryParams, String moduleId, String moduleType);

	/**
	 * 根据模型ID或者模型类型(host/business/dict等)
	 * @param moduleId 模型ID
	 * @param moduleType 模型类型
	 * @return
	 */
	String getModuleFlagByIdOrModuleType(String moduleId, String moduleType);

	/**
	 * 根据模型ID, 获取模型的主键
	 * @param moduleId 模型ID
	 * @return
	 */
	List<String> getModuleKeysById(String moduleId);

	/**
	 * 获取模型及父模型信息
	 */
    Map<String, Object> getParentInfo(String moduleId);

	/**
	 * 根据模型主键, 获取模型数据
	 * @param moduleId 模型ID
	 * @param primaryData 主键值
	 * @return
	 */
    Map<String, Object> getModuleDataByPrimarys(String moduleId, Map<String, Object> primaryData);

	/**
	 * 根据模型主键, 获取模型数据
	 * @param moduleId 模型ID
	 * @param primaryData 主键值
	 * @return
	 */
	List<String> getModulePrimaryKeys(String moduleId, Map<String, Object> primaryData);

	/**
	 * 获取模型列表
	 * @param catalogId 模型分组ID
	 * @param moduleId 模型ID
	 */
	List<Module> getTreeByCatalogIdOrModuleId(String catalogId, String moduleId);

	/**
	 * 获取模型所有简单列信息
	 * @param moduleId
	 * @return
	 */
	Map<String, Map<String, String>> getModuleColumns(String moduleId);

	/**
	 * 获取引用模型数据
	 * @param codeId refModuleId
	 * @return
	 * [{id:xx, key:xx, value:xx}]
	 */
	<T> List<Map<String, T>> getRefModuleDict(String codeId);

	/**
	 * 获取模型中文字段名称
	 * @param moduleType 模型类型
	 * @param name 实例ID
	 * @return
	 */
    String getIDByCNName(String moduleType, String name);

	/**
	 * 获取模型和tab管理的附属模型的查询sql
	 * @param moduleId 模型ID
	 * @param tabParamList tab管理查询参数
	 * @param type 操作类型 list - 列表查询,export - 主机配置信息导出
	 */
	String getModuleQuerySQL4Tab(String moduleId,List<String> tabParamList,String type);
	String getModuleQueryCountSQL4Tab(String moduleId,List<String> tabParamList);

	/**
	 * 根据设备类型获取模型信息
	 *
	 * @param deviceType 设备类型
	 * @return
	 */
	Map<String, Object> getModuleIdByDeviceType(String deviceType);

	/**
	 * 生成权限字符串
	 * @param moduleId
	 * @param parentModuleId
	 * @param grantMapper
	 * @return
	 */
	Map<String, Map<String, String>> grantRightJson(String moduleId, String parentModuleId, Map<String, String> grantMapper);
}
