package com.aspire.mirror.elasticsearch.server.controller.zabbix;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregation;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.DateHistogramValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IDayMonthReportService;
import com.aspire.mirror.elasticsearch.server.controller.CommonController;
import com.aspire.mirror.elasticsearch.server.enums.Constants;
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
public class DayMonthReportController extends CommonController implements IDayMonthReportService {
	@Autowired
	private TransportClient transportClient;
	
	@Value("${INDEX_HISTORY_DAY_PRE:history-*}")
	private   String INDEX_HISTORY_DAY_PRE;
	
	@Value("${INDEX_HISTORYUINT_DAY_PRE:history_uint-*}")
	private   String INDEX_HISTORYUINT_DAY_PRE;
	
	@Value("${INDEX_VALUE:value}")
	private   String INDEX_VALUE;
	
	@Value("${HIS_GRANULARITY: 10}")
	private   int HIS_GRANULARITY;

	@Override
	public Map<String, Map<String, Object>> idcTypeDeviceUsedRateByDay(
			@RequestBody MonthReportRequest monthReportRequest) throws Exception {
		Map<String, Map<String, Object>> idcTypeMap = monthReportRequest.getDataMap();
		String idcType = monthReportRequest.getIdcType();
		String deviceType = monthReportRequest.getDeviceType();
		if (null == idcTypeMap) {
			idcTypeMap = Maps.newHashMap();
		}
		SearchRequestBuilder request = initeRequst(monthReportRequest, false);
		String item = monthReportRequest.getItem();
		if (null == request) {
			return idcTypeMap;
		}
		SearchResponse response = request.get();
		if (response.getHits().totalHits == 0) {
			return idcTypeMap;
		}

		final Terms terms = response.getAggregations().get("host");

		// idcTypeMap.put(monthReportRequest.getIdcType(), rangeMap);
		for (final Terms.Bucket a : terms.getBuckets()) {
			String ip = a.getKeyAsString();
			String key = idcType + "_" + ip+"_"+deviceType;
			Map<String, Object> rangeMap = new HashMap<>();
			if (idcTypeMap.containsKey(key)) {
				rangeMap = idcTypeMap.get(key);
			} else {
				rangeMap.put("ip", ip);
				rangeMap.put("idcType", idcType);
				rangeMap.put("deviceType", deviceType);
				idcTypeMap.put(key, rangeMap);
			}

			String max = InstantUtils.getAggValues("max", a, "avg_max");
			String avg = InstantUtils.getAggValues("avg", a, "avg_used");
			if(StringUtils.isBlank(max)) {
				max = null;
			}
			if(StringUtils.isBlank(avg)) {
				avg = null;
			}
			rangeMap.put(item + "_max", max);
			rangeMap.put(item + "_avg", avg);
		}

		return idcTypeMap;

	}

	public SearchRequestBuilder initeRequst(MonthReportRequest monthReportRequest, boolean bizSystemFlag)
			throws ParseException {
		String start = monthReportRequest.getStartTime();
		String end = monthReportRequest.getEndTime();
		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		// Date endTime = DateUtils.parseDate(endDate, new String[] { "yyyy-MM-dd
		// HH:mm:ss" });

		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.INDEX_HISTORY_DAY_PRE);

