package com.aspire.mirror.elasticsearch.server.controller.zabbix;

import java.text.ParseException;
import java.util.ArrayList;
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
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.aspire.mirror.elasticsearch.api.dto.TenantRateRequest;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IResourceRateScreenService;
import com.aspire.mirror.elasticsearch.server.controller.CommonController;
import com.aspire.mirror.elasticsearch.server.enums.Constants;
import com.aspire.mirror.elasticsearch.server.enums.DeviceUsedRateEnum;
import com.aspire.mirror.elasticsearch.server.util.DateUtil;
import com.aspire.mirror.elasticsearch.server.util.FormConditionUtil;
import com.aspire.mirror.elasticsearch.server.util.InstantUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * @author longfeng
 * @title: ItemController
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2015:53
 */
@RestController
@Slf4j
public class ResourceRateScreenController extends CommonController implements IResourceRateScreenService {
	@Autowired
	private TransportClient transportClient;

	@Value("${physical_pused_item_names:custom.cpu.avg5.pused,custom.memory.pused,diskpused}")
	private String pusedItemNames;

	@Value("${virtual_pused_item_names:cpu.usage_average,mem.usage_average,disk.usage_average}")
	private String virtralPusedItemNames;
	
	private final static String INDEX = "history-*";
	
	@Value("${INDEX_VALUE_NEW:value_avg}")
	private   String INDEX_VALUE;
	
	@Value("${INDEX_HISTORY_HOUR_PRE_NEW:kpi-hour_}")
	private   String INDEX_HISTORY_HOUR_PRE;
	

