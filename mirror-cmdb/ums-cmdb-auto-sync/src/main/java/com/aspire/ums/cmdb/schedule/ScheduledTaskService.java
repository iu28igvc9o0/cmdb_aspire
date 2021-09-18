/**
 * 
 */
package com.aspire.ums.cmdb.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.CommonRequest;
import com.aspire.ums.cmdb.common.StringUtils;
import com.aspire.ums.cmdb.common.UUIDUtil;
import com.aspire.ums.cmdb.sync.entity.Dict;
import com.aspire.ums.cmdb.sync.entity.FormOptions;
import com.aspire.ums.cmdb.sync.entity.FormValue;
import com.aspire.ums.cmdb.sync.entity.Instance;
import com.aspire.ums.cmdb.sync.entity.Module;
import com.aspire.ums.cmdb.sync.service.CmdbSyncFormService;
import com.aspire.ums.cmdb.vo.AjaxResponseObject;

/**
 * @author lupeng
 *
 */
@Component
@ConditionalOnExpression("${schedule.scheduleTask.flag:false}")
public class ScheduledTaskService {

	private final Logger logger = Logger.getLogger(ScheduledTaskService.class);

	@Value("${sync.pm}")
	private String SYNC_PM;

	@Value("${sync.vm}")
	private String SYNC_VM;

	@Value("${sync.net}")
	private String SYNC_NET;
	
	@Value("${sync.dict}")
	private String SYNC_DICT;

	@Autowired
	private CmdbSyncFormService cmdbSyncFormService;

	private String MODULE_CODE_PM = "m_pm";

	private String MODULE_CODE_VM = "m_vm";

	private String MODULE_CODE_NET = "m_nd";

