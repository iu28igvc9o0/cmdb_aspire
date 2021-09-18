package com.aspire.mirror.elasticsearch.server.controller.zabbix;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.aspire.mirror.elasticsearch.api.dto.NetPerformanceAnalysis;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IZabbixRatioService;
import com.aspire.mirror.elasticsearch.server.controller.CommonController;
import com.aspire.mirror.elasticsearch.server.enums.Constants;
import com.aspire.mirror.elasticsearch.server.enums.DeviceUsedRateEnum;
import com.aspire.mirror.elasticsearch.server.util.DateUtil;
import com.aspire.mirror.elasticsearch.server.util.InstantUtils;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * @author baiwp
 * @title: TrendsController
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2116:18
 */
@Slf4j
@RestController
public class RatioController extends CommonController implements IZabbixRatioService {
   
    @Autowired
    private TransportClient transportClient;

   
    @Override
	public Map<String,NetPerformanceAnalysis> getServerRatioData(@RequestParam(value = "deviceType", required = false)String deviceType
			,@RequestParam(value = "idcType", required = false) String idcType,
			@RequestParam(value = "ipList", required = false) List<String> ipList) {
		log.info("getServerRatioData params： ipList:{}", ipList);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -1);
		DateFormat returndf = new SimpleDateFormat("yyyyMMdd");
		String index = "history-*"+returndf.format(calendar.getTime());
		index +="*";
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,index)).setExplain(true);
		
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		
		if (StringUtils.isNotBlank(deviceType)) {
			QueryBuilder queryClassId = QueryBuilders.termsQuery("deviceType.keyword", deviceType);
			queryBuilder.must(queryClassId);
		}
		
		if (StringUtils.isNotBlank(idcType)) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("idcType.keyword",idcType);
			queryBuilder.must(queryTermId);
		}
		if(null!=ipList && ipList.size()>0) {
			QueryBuilder queryIp = QueryBuilders
					.constantScoreQuery(QueryBuilders.termsQuery(Constants.DEVICE_IP + ".keyword", ipList));
			queryBuilder.must(queryIp);
		}
		
		if(deviceType.equals("云主机")) {
			 queryBuilder.must(QueryBuilders.termsQuery("item.keyword", new String[] {DeviceUsedRateEnum.VIRTUAL_CPU.getItemName()
					 ,DeviceUsedRateEnum.VIRTUAL_MEMORY.getItemName()}));
		}else {
			queryBuilder.must(QueryBuilders.termsQuery("item.keyword", new String[] {DeviceUsedRateEnum.PHYSICAL_CPU.getItemName()
					 ,DeviceUsedRateEnum.PHYSICAL_MEMORY.getItemName()}));
		}

		request.setQuery(queryBuilder).setSize(0);

		TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("host").field("host.keyword");
		
		termsBuilder.size(ipList.size());
		//TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("item").field("item.keyword").size(100000);
		TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("item").field("item.keyword");
		termsBuilder1.subAggregation(AggregationBuilders.avg("avg_used").field("value"));
		termsBuilder1.subAggregation(AggregationBuilders.max("max_used").field("value"));
		termsBuilder.subAggregation(termsBuilder1);
		request.addAggregation(termsBuilder);

		log.info("查询网络性能利用率es脚本： {}", request);
		SearchResponse response = request.get();
		
		Map<String,NetPerformanceAnalysis> mapNet = new HashMap<>();
		if (response.getHits().getTotalHits() > 0L) {
			final Terms hostTerms = response.getAggregations().get("host");
			
			for (final Terms.Bucket aa : hostTerms.getBuckets()) {
				final Terms terms = response.getAggregations().get("item");
				String ip = aa.getKeyAsString();
				NetPerformanceAnalysis net = new NetPerformanceAnalysis();
				net.setIp(ip);
				for (final Terms.Bucket a : terms.getBuckets()) {
					String item = a.getKeyAsString();

					//Max max = a.getAggregations().get("max_used");
					//Avg avg = a.getAggregations().get("avg_used");
					String maxVal = InstantUtils.getTermAggValues("max", a, "max_used");
					String avgVal = InstantUtils.getTermAggValues("avg", a, "avg_used");
					if(DeviceUsedRateEnum.getItemAliasByItemName(item).equals("cpu_pused")) {
						if(StringUtils.isNotBlank(avgVal)) {
							net.setCpuAvg(Double.parseDouble(avgVal));
						}
						if(StringUtils.isNotBlank(maxVal)) {
							net.setCpuMax(Double.parseDouble(maxVal));
						}
						
					}else {
						if(StringUtils.isNotBlank(avgVal)) {
							net.setMemAvg(Double.parseDouble(avgVal));
						}
						if(StringUtils.isNotBlank(maxVal)) {
							net.setMemMax(Double.parseDouble(maxVal));
						}
						//net.setMemAvg((double)Math.round(avg.getValue()*100)/100);
						//net.setMemMax((double)Math.round(max.getValue()*100)/100);
					}
					mapNet.put(ip, net);
					//returnList.add(net);
				}
			}
		}
		/*
		 * List<NetPerformanceAnalysis> returnList = Lists.newArrayList();
		 * if(mapNet.size()>0) { returnList =new ArrayList<>(mapNet.values()); }
		 */
		return mapNet;
	}
    
    @Override
    public Map<String,Map<String,Object>>  getDepartmentRatioData(@RequestBody MonthReportRequest monthReportRequest) throws ParseException {
		log.info("getDepartmentRatioData MonthReportRequest:{}",monthReportRequest);
		String start = monthReportRequest.getStartTime();
		String end = monthReportRequest.getEndTime();

		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		// String item = monthReportRequest.getItem();
		// Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();
		String INDEX = "history-*";
		List<String> indexList = DateUtil.getIndexList(startTime, endTime, INDEX);
		//log.info("index:{}",INDEX);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest,indexList.toArray(new String[] {})))
				.setExplain(true);
		log.info("index:{}", indexList);
		
		
		
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		
		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(startTime.getTime() / 1000);
		timeRange.lt(endTime.getTime() / 1000);
		queryBuilder.must(timeRange);
		
			QueryBuilder queryClassId = QueryBuilders.termQuery("deviceClass.keyword", "服务器");
			queryBuilder.must(queryClassId);
		
			if(monthReportRequest.getItem().equals("cpu")) {
				queryBuilder.must(QueryBuilders.termsQuery("item.keyword", new String[] {DeviceUsedRateEnum.VIRTUAL_CPU.getItemName()
						 ,DeviceUsedRateEnum.PHYSICAL_CPU.getItemName()}));
			}else {
				queryBuilder.must(QueryBuilders.termsQuery("item.keyword", new String[] {
						 DeviceUsedRateEnum.VIRTUAL_MEMORY.getItemName()
						 ,DeviceUsedRateEnum.PHYSICAL_MEMORY.getItemName()}));
			}
			 
		List<String> department1s = monthReportRequest.getDepartments();
		if(null!=department1s && department1s.size()>0) {
			queryBuilder.must(QueryBuilders.termsQuery("department1.keyword", department1s.toArray(new String[department1s.size()])));
		}
		request.setQuery(queryBuilder).setSize(0);

		TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("department1").field("department1.keyword").size(monthReportRequest.getTopn());
		if(monthReportRequest.getItem().equals("cpu")) {
			termsBuilder.order(BucketOrder.aggregation("avg_used", false));
		}
		termsBuilder.subAggregation(AggregationBuilders.avg("avg_used").field("value"));
		//termsBuilder.subAggregation(AggregationBuilders.max("max_used").field("value"));
		request.addAggregation(termsBuilder);
		log.info("查询网络性能利用率es脚本： {}", request);
		SearchResponse response = request.get();
		
		Map<String,Map<String,Object>> list = monthReportRequest.getDataMap();
		if(null== list) {
			list = Maps.newLinkedHashMap();
		}
		if (response.getHits().getTotalHits() > 0L) {
			final Terms hostTerms = response.getAggregations().get("department1");
			
			for (final Terms.Bucket aa : hostTerms.getBuckets()) {
				Map<String,Object> mapNet = new HashMap<>();
				String department = aa.getKeyAsString();
				if(list.containsKey(department)) {
					mapNet = list.get(department);
				}else {
					mapNet.put("tenant", department);
					mapNet.put("storage", "");
				}
				
					
					String avg = InstantUtils.getTermAggValues("avg", aa, "avg_used");
					if(monthReportRequest.getItem().equals("cpu")) {
						mapNet.put("cpu", avg+"%");
					}else {
						mapNet.put("men", avg+"%");
					}
					
					//returnList.add(net);
				list.put(department, mapNet);
			}
		}
		/*
		 * List<NetPerformanceAnalysis> returnList = Lists.newArrayList();
		 * if(mapNet.size()>0) { returnList =new ArrayList<>(mapNet.values()); }
		 */
		log.info("getDepartmentRatioData end");
		return list;
	}
}
