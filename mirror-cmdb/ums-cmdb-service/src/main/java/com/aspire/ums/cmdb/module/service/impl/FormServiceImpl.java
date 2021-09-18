package com.aspire.ums.cmdb.module.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.aspire.ums.cmdb.dict.entity.ConfigDict;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.google.common.collect.Lists;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.module.entity.Form;
import com.aspire.ums.cmdb.module.entity.FormBean;
import com.aspire.ums.cmdb.module.entity.FormFields;
import com.aspire.ums.cmdb.module.entity.FormOptions;
import com.aspire.ums.cmdb.module.entity.FormParam;
import com.aspire.ums.cmdb.module.entity.FormRule;
import com.aspire.ums.cmdb.module.entity.FormScript;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.module.entity.OptionBean;
import com.aspire.ums.cmdb.module.mapper.FormFieldsMapper;
import com.aspire.ums.cmdb.module.mapper.FormMapper;
import com.aspire.ums.cmdb.module.mapper.FormOptionsMapper;
import com.aspire.ums.cmdb.module.mapper.FormParamMapper;
import com.aspire.ums.cmdb.module.mapper.FormRuleMapper;
import com.aspire.ums.cmdb.module.mapper.FormScriptMapper;
import com.aspire.ums.cmdb.module.mapper.ModuleMapper;
import com.aspire.ums.cmdb.module.service.FormService;
import com.aspire.ums.cmdb.util.BusinessException;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import org.springframework.util.CollectionUtils;

@Service("formService")
@Transactional
public class FormServiceImpl implements FormService {

	@Autowired
	private ModuleMapper moduleMapper;

	@Autowired
	private FormMapper formMapper;

	@Autowired
	private FormOptionsMapper formOptionsMapper;

	@Autowired
	private FormFieldsMapper formFieldsMapper;

	@Autowired
	private FormParamMapper formParamMapper;

	@Autowired
	private FormRuleMapper formRuleMapper;
	
	@Autowired
	private FormScriptMapper formScriptMapper;

	@Autowired
	private ConfigDictMapper configDictMapper;
	
	private final Logger logger = Logger.getLogger(getClass());