	private Integer IS_DELETE_NO = 0;
	
//	@Scheduled(cron = "0 0,20,40 * * * ?")
	public void syncDict() {
		Thread current = Thread.currentThread();  
		logger.info("定时任务[thread="+current.getId()+"]：字典同步执行开始...");
		try {
			AjaxResponseObject resopnse = CommonRequest.get(SYNC_DICT);
			if (!resopnse.getSuccess()) {
				logger.info("定时任务[thread="+current.getId()+"]：字典同步执行中断,数据源获取失败！");
				return;
			}
			JSONObject json = JSONObject.parseObject(resopnse.getData().toString());
			// 解析内容
			JSONArray array = json.parseArray(json.getString("listData"));
			
			List<Dict> bizList = new ArrayList<Dict>();	//业务系统
			List<Dict> roomList = new ArrayList<Dict>(); //机房
			List<Dict> deviceList = new ArrayList<Dict>(); //网络设备类型
			for(int i = 0 ; i < array.size() ; i++) {
				JSONObject syncObj = array.getJSONObject(i);
				String dictCode = syncObj.getString("dictCode");
				String code = syncObj.getString("code");
				String name = syncObj.getString("name");
				
				Dict dict= new Dict();
				dict.setDictCode(dictCode);
				dict.setCode(code);
				dict.setName(name);
				
				if(dictCode.equals("bizSystem")) {
					bizList.add(dict);
				}else if(dictCode.equals("roomId")) {
					roomList.add(dict);
				}else if(dictCode.equals("categoryId")) {
					deviceList.add(dict);
				}else {
					logger.info("有未设置同步的字典数据：[dictCode="+dictCode+",code="+code+",name="+name+"]");
				}
				
			}
			
			Module module_pm = cmdbSyncFormService.getModuleByCode(MODULE_CODE_PM);
			Module module_vm = cmdbSyncFormService.getModuleByCode(MODULE_CODE_VM);
			Module module_net = cmdbSyncFormService.getModuleByCode(MODULE_CODE_NET);
			if(null == module_pm || null == module_vm || null == module_net) {
				logger.error("模型不存在！,关键code:["+MODULE_CODE_PM+","+MODULE_CODE_VM+","+MODULE_CODE_NET+"]");
				return;
			}
			Map<String,String> form_pm = cmdbSyncFormService.getFormByModuleId(module_pm.getId());
			Map<String,String> form_vm = cmdbSyncFormService.getFormByModuleId(module_vm.getId());
			Map<String,String> form_net = cmdbSyncFormService.getFormByModuleId(module_net.getId());
			if(form_pm.isEmpty() || form_vm.isEmpty() || form_vm.isEmpty()) {
				logger.error("模型表单为空！,关键code:["+MODULE_CODE_PM+","+MODULE_CODE_VM+","+MODULE_CODE_NET+"]");
				return;
			}
			//同步业务系统
			if(!bizList.isEmpty()) {
				//物理机
				String pm_formId = form_pm.get("bizSystem");
				if(null == pm_formId) {
					logger.error("物理机不存在业务系统属性，code=bizSystem");
				}
				List<FormOptions> pm_options = new ArrayList<FormOptions>();
				for(Dict dict : bizList) {
					FormOptions option = new FormOptions();
					option.setId(UUIDUtil.getUUID());
					option.setName(dict.getName());
					option.setValue(dict.getCode());
					option.setIsdefault("false");
					option.setFormid(pm_formId);
					pm_options.add(option);
				}
				cmdbSyncFormService.updateOptions(pm_formId, pm_options);
				
				//虚拟机
				String vm_formId = form_vm.get("bizSystem");
				if(null == vm_formId) {
					logger.error("虚拟机不存在业务系统属性，code=bizSystem");
				}
				List<FormOptions> vm_options = new ArrayList<FormOptions>();
				for(Dict dict : bizList) {
					FormOptions option = new FormOptions();
					option.setId(UUIDUtil.getUUID());
					option.setName(dict.getName());
					option.setValue(dict.getCode());
					option.setIsdefault("false");
					option.setFormid(vm_formId);
					vm_options.add(option);
				}
				cmdbSyncFormService.updateOptions(vm_formId, vm_options);
			}
			
			//同步机房
			if(!roomList.isEmpty()) {
				//物理机
				String pm_formId = form_pm.get("roomId");
				if(null == pm_formId) {
					logger.error("物理机不存在机房属性，code=roomId");
				}
				List<FormOptions> pm_options = new ArrayList<FormOptions>();
				for(Dict dict : roomList) {
					FormOptions option = new FormOptions();
					option.setId(UUIDUtil.getUUID());
					option.setName(dict.getName());
					option.setValue(dict.getCode());
					option.setIsdefault("false");
					option.setFormid(pm_formId);
					pm_options.add(option);
				}
				cmdbSyncFormService.updateOptions(pm_formId, pm_options);
				
				//虚拟机
				String vm_formId = form_vm.get("roomId");
				if(null == vm_formId) {
					logger.error("虚拟机不存在机房属性，code=roomId");
				}
				List<FormOptions> vm_options = new ArrayList<FormOptions>();
				for(Dict dict : roomList) {
					FormOptions option = new FormOptions();
					option.setId(UUIDUtil.getUUID());
					option.setName(dict.getName());
					option.setValue(dict.getCode());
					option.setIsdefault("false");
					option.setFormid(vm_formId);
					vm_options.add(option);
				}
				cmdbSyncFormService.updateOptions(vm_formId, vm_options);
				
				//网络设备
				String net_formId = form_net.get("roomId");
				if(null == net_formId) {
					logger.error("网络设备不存在机房属性，code=roomId");
				}
				List<FormOptions> net_options = new ArrayList<FormOptions>();
				for(Dict dict : roomList) {
					FormOptions option = new FormOptions();
					option.setId(UUIDUtil.getUUID());
					option.setName(dict.getName());
					option.setValue(dict.getCode());
					option.setIsdefault("false");
					option.setFormid(net_formId);
					net_options.add(option);
				}
				cmdbSyncFormService.updateOptions(net_formId, net_options);
			}
			
			//同步网络设备类型
			if(!deviceList.isEmpty()) {
				//网络设备
				String net_formId = form_net.get("categoryId");
				if(null == net_formId) {
					logger.error("网络设备不存在类型属性，code=categoryId");
				}
				List<FormOptions> net_options = new ArrayList<FormOptions>();
				for(Dict dict : deviceList) {
					FormOptions option = new FormOptions();
					option.setId(UUIDUtil.getUUID());
					option.setName(dict.getName());
					option.setValue(dict.getCode());
					option.setIsdefault("false");
					option.setFormid(net_formId);
					net_options.add(option);
				}
				cmdbSyncFormService.updateOptions(net_formId, net_options);
			}
			
		} catch (Exception e) {
			logger.error("定时任务[thread="+current.getId()+"]：字典同步执行异常！", e);
		}
		logger.info("定时任务[thread="+current.getId()+"]：字典同步执行结束...");
	}

//	@Scheduled(cron = "0 5,25,45 * * * ?")
	public void syncPm() {
		Thread current = Thread.currentThread();  
		logger.info("定时任务[thread="+current.getId()+"]：物理机同步执行开始...");
		try {
			sync(SYNC_PM, MODULE_CODE_PM);
		} catch (Exception e) {
			logger.error("定时任务[thread="+current.getId()+"]：物理机同步执行异常！", e);
		}
		logger.info("定时任务[thread="+current.getId()+"]：物理机同步执行结束...");
	}

//	@Scheduled(cron = "0 10,30,50 * * * ?")
	public void syncVm() {
		Thread current = Thread.currentThread();  
		logger.info("定时任务[thread="+current.getId()+"]：虚拟机同步执行开始...");
		try {
			sync(SYNC_VM, MODULE_CODE_VM);
		} catch (Exception e) {
			logger.error("定时任务[thread="+current.getId()+"]：虚拟机同步执行异常！", e);
		}
		logger.info("定时任务[thread="+current.getId()+"]：虚拟机同步执行结束...");
	}
	
//	@Scheduled(cron = "0 15,35,55 * * * ?")
	public void syncNet() {
		Thread current = Thread.currentThread();  
		logger.info("定时任务[thread="+current.getId()+"]：网络设备同步执行开始...");
		try {
			sync(SYNC_NET, MODULE_CODE_NET);
		} catch (Exception e) {
			logger.error("定时任务[thread="+current.getId()+"]：网络设备同步执行异常！", e);
		}
		logger.info("定时任务[thread="+current.getId()+"]：网络设备同步执行结束...");
	}
	
