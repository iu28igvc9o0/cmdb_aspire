package com.aspire.ums.cmdb.module.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.module.entity.Form;
import com.aspire.ums.cmdb.module.entity.FormScript;
import com.aspire.ums.cmdb.module.entity.FormTag;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.module.mapper.ModuleMapper;
import com.aspire.ums.cmdb.module.service.ModuleService;
import com.aspire.ums.cmdb.module.service.impl.IconServiceImpl;
import com.aspire.ums.cmdb.util.BusinessException;
/**
 * 
 * <p>Project: ums-cmdb-service</p>
 *
 * @Description: 
 *
 * @author: mingjianyong
 *
 * @Date: 2017-6-28
 */
@RestController
@RequestMapping("/cmdb/module")
public class ModuleController {
	
	@Autowired
	private ModuleService moduleService;
	
	private static Logger logger = Logger.getLogger(ModuleController.class);
	/**
	 * 
	 *Description:获取所有的模型信息
	 * @return
	 */
	@RequestMapping("/getModule")
	public List<Module> getModule(){
		List<Module> modules = moduleService.selectModule();
		return modules;
	}
	@RequestMapping("/addModule")
	public Map<String,Object> addModule(@RequestBody Map<String,Object> module){
		
		Module m = JSON.parseObject(JSON.toJSON(module).toString(), Module.class);
		List<String> tags = (List<String>) module.get("tags");
		
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			Module mo = moduleService.addModule(m,tags);
			map.put("module", mo);
			map.put("success", true);
			map.put("message", "新增成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("模型新增失败："+e);
			map.put("success", false);
			map.put("message", "新增失败");
		}
		return map;
	}
	/**
	 * 
	 *Description: 获取模型下的所有标签
	 * @param moduleId
	 * @return
	 */
	@RequestMapping("/getModuleTags")
	public List<FormTag> getModuleTags(String moduleId){
		return moduleService.getModuleTag(moduleId);
	}
	/**
	 * 
	 *Description: 根据标签id获取表单脚本
	 * @param tagId
	 * @return
	 */
	@RequestMapping("/getScriptByTagId")
	public List<FormScript> getScriptByTagId(String tagId){
		return moduleService.getScriptByTagId(tagId);
	}
	
	@RequestMapping("/deleteModule")
	public Map<String,Object> deleteModule(Module module){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			moduleService.deleteModule(module);
			map.put("success", true);
			map.put("message", "删除成功");
		} catch (BusinessException e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "删除失败！");
		}
		return map;
	}
	
	@RequestMapping("/getModuleSelective")
	public List<Module> getModuleSelective(Module module){
		return moduleService.selectSelective(module);
	}
	
	@RequestMapping("/updateModule")
	public Map<String,Object> updateModule(@RequestBody Map<String,Object> module){
		Module m = JSON.parseObject(JSON.toJSON(module).toString(), Module.class);
		List<FormTag> tags = new ArrayList<FormTag>();
		if(module.containsKey("tags")){
			tags = JSON.parseArray(JSON.toJSON(module.get("tags")).toString(), FormTag.class);
		}
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			moduleService.updateModule(m,tags);
			map.put("success", true);
			map.put("message", "更新成功");
		} catch (BusinessException e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message", "更新失败失败！");
		}
		return map;
	}
	
	
}