	@Override
	@Transactional
	public void updateForm(Map<String, Object> map) throws Exception,
			BusinessException {
		Module module = new Module();
		module.setId((String)map.get("mid"));
		List<Module> modules = moduleMapper.selectSelective(module);
		if(null==modules || modules.size()<1){
			logger.error("模型ID:["+module.getId()+"]不存在");
			throw new BusinessException("模型ID不存在");
		}
		//获取所有该模型下所有的Form id
		List<String> fids = getFormIdsbyModule(module.getId());
		List<Map<String,Object>> list = null;
		if(map.containsKey("forms")){
			list = (List<Map<String, Object>>) map.get("forms");
		}
		Form form = null;
		if(list!=null &&list.size()>0){
			for (Map<String, Object> map2 : list) {
				Form f = JSON.parseObject(JSON.toJSON(map2).toString(), Form.class);
				if(!"".equals(f.getId()) && null != f.getId() ){
					Form ff = formMapper.selectByPrimaryKey(f.getId());
					if(null != ff){
						//过滤出用户移除的表单元素
						if(fids.contains(f.getId())){
							fids.remove(f.getId());
						}
						//更新form表内容
						f.setIsdelete(0);
						formMapper.updateByPrimaryKeySelective(f);
						//先删除该form下的所有options，param，field
						formOptionsMapper.deleteByformId(f.getId());
						formParamMapper.deleteByformId(f.getId());
						formFieldsMapper.deleteByformId(f.getId());
						//保存新的options，param，field
						setParam(map2,f.getId());
						setScript(f.getId(),map2);
					}
				}else{
					//该form表单不存在时，插入新的form
					String id = UUIDUtil.getUUID();
					f.setId(id);
					f.setIsdelete(0);
					f.setModuleid(module.getId());

					//保存options，param，field
					if (map2.get("relformid") == null) {
						setParam(map2,id);
					}
					//保存到formscript
					setScript(f.getId(),map2);
					//保存form
					formMapper.insertSelective(f);
				}
			}
		}
		//删除用户移除的表单元素,并移除选项表中数据
		for (String fid : fids) {
			formMapper.deleteByPrimaryKey(fid);
			formOptionsMapper.deleteByformId(fid);
			formFieldsMapper.deleteByformId(fid);
			formParamMapper.deleteByformId(fid);
			formScriptMapper.deleteByFormId(fid);
		}
	}
	@SuppressWarnings("unchecked")
	public void setScript(String fid,Map<String,Object> fsmaps){
		Map<String,String> map = (Map<String, String>) fsmaps.get("script");
		
		if(null!=map){
			FormScript fs = JSON.parseObject(JSON.toJSON(map).toString(), FormScript.class);
			if(fs.getId()!=null){
				FormScript existFs = formScriptMapper.selectByPrimaryKey(fs.getId());
				if(existFs==null){
					return;
				}else{
					formScriptMapper.updateByPrimaryKey(fs);
				}
			}else{
				fs.setFormid(fid);
				fs.setId(UUIDUtil.getUUID());
				formScriptMapper.insertSelective(fs);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void setParam(Map<String,Object> map,String fid){
		String type=(String) map.get("type");
		Map<String,Object> options = (Map<String, Object>) map.get("params");
		if(null == options){
			return;
		}
		if(null == type || "".equals(type)){
			logger.error("表单项:["+fid+"]tpye属性为空！");
			return;
		}
		if(Constants.MODULE_FORM_TYPE_LISTSEL.equals(type) ||
		Constants.MODULE_FORM_TYPE_SINGLESEL.equals(type) ||
		Constants.MODULE_FORM_TYPE_MULTISEL.equals(type) ){
			if(!options.containsKey("options")){
				logger.error("该["+type+"]类型 options选项不存在！");
				return;
			}
			String ops = JSONArray.toJSON(options.get("options")).toString();
			List<FormOptions> fops = JSONArray.parseArray(ops, FormOptions.class);
			for (FormOptions fo : fops) {
				fo.setId(UUIDUtil.getUUID());
				if(StringUtils.isEmpty(fo.getName()) || StringUtils.isEmpty(fo.getValue())){
					continue;
				}
				fo.setFormid(fid);
				//插入数据到cmdb_form_option表
				formOptionsMapper.insertSelective(fo);
			}
		}else if(Constants.MODULE_FORM_TYPE_TABLE.equals(type)){
			if(!options.containsKey("fields")){
				logger.error("该["+type+"]类型 fields选项不存在！");
				return;
			}
			String fis = JSONArray.toJSON(options.get("fields")).toString();
			List<FormFields> fss = JSONArray.parseArray(fis, FormFields.class);
			for(int i=0;i<fss.size();i++){
				FormFields fs = fss.get(i);
				
				fs.setId(UUIDUtil.getUUID());
				fs.setFormid(fid);
				fs.setSortindex(i+1);
				//插入数据到cmdb_form_fields表
				formFieldsMapper.insertSelective(fs);
			}
		}else if(Constants.MODULE_FORM_TYPE_INT.equals(type) ||
				Constants.MODULE_FORM_TYPE_DOUBLE.equals(type) ||
				Constants.MODULE_FORM_TYPE_SINGLEROWTEXT.equals(type) ||
				Constants.MODULE_FORM_TYPE_MULTIROWTEXT.equals(type) ||
				Constants.MODULE_FORM_TYPE_DATETIME.equals(type)
		){
			FormParam fp = null;
			for(String ss:options.keySet()){
				fp = new FormParam();
				fp.setId(UUIDUtil.getUUID());
				fp.setKey(ss);
				fp.setValue(options.get(ss).toString());
				fp.setFormid(fid);
				//插入数据到cmdb_form_param表
				formParamMapper.insertSelective(fp);
			}
		}else if(Constants.MODULE_FORM_TYPE_CASCADER.equals(type)){//处理级联选择
			if(!options.containsKey("options")){
				logger.error("该["+type+"]类型 options选项不存在！");
				return;
			}
			//级联选项最多3层
			List<Object> lis = (List<Object>) options.get("options");
			for(Object o:lis){
				String id = UUIDUtil.getUUID();
				Map<String,Object> as = (Map<String, Object>) o;
				//处理第一层
				FormOptions op1 = new FormOptions();
				if(StringUtils.isEmpty((String)as.get("name")) || StringUtils.isEmpty((String)as.get("value"))){
					continue;
				}
				op1.setName((String)as.get("name"));
				op1.setValue((String)as.get("value"));
				op1.setId(id);
				op1.setFormid(fid);
				formOptionsMapper.insertSelective(op1);
				//处理第二层
				if(!as.containsKey("children")){
					continue;
				}
				List<Object> ls2 = (List<Object>) as.get("children");
				for(Object o2:ls2){
					String id2 = UUIDUtil.getUUID();
					Map<String,Object> as2 = (Map<String, Object>) o2;
					FormOptions op2 = new FormOptions();
					if(StringUtils.isEmpty((String)as2.get("name")) || StringUtils.isEmpty((String)as2.get("value"))){
						continue;
					}
					op2.setName((String)as2.get("name"));
					op2.setValue((String)as2.get("value"));
					op2.setId(id2);
					op2.setParentid(id);
					op2.setFormid(fid);
					formOptionsMapper.insertSelective(op2);
					//处理第三层
					if(!as2.containsKey("children")){
						continue;
					}
					List<Object> ls3 = (List<Object>) as2.get("children");
					for(Object o3:ls3){
						String id3 = UUIDUtil.getUUID();
						Map<String,Object> as3 = (Map<String, Object>) o3;
						FormOptions op3 = new FormOptions();
						if(StringUtils.isEmpty((String)as3.get("name")) || StringUtils.isEmpty((String)as3.get("value"))){
							continue;
						}
						op3.setName((String)as3.get("name"));
						op3.setValue((String)as3.get("value"));
						op3.setId(id3);
						op3.setParentid(id2);
						op3.setFormid(fid);
						formOptionsMapper.insertSelective(op3);
					}
				}
			}
		}
	}

	@Override
	public List<FormBean> getForms(Module module) throws Exception,
			BusinessException {
		List<FormBean> list = formMapper.selectFormBeanByModuleId(module);
		for (FormBean formBean : list) {
			if (!StringUtils.isEmpty(formBean.getRelformid())) {
				List<FormOptions> listOption = Lists.newArrayList();
				if ("3".equals(formBean.getRelformid())) {
					List<ConfigDict> dictByCode = configDictMapper.selectDictsByType(formBean.getCode(),null);
					for (ConfigDict configDict : dictByCode) {
						FormOptions formOptions = new FormOptions();
						String isDefaultValue = StringUtils.isNotEmpty(formBean.getDefaultvalue()) && configDict.getName().equals(formBean.getDefaultvalue()) ? "true" : "false";
						formOptions.setIsdefault(isDefaultValue);
						formOptions.setName(configDict.getName());
						formOptions.setValue(configDict.getValue());
						listOption.add(formOptions);
					}
				} else {
					listOption = formOptionsMapper.getByFormId(formBean.getRelformid());
				}
				formBean.setFormOptions(listOption);
			}
		}
		return list;
	}


	/**
	 *
	 *Description:获取 指定模型下面所有的Form ID
	 * @return
	 */
	public List<String> getFormIdsbyModule(String id){
		return formMapper.selectFormIdByModule(id);
	}

	@Override
	public List<OptionBean> getCascaderOptions(String formId) {
		return formOptionsMapper.getOptionBeanByFormId(formId);
	}

	@Override
	public List<FormRule> getFormRule() {
		return formRuleMapper.selectAllRule();
	}
	
	public FormScript getScriptByformId(String formId){
		return formScriptMapper.selectScriptByformId(formId);
	}

	public List<FormBean> getDicts() {
		List<FormBean> list = formMapper.selectDicts(null);
//        for (FormBean formBean : list) {
//            List<FormOptions> listOption = formOptionsMapper.getByFormId(formBean.getId());
//            formBean.setFormOptions(listOption);
//        }
		return list;
	}

    public FormBean getDictValue(String formId) {
        List<FormBean> list = formMapper.selectDicts(formId);
        if (CollectionUtils.isEmpty(list)) {
            return new FormBean();
        }
        FormBean formBean = list.get(0);
        List<FormOptions> listOption = formOptionsMapper.getByFormId(formBean.getId());
        formBean.setFormOptions(listOption);
        return formBean;
    }
//	@Override
//	public List<Map<String, String>> getFirstBusiness(List<String> ids) {
//		// TODO Auto-generated method stub
//		return formMapper.getBussiness(ids);
//	}
//	@Override
//	public List<Map<String, String>> findBusCodeAndName(List<String> ids) {
//		// TODO Auto-generated method stub
//		return formMapper.findBusCodeAndName(ids);
//	}
//	@Override
//	public List<Map<String, String>> findBusCodeByName(String name) {
//		// TODO Auto-generated method stub
//		return formMapper.findBusCodeByName(name);
//	}
}