	public void sync(String url, String moduleCode) throws Exception {
		Date date = new Date();
		Integer currentPage = 1;
		Integer maxPage = 1;
		Integer pageSize = 100;
		Module module = cmdbSyncFormService.getModuleByCode(moduleCode);
		if (null == module) {
			logger.error("模型[code=" + moduleCode + "]不存在！");
			return;
		}
		Map<String, String> codeMap = cmdbSyncFormService.getFormByModuleId(module.getId());
		if (null == codeMap || codeMap.isEmpty()) {
			logger.error("模型[code=" + moduleCode + "]表单为空！");
			return;
		}
		Map<String, String> syncMap = cmdbSyncFormService.getFieldMap(moduleCode);
		if (null == syncMap || syncMap.isEmpty()) {
			logger.error("模型[code=" + moduleCode + "]字段同步对应关系不存在！");
			return;
		}
		
		List<String> instanceIds = cmdbSyncFormService.getInstanceIdsByModuleId(module.getId());
		List<String> syncInstanceIds = new ArrayList<String>();
		for (int i = 1; i <= maxPage; i++) {
			AjaxResponseObject resopnse = CommonRequest
					.get(url + "?currentPage=" + currentPage + "&pageSize=" + pageSize);
			currentPage++;
			if (!resopnse.getSuccess()) {
				break;
			}
			JSONObject json = JSONObject.parseObject(resopnse.getData().toString());

			// 获取最大页码
			Integer total = json.getInteger("total");
			maxPage = total / pageSize + 1;

			// 解析内容
			JSONArray array = json.parseArray(json.getString("listData"));
			for (int j = 0; j < array.size(); j++) {
				JSONObject syncObj = array.getJSONObject(j);
				String manageIp = syncObj.getString("manageIp");
				if (StringUtils.isEmpty(manageIp)) {
					logger.info("manageIp为空,不同步处理！data={" + syncObj.toString() + "}");
					continue;
				}
				Instance instance = new Instance();
				instance.setModuleId(module.getId());
				instance.setName(manageIp);
				Instance oldInstance = cmdbSyncFormService.getInstance(instance);
				Boolean exist = false;
				if (null == oldInstance) {
					instance.setId(UUIDUtil.getUUID());
					instance.setInsertTime(date);
					instance.setIsDelete(IS_DELETE_NO);
				} else {
					instance = oldInstance;
					syncInstanceIds.add(instance.getId());
					exist = true;
				}

				List<FormValue> values = new ArrayList<FormValue>();
				for (String key : syncMap.keySet()) {
					FormValue formValue = new FormValue();
					formValue.setId(UUIDUtil.getUUID());
					if (null == syncObj.getString(key)) {
						continue;
					} else {
						formValue.setFormValue(syncObj.getString(key));
					}
					
					String formId = codeMap.get(syncMap.get(key));
					if(null == formId || formId.trim().equals("")) {
						formValue.setFormId(UUIDUtil.getUUID());
					}else {
						formValue.setFormId(formId);
					}
						
					formValue.setFormCode(syncMap.get(key));
					formValue.setInstanceId(instance.getId());

					values.add(formValue);
				}

				// 入库处理
				if (!values.isEmpty()) {
					cmdbSyncFormService.insertData(instance, values, exist);
				}
				
			}
		}
		
		//移除本地存在但远程没有的数据，保存数据一致。
		cmdbSyncFormService.DeleteInstances(module.getId(),instanceIds, syncInstanceIds);
	}

}