		log.info("INDEX:{}", indexList);

		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest,indexList.toArray(new String[] {})))
				.setExplain(true);

		/*
		 * RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		 * timeRange.gte(startTime.getTime() / 1000); timeRange.lt(endTime.getTime() /
		 * 1000); queryBuilder.must(timeRange);
		 */
		request.setQuery(queryBuilder);
		request.setSize(0);

		if (StringUtils.isNotBlank(monthReportRequest.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", monthReportRequest.getIdcType());
			queryBuilder.must(queryTermId);
		}
		
		if (StringUtils.isNotBlank(monthReportRequest.getPod())) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("podName.keyword", monthReportRequest.getPod().split(","));
			queryBuilder.must(queryTermId);
		}
		String deviceType = monthReportRequest.getDeviceType();
		if (StringUtils.isNotBlank(deviceType)) {

			FormConditionUtil.getDeviceType(deviceType,monthReportRequest.getItem(),queryBuilder);
		} else {
			log.error("设备利用率设备类型不能为空");
			return null;
		}

		TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("host").field("host.keyword").size(100000);
		termsBuilder.subAggregation(AggregationBuilders.max("avg_max").field("value"));
		termsBuilder.subAggregation(AggregationBuilders.avg("avg_used").field("value"));
		request.addAggregation(termsBuilder);
		log.info("查询设备利用率es脚本： {}", request);
		return request;
	}

	
	
	@Override
	public Map<String, Map<String, Object>> queryByDay(@RequestBody MonthReportRequest monthReportRequest)
			throws ParseException {
		log.info("**monthReport-queryByDay-begin***********************************");
		String start = monthReportRequest.getStartTime();
		String end = monthReportRequest.getEndTime();

		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		String idcType = monthReportRequest.getIdcType();
		String deviceType = monthReportRequest.getDeviceType();
		String item = monthReportRequest.getItem();
		Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();
		//String INDEX = "history-";

		/*
		 * DateFormat returnd = new SimpleDateFormat("yyyyMM"); INDEX = INDEX +
		 * returnd.format(startTime) + "*";
		 */
	
		/*
		 * Calendar calendar = Calendar.getInstance(); calendar.setTime(endTime);
		 * calendar.add(Calendar.DATE, 1);
		 */
		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.INDEX_HISTORY_DAY_PRE);
		log.info("index:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest,indexList.toArray(new String[] {}))).setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(startTime.getTime() / 1000);
		timeRange.lt(endTime.getTime() / 1000);
		queryBuilder.must(timeRange);

		if (StringUtils.isNotBlank(idcType)) {
			queryBuilder.must(QueryBuilders.termQuery("idcType.keyword", idcType));
		}
		if (StringUtils.isNotBlank(monthReportRequest.getPod())) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("podName.keyword", monthReportRequest.getPod().split(","));
			queryBuilder.must(queryTermId);
		}
		
		queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
		FormConditionUtil.getDeviceType(deviceType,monthReportRequest.getItem(),queryBuilder);

		request.setQuery(queryBuilder).setSize(0);

		// 时间分组
		String format = "yyyy-MM-dd HH:mm:ss";

		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
		dateAgg.format(format);// .timeZone(DateTimeZone.getDefault());

		dateAgg.extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()));

		TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("bizSystem").field("bizSystem.keyword")
				.size(100000);
		// dateAgg.subAggregation(AggregationBuilders.max(Constants.AGGREGATION_TYPE_MAX
		// + "_item").field("value"));
		dateAgg.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(this.INDEX_VALUE));
		termsBuilder2.subAggregation(dateAgg);

		termsBuilder2.subAggregation(PipelineAggregatorBuilders.statsBucket("stats_buckets3", "dateagg.avg_item"));

		request.addAggregation(termsBuilder2);
		// request.addAggregation(termsBuilder1New);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		/*
		 * if(response.getHits().getTotalHits() ==0) { return null; }
		 */
		if (null == dataMap || dataMap.size() == 0) {
			dataMap = new HashMap<>();
		}
		if(null == response.getAggregations() || null == response.getAggregations().get("bizSystem")) {
			return dataMap;
		}
		final Terms terms2 = response.getAggregations().get("bizSystem");
		for (final Terms.Bucket bucket2 : terms2.getBuckets()) {

			String biz = bucket2.getKeyAsString();
			String key = biz+deviceType;
			Map<String, Object> val = new HashMap<String, Object>();
			if (dataMap.containsKey(key)) {
				val = dataMap.get(key);
			} else {
				val.put("idcType", idcType);
				// val.put("department2", de2);
				val.put("bizSystem", biz);
				val.put("deviceType", deviceType);
				dataMap.put(key, val);
			}
			Stats stats = bucket2.getAggregations().get("stats_buckets3");
			if (null != stats) {
				
				String avg = InstantUtils.getStatsValues("avg", stats);
				String max = InstantUtils.getStatsValues("max", stats);
				
				/*
				 * Max max = bt1.getAggregations().get(name); if (max == null ||
				 * "-Infinity".equals(max.getValueAsString()) ||
				 * "NaN".equals(max.getValueAsString())) { value = ""; } else { value =
				 * max.getValueAsString(); }
				 */
				
				
				StringBuffer sb = new StringBuffer();
				
				sb.append(item).append("_max");
				if(StringUtils.isBlank(max)) {
					max = null;
				}
				if(StringUtils.isBlank(avg)) {
					avg = null;
				}
				val.put(sb.toString(), max);
				sb.setLength(0);
				sb.append(item).append("_avg");
				val.put(sb.toString(), avg);
			}

		}
		log.info("**monthReport-queryByDay-end***********************************");
		return dataMap;
	}
	
	@Override
	public Map<String, Map<String, Object>> bizSystemUsedRateByDay(@RequestBody MonthReportRequest monthReportRequest)
			throws Exception {
		Map<String, Map<String, Object>> idcTypeMap = monthReportRequest.getDataMap();
		if(null==idcTypeMap) {
			idcTypeMap = Maps.newLinkedHashMap();
			//return idcTypeMap;
		}
		SearchRequestBuilder request = initeDayRequst(monthReportRequest,false);
		String deviceType = monthReportRequest.getDeviceType();
		
		//组装composite
		  List<CompositeValuesSourceBuilder<?>> sources = Lists.newArrayList();
		  //sources.add(new TermsValuesSourceBuilder("idcType").field("idcType.keyword").missingBucket(false).order(SortOrder.ASC));
		  sources.add(new TermsValuesSourceBuilder("bizSystem").field("bizSystem.keyword").missingBucket(false).order(SortOrder.ASC));
	
		  String format = "yyyy-MM-dd HH:mm:ss";
DateHistogramValuesSourceBuilder datetime = new DateHistogramValuesSourceBuilder(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS)
                .field("datetime")
                .dateHistogramInterval(DateHistogramInterval.minutes(HIS_GRANULARITY))
                .format(format).order(SortOrder.ASC).missingBucket(false);
        sources.add(datetime);

		  CompositeAggregationBuilder composite = new CompositeAggregationBuilder("composite", sources).size(10000);

		request.addAggregation(composite);
		String cpuItem = "";
		String memItem = "";
		cpuItem = Constants.CPU_ITEM;
		memItem = Constants.MEMORY_ITEM;
		QueryBuilder cpuItemQb =QueryBuilders.termsQuery("item.keyword", cpuItem.split(","));
		
		QueryBuilder memItemQb = QueryBuilders.termsQuery("item.keyword", memItem.split(","));
		FilterAggregationBuilder  cpuFilter = AggregationBuilders.filter("cpuFilter", cpuItemQb);
		
		FilterAggregationBuilder  memFilter = AggregationBuilders.filter("memFilter", memItemQb);
		cpuFilter.subAggregation(AggregationBuilders.avg("value_avg").field(INDEX_VALUE));
		memFilter.subAggregation(AggregationBuilders.avg("value_avg").field(INDEX_VALUE));
		composite.subAggregation(cpuFilter);
		composite.subAggregation(memFilter);
		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.execute().actionGet();
		if(null==response.getAggregations()) {
			return idcTypeMap;
		}
		CompositeAggregation  parsedComposite = response.getAggregations().get("composite");
		Map<String, Object> afterKey = parsedComposite.afterKey();
		getBizSystemComposite(monthReportRequest,idcTypeMap,parsedComposite);
		while(null!=afterKey) {
			composite.aggregateAfter(afterKey);
			log.info("查询设备利用率es脚本： {}", request);
			 parsedComposite = request.execute().actionGet().getAggregations().get("composite");
			 getBizSystemComposite(monthReportRequest,idcTypeMap,parsedComposite);
			afterKey = parsedComposite.afterKey();
		}
		
		
		syncDate(idcTypeMap);
		return idcTypeMap;

	}
	
	private void syncDate(Map<String, Map<String, Object>> dataMap) {
		for(Map.Entry<String, Map<String, Object>> m :  dataMap.entrySet()) {
			Map<String, Object> val = m.getValue();
		
			if(null!=val.get("cpu_count")) {
				double maxVal = Double.parseDouble(val.get("cpu_max").toString());
				double sumVal = Double.parseDouble(val.get("cpu_sum").toString());
				int count =  Integer.parseInt(val.get("cpu_count").toString()); 
				maxVal =  (double)Math.round(maxVal*100)/100;
				double vagVal = (double) Math.round((double)(sumVal/count)*100)/100;
				val.put("cpu_max",maxVal);
				val.put("cpu_avg", vagVal);
			}
			
			if(null!=val.get("memory_count")) {
				double mem_maxVal = Double.parseDouble(val.get("memory_max").toString());
				double mem_sumVal = Double.parseDouble(val.get("memory_sum").toString());
				int mem_count =  Integer.parseInt(val.get("memory_count").toString()); 
				mem_maxVal =  (double)Math.round(mem_maxVal*100)/100;
				double mem_vagVal =  (double)Math.round((double)(mem_sumVal/mem_count)*100)/100;
				val.put("memory_max",mem_maxVal);
				val.put("memory_avg", mem_vagVal);
			}
			
			
		}
		
	} 

	public void getBizSystemComposite(MonthReportRequest monthReportRequest,Map<String, Map<String, Object>> idcTypeMap,
			CompositeAggregation parsedComposite){
		String startTime = monthReportRequest.getStartTime();
		String day = startTime.split(" ")[0];
		String month = startTime.split("-")[0]+"-"+startTime.split("-")[1];
		String deviceType = monthReportRequest.getDeviceType();
		String idcType = monthReportRequest.getIdcType();
		List<? extends CompositeAggregation.Bucket> list1 =  parsedComposite.getBuckets();
		for(int i=0;i<list1.size();i++){
			String bizSystem = "";
			for (Map.Entry<String, Object> m :  list1.get(i).getKey().entrySet()) {
				String keyTemp = m.getKey();
				
				if(keyTemp.equals("bizSystem")) {
					bizSystem = m.getValue()==null?null:m.getValue().toString();
				}
				
			}
			String key = bizSystem;
			
			Map<String, Object> val = Maps.newHashMap();
			if(idcTypeMap.containsKey(key)) {
				val = idcTypeMap.get(key);
			}else {
				idcTypeMap.put(key, val);
				val.put("idcType", idcType);
				val.put("bizSystem", bizSystem);
				val.put("deviceType", deviceType);
				val.put("day", day);
				val.put("month", month);
				val.put("history_min_flag", 1);
			}
			final InternalFilter cpuTerms = list1.get(i).getAggregations().get("cpuFilter");
			final InternalFilter memTerms = list1.get(i).getAggregations().get("memFilter");
			if(null!=cpuTerms) {
				Avg max = cpuTerms.getAggregations().get("value_avg");
				if ( max != null && !"-Infinity".equals( max.getValueAsString()) && !"NaN".equals( max.getValueAsString())) {
					double value = max.getValue();
					double maxVal = val.get("cpu_max")==null?0:Double.parseDouble(val.get("cpu_max").toString());
					double sumVal = val.get("cpu_sum")==null?0:Double.parseDouble(val.get("cpu_sum").toString());
					int count =  val.get("cpu_count")==null?0:Integer.parseInt(val.get("cpu_count").toString()); 
					val.put("cpu_count", ++count);
					val.put("cpu_max", value>maxVal?value:maxVal);
					val.put("cpu_sum", sumVal+value);
				}
			}
			
			if(null!=memTerms) {
				Avg memMax = memTerms.getAggregations().get("value_avg");
				if ( memMax != null && !"-Infinity".equals( memMax.getValueAsString()) && !"NaN".equals( memMax.getValueAsString())) {
					double value = memMax.getValue();
					double maxVal = val.get("memory_max")==null?0:Double.parseDouble(val.get("memory_max").toString());
					double sumVal = val.get("memory_sum")==null?0:Double.parseDouble(val.get("memory_sum").toString());
					int count =  val.get("memory_count")==null?0:Integer.parseInt(val.get("memory_count").toString()); 
					val.put("memory_count", ++count);
					val.put("memory_max", value>maxVal?value:maxVal);
					val.put("memory_sum", sumVal+value);
				}
			}
			
			
		}
	}
	
public SearchRequestBuilder initeDayRequst(MonthReportRequest monthReportRequest,boolean bizSystemFlag) throws ParseException {
		
		// 设置查询条件
				BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
				
		String startDate = monthReportRequest.getStartTime();
		String endDate = monthReportRequest.getEndTime();
		Date startTime = DateUtils.parseDate(startDate, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(endDate, new String[] { "yyyy-MM-dd HH:mm:ss" });
		
		 
		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.INDEX_HISTORY_DAY_PRE);
		log.info("index:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest,indexList.toArray(new String[] {}))).setExplain(true);
		
		if (StringUtils.isNotBlank(monthReportRequest.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", monthReportRequest.getIdcType());
			queryBuilder.must(queryTermId);
		}
		String deviceType = monthReportRequest.getDeviceType();
		if (StringUtils.isNotBlank(deviceType)) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", monthReportRequest.getDeviceType());
			queryBuilder.must(queryTermId);
			//FormConditionUtil.getDeviceType(deviceType,monthReportRequest.getItem(),queryBuilder);
		} else {
			log.error("设备利用率设备类型不能为空");
			return null;
		}
		queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
		request.setQuery(queryBuilder).setSize(0);
		
		return request;
	}

}
