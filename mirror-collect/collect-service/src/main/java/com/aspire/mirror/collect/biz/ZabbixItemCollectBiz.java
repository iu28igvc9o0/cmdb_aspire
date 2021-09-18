package com.aspire.mirror.collect.biz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.app.ums.zabbixApi.model.ZbxApiSvrInfo;
import com.aspire.mirror.collect.api.payload.MonitorApiServerConfig;
import com.aspire.mirror.collect.api.payload.MonitorItemDetailResponse;
import com.aspire.mirror.collect.api.payload.TemplateItemListRequest;
import com.aspire.mirror.collect.api.payload.ZabbixTemplateDetailResponse;
import com.aspire.mirror.collect.util.JsonUtil;
import com.aspire.mirror.collect.zabbix.ZabbixApiFacade;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;

/**
* ZABBIX监控项collect业务   <br/>
* Project Name:collect-service
* File Name:ZabbixItemCollectBiz.java
* Package Name:com.aspire.mirror.collect.biz
* ClassName: ZabbixItemCollectBiz <br/>
* date: 2018年9月6日 下午2:22:06 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Component
public class ZabbixItemCollectBiz {
	@Autowired
	private ZabbixApiFacade zbxFacade;
	
	public List<ZabbixTemplateDetailResponse> loadZbxTemplateList(
							MonitorApiServerConfig zbxApiSvrInfo) throws Exception {
		ZbxApiSvrInfo zbxApiSvr = buildZbxApiSvrInfo(zbxApiSvrInfo);
		TypeRef<List<ZabbixTemplateDetailResponse>> typeRef = new TypeRef<List<ZabbixTemplateDetailResponse>>() {};
		return zbxFacade.requestZbxApi(zbxApiSvr, "template.listall", typeRef);
	}
	
	/**
	* 查询监控项列表. <br/>
	*
	* 作者： pengguihua
	* @param paramObj
	* @return
	* @throws Exception
	*/  
	public List<MonitorItemDetailResponse> queryZbxTemplateItemList(
							TemplateItemListRequest paramObj) throws Exception {
		Object templateIdParam = paramObj.getParamValue(TemplateItemListRequest.PARAM_TEMPLATE_ID);
		Object itemKeyParam = paramObj.getParamValue(TemplateItemListRequest.PARAM_ITEM_KEY);
		Object itemNameParam = paramObj.getParamValue(TemplateItemListRequest.PARAM_ITEM_NAME);
		Integer templateId = templateIdParam == null ? null : Integer.class.cast(templateIdParam);
		String itemKey = itemKeyParam == null ? "" : String.class.cast(itemKeyParam);
		String itemName = itemNameParam == null ? "" : String.class.cast(itemNameParam);
		
		String rawJson = zbxFacade.getZbxApiTemplateJson(CollectBizConst.ZBX_JSON_LIST_ITEMS);
		DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(rawJson);
		if (templateId == null) {
			jsonCtx.delete("$.params.templateids");
		} else {
			jsonCtx.set("$.params.templateids", templateId);
		}
		
		if (StringUtils.isBlank(itemKey)) {
			jsonCtx.delete("$.params.search.key_");
		} else {
			jsonCtx.set("$.params.search.key_", itemKey);
		}
		
		if (StringUtils.isBlank(itemName)) {
			jsonCtx.delete("$.params.search.name");
		} else {
			jsonCtx.set("$.params.search.name", itemName);
		}
		
		ZbxApiSvrInfo zbxApiSvr = buildZbxApiSvrInfo(paramObj.getApiServerConfig());
		TypeRef<List<MonitorItemDetailResponse>> typeRef = new TypeRef<List<MonitorItemDetailResponse>>() {};
		List<MonitorItemDetailResponse> resultList 
				= zbxFacade.requestZbxApiJson(zbxApiSvr, jsonCtx.jsonString(), typeRef);
		return new ArrayList<>(new HashSet<>(resultList));	// 去重
	}
	
	/**
	* 如果存在模版名称的参数，则先获取模版id. <br/>
	*
	* 作者： pengguihua
	* @param paramObj
	* @return
	* @throws Exception
	private List<Integer> getParamTemplateIds(TemplateItemListRequest paramObj) throws Exception {
		ZbxApiSvrInfo zbxApiSvr = buildZbxApiSvrInfo(paramObj.getApiServerConfig());
		Object templateParam = paramObj.getParams().get(TemplateItemListRequest.PARAM_TEMPLATE_ID);
		
		List<ZbxTemplate> templateList = null;
		if (templateParam != null && StringUtils.isNotBlank(String.class.cast(templateParam))) {
			String templateName = String.class.cast(templateParam);
			TypeRef<List<ZbxTemplate>> typeRef = new TypeRef<List<ZbxTemplate>>() {};
			templateList = zbxFacade.requestZbxApi(
					zbxApiSvr, CollectBizConst.ZBX_JSON_VAGUE_TEMPLATE, typeRef, templateName);
		}
		List<Integer> resultList = new ArrayList<>();
		if (templateList == null) {
			return resultList;
		}
		for (ZbxTemplate temp : templateList) {
			resultList.add(Integer.valueOf(temp.getTemplateId()));
		}
		return resultList;
	}
	 */  
	
	private ZbxApiSvrInfo buildZbxApiSvrInfo(MonitorApiServerConfig	apiServerConfig) {
		if (!MonitorApiServerConfig.SERVER_TYPE_ZABBIX.equalsIgnoreCase(apiServerConfig.getServerType())) {
			String tipMsg = "Error to build ZbxApiSvrInfo as the the monitor api server is not of type 'ZABBIX'";
			throw new RuntimeException(tipMsg);
		}
		ZbxApiSvrInfo zbxApiSvr = new ZbxApiSvrInfo();
		zbxApiSvr.setUrl(apiServerConfig.getUrl());
		zbxApiSvr.setUserName(apiServerConfig.getUsername());
		zbxApiSvr.setPassword(apiServerConfig.getPassword());
		return zbxApiSvr;
	}
}
