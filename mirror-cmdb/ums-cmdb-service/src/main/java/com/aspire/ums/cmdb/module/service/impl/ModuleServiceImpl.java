package com.aspire.ums.cmdb.module.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.maintain.entity.ConfigLog;
import com.aspire.ums.cmdb.maintain.mapper.CircleMapper;
import com.aspire.ums.cmdb.maintain.mapper.ConfigLogMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceRelationMapper;
import com.aspire.ums.cmdb.maintain.mapper.MaintainViewMapper;
import com.aspire.ums.cmdb.module.entity.Form;
import com.aspire.ums.cmdb.module.entity.FormParam;
import com.aspire.ums.cmdb.module.entity.FormScript;
import com.aspire.ums.cmdb.module.entity.FormTag;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.module.mapper.FormMapper;
import com.aspire.ums.cmdb.module.mapper.FormParamMapper;
import com.aspire.ums.cmdb.module.mapper.FormScriptMapper;
import com.aspire.ums.cmdb.module.mapper.FormTagMapper;
import com.aspire.ums.cmdb.module.mapper.ModuleMapper;
import com.aspire.ums.cmdb.module.service.ModuleService;
import com.aspire.ums.cmdb.util.BusinessException;
import com.aspire.ums.cmdb.util.UUIDUtil;
/**
 * 
 * <p>Project: ums-cmdb-service</p>
 *
 * @Description: 
 *
 * @author: mingjianyong
 *
 * @Date: 2017-6-30
 */
@Service("moduleService")
@Transactional
public class ModuleServiceImpl implements ModuleService {
	
	@Autowired
	private ModuleMapper moduleMapper;
	@Autowired
	private FormMapper formMapper;
	@Autowired
	private FormParamMapper formParmMapper;
	@Autowired
	private FormTagMapper formTagMapper;
    @Autowired
    private InstanceMapper instanceMapper;
    @Autowired
    private CircleMapper circleMapper;
    @Autowired
    private FormScriptMapper formScriptMapper;
    @Autowired
    private InstanceRelationMapper instanceRelationMapper;
    @Autowired
    private ConfigLogMapper configLogMapper;
	   
	private static Logger logger = Logger.getLogger(ModuleServiceImpl.class);
	
	@Override
	@Transactional
	public Module addModule(Module module,List<String> list) throws Exception {
		String moduleId = UUIDUtil.getUUID();
		module.setId(moduleId);
		module.setDisabled("false");//新增为可用
		module.setIsdelete(0);
		module.setBuiltin("false");
		int maxIndex = moduleMapper.selectMaxIndex();
		module.setSortindex(maxIndex+1);
		//插入数据库
		moduleMapper.insertSelective(module);
		//为新增的模型添加一个默认内置的表单元素
		Form f = new Form();
		String fid = UUIDUtil.getUUID();
		f.setId(fid);
		f.setType(Constants.MODULE_FORM_TYPE_SINGLEROWTEXT);
		f.setBuiltin("true");f.setSortindex(1);
		f.setModuleid(moduleId);f.setCode("Y_name");f.setName("名称");f.setDefaultvalue("");
		f.setKeyattr("true");f.setRequired("true");f.setIsdelete(0);f.setHidden("false");
		//添加默认到表单参数表
		FormParam fp = new FormParam(UUIDUtil.getUUID(),"validation","",fid,0);
		FormParam fp2 = new FormParam(UUIDUtil.getUUID(),"minLength","1",fid,0);
		FormParam fp3 = new FormParam(UUIDUtil.getUUID(),"maxLength","100",fid,0);
		formParmMapper.insertSelective(fp);
		formParmMapper.insertSelective(fp2);
		formParmMapper.insertSelective(fp3);
		//添加默认的属性分组
		Form group = new Form();
		group.setId(UUIDUtil.getUUID());group.setName("基本信息");group.setIsdelete(0);
		group.setModuleid(moduleId);group.setCode("group");group.setHidden("false");
		group.setKeyattr("false");group.setRequired("false");
		group.setType(Constants.MODULE_FORM_TYPE_GROUPLINE);
		group.setSortindex(0);
		formMapper.insertSelective(f);
		formMapper.insertSelective(group);
		//添加标签到模型标签表
		for(String s:list){
			FormTag ft = new FormTag();
			ft.setId(UUIDUtil.getUUID());
			ft.setModuleid(moduleId);
			ft.setTag(s);
			formTagMapper.insertSelective(ft);
		}	
		return module;
	}