	@Override
	public Map<String, Object> queryData(@RequestBody MonthReportRequest monthReportRequest) throws ParseException {
		log.info(
				"************************************ResourceRateScreenController queryData  begin***********************************");
		String start = monthReportRequest.getStartTime();
		String end = monthReportRequest.getEndTime();

		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		String idcType = monthReportRequest.getIdcType();
		String deviceType = monthReportRequest.getDeviceType();
		// String item = monthReportRequest.getItem();
		// Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();
		String INDEX = this.INDEX;
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

		if (StringUtils.isNotBlank(idcType)) {
			queryBuilder.must(QueryBuilders.termQuery("idcType.keyword", idcType));
		}

		QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", deviceType);
		queryBuilder.must(queryTermId);

		if (deviceType.equals("云主机")) {
			queryBuilder.must(QueryBuilders.termsQuery("item.keyword", virtralPusedItemNames.split(",")));
		} else {
			queryBuilder.must(QueryBuilders.termsQuery("item.keyword", pusedItemNames.split(",")));
		}

		if (StringUtils.isNotBlank(monthReportRequest.getCol())) {
			queryBuilder.must(QueryBuilders.termQuery(monthReportRequest.getCol() + ".keyword",
					monthReportRequest.getColValue()));
		}

		request.setQuery(queryBuilder).setSize(0);

		// 时间分组
		String format = "yyyy-MM-dd HH:mm:ss";

		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		if (monthReportRequest.getGranularity().equals("d")) {
			dateAgg.dateHistogramInterval(DateHistogramInterval.DAY);
		} else if (monthReportRequest.getGranularity().equals("h")) {
			dateAgg.dateHistogramInterval(DateHistogramInterval.HOUR);
		} else if (monthReportRequest.getGranularity().equals("m")) {
			dateAgg.dateHistogramInterval(DateHistogramInterval.minutes(5));
		}

		dateAgg.format(format);// .timeZone(DateTimeZone.getDefault());

		dateAgg.extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()));

		TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("item").field("item.keyword").size(3);

		termsBuilder1.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field("value"));
		dateAgg.subAggregation(termsBuilder1);
		TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("item_all").field("item.keyword").size(3);
		termsBuilder2.subAggregation(AggregationBuilders.avg("allAvg_item").field("value"));
		request.addAggregation(termsBuilder2);
		request.addAggregation(dateAgg);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();

		/*
		 * if(response.getHits().getTotalHits()==0) { return null; }
		 */
		Map<String, Object> tmpMap = new HashMap<String, Object>();
		if (null == response.getAggregations()) {
			return null;
		}
		
		
		final Terms termsAll = response.getAggregations().get("item_all");
		for (final Terms.Bucket buckt : termsAll.getBuckets()) {
			String itemStr = buckt.getKeyAsString();
			String avgVal = InstantUtils.getTermAggValues("avg", buckt, "allAvg_item");
			itemStr = DeviceUsedRateEnum.getItemAliasByItemName(itemStr);
			tmpMap.put(itemStr+"_avg", avgVal);
		}
		
		

		Histogram h = response.getAggregations().get(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS);
		List<Bucket> bucketss = (List<Bucket>) h.getBuckets();
		List<String> intervalTime = new ArrayList<>();
		// List<Object> vaues = new ArrayList<>();

		Map<String, List<Object>> valueMap = new HashMap<>();
		for (Bucket bt : bucketss) {
			String timeStr = bt.getKeyAsString();
			intervalTime.add(timeStr);
			final Terms terms = bt.getAggregations().get("item");

			String timeStrName = "cpu_pused";
			if (valueMap.containsKey(timeStrName)) {
				valueMap.get(timeStrName).add("");
			} else {
				List<Object> list = Lists.newArrayList();
				list.add("");
				valueMap.put(timeStrName, list);
			}
			timeStrName = "memory_pused";
			if (valueMap.containsKey(timeStrName)) {
				valueMap.get(timeStrName).add("");
			} else {
				List<Object> list = Lists.newArrayList();
				list.add("");
				valueMap.put(timeStrName, list);
			}
			timeStrName = "disk_pused";
			if (valueMap.containsKey(timeStrName)) {
				valueMap.get(timeStrName).add("");
			} else {
				List<Object> list = Lists.newArrayList();
				list.add("");
				valueMap.put(timeStrName, list);
			}

			for (final Terms.Bucket buckt : terms.getBuckets()) {
				String itemStr = buckt.getKeyAsString();
				String avgVal = InstantUtils.getTermAggValues("avg", buckt, "avg_item");
				itemStr = DeviceUsedRateEnum.getItemAliasByItemName(itemStr);
				if (valueMap.containsKey(itemStr)) {
					List<Object> list = valueMap.get(itemStr);
					list.add(avgVal);
				} else {
					List<Object> list = Lists.newArrayList();
					list.add(avgVal);
					valueMap.put(itemStr, list);
				}
			}

		}

		tmpMap.put("xAxis", intervalTime);
		tmpMap.put("series", valueMap);
		// log.info("map:{}",tmpMap);
		return tmpMap;
	}

	@Override
	public Map<String, Map<String, String>> queryDepartmentData(@RequestBody TenantRateRequest raterequest)
			throws ParseException {
		log.info("************************************queryDepartmentData begin***********************************");
		String item = raterequest.getItem();
		String start = raterequest.getStartTime();
		String end = raterequest.getEndTime();
		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		// String item = monthReportRequest.getItem();
		// Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();
		String INDEX = this.INDEX;
		List<String> indexList = DateUtil.getIndexList(startTime, endTime, INDEX);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(raterequest,indexList.toArray(new String[] {})))
				.setExplain(true);

		log.info("index:{}", indexList);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(startTime.getTime() / 1000);
		timeRange.lt(endTime.getTime() / 1000);
		queryBuilder.must(timeRange);
		

		if (StringUtils.isNotBlank(raterequest.getDepartment1())) {
			queryBuilder.must(QueryBuilders.termQuery( "department1.keyword",
					raterequest.getDepartment1()));
		}

		QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", raterequest.getDeviceType());
		queryBuilder.must(queryTermId);

		if (raterequest.getDeviceType().equals("云主机")) {
			if (item.equals("cpu")) {
				queryBuilder
						.must(QueryBuilders.termQuery("item.keyword", DeviceUsedRateEnum.VIRTUAL_CPU.getItemName()));
			} else if (item.equals("memory")) {
				queryBuilder
						.must(QueryBuilders.termQuery("item.keyword", DeviceUsedRateEnum.VIRTUAL_MEMORY.getItemName()));
			} else {
				queryBuilder
						.must(QueryBuilders.termQuery("item.keyword", DeviceUsedRateEnum.VIRTUAL_DISK.getItemName()));
			}
		} else {
			if (item.equals("cpu")) {
				queryBuilder
						.must(QueryBuilders.termQuery("item.keyword", DeviceUsedRateEnum.PHYSICAL_CPU.getItemName()));
			} else if (item.equals("memory")) {
				queryBuilder.must(
						QueryBuilders.termQuery("item.keyword", DeviceUsedRateEnum.PHYSICAL_MEMORY.getItemName()));
			} else {
				queryBuilder
						.must(QueryBuilders.termQuery("item.keyword", DeviceUsedRateEnum.PHYSICAL_DISK.getItemName()));
			}
		}

		request.setQuery(queryBuilder).setSize(0);

		TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("col").field(raterequest.getCol() + ".keyword")
				.size(10000);
		if (raterequest.isOrderflag()) {
			termsBuilder.size(raterequest.getTopNum());
		}

		termsBuilder.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field("value"));
		termsBuilder.subAggregation(AggregationBuilders.max(Constants.AGGREGATION_TYPE_MAX + "_item").field("value"));
		String sortCol = "max_item";
		if (raterequest.getType().equals("avg")) {
			sortCol = "avg_item";
		}
		if (raterequest.getSort().equals("desc")) {
			termsBuilder.order(BucketOrder.aggregation(sortCol, false));
		} else {
			termsBuilder.order(BucketOrder.aggregation(sortCol, true));
		}

		request.addAggregation(termsBuilder);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		List<Map<String, Object>> list = Lists.newArrayList();
		Map<String, Map<String, String>> tmpMap = Maps.newLinkedHashMap();
		if (response.getHits().getTotalHits() == 0L) {
			return tmpMap;
		}

		// List<Object> vaues = new ArrayList<>();

		// Map<String, List<Object>> valueMap = new HashMap<>();
		final Terms terms = response.getAggregations().get("col");

		for (final Terms.Bucket a : terms.getBuckets()) {
			Map<String, String> map = Maps.newHashMap();
			String name = a.getKeyAsString();
			String avgVal = InstantUtils.getTermAggValues("avg", a, "avg_item");
			String maxVal = InstantUtils.getTermAggValues("max", a, "max_item");
			map.put("avg", avgVal);
			map.put("max", maxVal);
			map.put("col", name);
			tmpMap.put(name, map);
			// list.add(tmpMap);
		}

		return tmpMap;
	}
	
	
	
	
	
	public Map<String, Double> queryDepartmentUseRateData(@RequestBody TenantRateRequest raterequest)
			throws ParseException {
		log.info("************************************queryDepartmentData begin***********************************");
		String item = raterequest.getItem();
		String start = raterequest.getStartTime();
		String end = raterequest.getEndTime();
		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		
		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(raterequest,indexList.toArray(new String[] {})))
				.setExplain(true);

		log.info("index:{}", indexList);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		/*
		 * RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		 * timeRange.gte(startTime.getTime() / 1000); timeRange.lt(endTime.getTime() /
		 * 1000); queryBuilder.must(timeRange);
		 */
		
		if (StringUtils.isNotBlank(raterequest.getIdcType())) {
			queryBuilder.must(QueryBuilders.termQuery( "idcType.keyword",
					raterequest.getIdcType()));
		}
		
		boolean departmen1Flag = false;
		boolean departmen2Flag = false;
		
		BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();
		
		if (null!=raterequest.getDepartment1List() && raterequest.getDepartment1List().size()>0) {
			shouldBuilder.should(QueryBuilders.termsQuery( "department1.keyword",
					raterequest.getDepartment1List()));
			departmen1Flag = true;
		}
		if (null!=raterequest.getDepartment2List() && raterequest.getDepartment2List().size()>0) {
			shouldBuilder.should(QueryBuilders.termsQuery( "department2.keyword",
					raterequest.getDepartment2List()));
			departmen2Flag = true;
		}

		queryBuilder.must(shouldBuilder);
		FormConditionUtil.getDeviceType(null,item,queryBuilder);
		
		request.setQuery(queryBuilder).setSize(0);

		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1)).timeZone(DateTimeZone.getDefault());
		//dateAgg.extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()));
		
		if(departmen1Flag) {
			TermsAggregationBuilder termsBuilderDe1 = AggregationBuilders.terms("department1").field("department1.keyword")
					.size(10);
		
			termsBuilderDe1.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "1_item").field(this.INDEX_VALUE));
			dateAgg.subAggregation(termsBuilderDe1);
		}
		if(departmen2Flag) {
			TermsAggregationBuilder termsBuilderDe2 = AggregationBuilders.terms("department2").field("department2.keyword")
					.size(10);
		
			termsBuilderDe2.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "2_item").field(this.INDEX_VALUE));
			dateAgg.subAggregation(termsBuilderDe2);
		}
		

		request.addAggregation(dateAgg);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		
		Map<String, Double> dataMap = Maps.newHashMap();
		if (response.getHits().getTotalHits() == 0L) {
			return dataMap;
		}
		 
		// List<Object> vaues = new ArrayList<>();

		// Map<String, List<Object>> valueMap = new HashMap<>();
		Histogram h = response.getAggregations().get(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS);
		List<Bucket> bucketss = (List<Bucket>) h.getBuckets();
		for (Bucket bt : bucketss) {
			if(departmen1Flag) {
				Terms terms =  bt.getAggregations().get("department1");
				for (final Terms.Bucket a : terms.getBuckets()) {
					formdata("department1",a,dataMap);
					
				}
				
			}
			if(departmen2Flag) {
				Terms terms =  bt.getAggregations().get("department2");
				for (final Terms.Bucket a : terms.getBuckets()) {
					formdata("department2",a,dataMap);
				}
			}
			
		}

		return dataMap;
	}

	void formdata(String type,Terms.Bucket a,Map<String, Double> dataMap){
		String name = a.getKeyAsString();
		String termName = "avg_item";
		if(type.equals("department1")) {
			 termName = "avg1_item";
		}else {
			 termName = "avg2_item";
		}
		Avg avg = a.getAggregations().get(termName);
		Double avgValue = InstantUtils.getNumericValue(avg);
		if(null!=avgValue) {
			if(dataMap.containsKey(name)) {
				if(dataMap.get(name)<avgValue) {
					dataMap.put(name,avgValue);
				}
			}else {
				dataMap.put(name,avgValue);
			}
		}
	}
}
