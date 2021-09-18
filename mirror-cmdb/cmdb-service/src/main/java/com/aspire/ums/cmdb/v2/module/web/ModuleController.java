package com.aspire.ums.cmdb.v2.module.web;

import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.module.IModuleAPI;
import com.aspire.ums.cmdb.module.payload.FullModule;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.module.payload.ModuleTag;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.cache.CacheConst;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class ModuleController implements IModuleAPI {
	
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private ICmdbInstanceService instanceService;
	@Autowired
	private ICmdbV3ModuleCatalogService catalogService;

	/**
	 *
	 *Description:获取所有的模型信息
	 * @return
	 */
	public List<Module> getModuleTree(@RequestParam(value = "catalogId", required = false) String catalogId,
									  @RequestParam(value = "moduleType", required = false) String moduleType){
		return moduleService.getModuleTree(catalogId, moduleType);
	}

	/**
	 * 查询模型列表
	 *
	 * @return
	 */
	@Override
	public List<Module> getModuleList() {
		return moduleService.getModuleList();
	}

	/**
	 * 获取模型列表
	 * @param catalogId 模型分组ID
	 * @param moduleId 模型ID
	 */
	public List<Module> getTreeByCatalogIdOrModuleId(@RequestParam(value = "catalogId", required = false) String catalogId,
											  @RequestParam(value = "moduleId", required = false) String moduleId){
		return moduleService.getTreeByCatalogIdOrModuleId(catalogId, moduleId);
	}

	/**
	 * 根据id获取模型详情
	 */
	@Override
	public Module getModuleDetail(@RequestParam("moduleId") String moduleId) {
		return moduleService.getModuleDetail(moduleId);
	}

	/**
	 * 根据信息获取模型
	 */
	@Override
	public List<Module> getModuleSelective(@RequestBody Module module) {
		return moduleService.getModuleSelective(module);
	}

	/**
	 *
	 *Description:新增模型
	 * @return
	 */
	public Map<String,Object> addModule(@RequestParam("topCatalogId") String topCatalogId, @RequestBody Module module){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			// 创建模型
			moduleService.addModule(topCatalogId, module);
			map.put("success", true);
			map.put("message", "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("模型新增失败："+e);
			map.put("success", false);
			map.put("message", "新增失败!" + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> updateModule(@RequestParam("topCatalogId") String topCatalogId, @RequestBody Module module) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			log.info("start to update module, update Data: {}", module);
			moduleService.updateModule(topCatalogId, module);
			map.put("success", true);
			map.put("message", "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("模型更新失败：" + e);
			map.put("success", false);
			map.put("message", "更新失败!" + e.getMessage() );
		}
		return map;
	}

	@Override
	public Map<String, Object> updateModuleSort(@RequestParam("sortType") String sortType,
												@RequestParam("moduleId") String moduleId) {
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			log.info("start to updateModuleSort, : sortType {} , moduleId {}", sortType, moduleId);
			moduleService.updateModuleSort(sortType, moduleId);
			map.put("success", true);
			map.put("message", "更新模型顺序成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("更新模型顺序失败："+e);
			map.put("success", false);
			map.put("message", "更新模型顺序失败!" + e.getMessage());
		}
		return map;
	}

	/**
	 * Description:删除模型
	 * @return
	 */
	@Override
	public Map<String, Object> deleteModule(@PathVariable String moduleId) {
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			log.info("start to delete module, delete module id: {}", moduleId);
			moduleService.deleteModule(moduleId);
			map.put("success", true);
			map.put("message", "删除模型成功");
			CacheConst.CACHE_MODULE_MAP.remove(moduleId);
			CacheConst.CACHE_INSTANCE_MAP.remove(moduleId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("删除模型失败："+e);
			map.put("success", false);
			map.put("message", "删除模型失败!" + e.getMessage());
		}
		return map;
	}

	@Override
	public List<ModuleTag> getModuleTag(@PathVariable String moduleId) {
		return moduleService.getModuleTag(moduleId);
	}

	@Override
	public List<Map<String, Object>> queryTableData(@RequestBody Map<String, Object> queryData) {
		return moduleService.queryTableData(queryData);
	}

	@Override
	public FullModule getModuleByInstanceId(@RequestParam("instance_id") String instanceId) {
		CmdbInstance queryInstance = new CmdbInstance();
		queryInstance.setId(instanceId);
		CmdbInstance instance = instanceService.get(queryInstance);
		if (instance != null) {
//			return getModuleDetail(instance.getModuleId());
			return null;
		}
		return null;
	}

	@Override
	public void refreshCache() {
		// todo 刷新redis缓存
//		//刷新 模型缓存
//		moduleCache.refreshCache();
//		//刷新 码表缓存
//		codeCache.refreshCache();
//		//刷新CI模型 所属列 列表
//		instanceCache.refreshCache();
	}

	@Override
	public Module getModuleByCatalogId(@RequestParam("catalogId") String catalogId) {
		return moduleService.getModuleDetailByCatalogId(catalogId);
	}

	@Override
	public String moveModuleData() {
		return moduleService.moveModuleData();
	}

	@Override
	public Map<String, Object> getModuleExist(@RequestParam("catalogId") String catalogId,
											  @RequestParam("moduleCode") String moduleCode) {
		Map<String, Object> resultMap = new HashMap<>();
		Module module = new Module();
		// 判断父模型是否被创建
		List<Module> modules = new ArrayList<>();
		CmdbV3ModuleCatalog catalog = catalogService.getById(catalogId);
		if (!catalog.getParentCatalogId().equals("0")) {
			Module pModule = moduleService.getModuleDetailByCatalogId(catalog.getParentCatalogId());
			if (pModule == null || StringUtils.isEmpty(pModule.getId())) {
				resultMap.put("success", false);
				resultMap.put("message", "父模型编码尚未创建,请先创建父模型！");
				return resultMap;
			}
		}
		// 判断编码是否存在
		module.setCode(moduleCode);
		modules = moduleService.getModuleSelective(module);
		if (modules != null && modules.size() > 0) {
			resultMap.put("success", false);
			resultMap.put("message", "模型编码已存在,请重新编辑模型编码");
			return resultMap;
		}
		// 判断分组是否已被创建
		module = new Module();
		module.setCatalogId(catalogId);
		modules = moduleService.getModuleSelective(module);
		if (modules != null && modules.size() > 0) {
			resultMap.put("success", false);
			resultMap.put("message", "当前分组模型已存在，请切换模型分组");
			return resultMap;
		}
		resultMap.put("success", true);
		resultMap.put("message", "当前模型可新增");
		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getModuleData(@RequestBody Map<String, Object> queryParams,
												   @RequestParam(value = "moduleId", required = false) String moduleId,
												   @RequestParam(value = "moduleType", required = false) String moduleType) {
		return moduleService.getModuleData(queryParams, moduleId, moduleType);
	}

	@Override
	public Map<String, Object> getParentInfo(@RequestParam("module_id") String moduleId) {
		return moduleService.getParentInfo(moduleId);
	}

	@Override
	public String getModuleQuerySQL(@RequestParam("module_id") String moduleId) {
		return moduleService.getModuleQuerySQL(moduleId);
	}

	@Override
	public String getTempModuleQuerySQL(@RequestParam("module_id") String moduleId) {
		return moduleService.getTempModuleQuerySQL(moduleId);
	}

	@Override
	public List<Map<String, Object>> getRefModuleDict(@RequestParam("codeId") String codeId) {
		return moduleService.getRefModuleDict(codeId);
	}

	@Override
	public Map<String, Map<String, String>> getModuleColumns(@RequestParam("moduleId") String moduleId) {
		return moduleService.getModuleColumns(moduleId);
	}

	/**
	 * 根据设备类型获取模型信息
	 *
	 * @param deviceType
	 * @return
	 */
	@Override
	public Map<String, Object> getModuleIdByDeviceType(@RequestParam(value = "device_type") String deviceType) {
		return moduleService.getModuleIdByDeviceType(deviceType);
	}

	/**
	 * 生成权限字符串
	 *
	 * @param moduleId       模型ID
	 * @param parentModuleId
	 * @param grantMapper
	 * @return
	 */
	@Override
	public Map<String, Map<String, String>> grantRightJson(@RequestParam(value = "module_id", required = false) String moduleId,
														   @RequestParam(value = "parent_module_id", required = false) String parentModuleId,
														   @RequestBody Map<String, String> grantMapper) {
		return moduleService.grantRightJson(moduleId, parentModuleId, grantMapper);
	}
}
