package com.aspire.mirror.collect.biz.itemvaluefetch;

import static org.apache.commons.lang3.StringUtils.join;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.aspire.app.ums.zabbixApi.model.ZbxApiSvrInfo;
import com.aspire.mirror.collect.api.payload.FetchObjectItemsValueRequest;
import com.aspire.mirror.collect.api.payload.MonitorApiServerConfig;
import com.aspire.mirror.collect.api.payload.ObjectItemInfo;
import com.aspire.mirror.collect.api.payload.ObjectItemValueWrap;
import com.aspire.mirror.collect.biz.model.ZbxHostInterface;
import com.aspire.mirror.collect.zabbix.ZabbixApiFacade;
import com.jayway.jsonpath.TypeRef;

import lombok.extern.slf4j.Slf4j;

/**
* ZABBIX设备监控项抓取实现    <br/>
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
class ZabbixDeviceItemValueFetchImpl implements IMonitorItemValueFetch {
	@Autowired
	private ZabbixApiFacade zbxApiFacade;

	@Override
	public void fetchMonitorItemValues(
			FetchObjectItemsValueRequest request, final List<ObjectItemValueWrap> resultHolder) {
		
		List<ObjectItemInfo> intrestItemList = resolveIntrestItemList(request);
		if (intrestItemList.isEmpty()) {
			return;
		}
		
		Map<MonitorApiServerConfig, List<ObjectItemInfo>> roomGroup = groupItemsByRoom(
				request.getApiServerConfigList(), intrestItemList);
		
		for (Map.Entry<MonitorApiServerConfig, List<ObjectItemInfo>> entry : roomGroup.entrySet()) {
			fetchDeviceItemValues(resultHolder, entry.getKey(), entry.getValue());
		}
	}
	
	/**
	* 抓取设备监控项值. <br/>
	*
	* 作者： pengguihua
	* @param resultHolder
	* @param zbxSvrInfo
	* @param deviceItemList
	*/  
	private void fetchDeviceItemValues(final List<ObjectItemValueWrap> resultHolder, 
			final MonitorApiServerConfig apiSvrCfg, final List<ObjectItemInfo> deviceItemList) {
		try {
			// 关联出设备hostId
			popupDeviceHostIds(apiSvrCfg, deviceItemList);
			// 准备zabbixApi查询参数
			Pair<List<Integer>, List<String>> paramList = getParamList(deviceItemList);
			String joinHostIdArr = join(paramList.getLeft(), ",");
			String joinKeyArr = join(paramList.getRight(), "\",\"");
			
			ZbxApiSvrInfo zbxApiSvr = buildZbxApiSvrInfo(apiSvrCfg);
			TypeRef<List<ObjectItemValueWrap>> typeRef = new TypeRef<List<ObjectItemValueWrap>>() {};
			List<ObjectItemValueWrap> itemValList = zbxApiFacade.requestZbxApi(
					zbxApiSvr, "item.get.bykeysAndHostIds", typeRef, joinHostIdArr, joinKeyArr);
			
			if (CollectionUtils.isEmpty(itemValList)) {
				log.warn("There is no result when try to retrieve the item values from zabbix API Server: "
						+ apiSvrCfg.getUrl());
				return;
			}
			
			// 回填deviceid信息
			for (ObjectItemValueWrap itemVal : itemValList) {
				for (ObjectItemInfo deviceItem : deviceItemList) {
					if (itemVal.getItemKey().equals(deviceItem.getItemKey())
							&& itemVal.getHostId().equals(deviceItem.getHostId())) {
						itemVal.setObjectType(deviceItem.getObjectType());
						itemVal.setObjectId(deviceItem.getObjectId());
						itemVal.setItemId(deviceItem.getItemId());
						break;
					}
				}
			}
			
			resultHolder.addAll(itemValList);
		} catch (Exception e) {
			log.error("Error to fetch zabbix device item value.", e);
		}
	}
	
	private Pair<List<Integer>, List<String>> getParamList(List<ObjectItemInfo> deviceItemList) {
		List<Integer> hostIdList = new ArrayList<>();
		List<String> itemKeyList = new ArrayList<>();
		for (ObjectItemInfo deviceItem : deviceItemList) {
			if (!hostIdList.contains(deviceItem.getHostId())) {
				hostIdList.add(deviceItem.getHostId());
			}
			if (!itemKeyList.contains(deviceItem.getItemKey())) {
				itemKeyList.add(deviceItem.getItemKey());
			}
		}
		return Pair.of(hostIdList, itemKeyList);
	}
	
	/**
	* 填充设备的hostid信息. <br/>
	*
	* 作者： pengguihua
	* @param apiSvrCfg
	* @param deviceItemList
	* @throws Exception
	*/  
	private void popupDeviceHostIds(
			MonitorApiServerConfig apiSvrCfg, final List<ObjectItemInfo> deviceItemList) throws Exception {
		List<String> deviceIPList = new ArrayList<>();
		for (ObjectItemInfo deviceItem : deviceItemList) {
			if (!deviceIPList.contains(deviceItem.getExtendObj())) {
				deviceIPList.add(deviceItem.getExtendObj());
			}
		}
		String joinDeviceIpArr = StringUtils.join(deviceIPList, "\",\"");
		ZbxApiSvrInfo zbxApiSvr = buildZbxApiSvrInfo(apiSvrCfg);
		TypeRef<List<ZbxHostInterface>> typeRef = new TypeRef<List<ZbxHostInterface>>() {};
		List<ZbxHostInterface> hostInterfaceList 
			= zbxApiFacade.requestZbxApi(zbxApiSvr, "hostinterface.get.byDeviceIp", typeRef, joinDeviceIpArr);
		
		if (CollectionUtils.isEmpty(hostInterfaceList)) {
			return;
		}
		
		for (ObjectItemInfo deviceItem : deviceItemList) {
			for (ZbxHostInterface hostInter : hostInterfaceList) {
				if (deviceItem.getExtendObj().equals(hostInter.getIp())) {
					deviceItem.setHostId(hostInter.getHostId());
					break;
				}
			}
		}
	}
	
	/**
	* 把设备按照机房apiServer进行分组. <br/>
	*
	* 作者： pengguihua
	* @param apiServerConfigList
	* @param deviceItemList
	* @return
	*/  
	private Map<MonitorApiServerConfig, List<ObjectItemInfo>> groupItemsByRoom(
			List<MonitorApiServerConfig> apiServerConfigList, List<ObjectItemInfo> deviceItemList) {
		Map<String, List<ObjectItemInfo>> group = new HashMap<>();
		
		for (ObjectItemInfo item : deviceItemList) {
			if (StringUtils.isBlank(item.getRoom())) {
				continue;
			}
			List<ObjectItemInfo> roomItemList = group.get(item.getRoom());
			if (roomItemList == null) {
				roomItemList = new ArrayList<ObjectItemInfo>();
				group.put(item.getRoom(), roomItemList);
			}
			roomItemList.add(item);
		}
		
		Map<MonitorApiServerConfig, List<ObjectItemInfo>> result = new HashMap<>();
		for (Map.Entry<String, List<ObjectItemInfo>> entry : group.entrySet()) {
			for (MonitorApiServerConfig apiSvr : apiServerConfigList) {
				if (ObjectItemInfo.API_SERVER_ZABBIX.equalsIgnoreCase(apiSvr.getServerType())
						&& entry.getKey().equals(apiSvr.getRoom())) {
					result.put(apiSvr, entry.getValue());
					break;
				}
			}
		}
		return result;
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
			if (ObjectItemInfo.API_SERVER_ZABBIX.equalsIgnoreCase(itemInfo.getApiServerType())
					&& ObjectItemInfo.OBJECT_TYPE_DEVICE.equals(itemInfo.getObjectType())) {
				resultList.add(itemInfo);
			}
		}
		return resultList;
	}
	
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