	@Override
	public List<Module> selectModule() {
		return moduleMapper.selectModule();
	}
	/**
	 * 1.删除表单定义表中该模型的的表单项
	 */
	@Override
	@Transactional
	public void deleteModule(Module module) throws BusinessException, Exception {
		if(!"".equals(module.getId()) && null!= module.getId()){
			try {
				formMapper.deleteByModuleId(module.getId());
				module.setIsdelete(1);
				moduleMapper.updateByPrimaryKeySelective(module);
			} catch (Exception e) {
				logger.error("模型删除失败！");
				throw new BusinessException("模型删除失败！");
			}
		}else{
			logger.error("模型删除失败！模型ID为空");
			throw new BusinessException("模型删除失败！模型ID为空");
		}
		
	}

	@Override
	public List<Module> selectSelective(Module module) {
		return moduleMapper.selectSelective(module);
	}
	/**
	 * 更新模型
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	@Transactional
	public void updateModule(Module module,List<FormTag> tags) {
		moduleMapper.updateByPrimaryKeySelective(module);
		//禁用模型
		if("true".equals(module.getDisabled())){
		    try{
    		    List<Map> ids =   instanceMapper.getInstanceIdByModuleId(module.getId());
    		    if(ids !=null && ids.size()>0 ){
    		        for(Map m:ids){
        	            circleMapper.deleteInstance((String) m.get("instanceId"));
        	            //删除关系
        	            Map inMap = new HashMap();
        	            inMap.put("targerInstanceId", (String) m.get("instanceId"));
        	            instanceRelationMapper.delete(inMap);
        	            inMap.clear();
        	            inMap.put("sourceInstanceId", (String) m.get("instanceId"));
        	            instanceRelationMapper.delete(inMap);
        	            
                        ConfigLog configLog = new ConfigLog();
                        configLog.setId(UUIDUtil.getUUID());
                        configLog.setName( (String) m.get("instanceName"));
                        configLog.setModuleName((String) m.get("moduleName"));
                        configLog.setAction(Constants.LOG_ACTION_TYPE_DELINSTANCE_NAME);
                        configLog.setCircleId((String)  m.get("circleId"));
                        configLog.setInstanceId((String) m.get("instanceId"));
                        configLogMapper.insert(configLog);
    		        }
    		    }
		    }catch(Exception e){
                logger.error("禁用模型[" + module.getId() + "]删除配置失败！", e);
                e.printStackTrace();
		    }
		}
		//获取该模型下的所有标签
		List<String> tagids = formTagMapper.selectTagIdByModuleId(module.getId());
		for (FormTag ft:tags){
			if(ft.getId()!=null && !"".equals(ft.getId())){
				if(tagids.contains(ft.getId())){
					tagids.remove(ft.getId());
				}
				formTagMapper.updateByPrimaryKeySelective(ft);
			}else{
				ft.setId(UUIDUtil.getUUID());
				ft.setModuleid(module.getId());
				formTagMapper.insertSelective(ft);
			}
		}
		//删除用户移除的tag
		if(tagids!=null && tagids.size()>0){
			for(String id:tagids){
				formTagMapper.deleteByPrimaryKey(id);
				//移除表单脚本表中与改标签关联的脚本
				formScriptMapper.deleteByTagId(id);
			}
		}
	}

	@Override
	public Module getModuleByModuleName(String moduleName) {
		return moduleMapper.getModuleByModuleName(moduleName);
	}

	@Override
	public List<FormTag> getModuleTag(String mid) {
		return formTagMapper.selectByModuleId(mid);
	}
	/**
	 * 
	 *Description:根据标签id获取表单脚本
	 * @param tagId
	 * @return
	 */
	@Override
	public List<FormScript> getScriptByTagId(String tagId) {
		return formScriptMapper.selectScriptByTag(tagId);
	}

	@Override
	public List<Module> selectModuleByParentId(String parentId) {
		return moduleMapper.selectModuleByParentId(parentId);
	}

}
