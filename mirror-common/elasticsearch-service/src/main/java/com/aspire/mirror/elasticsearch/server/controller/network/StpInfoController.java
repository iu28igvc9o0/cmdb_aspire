package com.aspire.mirror.elasticsearch.server.controller.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.elasticsearch.api.dto.RouteDataInfo;
import com.aspire.mirror.elasticsearch.api.dto.RouteDeviceInfo;
import com.aspire.mirror.elasticsearch.api.dto.RouteQueryRequest;
import com.aspire.mirror.elasticsearch.api.dto.StpDataInfo;
import com.aspire.mirror.elasticsearch.api.dto.StpDeviceInfo;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IStpInfoService;
import com.aspire.mirror.elasticsearch.server.controller.CommonController;
import com.aspire.mirror.elasticsearch.server.enums.stpStateEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 * <p>
 * 项目名称: mirror平台 包: com.aspire.mirror.elasticsearch.server.controller.lldp 类名称:
 * LldpInfoController.java 类描述: TODO 创建人: JinSu 创建时间: 2019/9/20 14:59 版本: v1.0
 */
@RestController
@Slf4j
public class StpInfoController extends CommonController implements IStpInfoService {
	@Value(value = "${stp_index_name:业支域非池化:stp-network-feichi-;信息港资源池:stp-network-xxgchi-}")
	private String indexNames;
	
	@Value(value = "${route_index_name:哈尔滨资源池:route-network-hachi-}")
	private String routeIndexNames;

	@Autowired
	private TransportClient transportClient;

