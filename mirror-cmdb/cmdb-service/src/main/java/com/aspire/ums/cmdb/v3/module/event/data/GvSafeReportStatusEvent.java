package com.aspire.ums.cmdb.v3.module.event.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.dict.ICmdbDictAPI;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: NetworkSegmentOwnerEvent
 * Author:   hangfang
 * Date:     2020/5/28
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class GvSafeReportStatusEvent extends AbstractModuleEvent {
	/*
	 * private static String[] cols = {"id","is_delete"
	 * ,"insert_person","insert_time","update_person","update_time"};
	 */
	
	 private SchemaService schemaService;
	 private static String[] cols = {"is_optimize_finished","complaint_count"
			  ,"optimize_method","optimize_expect_time","emergency_method"};

	
	@Autowired
	private ICmdbDictAPI iCmdbDictAPI;
	
	private ModuleService moduleService;

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        this.initSpringBeans();
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
       List<String> colList =  Arrays.asList(cols);
       boolean flag = true;
       List<CmdbCollectApproval> approvals = JSONArray.parseArray(JSON.toJSONString(handleData.get("approvals")),
               CmdbCollectApproval.class);
       for (CmdbCollectApproval approval : approvals) {
    	   Map<String, Object> instanceData = (Map<String, Object>) JSONObject.parse(approval.getResourceData());
    	  
    	   for(Map.Entry<String, Object>entry:instanceData.entrySet()) {
           	String key = entry.getKey();
           	if(!colList.contains(key)) {
           		continue;
           	}
           	String value = entry.getValue()==null?null:entry.getValue().toString();
           	if(StringUtils.isEmpty(value)) {
           		flag = false;
           		break;
           	}
           }
           List<ConfigDict> dicts = iCmdbDictAPI.getDictsByType("gvSafe_report_status", null, null, null);
          String value = "";
           if (flag==false) {
           	value = "未完成";
           } else {
           	value = "已完成";
           }
           for(ConfigDict c:dicts) {
           	String val = c.getValue();
           	if(val.equals(value)) {
           		  dataMap.put("report_status", c.getId());
           		  continue;
           	}
           }
         
           //String username = handleData.get("update_person").toString();
          // iCmdbInstanceService.updateInstance(instanceId, username, handleData, "手动更新");//TODO
           Module module = moduleService.getModuleDetail(moduleId);
           schemaService.updateCi(module.getModuleCatalog().getCatalogCode(), instanceId, dataMap);
       }
       
        returnMap.put("flag", true);
        returnMap.put("msg", "保存成功");
        return returnMap;
    }

	@Override
	public void initSpringBeans() {
		log.info("****故障上报模型事件启动******");
		if (this.schemaService == null) {
            this.schemaService = SpringUtils.getBean(SchemaService.class);
        }
		if (this.moduleService == null) {
            this.moduleService = SpringUtils.getBean(ModuleService.class);
        }
		if (this.iCmdbDictAPI == null) {
            this.iCmdbDictAPI = SpringUtils.getBean(ICmdbDictAPI.class);
        }
	}
}
