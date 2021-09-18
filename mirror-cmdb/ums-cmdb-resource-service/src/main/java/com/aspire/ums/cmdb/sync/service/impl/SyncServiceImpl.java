package com.aspire.ums.cmdb.sync.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.Instance;
import com.aspire.ums.cmdb.module.entity.Form;
import com.aspire.ums.cmdb.module.entity.FormOptions;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.sync.Constants;
import com.aspire.ums.cmdb.sync.entity.Dict;
import com.aspire.ums.cmdb.sync.entity.SyncEntity;
import com.aspire.ums.cmdb.sync.mapper.SyncMapper;
import com.aspire.ums.cmdb.sync.service.SyncService;

@Service("syncService")
public class SyncServiceImpl implements SyncService {

	private static Logger logger = Logger.getLogger(SyncServiceImpl.class);

	@Autowired
	private SyncMapper syncMapper;

	@Override
	public List<SyncEntity> syncEntity() {
		List<SyncEntity> entities= new ArrayList<>();
		
		//物理机
		Module module_pm = syncMapper.getModuleByCode(Constants.SYNC_MODULE_PM);
		if(null != module_pm) {
			List<Instance> instance_pms = syncMapper.getInstanceByModuleId(module_pm.getId());
			for(Instance instance_pm : instance_pms) {
				List<FormValue> list_pm = syncMapper.getFormValueMapByInstanceId(instance_pm.getId());
				Map<String,String> pmMap = new HashMap<String,String>();
				for(FormValue formValue : list_pm) {
					pmMap.put(formValue.getFormCode(), formValue.getFormValue());
				}
				if(!pmMap.isEmpty()) {
					SyncEntity entity = new SyncEntity();
					entity.setId(StringUtils.isEmpty(pmMap.get("syncId")) ? instance_pm.getId() : pmMap.get("syncId"));
					entity.setHostName(pmMap.get("hostName"));
					entity.setBizIp(pmMap.get("bizIp"));
					entity.setIpmiIp(pmMap.get("ipmiIp"));
					entity.setManageIp(pmMap.get("manageIp"));
					entity.setRoomId(pmMap.get("roomId"));
					entity.setBizSystem(pmMap.get("bizSystem"));
					entity.setDeviceType(Constants.DEVICE_TYPE_HOST);
					entity.setDeviceCatgory(Constants.DEVICE_CATEGOTY_PM);
					entity.setSystemVersion(pmMap.get("systemVersion"));
					entity.setBrand(pmMap.get("brand"));
					entity.setHostEnv(pmMap.get("hostEnv").equals("2") ? "3" : pmMap.get("hostEnv"));
					
					entities.add(entity);
				}
			}
		}else {
			logger.error("物理机模型不存在！");
		}
		
		
		
		//虚拟机
		Module module_vm = syncMapper.getModuleByCode(Constants.SYNC_MODULE_VM);
		if(null != module_vm) {
			List<Instance> instance_vms = syncMapper.getInstanceByModuleId(module_vm.getId());
			for(Instance instance_vm : instance_vms) {
				List<FormValue> list_vm = syncMapper.getFormValueMapByInstanceId(instance_vm.getId());
				Map<String,String> vmMap = new HashMap<String,String>();
				for(FormValue formValue : list_vm) {
					vmMap.put(formValue.getFormCode(), formValue.getFormValue());
				}
				if(!vmMap.isEmpty()) {
					SyncEntity entity = new SyncEntity();
					entity.setId(StringUtils.isEmpty(vmMap.get("syncId")) ? instance_vm.getId() : vmMap.get("syncId"));
					entity.setHostName(vmMap.get("hostName"));
					entity.setBizIp(vmMap.get("bizIp"));
					entity.setIpmiIp(vmMap.get("ipmiIp"));
					entity.setManageIp(vmMap.get("manageIp"));
					entity.setRoomId(vmMap.get("roomId"));
					entity.setBizSystem(vmMap.get("bizSystem"));
					entity.setDeviceType(Constants.DEVICE_TYPE_HOST);
					entity.setDeviceCatgory(Constants.DEVICE_CATEGOTY_VM);
					entity.setSystemVersion(vmMap.get("systemVersion"));
					//entity.setBrand(vmMap.get(""));
					entity.setHostEnv(vmMap.get("hostEnv").equals("2") ? "3" : vmMap.get("hostEnv"));
					
					entities.add(entity);
				}
			}
		}else {
			logger.error("虚拟机模型不存在！");
		}
		
		
		
		//网络设备
		Module module_net = syncMapper.getModuleByCode(Constants.SYNC_MODULE_NET);
		if(null != module_net) {
			List<Instance> instance_nets = syncMapper.getInstanceByModuleId(module_net.getId());
			for(Instance instance_net : instance_nets) {
				List<FormValue> list_net = syncMapper.getFormValueMapByInstanceId(instance_net.getId());
				Map<String,String> netMap = new HashMap<String,String>();
				for(FormValue formValue : list_net) {
					netMap.put(formValue.getFormCode(), formValue.getFormValue());
				}
				if(!netMap.isEmpty()) {
					SyncEntity entity = new SyncEntity();
					entity.setId(StringUtils.isEmpty(netMap.get("syncId")) ? instance_net.getId() : netMap.get("syncId"));
					entity.setHostName(netMap.get("hostName"));
					//entity.setBizIp(netMap.get(""));
					//entity.setIpmiIp(netMap.get(""));
					entity.setManageIp(netMap.get("manageIp"));
					entity.setRoomId(netMap.get("roomId"));
					//entity.setBizSystem(netMap.get(""));
					entity.setDeviceType(Constants.DEVICE_TYPE_NETWORK);
					entity.setDeviceCatgory(netMap.get("categoryId"));
					//entity.setSystemVersion(netMap.get(""));
					entity.setBrand(netMap.get("brand"));
					//entity.setHostEnv(netMap.get(""));
					
					entities.add(entity);
				}
			}
		}else {
			logger.error("网络设备模型不存在！");
		}
		
		return entities;
	}

