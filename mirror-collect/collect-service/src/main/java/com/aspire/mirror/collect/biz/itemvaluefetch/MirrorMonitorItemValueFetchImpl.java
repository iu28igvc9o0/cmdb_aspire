package com.aspire.mirror.collect.biz.itemvaluefetch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.aspire.mirror.collect.api.payload.FetchObjectItemsValueRequest;
import com.aspire.mirror.collect.api.payload.ObjectItemInfo;
import com.aspire.mirror.collect.api.payload.ObjectItemValueWrap;
import com.aspire.mirror.collect.clientservice.TemplateServiceClient;

import lombok.extern.slf4j.Slf4j;

/**
* MIRROR业务监控项抓取实现    <br/>
* Project Name:collect-service
* File Name:ZabbixDeviceItemValueFetchImpl.java
* Package Name:com.aspire.mirror.collect.biz
* ClassName: ZabbixDeviceItemValueFetchImpl <br/>
* date: 2018年9月6日 上午10:10:28 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
@Qualifier("itemValFetchImpl")
class MirrorMonitorItemValueFetchImpl implements IMonitorItemValueFetch {
	@Autowired
	private TemplateServiceClient tempServiceClient;

	@Override
	public void fetchMonitorItemValues(
			FetchObjectItemsValueRequest request, final List<ObjectItemValueWrap> resultHolder) {
		List<ObjectItemInfo> intrestItemList = resolveIntrestItemList(request);
		if (intrestItemList.isEmpty()) {
			return;
		}
		resultHolder.addAll(fetchMirrorBizItemVals(intrestItemList));
	}
	
	/**
	* 解析当前处理器感兴趣的监控项. <br/>
	*
	* 作者： pengguihua
	* @param request
	* @return
	*/  
	private List<ObjectItemInfo> resolveIntrestItemList(FetchObjectItemsValueRequest request) {
		List<ObjectItemInfo> resultList = new ArrayList<>();
		if (CollectionUtils.isEmpty(request.getObjectItemList())) {
			return resultList;
		}
		for (ObjectItemInfo itemInfo : request.getObjectItemList()) {
			if (ObjectItemInfo.API_SERVER_THEME.equalsIgnoreCase(itemInfo.getApiServerType())) {
				resultList.add(itemInfo);
			}
		}
		return resultList;
	}
	
	private List<ObjectItemValueWrap> fetchMirrorBizItemVals(List<ObjectItemInfo> itemList) {
		log.debug("Begin to fetch mirror biz item values ... ");
		List<ObjectItemValueWrap> resultList = new ArrayList<>();
		for (ObjectItemInfo itemInfo : itemList) {
			String val = tempServiceClient.findLastUpValueByItemId(itemInfo.getItemKey(), itemInfo.getObjectId());
			ObjectItemValueWrap wrap = new ObjectItemValueWrap();
			wrap.setClock(Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())).intValue());
			wrap.setItemId(itemInfo.getItemId());
			wrap.setItemKey(itemInfo.getItemKey());
			wrap.setNs(Long.valueOf(TimeUnit.SECONDS.toNanos(wrap.getClock())).intValue());
			wrap.setObjectType(itemInfo.getObjectType());
			wrap.setObjectId(itemInfo.getObjectId());
			wrap.setValue(val);
			resultList.add(wrap);
		}
		log.debug("Finished fetching mirror biz item values with size {}.", resultList.size());
		return resultList;
	}
}