	@Override
	public StpDeviceInfo queryStpInfo(@RequestParam(value = "idcType") String idcType,
			@RequestParam(value = "ip") String ip,
			@RequestParam(value = "indexDate", required = false) String indexDate) {
		String[] indexNameArray = indexNames.split(";");
		String indexName = "";
		for (String str : indexNameArray) {
			String str1 = str.split(":")[0];
			if (str1.equals(idcType)) {
				indexName = str.split(":")[1];
			}
		}

		String indexUse = indexName;
		if (StringUtils.isNotBlank(indexUse)) {
			if (StringUtils.isNotBlank(indexDate)) {
				indexUse = indexName + indexDate + "*";
			} else {
				indexUse += "*";
			}
		}
		log.info("stp indexList:{},use:{}", indexNameArray, indexUse);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,indexUse)).setTypes("data").setExplain(true);
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		queryBuilder.must(QueryBuilders.termQuery("host", ip));
		request.setQuery(queryBuilder).setSize(1);
		request.addSort("start_time", SortOrder.DESC);

		log.info("输入queryStpInfo--request:{}", request);
		SearchResponse resp = request.get();
		if (resp.getHits().getTotalHits() > 0) {
			List<StpDataInfo> stpDataList = Lists.newArrayList();
			StpDeviceInfo stpDeviceInfo = new StpDeviceInfo();
			SearchHit hit = resp.getHits().getAt(0);
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();
			Map<String, Object> stpDataMap = (Map<String, Object>) sourceAsMap.get("stp_data");
			stpDeviceInfo.setDeviceMfrs(
					sourceAsMap.get("device_brand") == null ? "" : sourceAsMap.get("device_brand").toString());
			stpDeviceInfo.setDeviceModel(
					sourceAsMap.get("device_model") == null ? "" : sourceAsMap.get("device_model").toString());
			stpDeviceInfo.setDeviceType(
					sourceAsMap.get("device_type") == null ? "" : sourceAsMap.get("device_type").toString());
			stpDeviceInfo.setIdcType(idcType);
			stpDeviceInfo.setIndexDate(
					sourceAsMap.get("start_time") == null ? "" : sourceAsMap.get("start_time").toString());
			stpDeviceInfo.setIp(ip);

			List<Map<String, String>> port = (List<Map<String, String>>) stpDataMap.get("Dot1dStpPort");
			Map<String, String> portMap = getMap(port, 0);

			/*
			 * List<Map<String, String>> priority = (List<Map<String,
			 * String>>)stpDataMap.get("stpPortPriority"); Map<String, String> priorityMap =
			 * getMap(port);
			 */

			List<Map<String, String>> state = (List<Map<String, String>>) stpDataMap.get("Dot1dStpPortState");
			Map<String, String> stateMap = getMap(state, 2);

			List<Map<String, String>> cost = (List<Map<String, String>>) stpDataMap.get("Dot1dStpPortPathCost");
			Map<String, String> costMap = getMap(cost, 0);

			List<Map<String, String>> root = (List<Map<String, String>>) stpDataMap.get("Dot1dStpPortDesignatedRoot");
			Map<String, String> rootMap = getMap(root, 0);

			List<Map<String, String>> designatedBridge = (List<Map<String, String>>) stpDataMap
					.get("Dot1dStpPortDesignatedBridge");
			Map<String, String> designatedBridgeMap = getMap(designatedBridge, 0);

			List<Map<String, String>> designatedPort = (List<Map<String, String>>) stpDataMap
					.get("Dot1dStpPortDesignatedPort");
			Map<String, String> designatedPortMap = getMap(designatedPort, 0);

			List<Map<String, String>> transitions = (List<Map<String, String>>) stpDataMap
					.get("Dot1dStpPortForwardTransitions");
			Map<String, String> transitionsMap = getMap(transitions, 0);

			List<Map<String, String>> desc = (List<Map<String, String>>) stpDataMap.get("IfDescr");
			Map<String, String> descMap = getMap(desc, 1);

			/*
			 * List<Map<String, String>> designatedCost = (List<Map<String,
			 * String>>)stpDataMap.get("stpPortDesignatedCost"); Map<String, String>
			 * designatedCostMap = getMap(port);
			 */

			for (Entry<String, String> entry : portMap.entrySet()) {
				String portKey = entry.getKey();
				StpDataInfo stpData = new StpDataInfo();
				stpData.setStpPort(portKey);
				if (StringUtils.isNotBlank(descMap.get(portKey))) {
					stpData.setIfDescr(descMap.get(portKey));
				} else {
					stpData.setIfDescr(portKey);
				}

				stpData.setStpPortState(stateMap.get(portKey));
				stpData.setStpPortPathCost(costMap.get(portKey));
				stpData.setStpPortDesignatedRoot(rootMap.get(portKey));
				stpData.setStpPortDesignatedBridge(designatedBridgeMap.get(portKey));
				stpData.setStpPortDesignatedPort(designatedPortMap.get(portKey));
				stpData.setStpPortForwardTransitions(transitionsMap.get(portKey));
				stpDataList.add(stpData);
			}
			stpDeviceInfo.setStpDataList(stpDataList);
			return stpDeviceInfo;
			// }
		}

		return null;
	}

	private Map<String, String> getMap(List<Map<String, String>> list, int flag) {
		Map<String, String> mapData = new HashMap<>();
		if (null != list) {
			for (Map<String, String> map : list) {
				if (StringUtils.isNotBlank(map.get("target_name"))) {
					if (flag == 1) {
						mapData.put(map.get("target_name").trim(), map.get("target_descr").trim());
					} else if (flag == 2) {
						String name = stpStateEnum.getName(Integer.parseInt(map.get("target_value").trim()));
						mapData.put(map.get("target_name").trim(),
								name == null ? map.get("target_value").trim() : name);
					} else {
						mapData.put(map.get("target_name").trim(), map.get("target_value").trim());
					}

				}
			}

		}

		return mapData;
	}

	@Override
	public RouteDeviceInfo queryRouteInfo(@RequestBody RouteQueryRequest queryRequest) {
		
		/*
		 * routeIndexNames = "业支域非池化:stp-network-feichi-;信息港资源池:stp-network-xxgchi-" +
		 * ";呼和浩特资源池:route-network-hachi-";
		 */
		 
		log.info("queryRouteInfo RouteQueryRequest:{}", queryRequest);
		String idcType = queryRequest.getIdcType();
		String ip = queryRequest.getIp();
		//ip="10.198.95.41";
		//ip="10.198.79.1";
		String[] indexNameArray = routeIndexNames.split(";");
		String indexName = "";
		for (String str : indexNameArray) {
			String str1 = str.split(":")[0];
			if (str1.equals(idcType)) {
				indexName = str.split(":")[1];
			}
		}

		String indexUse = indexName;
		String indexDate = queryRequest.getCollectDate();
		if (StringUtils.isNotBlank(indexUse)) {

			if (StringUtils.isNotBlank(indexDate)) {
				indexUse = indexName + indexDate + "*";
			} else {
				indexUse += "*";
			}

			// indexUse += "*";
		}
		log.info("route indexList:{},use:{}", indexNameArray, indexUse);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(queryRequest,indexUse)).setTypes("data").setExplain(true);
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		queryBuilder.must(QueryBuilders.termQuery("host", ip));
		queryBuilder.must(QueryBuilders.termQuery("status.keyword", "true"));
		request.setQuery(queryBuilder).setSize(1);
		
		request.addSort("start_time", SortOrder.DESC);

		log.info("输入queryRouteInfo--request:{}", request);
		SearchResponse resp = request.get();
		if (resp.getHits().getTotalHits() > 0) {
			boolean flag = false;
			int start = 0;
			int end = 0;
			if(queryRequest.getPageNum()!=null && queryRequest.getPageSize()!=null) {
				int pageNum = queryRequest.getPageNum()-1<0?0:queryRequest.getPageNum()-1;
				start = pageNum*queryRequest.getPageSize();
				end = start + queryRequest.getPageSize();
				flag = true;
			}
			
			List<RouteDataInfo> routeDataList = Lists.newArrayList();
			RouteDeviceInfo routeDeviceInfo = new RouteDeviceInfo();
			SearchHit hit = resp.getHits().getAt(0);
			Map<String, Object> sourceAsMap = hit.getSourceAsMap();
			Map<String, Object> routeDataMap = (Map<String, Object>) sourceAsMap.get("route_data");
			routeDeviceInfo.setStartTime(
					sourceAsMap.get("start_time") == null ? "" : sourceAsMap.get("start_time").toString());
			routeDeviceInfo.setStatus(
					sourceAsMap.get("status") == null ? "" : sourceAsMap.get("status").toString());
			routeDeviceInfo.setCode(
					sourceAsMap.get("code") == null ? "" : sourceAsMap.get("code").toString());
			routeDeviceInfo.setIdcType(idcType);
			routeDeviceInfo.setMsg(
					sourceAsMap.get("msg") == null ? "" : sourceAsMap.get("msg").toString());
			routeDeviceInfo.setDeviceDescription(
					sourceAsMap.get("device_description") == null ? "" : sourceAsMap.get("device_description").toString());
			routeDeviceInfo.setDeviceName(
					sourceAsMap.get("device_name") == null ? "" : sourceAsMap.get("device_name").toString());
			routeDeviceInfo.setIp(ip);

			List<Map<String, String>> dest = (List<Map<String, String>>) routeDataMap.get("ipCidrRouteDest");
			Map<String, String> destMap = getRouteMap(dest);

		

			List<Map<String, String>> mask = (List<Map<String, String>>) routeDataMap.get("ipCidrRouteMask");
			Map<String, String> maskMap = getRouteMap(mask);

			List<Map<String, String>> nextHop = (List<Map<String, String>>) routeDataMap.get("ipCidrRouteNextHop");
			Map<String, String> nextHopMap =getRouteMap(nextHop);

			List<Map<String, String>> proto = (List<Map<String, String>>) routeDataMap.get("ipCidrRouteProto");
			Map<String, String> protoMap = getRouteMap(proto);
			int total = 0;
			for (Entry<String, String> entry : destMap.entrySet()) {
				String destKey = entry.getKey();
				RouteDataInfo routeData = new RouteDataInfo();
				String dests = entry.getValue();
				String masks = maskMap.get(destKey);
				String next = nextHopMap.get(destKey);
				String protos = protoMap.get(destKey);
				 if(StringUtils.isNotBlank(queryRequest.getRouteDest())){
		                if(dests.indexOf(queryRequest.getRouteDest())==-1){
		                  continue;
		                }
		              }
				 if(StringUtils.isNotBlank(queryRequest.getRouteMask())){
		                if(masks.indexOf(queryRequest.getRouteMask())==-1){
		                	continue;
		                }
		              }
				 if(StringUtils.isNotBlank(queryRequest.getRouteNextHop())){
		                if(next.indexOf(queryRequest.getRouteNextHop())==-1){
		                	continue;
		                }
		              }
				 if(StringUtils.isNotBlank(queryRequest.getRouteProto())){
		                if(protos.indexOf(queryRequest.getRouteProto())==-1){
		                	continue;
		                }
		              }
				 total ++;
				 if(flag) {
					 if(total>start && total<=end) {
							routeData.setRouteDest(dests);
							routeData.setRouteMask(masks);
							routeData.setRouteNextHop(next);
							routeData.setRouteProto(protos);
							routeDataList.add(routeData);
						}
				 }else {
					 routeData.setRouteDest(dests);
						routeData.setRouteMask(masks);
						routeData.setRouteNextHop(next);
						routeData.setRouteProto(protos);
						routeDataList.add(routeData);
				 }
				
				
			}
			routeDeviceInfo.setTotal(total);
			routeDeviceInfo.setRouteDataList(routeDataList);
			return routeDeviceInfo;
			// }
		}

		return null;
	}
	
	private Map<String, String> getRouteMap(List<Map<String, String>> list) {
		Map<String, String> mapData = Maps.newLinkedHashMap();
		if (null != list) {
			for (Map<String, String> map : list) {
				if (StringUtils.isNotBlank(map.get("metric_name"))) {
					mapData.put(map.get("metric_name").trim(), map.get("metric_value").trim());

				}
			}

		}

		return mapData;
	}
}