	@Override
	public List<Dict> syncDicts() {
		Set<Dict> dicts = new HashSet<Dict>();
		Module module_pm = syncMapper.getModuleByCode(Constants.SYNC_MODULE_PM);
		if(null == module_pm) {
			logger.error("物理机模型不存在！");
		}else {
			for (String syncCode : Constants.DICT_MAP.keySet()) {
				//网络设备类型此处不做处理
				if(syncCode.equals(Constants.DICT_CODE_NET_TYPE)) {	
					continue;
				}
				Form tempForm = new Form();
				tempForm.setModuleid(module_pm.getId());
				tempForm.setCode(Constants.DICT_MAP.get(syncCode));
				Form form = syncMapper.getFormByForm(tempForm);
				if (null == form) {
					continue;
				}
				List<FormOptions> options = syncMapper.getOptionsByFormId(form.getId());
				for (FormOptions option : options) {
					Dict dict = new Dict();
					dict.setDictCode(syncCode);
					dict.setCode(option.getValue());
					dict.setName(option.getName());
					dicts.add(dict);
				}

			}
		}
		
		Module module_net = syncMapper.getModuleByCode(Constants.SYNC_MODULE_NET);
		if(null == module_net) {
			logger.error("网络设备模型不存在！");
		}else {
			for (String syncCode : Constants.DICT_MAP.keySet()) {
				Form tempForm = new Form();
				tempForm.setModuleid(module_net.getId());
				tempForm.setCode(Constants.DICT_MAP.get(syncCode));
				Form form = syncMapper.getFormByForm(tempForm);
				if (null == form) {
					continue;
				}
				List<FormOptions> options = syncMapper.getOptionsByFormId(form.getId());
				for (FormOptions option : options) {
					Dict dict = new Dict();
					dict.setDictCode(syncCode);
					dict.setCode(option.getValue());
					dict.setName(option.getName());
					dicts.add(dict);
				}

			}
		}
		
		//服务器类型:物理机
		Dict dict_category_pm = new Dict();
		dict_category_pm.setDictCode(Constants.DEVICE_CATEGOTY_DICT_CODE);
		dict_category_pm.setCode(Constants.DEVICE_CATEGOTY_PM);
		dict_category_pm.setName("物理机");
		dicts.add(dict_category_pm);
		
		//服务器类型:虚拟机
		Dict dict_category_vm = new Dict();
		dict_category_vm.setDictCode(Constants.DEVICE_CATEGOTY_DICT_CODE);
		dict_category_vm.setCode(Constants.DEVICE_CATEGOTY_VM);
		dict_category_vm.setName("虚拟机");
		dicts.add(dict_category_vm);
		
		return  new ArrayList(dicts);
	}

}
