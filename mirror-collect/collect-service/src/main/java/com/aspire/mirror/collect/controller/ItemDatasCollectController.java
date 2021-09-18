package com.aspire.mirror.collect.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.collect.api.ItemDatasCollectService;
import com.aspire.mirror.collect.api.payload.ObjectItemValueWrap;
import com.aspire.mirror.collect.api.payload.FetchObjectItemsValueRequest;
import com.aspire.mirror.collect.api.payload.MonitorApiServerConfig;
import com.aspire.mirror.collect.api.payload.MonitorItemDetailResponse;
import com.aspire.mirror.collect.api.payload.TemplateItemListRequest;
import com.aspire.mirror.collect.api.payload.ZabbixTemplateDetailResponse;
import com.aspire.mirror.collect.biz.ZabbixItemCollectBiz;
import com.aspire.mirror.collect.biz.itemvaluefetch.IMonitorItemValueFetch;

/**
* 监控项相关数据收集服务实现    <br/>
* Project Name:collect-service
* File Name:ItemDatasCollectController.java
* Package Name:com.aspire.mirror.collect.controller
* ClassName: ItemDatasCollectController <br/>
* date: 2018年9月5日 下午6:37:42 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@RestController
public class ItemDatasCollectController implements ItemDatasCollectService {
	@Autowired
	private ZabbixItemCollectBiz zbxCollectBiz;
	
	@Autowired
	private IMonitorItemValueFetch itemValFetcher;
	
	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<ZabbixTemplateDetailResponse> loadZbxTemplateList(@RequestBody MonitorApiServerConfig zbxApiSvrInfo) {
		try {
			return zbxCollectBiz.loadZbxTemplateList(zbxApiSvrInfo);
		} catch (Exception e) {
			throw new RuntimeException("Error to load zabbix templateList.", e);
		}
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<MonitorItemDetailResponse> queryZbxTemplateItemList(@RequestBody TemplateItemListRequest paramObj) {
		try {
			return zbxCollectBiz.queryZbxTemplateItemList(paramObj);
		} catch (Exception e) {
			throw new RuntimeException("Error to query itemList.", e);
		}
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	public List<ObjectItemValueWrap> fetchDeviceItemsValues(@RequestBody FetchObjectItemsValueRequest request) {
		final List<ObjectItemValueWrap> resultList = new ArrayList<>();
		itemValFetcher.fetchMonitorItemValues(request, resultList);
		return resultList;
	}
}
