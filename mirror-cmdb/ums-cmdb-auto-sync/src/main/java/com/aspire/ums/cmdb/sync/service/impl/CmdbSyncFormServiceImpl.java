package com.aspire.ums.cmdb.sync.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.schedule.ScheduledTaskService;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncForm;
import com.aspire.ums.cmdb.sync.entity.Form;
import com.aspire.ums.cmdb.sync.entity.FormOptions;
import com.aspire.ums.cmdb.sync.entity.FormValue;
import com.aspire.ums.cmdb.sync.entity.Instance;
import com.aspire.ums.cmdb.sync.entity.Module;
import com.aspire.ums.cmdb.sync.mapper.SyncMapper;
import com.aspire.ums.cmdb.sync.service.CmdbSyncFormService;

/**
 * @author lupeng
 *
 */
@Service
public class CmdbSyncFormServiceImpl implements CmdbSyncFormService {
	
	private final Logger logger = Logger.getLogger(CmdbSyncFormServiceImpl.class);
	
	@Autowired
	private SyncMapper syncMapper;
	
	@Override
	public Map<String, String> getFieldMap(String moduleCode) {
		Map<String, String> fieldMap = new HashMap<String, String >();
		List<CmdbSyncForm> list = syncMapper.getByModuleCode(moduleCode);
		for(CmdbSyncForm form : list) {
			fieldMap.put(form.getSyncCode(), form.getFormCode());
		}
		return fieldMap;
	}

	@Override
	@Transactional
	public void insertData(Instance instance, List<FormValue> values,boolean exist) {
		if(exist) {
			syncMapper.deleteFormValue(instance.getId());
		}else {
			syncMapper.insertInstance(instance);
		}
		syncMapper.insertFormValue(values);
	}
	
	@Override
	public Module getModuleByCode(String code) {
		return syncMapper.getModuleByCode(code);
	}

	@Override
	public Map<String,String> getFormByModuleId(String moduleId) {
		List<Form> forms = syncMapper.getFormByModuleId(moduleId);
		Map<String , String> map = new HashMap<String,String>();
		for(Form form : forms) {
			map.put(form.getCode(), form.getId());
		}
		return map;
	}

	@Override
	public Instance getInstance(Instance instance) {
		return syncMapper.getInstance(instance);
	}

	@Override
	public List<String> getInstanceIdsByModuleId(String moduleId) {
		return syncMapper.getInstanceIdsByModuleId(moduleId);
	}
	
	@Override
	public void DeleteInstances(String moduleId,List<String> localList, List<String> syncList) {
		if(localList.isEmpty()) {
			logger.info("本地仓库无模型[id="+moduleId+"]相关实例，不做冗余处理！");
			return;
		}
		if(syncList.isEmpty()) {
			logger.info("远程数据无模型[id="+moduleId+"]相关实例，不做冗余处理！");
			return;
		}
		
		List<String> ids = new ArrayList<String>();
		for(String localId : localList) {
			if(!syncList.contains(localId)) {
				ids.add(localId);
			}
		}
		
		if(!ids.isEmpty()) {
			logger.info("模型[id="+moduleId+"]实例冗余处理,id:"+ids);
			for(String id : ids) {
				//逻辑删除
				syncMapper.deleteInstance(id);
			}
		}
	}

	@Override
	public void updateOptions(String formId, List<FormOptions> options) {
		syncMapper.deleteFormOptions(formId);
		syncMapper.insertFormOptions(options);
	}

}
