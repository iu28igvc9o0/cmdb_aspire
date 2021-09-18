package com.aspire.mirror.elasticsearch.server.controller.IndexPage;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.elasticsearch.api.dto.DevicePusedTopN;
import com.aspire.mirror.elasticsearch.server.biz.IKpiKeyConfigBiz;
import com.aspire.mirror.elasticsearch.server.controller.monitor.MonitorCommonController;
import com.aspire.mirror.elasticsearch.server.enums.KpiTypeEnum;
import com.aspire.mirror.elasticsearch.server.helper.EsAuthHelper;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.pipeline.InternalSimpleValue;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.InternalBucketMetricValue;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.stats.StatsBucketPipelineAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import com.aspire.mirror.elasticsearch.api.dto.IdcTypePhysicalReq;
import com.aspire.mirror.elasticsearch.api.service.indexPage.IIndexPageService;
import com.aspire.mirror.elasticsearch.server.enums.Constants;
import com.aspire.mirror.elasticsearch.server.util.DateUtil;
import com.aspire.mirror.elasticsearch.server.util.InstantUtils;
import com.aspire.mirror.elasticsearch.server.util.Md5Utils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class IndexPage  extends MonitorCommonController implements IIndexPageService{

	@Autowired
	private TransportClient transportClient;

	/*
	 * private static final String INDEX = "history-*"; private static final String
	 * INDEX_PRE = "history-";
	 */

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private IKpiKeyConfigBiz iKpiKeyConfigBiz;

	private static String INDEX_HISTORY_DAY_PRE = "kpi-day_";

	private static String INDEX_HISTORY_HOUR_PRE = "kpi-hour_";

	private static String INDEX_HISTORY_HOUR_UINT_PRE = "kpi-hour-uint_";

	private static String INDEX_VALUE = "value_avg";

	private static String INDEX_VALUE_MAX = "value_max";

	@Value("${cmdb.kg.deviceType.phyServer:X86服务器}")
	private String phyServer;
	@Value("${cmdb.kg.deviceType.vmHost:虚拟机}")
	private String vmHost;
	@Value("${cmdb.kg.deviceType.suHost:宿主机}")
	private String suHost;
	
	@Autowired
	private EsAuthHelper esAuthHelper;
	/**
	 * 资源池-资源利用率分布
	 */
	@Override
	public Map<String, Object> deviceUtilization(@RequestBody IdcTypePhysicalReq monthReportRequest) throws Exception {
		log.info("**IndexPage-deviceUtilization-begin***********************************");
		String item = monthReportRequest.getSourceType();
		//String bizSystem = monthReportRequest.getBizSystem();
		StringBuffer sb = new StringBuffer();
		sb.append("IndexPage.deviceUtilization").append(JSON.toJSONString(monthReportRequest));
		String key = Md5Utils.generateCheckCode(sb.toString());

		
		  if (redisTemplate.opsForHash().hasKey(Constants.redisKey, key)) { if (null ==
		  redisTemplate.opsForHash().get(Constants.redisKey, key)) { return null; }
		  return (Map<String, Object>)
		  redisTemplate.opsForHash().get(Constants.redisKey, key); }
		 

		String start = monthReportRequest.getStartDate();
		String end = monthReportRequest.getEndDate();

		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		String deviceType = monthReportRequest.getDeviceType();
		if(null==deviceType) {
			deviceType = "";
		}
		
		List<String> indexList = DateUtil.getIndexList(startTime, endTime, INDEX_HISTORY_HOUR_PRE);
		log.info("index:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest,indexList.toArray(new String[] {})))
				.setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(startTime.getTime() / 1000);
		timeRange.lt(endTime.getTime() / 1000);
		queryBuilder.must(timeRange);

//		FormConditionUtil.getDeviceType(deviceType, monthReportRequest.getSourceType(), queryBuilder);
		if (StringUtils.isNotEmpty(deviceType)) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", deviceType);
			queryBuilder.must(queryTermId);
		}
		if (StringUtils.isNotEmpty(monthReportRequest.getDepartment1())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("department1.keyword", monthReportRequest.getDepartment1());
			queryBuilder.must(queryTermId);
		}
		if (StringUtils.isNotEmpty(monthReportRequest.getDepartment2())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("department2.keyword", monthReportRequest.getDepartment2());
			queryBuilder.must(queryTermId);
		}
		queryBuilder.must(QueryBuilders.rangeQuery(INDEX_VALUE).gte(0));
		if ("cpu".equalsIgnoreCase(item)) {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(), "item"));
		} else {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.MEMORY_PUSED.name(), "item"));
		}

		request.setQuery(queryBuilder).setSize(0);

		// 时间分组
		String format = "yyyy-MM-dd HH:mm:ss";

		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
		dateAgg.format(format);// .timeZone(DateTimeZone.getDefault());

		dateAgg.extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()));

		// dateAgg.subAggregation(AggregationBuilders.max(Constants.AGGREGATION_TYPE_MAX
		// + "_item").field("value"));
		dateAgg.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(INDEX_VALUE));

		request.addAggregation(dateAgg);
		request.addAggregation(PipelineAggregatorBuilders.statsBucket("stats_buckets3", "dateagg.avg_item"));
		// request.addAggregation(termsBuilder1New);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		/*
		 * if(response.getHits().getTotalHits() ==0) { return null; }
		 */
		Map<String, Object> dataMap = new HashMap<>();
		 Map<String, Double> resultDefault = Maps.newHashMap();
		resultDefault.put("max", null);
		resultDefault.put("avg", null);
		/*
		 * if (deviceType.equals("云主机")) { dataMap.put("云主机", resultDefault); } else if
		 * (deviceType.equals("X86服务器")) { dataMap.put("X86服务器", resultDefault); } else
		 * if (deviceType.equals("宿主机")) { dataMap.put("宿主机", resultDefault); } else if
		 * (deviceType.equals("虚拟机")) { dataMap.put("虚拟机", resultDefault); }else {
		 * dataMap.put(deviceType, resultDefault); }
		 */
		dataMap.put(deviceType, resultDefault);
		if (null == response.getAggregations()) {
			putRedis(dataMap, key);
			return dataMap;
		}

		Stats stats = response.getAggregations().get("stats_buckets3");
		if (null != stats) {

			String avg = InstantUtils.getStatsValues("avg", stats);
			String max = InstantUtils.getStatsValues("max", stats);

			if (StringUtils.isBlank(max)) {
				max = null;
			}
			if (StringUtils.isBlank(avg)) {
				avg = null;
			}
			resultDefault.put("max", max==null?null:Double.parseDouble(max));
			resultDefault.put("avg", avg==null?null:Double.parseDouble(avg));
		}
		putRedis(dataMap, key);
		log.info("**IndexPage-deviceUtilization-end***********************************");
		return dataMap;
	}

	/**
	 * 资源池-资源利用率分布
	 */
	@Override
	public Map<String, Map<String, Long>> idcTypeDeviceUsedRate(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("idcTypeDeviceUsedRate").append(idcTypePhysicalReq.getStartDate())
				.append(idcTypePhysicalReq.getEndDate())
				.append(idcTypePhysicalReq.getIdcType() == null ? "" : idcTypePhysicalReq.getIdcType())
				.append(idcTypePhysicalReq.getDeviceType() == null ? "" : idcTypePhysicalReq.getDeviceType())
				.append(idcTypePhysicalReq.getSourceType());
		String key = Md5Utils.generateCheckCode(sb.toString());
		if (redisTemplate.opsForHash().hasKey(Constants.redisKey, key)) {
			return (Map<String, Map<String, Long>>) redisTemplate.opsForHash().get(Constants.redisKey, key);
		}

		Map<String, Map<String, Long>> idcTypeMap = new HashMap<>();
		SearchRequestBuilder request = initeRequst(idcTypePhysicalReq);

		if (null == request) {
			putRedis(idcTypeMap, key);
			return idcTypeMap;
		}
		SearchResponse response = request.get();
		if (null == response.getAggregations()) {
			putRedis(idcTypeMap, key);
			return idcTypeMap;
		}
		final Terms terms = response.getAggregations().get("idcType");
		for (final Terms.Bucket a : terms.getBuckets()) {
			String col = a.getKeyAsString();
			final Terms termsHost = a.getAggregations().get("host");
			for (final Terms.Bucket aa : termsHost.getBuckets()) {
				Max max = aa.getAggregations().get("avg_used");
				formCountVal(max,  col,idcTypeMap);
			}
			Map<String, Long> val = idcTypeMap.get(col);
			if(null!=val) {
				val.put("count", val.get("15")+ val.get("15-40")+ val.get("40-80")+ val.get("80"));
			}
		}
		

		putRedis(idcTypeMap, key);
		return idcTypeMap;
	}

	private void putRedis(Object object, String key) {
		redisTemplate.opsForHash().put(Constants.redisKey, key, object);
		redisTemplate.expire(Constants.redisKey, Constants.redisTimeOut, TimeUnit.HOURS);

	}

	public SearchRequestBuilder initeRequst(IdcTypePhysicalReq idcTypePhysicalReq) throws ParseException {

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		String startDate = idcTypePhysicalReq.getStartDate();
		String endDate = idcTypePhysicalReq.getEndDate();
		Date start = DateUtils.parseDate(startDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		Date end = DateUtils.parseDate(endDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });

		// end = getEndDate(start,end);
		List<String> indexList = DateUtil.getIndexMonthList(start, end, INDEX_HISTORY_DAY_PRE);
		log.info("indexList:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(idcTypePhysicalReq,indexList.toArray(new String[] {})))
				.setExplain(true);

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(start.getTime() / 1000);
		timeRange.lt(end.getTime() / 1000);
		queryBuilder.must(timeRange);
		request.setQuery(queryBuilder);
		request.setSize(0);

		if (StringUtils.isNotBlank(idcTypePhysicalReq.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", idcTypePhysicalReq.getIdcType());
			queryBuilder.must(queryTermId);
		}
		String deviceType = idcTypePhysicalReq.getDeviceType();
		if (StringUtils.isNotBlank(deviceType)) {

			QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", deviceType);
			queryBuilder.must(queryTermId);
		} else {
			log.error("设备利用率设备类型不能为空");
			return null;
		}

		if ("cpu".equalsIgnoreCase(idcTypePhysicalReq.getSourceType())) {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(), "item"));
		} else {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.MEMORY_PUSED.name(), "item"));
		}
		TermsAggregationBuilder termsBuilder;
		termsBuilder = AggregationBuilders.terms("idcType").field("idcType.keyword").size(100);

		TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("host").field("host.keyword").size(100000);
		termsBuilder1.subAggregation(AggregationBuilders.max("avg_used").field(INDEX_VALUE_MAX));
		termsBuilder.subAggregation(termsBuilder1);
		
		
		request.addAggregation(termsBuilder);
		log.info("查询设备利用率es脚本： {}", request);
		return request;
	}

	/**
	 * 资源利用率趋势图
	 */
	@Override
	public Map<String, Object> deviceUsedRateTrends(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("deviceUsedRateTrends").append(JSON.toJSONString(idcTypePhysicalReq));
		String key = Md5Utils.generateCheckCode(sb.toString());
		
		  if (redisTemplate.opsForHash().hasKey(Constants.redisKey, key)) { if (null ==
		  redisTemplate.opsForHash().get(Constants.redisKey, key)) { return null; }
		  return (Map<String, Object>)
		  redisTemplate.opsForHash().get(Constants.redisKey, key); }
		 

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		esAuthHelper.packQueryBuilder(idcTypePhysicalReq.getAuthMap(), queryBuilder);
		
		String startDate = idcTypePhysicalReq.getStartDate();
		String endDate = idcTypePhysicalReq.getEndDate();
		Date start = DateUtils.parseDate(startDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		Date end = DateUtils.parseDate(endDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		// end = getEndDate(start,end);
		List<String> indexList = DateUtil.getIndexList(start, end, INDEX_HISTORY_HOUR_PRE);
		log.info("indexList:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(idcTypePhysicalReq,indexList.toArray(new String[] {})))
				.setExplain(true);

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(start.getTime() / 1000);
		timeRange.lt(end.getTime() / 1000);

		queryBuilder.must(timeRange);

		if (StringUtils.isNotBlank(idcTypePhysicalReq.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", idcTypePhysicalReq.getIdcType());
			queryBuilder.must(queryTermId);
		}
		String deviceType = idcTypePhysicalReq.getDeviceType();
		if (StringUtils.isNotBlank(deviceType)) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", deviceType);
			queryBuilder.must(queryTermId);
		}
		queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
		if(StringUtils.isNotBlank(idcTypePhysicalReq.getSourceType())) {
			if ("cpu".equalsIgnoreCase(idcTypePhysicalReq.getSourceType())) {
				queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(), "item"));
			} else {
				queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.MEMORY_PUSED.name(), "item"));
			}
		}

		if (StringUtils.isNotEmpty(idcTypePhysicalReq.getBizSystem())) {
			queryBuilder.must(QueryBuilders.termsQuery("bizSystem.keyword",
					idcTypePhysicalReq.getBizSystem().trim().split("[\\,,\\，]")));
		}

		request.setQuery(queryBuilder).setSize(0);

		// 时间分组
		String format = "yyyy-MM-dd";
		//DateFormat returndf = null;
		/*
		 * HistogramAggregationBuilder dateAgg =
		 * AggregationBuilders.histogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS)
		 * .field("clock");
		 */
		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		String timeType = idcTypePhysicalReq.getStateTimeType();
		if(StringUtils.isNotBlank(timeType)) {
			if (Constants.TIME_UNIT_DAY.equals(timeType)) {
				// DateHistogramInterval.DAY
				// dateAgg.interval(24 * 60 * 60);
				dateAgg.dateHistogramInterval(DateHistogramInterval.DAY);
				format = "yyyy-MM-dd";

			} else if (Constants.TIME_UNIT_WEEK.equals(timeType)) {
				dateAgg.dateHistogramInterval(DateHistogramInterval.WEEK);
				format = "yyyy-MM-dd";
			} else if (Constants.TIME_UNIT_MONTH.equals(timeType)) {
				dateAgg.dateHistogramInterval(DateHistogramInterval.MONTH);
				format = "yyyy-MM";
			} else {
				throw new NullPointerException("无效的枚举类型");
			}
		}else {
			dateAgg.dateHistogramInterval(DateHistogramInterval.DAY);
		}
		

		dateAgg.format(format).timeZone(DateTimeZone.getDefault());

		
		DateHistogramAggregationBuilder dateAgg1 = AggregationBuilders
				.dateHistogram("hourDateAgg").field("datetime").dateHistogramInterval(DateHistogramInterval.HOUR)
				.format("yyyy-MM-dd HH:mm:ss");
		dateAgg1.subAggregation(
				AggregationBuilders.avg("avg_item").field(INDEX_VALUE));
		//dateAgg.extendedBounds(new ExtendedBounds(start.getTime(), end.getTime()));
		dateAgg.subAggregation(dateAgg1);
		dateAgg.subAggregation(PipelineAggregatorBuilders.avgBucket("avg_buckets", "hourDateAgg.avg_item"));
		dateAgg.subAggregation(PipelineAggregatorBuilders.maxBucket("max_buckets", "hourDateAgg.avg_item"));
		//returndf = new SimpleDateFormat(format);
	
		request.addAggregation(dateAgg);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		if (response.getHits().getTotalHits() == 0) {
			putRedis(null, key);
			return null;
		}
		
		Map<String, Object> tmpMap = new HashMap<String, Object>();
		Histogram h = response.getAggregations().get(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS);
		
		List<Bucket> bucketss = (List<Bucket>) h.getBuckets();
		List<String> intervalTime = new ArrayList<>();
		// List<Object> vaues = new ArrayList<>();

		Map<String, List<Object>> valueMap = new HashMap<>();
		for (Bucket bt : bucketss) {
			// x轴
			if (StringUtils.isNotBlank(bt.getKeyAsString())) {
				intervalTime.add(bt.getKeyAsString());
			}
			InternalSimpleValue avg = bt.getAggregations().get("avg_buckets");
			InternalBucketMetricValue max = bt.getAggregations().get("max_buckets");
			Double avgValue = InstantUtils.getValue(avg);
			Double maxValue = InstantUtils.getValue(max);
			if (valueMap.containsKey("max")) {
				List<Object> avgList = valueMap.get("avg");
				List<Object> maxList = valueMap.get("max");
				avgList.add(avgValue);
				maxList.add(maxValue);
				valueMap.put("max", maxList);
				valueMap.put("avg", avgList);
			} else {
				List<Object> avgList = Lists.newArrayList();
				List<Object> maxList = Lists.newArrayList();
				avgList.add(avgValue);
				maxList.add(maxValue);
				valueMap.put("max", maxList);
				valueMap.put("avg", avgList);
			}
			//getDateAggValues(valueMap, bt);
		}
		tmpMap.put("xAxis", intervalTime);
		tmpMap.put("series", valueMap);

		putRedis(tmpMap, key);
		return tmpMap;
	}
	
	
	
	@Override
	public Map<String, Object> deviceUsedRateTrendsForKG(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("deviceUsedRateTrendsForKG").append(idcTypePhysicalReq.getStartDate())
				.append(idcTypePhysicalReq.getEndDate())
				.append(idcTypePhysicalReq.getIdcType() == null ? "" : idcTypePhysicalReq.getIdcType())
				.append(StringUtils.isEmpty(idcTypePhysicalReq.getSegmentAddr()) ? "" : idcTypePhysicalReq.getSegmentAddr())
				.append(idcTypePhysicalReq.getDeviceType() == null ? "" : idcTypePhysicalReq.getDeviceType())
				.append(idcTypePhysicalReq.getSourceType())
				.append(idcTypePhysicalReq.getBizSystem() == null ? "" : idcTypePhysicalReq.getBizSystem())
				.append(idcTypePhysicalReq.getStateTimeType() == null ? "" : idcTypePhysicalReq.getStateTimeType());
		String key = Md5Utils.generateCheckCode(sb.toString());
		if (redisTemplate.opsForHash().hasKey(Constants.redisKey, key)) {
			if (null == redisTemplate.opsForHash().get(Constants.redisKey, key)) {
				return null;
			}
			return (Map<String, Object>) redisTemplate.opsForHash().get(Constants.redisKey, key);
		}

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		String startDate = idcTypePhysicalReq.getStartDate();
		String endDate = idcTypePhysicalReq.getEndDate();
		Date start = DateUtils.parseDate(startDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		Date end = DateUtils.parseDate(endDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		// end = getEndDate(start,end);
		List<String> indexList = DateUtil.getIndexMonthList(start, end, INDEX_HISTORY_DAY_PRE);
		log.info("indexList:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(idcTypePhysicalReq,indexList.toArray(new String[] {})))
				.setExplain(true);

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(start.getTime() / 1000);
		timeRange.lt(end.getTime() / 1000);

		queryBuilder.must(timeRange);

		if (StringUtils.isNotBlank(idcTypePhysicalReq.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", idcTypePhysicalReq.getIdcType());
			queryBuilder.must(queryTermId);
		}
		// TODO 网段地址的ES指标
		if (StringUtils.isNotBlank(idcTypePhysicalReq.getSegmentAddr())) {
			for (String param : idcTypePhysicalReq.getSegmentAddr().split(",")) {
				queryBuilder.must(QueryBuilders.wildcardQuery("segment_addr.keyword", "*" + QueryParser.escape(param.trim()) + "*"));
			}
		}

		String deviceType = idcTypePhysicalReq.getDeviceType();
		if (StringUtils.isNotBlank(deviceType)) {
			/*
			 * QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword",
			 * idcTypePhysicalReq.getDeviceType()); queryBuilder.must(queryTermId);
			 */

			if (deviceType.equals(vmHost)) {
				QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", new String[] {vmHost});
				queryBuilder.must(queryTermId);

			} else {
				QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", new String[] {phyServer});
				queryBuilder.must(queryTermId);
			}
		} else {
			log.error("设备利用率设备类型不能为空");
			return null;
		}
		if ("cpu".equalsIgnoreCase(idcTypePhysicalReq.getSourceType())) {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(), "item"));
		} else if ("memory".equalsIgnoreCase(idcTypePhysicalReq.getSourceType())) {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.MEMORY_PUSED.name(), "item"));
		} else if ("disk".equalsIgnoreCase(idcTypePhysicalReq.getSourceType())) {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.DISK_PUSED.name(), "item"));
		}
		if (StringUtils.isNotEmpty(idcTypePhysicalReq.getBizSystem())) {
			queryBuilder.must(QueryBuilders.termsQuery("bizSystem.keyword",
					idcTypePhysicalReq.getBizSystem().trim().split("[\\,,\\，]")));
		}

		request.setQuery(queryBuilder).setSize(0);

		// 时间分组
		String format = "";
		DateFormat returndf = null;
		/*
		 * HistogramAggregationBuilder dateAgg =
		 * AggregationBuilders.histogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS)
		 * .field("clock");
		 */
		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		String timeType = idcTypePhysicalReq.getStateTimeType();
		if (Constants.TIME_UNIT_DAY.equals(timeType)) {
			// DateHistogramInterval.DAY
			// dateAgg.interval(24 * 60 * 60);
			dateAgg.dateHistogramInterval(DateHistogramInterval.DAY);
			format = "yyyy-MM-dd";

		} else if (Constants.TIME_UNIT_WEEK.equals(timeType)) {
			dateAgg.dateHistogramInterval(DateHistogramInterval.WEEK);
			format = "yyyy-MM-dd";
		} else if (Constants.TIME_UNIT_MONTH.equals(timeType)) {
			dateAgg.dateHistogramInterval(DateHistogramInterval.MONTH);
			format = "yyyy-MM";
		} else {
			throw new NullPointerException("无效的枚举类型");
		}

		dateAgg.format(format).timeZone(DateTimeZone.getDefault());

		dateAgg.extendedBounds(new ExtendedBounds(start.getTime(), end.getTime()));

		returndf = new SimpleDateFormat(format);
		dateAgg.subAggregation(
				AggregationBuilders.avg(Constants.AGGREGATION_TYPE_MAX + "_item").field(INDEX_VALUE_MAX));
		dateAgg.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(INDEX_VALUE));
		request.addAggregation(dateAgg);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		if (response.getHits().getTotalHits() == 0) {
			putRedis(null, key);
			return null;
		}
		Map<String, Object> tmpMap = new HashMap<String, Object>();
		Histogram h = response.getAggregations().get(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS);
		List<Bucket> bucketss = (List<Bucket>) h.getBuckets();
		List<String> intervalTime = new ArrayList<>();
		// List<Object> vaues = new ArrayList<>();

		Map<String, List<Object>> valueMap = new HashMap<>();
		for (Bucket bt : bucketss) {
			// x轴
			if (StringUtils.isNotBlank(bt.getKeyAsString())) {
				if (bt.getKey().toString().contains("E")) {
					Double dd = (Double) bt.getKey();
					BigDecimal bd1 = new BigDecimal(dd * 1000);
					intervalTime.add(returndf.format(new Date(new Long(bd1.toPlainString()))));
				} else {
					intervalTime.add(bt.getKeyAsString());
				}
			}
			getDateAggValues(valueMap, bt);
		}
		tmpMap.put("xAxis", intervalTime);
		tmpMap.put("series", valueMap);

		putRedis(tmpMap, key);
		return tmpMap;
	}

	private Map<String, List<Object>> getDateAggValues(Map<String, List<Object>> temp, Bucket bt1) {
		Object maxValue = "";
		Object avgValue = "";
		/*
		 * if (count == 0) { return null; } else {
		 */

		Avg max = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_MAX + "_item");// TODO
		Avg avg = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_AVG + "_item");

		if (max != null && !"-Infinity".equals(max.getValueAsString()) && !"NaN".equals(max.getValueAsString())) {
			maxValue = (double) Math.round(max.getValue() * 100) / 100;
		}

		if (avg != null && !"-Infinity".equals(avg.getValueAsString()) && !"NaN".equals(avg.getValueAsString())) {
			avgValue = (double) Math.round(avg.getValue() * 100) / 100;
		}

		if (temp.containsKey("max")) {
			List<Object> avgList = temp.get("avg");
			List<Object> maxList = temp.get("max");
			avgList.add(avgValue);
			maxList.add(maxValue);
			temp.put("max", maxList);
			temp.put("avg", avgList);
		} else {
			List<Object> avgList = Lists.newArrayList();
			List<Object> maxList = Lists.newArrayList();
			avgList.add(avgValue);
			maxList.add(maxValue);
			temp.put("max", maxList);
			temp.put("avg", avgList);
		}

		return temp;
	}

	@Override
	public Map<String, Map<String, Long>> bizSystemDeviceUsedRate(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("bizSystemDeviceUsedRate").append(idcTypePhysicalReq.getStartDate())
				.append(idcTypePhysicalReq.getEndDate())
				.append(idcTypePhysicalReq.getIdcType() == null ? "" : idcTypePhysicalReq.getIdcType())
				.append(idcTypePhysicalReq.getDeviceType() == null ? "" : idcTypePhysicalReq.getDeviceType())
				.append(idcTypePhysicalReq.getSourceType())
				.append(idcTypePhysicalReq.getBizSystem() == null ? "" : idcTypePhysicalReq.getBizSystem());
		String key = Md5Utils.generateCheckCode(sb.toString());
		if (redisTemplate.opsForHash().hasKey(Constants.redisKey, key)) {
			return (Map<String, Map<String, Long>>) redisTemplate.opsForHash().get(Constants.redisKey, key);
		}

		Map<String, Map<String, Long>> idcTypeMap = new HashMap<>();
		SearchRequestBuilder request = initeBizSystemRequst(idcTypePhysicalReq);
		if (null == request) {
			putRedis(idcTypeMap, key);
			return idcTypeMap;
		}
		SearchResponse response = request.get();
		if (null == response.getAggregations()) {
			putRedis(idcTypeMap, key);
			return idcTypeMap;
		}
		final Terms terms = response.getAggregations().get("colType");
		for (final Terms.Bucket a : terms.getBuckets()) {
			String col = a.getKeyAsString();
			final Terms termsHost = a.getAggregations().get("host");
			for (final Terms.Bucket aa : termsHost.getBuckets()) {
				Max max = aa.getAggregations().get("avg_used");
				formCountVal(max,  col,idcTypeMap);
			}
			Map<String, Long> val = idcTypeMap.get(col);
			if(null!=val) {
				val.put("count", val.get("15")+ val.get("15-40")+ val.get("40-80")+ val.get("80"));
			}
		}

		putRedis(idcTypeMap, key);
		return idcTypeMap;
	}

	public SearchRequestBuilder initeBizSystemRequst(IdcTypePhysicalReq idcTypePhysicalReq) throws ParseException {
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		String startDate = idcTypePhysicalReq.getStartDate();
		String endDate = idcTypePhysicalReq.getEndDate();
		Date start = DateUtils.parseDate(startDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		Date end = DateUtils.parseDate(endDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		// end = getEndDate(start,end);
		List<String> indexList = DateUtil.getIndexMonthList(start, end, INDEX_HISTORY_DAY_PRE);
		log.info("indexList:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(idcTypePhysicalReq,indexList.toArray(new String[] {})))
				.setExplain(true);

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(start.getTime() / 1000);
		timeRange.lt(end.getTime() / 1000);
		queryBuilder.must(timeRange);

		if (StringUtils.isNotBlank(idcTypePhysicalReq.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", idcTypePhysicalReq.getIdcType());
			queryBuilder.must(queryTermId);
		}
		if (StringUtils.isNotEmpty(idcTypePhysicalReq.getBizSystem())) {
			queryBuilder.must(QueryBuilders.termsQuery("bizSystem.keyword",
					idcTypePhysicalReq.getBizSystem().trim().split("[\\,,\\，]")));
		}

		String deviceType = idcTypePhysicalReq.getDeviceType();
		if (StringUtils.isNotBlank(deviceType)) {
			/*
			 * QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword",
			 * idcTypePhysicalReq.getDeviceType()); queryBuilder.must(queryTermId);
			 */

			if (deviceType.equals("云主机")) {
				QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", new String[] { "云主机" });
				queryBuilder.must(queryTermId);

			} else {
				QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", new String[] { "X86服务器" });
				queryBuilder.must(queryTermId);
			}
		} else {
			log.error("设备利用率设备类型不能为空");
			return null;
		}

		if ("cpu".equalsIgnoreCase(idcTypePhysicalReq.getSourceType())) {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(), "item"));
		} else {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.MEMORY_PUSED.name(), "item"));
		}
		if (StringUtils.isNotEmpty(idcTypePhysicalReq.getBizSystem())) {
			queryBuilder.must(QueryBuilders.termsQuery("bizSystem.keyword",
					idcTypePhysicalReq.getBizSystem().trim().split("[\\,,\\，]")));
		}

		request.setQuery(queryBuilder);
		request.setSize(0);

		TermsAggregationBuilder termsBuilder;
		termsBuilder = AggregationBuilders.terms("colType").field("department1.keyword").size(100);

		TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("host").field("host.keyword").size(100000);
		termsBuilder1.subAggregation(AggregationBuilders.max("avg_used").field(INDEX_VALUE_MAX));
		termsBuilder.subAggregation(termsBuilder1);


		request.addAggregation(termsBuilder);
		log.info("查询设备利用率es脚本： {}", request);
		return request;
	}

	@Override
	public List<Map<String, Object>> bizSystemDeviceUsedRateLow(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("bizSystemDeviceUsedRateLow").append(idcTypePhysicalReq.getStartDate())
				.append(idcTypePhysicalReq.getEndDate())
				.append(idcTypePhysicalReq.getIdcType() == null ? "" : idcTypePhysicalReq.getIdcType())
				.append(idcTypePhysicalReq.getDeviceType() == null ? "" : idcTypePhysicalReq.getDeviceType())
				.append(idcTypePhysicalReq.getSourceType()).append(idcTypePhysicalReq.getStateType());
		String key = Md5Utils.generateCheckCode(sb.toString());
		if (redisTemplate.opsForHash().hasKey(Constants.redisKey, key)) {
			if (null == redisTemplate.opsForHash().get(Constants.redisKey, key)) {
				return null;
			}
			return (List<Map<String, Object>>) redisTemplate.opsForHash().get(Constants.redisKey, key);
		}

		List<Map<String, Object>> bizSystemList = Lists.newArrayList();
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		String startDate = idcTypePhysicalReq.getStartDate();
		String endDate = idcTypePhysicalReq.getEndDate();
		Date start = DateUtils.parseDate(startDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		Date end = DateUtils.parseDate(endDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		// end = getEndDate(start,end);

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");

		List<String> indexList = DateUtil.getIndexList(start, end, INDEX_HISTORY_HOUR_PRE);
		log.info("indexList:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(idcTypePhysicalReq,indexList.toArray(new String[] {})))
				.setExplain(true);

		timeRange.gte(start.getTime() / 1000);
		timeRange.lt(end.getTime() / 1000);
		queryBuilder.must(timeRange);

		if (StringUtils.isNotBlank(idcTypePhysicalReq.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", idcTypePhysicalReq.getIdcType());
			queryBuilder.must(queryTermId);
		}

		String deviceType = idcTypePhysicalReq.getDeviceType();
		if (StringUtils.isNotBlank(deviceType)) {
			/*
			 * QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword",
			 * idcTypePhysicalReq.getDeviceType()); queryBuilder.must(queryTermId);
			 */

			if (deviceType.equals("云主机")) {
				QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", new String[] { "云主机" });
				queryBuilder.must(queryTermId);

			} else {
				QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", new String[] { "X86服务器" });
				queryBuilder.must(queryTermId);
			}
		} else {
			log.error("设备利用率设备类型不能为空");
			return null;
		}
		if ("cpu".equalsIgnoreCase(idcTypePhysicalReq.getSourceType())) {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(), "item"));
		} else {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.MEMORY_PUSED.name(), "item"));
		}
		if (StringUtils.isNotEmpty(idcTypePhysicalReq.getBizSystem())) {
			queryBuilder.must(QueryBuilders.termsQuery("bizSystem.keyword",
					idcTypePhysicalReq.getBizSystem().trim().split("[\\,,\\，]")));
		}
		
		queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
		request.setQuery(queryBuilder);
		request.setSize(0);

		TermsAggregationBuilder termsBuilder;
		String statType = "avg_used";
		if (idcTypePhysicalReq.getStateType().equals("max")) {
			statType = "max_used";
		}
		termsBuilder = AggregationBuilders.terms("colType").field("bizSystem.keyword").size(100000);

		// 时间分组
		String format = "yyyy-MM-dd HH:mm:ss";
		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
		dateAgg.format(format);// .timeZone(DateTimeZone.getDefault());

		dateAgg.extendedBounds(new ExtendedBounds(start.getTime(), end.getTime()));
		dateAgg.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(INDEX_VALUE));
		termsBuilder.subAggregation(dateAgg);

		List<FieldSortBuilder> sorts = Lists.newArrayList();
		FieldSortBuilder ss = new FieldSortBuilder(statType);
		ss.order(SortOrder.ASC);
		
		sorts.add(ss);
		termsBuilder.subAggregation(PipelineAggregatorBuilders.bucketSort("r_bucket_sort", sorts).from(0).size(5));

		if (idcTypePhysicalReq.getStateType().equals("avg")) {
			termsBuilder.subAggregation(PipelineAggregatorBuilders.avgBucket(statType, "dateagg.avg_item"));
		} else {
			termsBuilder.subAggregation(PipelineAggregatorBuilders.maxBucket(statType, "dateagg.avg_item"));
		}
		request.addAggregation(termsBuilder);
		log.info("查询设备利用率es脚本： {}", request);

		SearchResponse response = request.get();

		if (null == response.getAggregations()) {
			putRedis(bizSystemList, key);
			return bizSystemList;
		}
		// if (response.getHits().getTotalHits() > 0L) {
		final Terms terms = response.getAggregations().get("colType");
		for (final Terms.Bucket a : terms.getBuckets()) {
			Map<String, Object> result = Maps.newHashMap();
			double value = 0;
			if (idcTypePhysicalReq.getStateType().equals("avg")) {
				InternalSimpleValue avg = a.getAggregations().get(statType);
				if(null!=avg) {
					value = avg.value();
				}
				
			} else {
				InternalBucketMetricValue max = a.getAggregations().get(statType);
				if(null!=max) {
					value = max.value();
				}
				
			}
			value = (double)Math.round(value * 100) / 100;
			result.put("bizSystem", String.valueOf(a.getKey()));
			result.put("value", value);
			// result.put(String.valueOf(a.getKey()), value);
			bizSystemList.add(result);
		}
		// }

		putRedis(bizSystemList, key);
		return bizSystemList;
	}

	@Override
	public List<Map<String, Object>> bizSystemDeviceUsedRateForKG(@RequestBody IdcTypePhysicalReq idcTypePhysicalReq)
			throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("bizSystemDeviceUsedRateForKG")
				.append(idcTypePhysicalReq.getStartDate())
				.append(idcTypePhysicalReq.getEndDate())
				.append(StringUtils.isEmpty(idcTypePhysicalReq.getSegmentAddr()) ? "" : idcTypePhysicalReq.getSegmentAddr())
				.append(idcTypePhysicalReq.getDeviceType() == null ? "" : idcTypePhysicalReq.getDeviceType())
				.append(idcTypePhysicalReq.getSourceType())
				.append(idcTypePhysicalReq.getStateType());
		String key = Md5Utils.generateCheckCode(sb.toString());
		if (redisTemplate.opsForHash().hasKey(Constants.redisKey, key)) {
			if (null == redisTemplate.opsForHash().get(Constants.redisKey, key)) {
				return new ArrayList<Map<String, Object>>();
			}
			return (List<Map<String, Object>>) redisTemplate.opsForHash().get(Constants.redisKey, key);
		}

		List<Map<String, Object>> bizSystemList = Lists.newArrayList();
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		String startDate = idcTypePhysicalReq.getStartDate();
		String endDate = idcTypePhysicalReq.getEndDate();
		Date start = DateUtils.parseDate(startDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		Date end = DateUtils.parseDate(endDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		// end = getEndDate(start,end);

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");

		List<String> indexList = DateUtil.getIndexList(start, end, INDEX_HISTORY_HOUR_PRE);
		log.info("indexList:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(idcTypePhysicalReq,indexList.toArray(new String[] {})))
				.setExplain(true);

		timeRange.gte(start.getTime() / 1000);
		timeRange.lt(end.getTime() / 1000);
		queryBuilder.must(timeRange);

		// TODO 网段地址的ES指标
		if (StringUtils.isNotBlank(idcTypePhysicalReq.getSegmentAddr())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("segment_addr.keyword", idcTypePhysicalReq.getSegmentAddr());
			queryBuilder.must(queryTermId);
		}

		String deviceType = idcTypePhysicalReq.getDeviceType();
		if (StringUtils.isNotBlank(deviceType)) {
			/*
			 * QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword",
			 * idcTypePhysicalReq.getDeviceType()); queryBuilder.must(queryTermId);
			 */

			if (deviceType.equals(vmHost)) {
				QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", new String[] {vmHost});
				queryBuilder.must(queryTermId);

			} else if (deviceType.equals(phyServer)) {
				QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", new String[] { phyServer });
				queryBuilder.must(queryTermId);
			}
		} else {
			log.error("设备利用率设备类型不能为空");
			return new ArrayList<Map<String, Object>>();
		}

		if ("cpu".equalsIgnoreCase(idcTypePhysicalReq.getSourceType())) {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(), "item"));
		} else if ("disk".equalsIgnoreCase(idcTypePhysicalReq.getSourceType())) {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.DISK_PUSED.name(), "item"));
		} else if ("memory".equalsIgnoreCase(idcTypePhysicalReq.getSourceType())) {
			queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.MEMORY_PUSED.name(), "item"));
		}
		if (StringUtils.isNotEmpty(idcTypePhysicalReq.getBizSystem())) {
			queryBuilder.must(QueryBuilders.termsQuery("bizSystem.keyword",
					idcTypePhysicalReq.getBizSystem().trim().split("[\\,,\\，]")));
		}

		request.setQuery(queryBuilder);
		request.setSize(0);

		TermsAggregationBuilder termsBuilder;
		String statType = "avg_used";
		if (idcTypePhysicalReq.getStateType().equals("max")) {
			statType = "max_used";
		}
		termsBuilder = AggregationBuilders.terms("colType").field("bizSystem.keyword").size(100000);

		// 时间分组
		String format = "yyyy-MM-dd HH:mm:ss";
		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
		dateAgg.format(format);// .timeZone(DateTimeZone.getDefault());

		dateAgg.extendedBounds(new ExtendedBounds(start.getTime(), end.getTime()));
		dateAgg.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(INDEX_VALUE));
		termsBuilder.subAggregation(dateAgg);

		List<FieldSortBuilder> sorts = Lists.newArrayList();
		FieldSortBuilder ss = new FieldSortBuilder(statType);
		ss.order(SortOrder.ASC);

		sorts.add(ss);
		termsBuilder.subAggregation(PipelineAggregatorBuilders.bucketSort("r_bucket_sort", sorts).from(0).size(5));

		if (idcTypePhysicalReq.getStateType().equals("avg")) {
			termsBuilder.subAggregation(PipelineAggregatorBuilders.avgBucket(statType, "dateagg.avg_item"));
		} else {
			termsBuilder.subAggregation(PipelineAggregatorBuilders.maxBucket(statType, "dateagg.avg_item"));
		}
		request.addAggregation(termsBuilder);
		log.info("查询设备利用率es脚本： {}", request);

		SearchResponse response = request.get();

		if (null == response.getAggregations()) {
			putRedis(bizSystemList, key);
			return bizSystemList;
		}
		// if (response.getHits().getTotalHits() > 0L) {
		final Terms terms = response.getAggregations().get("colType");
		for (final Terms.Bucket a : terms.getBuckets()) {
			Map<String, Object> result = Maps.newHashMap();
			double value = 0;
			if (idcTypePhysicalReq.getStateType().equals("avg")) {
				InternalSimpleValue avg = a.getAggregations().get(statType);
				if(null!=avg) {
					value = avg.value();
				}

			} else {
				InternalBucketMetricValue max = a.getAggregations().get(statType);
				if(null!=max) {
					value = max.value();
				}

			}
			value = (double)Math.round(value * 100) / 100;
			result.put("bizSystem", String.valueOf(a.getKey()));
			result.put("value", value);
			// result.put(String.valueOf(a.getKey()), value);
			bizSystemList.add(result);
		}
		// }

		putRedis(bizSystemList, key);
		return bizSystemList;
	}

	@Override
	public Map<String, Map<String, Long>> bizSystemDeviceUsedRateTopN(
			@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "groupCol") String groupCol, @RequestParam(value = "order") String order)
			throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("bizSystemDeviceUsedRateTopN").append(startDate).append(endDate).append(groupCol).append(order);
		String key = Md5Utils.generateCheckCode(sb.toString());

		if (redisTemplate.opsForHash().hasKey(Constants.redisKey, key)) {
			if (null == redisTemplate.opsForHash().get(Constants.redisKey, key)) {
				return null;
			}
			return (Map<String, Map<String, Long>>) redisTemplate.opsForHash().get(Constants.redisKey, key);
		}

		Map<String, Map<String, Long>> idcTypeMap = new LinkedHashMap<>();
		SearchRequestBuilder request = initeBizSystemRequstTopN(startDate, endDate, size, groupCol, order);
		if (null == request) {
			putRedis(idcTypeMap, key);
			return idcTypeMap;
		}
		SearchResponse response = request.get();
		if (null == response.getAggregations()) {
			putRedis(idcTypeMap, key);
			return idcTypeMap;
		}
		final Terms terms = response.getAggregations().get("colType");
		
		for (final Terms.Bucket a : terms.getBuckets()) {
			String col = a.getKeyAsString();
			final Terms termsHost = a.getAggregations().get("host");
			for (final Terms.Bucket aa : termsHost.getBuckets()) {
				Max max = aa.getAggregations().get("avg_used");
				formCountVal(max,  col,idcTypeMap);
			}
			Map<String, Long> val = idcTypeMap.get(col);
			if(null!=val) {
				val.put("count", val.get("15")+ val.get("15-40")+ val.get("40-80")+ val.get("80"));
			}
			
			/*
			 * String idcTypeKey = a.getKeyAsString(); Stats stats =
			 * a.getAggregations().get("stats_buckets"); long count = stats.getCount();
			 * Map<String, Long> rangeMap = new HashMap<>(); rangeMap.put("15-40", count);
			 * idcTypeMap.put(idcTypeKey, rangeMap);
			 * 
			 * Stats stats1 = a.getAggregations().get("stats_buckets1"); long count1 =
			 * stats1.getCount(); rangeMap.put("40-80", count1);
			 * 
			 * Stats stats2 = a.getAggregations().get("stats_buckets2"); long count2 =
			 * stats2.getCount(); rangeMap.put("80", count2);
			 * 
			 * Stats stats3 = a.getAggregations().get("stats_buckets3"); long count3 =
			 * stats3.getCount(); rangeMap.put("15", count3);
			 * 
			 * long sum = count + count1 + count2 + count3; rangeMap.put("count", sum);
			 */
		}
		putRedis(idcTypeMap, key);
		return idcTypeMap;
	}
	
	public void formCountVal(Max max, String item, Map<String, Map<String, Long>> map) {
		if (max == null || "-Infinity".equals(max.getValueAsString()) || "NaN".equals(max.getValueAsString())) {
			return;
		}
		Map<String, Long> val = map.get(item);
		if(null==val) {
			val = Maps.newHashMap();
			val.put("15", 0l);
			val.put("15-40", 0l);
			val.put("40-80", 0l);
			val.put("80", 0l);
			map.put(item, val);
		}
		double maxVal = max.getValue();
		if (maxVal < 15) {
			String keyStr = "15";
			long count = val.get(keyStr);
			count++;
			val.put(keyStr, count);
		}
		if (15 <= maxVal && maxVal < 40) {
			String keyStr = "15-40";
			long count = val.get(keyStr);
			count++;
			val.put(keyStr, count);
		}
		if (40 <= maxVal && maxVal < 80) {
			String keyStr = "40-80";
			long count = val.get(keyStr);
			count++;
			val.put(keyStr, count);
		}
		if (maxVal >= 80) {
			String keyStr =  "80";
			long count = val.get(keyStr);
			count++;
			val.put(keyStr, count);
		}
	}

	public SearchRequestBuilder initeBizSystemRequstTopN(String startDate, String endDate, Integer size,
			String groupCol, String order) throws ParseException {
		// 设置查询条件

		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		Date start = DateUtils.parseDate(startDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		Date end = DateUtils.parseDate(endDate + " 00:00:00", new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" });
		// end = getEndDate(start,end);

		List<String> indexList = DateUtil.getIndexMonthList(start, end, INDEX_HISTORY_DAY_PRE);
		log.info("indexList:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,indexList.toArray(new String[] {})))
				.setExplain(true);

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(start.getTime() / 1000);
		timeRange.lt(end.getTime() / 1000);
		queryBuilder.must(timeRange);

		QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", new String[] { "X86服务器", "云主机" });
		queryBuilder.must(queryTermId);

		queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(), "item"));

		request.setQuery(queryBuilder);
		request.setSize(0);

		TermsAggregationBuilder termsBuilder;
		termsBuilder = AggregationBuilders.terms("colType").field(groupCol + ".keyword");
		if (order.equals("asc")) {
			termsBuilder.order(BucketOrder.aggregation("avg_sort", true));
		} else {
			termsBuilder.order(BucketOrder.aggregation("avg_sort", false));
		}
		if (null != size) {
			termsBuilder.size(size);
		}

		termsBuilder.subAggregation(AggregationBuilders.avg("avg_sort").field(INDEX_VALUE));

		TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("host").field("host.keyword").size(100000);
		
		termsBuilder1.subAggregation(AggregationBuilders.max("avg_used").field(INDEX_VALUE_MAX));
		termsBuilder.subAggregation(termsBuilder1);

		
		request.addAggregation(termsBuilder);
		log.info("查询设备利用率es脚本： {}", request);
		return request;
	}

	public Date getEndDate(Date start, Date end) {
		Calendar c = Calendar.getInstance();
		c.setTime(start);
		c.add(Calendar.DATE, +7);
		Date d = c.getTime();
		if (d.getTime() > end.getTime()) {
			return end;
		} else {
			return d;
		}
	}

	/**
	 * 资源池-网络带宽资源
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> storageUsedRateForKG(@RequestBody Map<String,String> param) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("blockAvg", 0);
		map.put("blockMax", 0);
		map.put("storage", 0);
		Date start = null;
		Date end = null;
		String startDate = param.get("startDate");
		long l = System.currentTimeMillis();
		if (StringUtils.isNotEmpty(startDate)) {
			try {
				start = DateUtils.parseDate(startDate, new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"});
			} catch (ParseException e) {
				start = new Date(l - 24 * 3600 * 1000);
			}
		} else {
			start = new Date(l - 24 * 3600 * 1000);
		}
		String endDate = param.get("endDate");
		if (StringUtils.isNotEmpty(endDate)) {
			try {
				end = DateUtils.parseDate(endDate, new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"});
			} catch (ParseException e) {
				end = new Date(l);
			}
		} else {
			end = new Date(l);
		}
		List<String> indexList = DateUtil.getIndexMonthList(start, end, INDEX_HISTORY_HOUR_PRE);
		List<String> indexuintList = DateUtil.getIndexMonthList(start, end, INDEX_HISTORY_HOUR_UINT_PRE);
		String key = Md5Utils.generateCheckCode(startDate + endDate);
		getSanPused(map, indexList, key);
		getSanTotal(map, indexuintList, key);
		return map;
	}

	/**
	 * 获取san存储总量
	 * @param map
	 * @param indexList
	 */
	private void getSanTotal (Map<String, Object> map, List<String> indexList, String key) {
		if (redisTemplate.opsForHash().hasKey(Constants.redisKey, Constants.REDIS_KEY_SAN_TOTAL + key)) {
			map.put("san",redisTemplate.opsForHash().get(Constants.redisKey, Constants.REDIS_KEY_SAN_TOTAL + key));
			return ;
		}

		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,indexList.toArray(new String[] {})))
				.setExplain(true);

		String format = "yyyy-MM-dd HH:mm:ss";
		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram("datetime").field("datetime");
		dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
		dateAgg.format(format);

//		dateAgg.extendedBounds(new ExtendedBounds(start.getTime(), end.getTime()));
		dateAgg.subAggregation(AggregationBuilders.sum("total").field(INDEX_VALUE));
		request.setQuery(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.SAN_STORAGE_TOTAL.name(), "item")).addAggregation(dateAgg).setSize(0);
		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.execute().actionGet();
		Aggregations aggregations = response.getAggregations();
		List<Map<String, Object>> list = Lists.newArrayList();
		if (aggregations == null) {
			log.warn("aggregations are null!");
			map.put("san",0);
			return ;
		}
		List<Aggregation> aggregationList = aggregations.asList();
		for (Aggregation aggregation : aggregationList) {
			String s = aggregation.toString();
			JSONObject jsonObject = JSON.parseObject(s);
			generateAggs(jsonObject, list, null);
		}

		if (list.size() == 0) {
			map.put("san",0);
		} else {
			Object total = list.get(0).get("value");
			Double d = Double.parseDouble(total == null ? "0" : total.toString());
			// 转换单位b --> tb
			d = d / 1024 / 1024 / 1024 / 1024;
			d = (double)Math.round(d * 100)/ 100;
			map.put("san",d);
			redisTemplate.opsForHash().put(Constants.redisKey, Constants.REDIS_KEY_SAN_TOTAL + key, d);
			redisTemplate.expire(Constants.redisKey, Constants.redisTimeOut, TimeUnit.HOURS);
		}
	}

	/**
	 * 获取san存储总量
	 * @param map
	 * @param indexList
	 */
	private void getSanPused (Map<String, Object> map, List<String> indexList, String key) {
		if (redisTemplate.opsForHash().hasKey(Constants.redisKey, Constants.REDIS_KEY_SAN_PUSED_M + key)) {
			map.put("sanMax",redisTemplate.opsForHash().get(Constants.redisKey, Constants.REDIS_KEY_SAN_PUSED_M + key));
			if (redisTemplate.opsForHash().hasKey(Constants.redisKey, Constants.REDIS_KEY_SAN_PUSED_A + key)) {
				map.put("sanAvg",redisTemplate.opsForHash().get(Constants.redisKey, Constants.REDIS_KEY_SAN_PUSED_A + key));
			} else {
				map.put("sanAvg",0);
			}
			return ;
		}

		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,indexList.toArray(new String[] {})))
				.setExplain(true);

		String format = "yyyy-MM-dd HH:mm:ss";
		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram("datetime").field("datetime");
		dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
		dateAgg.format(format);
		dateAgg.subAggregation(AggregationBuilders.avg("avg1").field(INDEX_VALUE));
		StatsBucketPipelineAggregationBuilder pipelineAggregationBuilder = PipelineAggregatorBuilders.statsBucket("stats_buckets2", "datetime.avg1");

		request.setQuery(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.SAN_STORAGE_PUSED.name(), "item")).addAggregation(dateAgg).addAggregation(pipelineAggregationBuilder).setSize(0);
		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.execute().actionGet();
				Aggregations aggregations = response.getAggregations();
		List<Map<String, Object>> list = Lists.newArrayList();
		if (aggregations == null) {
			log.warn("aggregations are null!");
			map.put("san",0);
			return ;
		}
		List<Aggregation> aggregationList = aggregations.asList();
		for (Aggregation aggregation : aggregationList) {
			String s = aggregation.toString();
			JSONObject jsonObject = JSON.parseObject(s);
			generateAggs(jsonObject, list, null);
		}

		if (list.size() != 0) {
			log.info("translate result are : {}", list);
			for (Map<String, Object> listMap: list) {
				if (listMap.containsKey("datetime")) {
					continue;
				}
				Object avg = listMap.get("avg");
				Double avgD = Double.parseDouble(avg == null ? "0" : avg.toString());
				avgD = (double)Math.round(avgD * 100)/ 100;
				map.put("sanAvg",avgD);
				Object max = listMap.get("max");
				Double avgM = Double.parseDouble(max == null ? "0" : max.toString());
				avgM = (double)Math.round(avgM * 100)/ 100;
				map.put("sanMax",avgM);
				redisTemplate.opsForHash().put(Constants.redisKey, Constants.REDIS_KEY_SAN_PUSED_M + key, avgM);
				redisTemplate.opsForHash().put(Constants.redisKey, Constants.REDIS_KEY_SAN_PUSED_A + key, avgD);
				redisTemplate.expire(Constants.redisKey, Constants.redisTimeOut, TimeUnit.HOURS);
				break;
			}
		}

		if (!map.containsKey("sanAvg")) {
			map.put("sanAvg",0);
		}

		if (!map.containsKey("sanAvg")) {
			map.put("sanMax",0);
		}
	}

    /**
     * 存储资源使用总览
     * @param param
     * @return
     */
    public Map<String, Object> storageUseView(@RequestBody Map<String,String> param) {
        Map<String, Object> map = Maps.newHashMap();
        Date start = null;
        Date end = null;
        String startDate = param.get("startDate");
        long l = System.currentTimeMillis();
        if (StringUtils.isNotEmpty(startDate)) {
            try {
                start = DateUtils.parseDate(startDate, new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"});
            } catch (ParseException e) {
                start = new Date(l - 24 * 3600 * 1000);
            }
        } else {
            start = new Date(l - 24 * 3600 * 1000);
        }
        String endDate = param.get("endDate");
        if (StringUtils.isNotEmpty(endDate)) {
            try {
                end = DateUtils.parseDate(endDate, new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"});
            } catch (ParseException e) {
                end = new Date(l);
            }
        } else {
            end = new Date(l);
        }
        String deviceMrfs = param.get("deviceMrfs");
        // 获取缓存数据
        String key = Md5Utils.generateCheckCode(startDate + endDate + deviceMrfs);
        if (redisTemplate.opsForHash().hasKey(Constants.redisKey, Constants.REDIS_KEY_STORAGE_TREND + key)) {
            Map<String, Object> o = (Map<String, Object>)redisTemplate.opsForHash().get(Constants.redisKey, Constants.REDIS_KEY_STORAGE_TREND + key);
            return o;
        }
        List<String> indexuintList = DateUtil.getIndexMonthList(start, end, INDEX_HISTORY_HOUR_UINT_PRE);
        Map<String, Object> total = getStorageTrends(indexuintList, deviceMrfs, KpiTypeEnum.SAN_STORAGE_TOTAL.name());
        Map<String, Object> series = Maps.newHashMap();
        if (!total.isEmpty()) {
            map.put("xAxis", total.get("xAxis"));
            series.put("sum_count", total.get("result"));
        }
        Map<String, Object> used = getStorageTrends(indexuintList, deviceMrfs, KpiTypeEnum.SAN_STORAGE_USED.name());
        if (!used.isEmpty()) {
            map.put("xAxis", used.get("xAxis"));
            series.put("use_count", used.get("result"));
        }
        if (!series.isEmpty()) {
            map.put("series",series);
            redisTemplate.opsForHash().put(Constants.redisKey, Constants.REDIS_KEY_STORAGE_TREND + key, map);
            redisTemplate.expire(Constants.redisKey, Constants.redisTimeOut, TimeUnit.HOURS);
        } else {
            map.put("xAxis",new ArrayList<>());
            series.put("use_count",new ArrayList<>());
            series.put("sum_count",new ArrayList<>());
            map.put("series",series);
        }
        return map;
    }

    /**
     * 获取san存储总量趋势
     * @param indexList
     * @param indexList
     */
    private Map<String, Object> getStorageTrends (List<String> indexList, String deviceMrfs, String kpiType) {
        Map<String, Object> map = Maps.newHashMap();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(deviceMrfs)) {
            QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("device_mfrs", deviceMrfs);
            queryBuilder.must(queryTermId);
        }
        queryBuilder.must(iKpiKeyConfigBiz.query4Builder(kpiType, "item"));
        SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,indexList.toArray(new String[] {})))
                .setExplain(true);

        String format = "yyyy-MM-dd HH:mm:ss";
		DateHistogramAggregationBuilder dateAgg1 = AggregationBuilders
				.dateHistogram("datetime1").field("datetime");
		dateAgg1.dateHistogramInterval(DateHistogramInterval.days(1)).timeZone(DateTimeZone.getDefault());
		dateAgg1.format(format);

        DateHistogramAggregationBuilder dateAgg2 = AggregationBuilders
                .dateHistogram("datetime2").field("datetime");
		dateAgg2.dateHistogramInterval(DateHistogramInterval.hours(1)).timeZone(DateTimeZone.getDefault());
		dateAgg2.format(format);
		dateAgg2.subAggregation(AggregationBuilders.sum("total").field(INDEX_VALUE));
		dateAgg1.subAggregation(dateAgg2);
		dateAgg1.subAggregation(PipelineAggregatorBuilders.avgBucket("avg_bucket", "datetime2.total"));
        request.setQuery(queryBuilder).addAggregation(dateAgg1).setSize(0);
        log.info("查询设备利用率es脚本： {}", request);
        SearchResponse response = request.execute().actionGet();
		Aggregations aggregations = response.getAggregations();
		List<Map<String, Object>> list = Lists.newArrayList();
		if (aggregations == null) {
			log.warn("aggregations are null!");
			return map;
		}
		List<Aggregation> aggregationList = aggregations.asList();
		for (Aggregation aggregation : aggregationList) {
			String s = aggregation.toString();
			JSONObject jsonObject = JSON.parseObject(s);
			generateAggs(jsonObject, list, null);
		}
        List<Double> totalList = Lists.newArrayList();
        List<Object> xAxisList = Lists.newArrayList();
        if (list.isEmpty()) {
            return map;
        }
        for (Map<String, Object> resultMap: list) {
        	if (resultMap.containsKey("datetime2")) {
        		continue;
			}
            Object total = resultMap.get("value");
            Double d = Double.parseDouble(total == null ? "0" : total.toString());
            // 转换单位b --> tb
            d = d / 1024 / 1024 / 1024 / 1024;
            d = (double)Math.round(d * 100)/ 100;
            totalList.add(d);
            xAxisList.add(resultMap.get("key_as_string"));
        }
        map.put("result", totalList);
        map.put("xAxis", xAxisList);
        return map;
    }

	/**
	 * 实时资源利用率topN
	 * @param kpi
	 * @return
	 */
	public List<Map<String, Object>> devicePusedTopN(@PathVariable("kpi") String kpi,
													 @RequestBody DevicePusedTopN devicePusedTopN) {
		int size = devicePusedTopN.getSize();
		String idcType = devicePusedTopN.getIdcType();
		List<String> resourceIds = devicePusedTopN.getResourceIds();
		// 防止恶意使用，最大支持100条
		if (size > 100) {
			size = 100;
		}
		DateFormat returndf = new SimpleDateFormat("yyyyMMdd");
		long l = System.currentTimeMillis();
//		String index = "history-*"+returndf.format(System.currentTimeMillis() - 8 * 3600 * 1000);
		String index = "history-*"+returndf.format(System.currentTimeMillis());
		index +="*";
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,index))
				.setExplain(true);

		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.must(iKpiKeyConfigBiz.query4Builder(kpi, "item"));
		if (!CollectionUtils.isEmpty(resourceIds)) {
			queryBuilder.must(QueryBuilders.termsQuery("resourceId.keyword", resourceIds.toArray()));
		} else {
			if (StringUtils.isNotEmpty(idcType)) {
				QueryBuilder queryIdcType = QueryBuilders.matchPhraseQuery("idcType", idcType);
				queryBuilder.must(queryIdcType);
			}
			queryBuilder.must(QueryBuilders.matchPhraseQuery("deviceType", phyServer));
		}
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("datetime");
		rangeQueryBuilder.lte(l).gte(l-3600000);
		queryBuilder.must(rangeQueryBuilder);
		TermsAggregationBuilder termsBuilder;
		termsBuilder = AggregationBuilders.terms("resourceId").field("resourceId.keyword").size(size);
		termsBuilder.order(BucketOrder.aggregation("kpi_value", false));
		termsBuilder.subAggregation(AggregationBuilders.avg("kpi_value").field("value"));
		request.addAggregation(termsBuilder).setQuery(queryBuilder).setSize(0);
		log.info("查询设备利用率TOPN es索引： {}", Arrays.asList(request.request().indices()));
		log.info("查询设备利用率TOPN es脚本： {}", request.toString());
		SearchResponse response = request.execute().actionGet();
		Aggregations aggregations = response.getAggregations();
		List<Map<String, Object>> list = Lists.newArrayList();
		if (aggregations == null) {
			log.warn("aggregations are null!");
			return list;
		}
		List<Aggregation> aggregationList = aggregations.asList();
		for (Aggregation aggregation : aggregationList) {
			String s = aggregation.toString();
			JSONObject jsonObject = JSON.parseObject(s);
			generateAggs(jsonObject, list, null);
		}
		for (Map<String, Object> map: list) {
			Float kpi_value = MapUtils.getFloat(map, "value");
			if (kpi_value == null ) {
				continue;
			}
			map.put("value", (float)Math.round(kpi_value * 100)/ 100);
		}
		return list;
	}

	/**
	 * 根据resourceId和key获取实时监控数据列表
	 * @param paramsList
	 * @return
	 */
	public List<Map<String, Object>> getKpiListByKey (@RequestBody List<Map<String, Object>> paramsList) {
		if (CollectionUtils.isEmpty(paramsList)) {
			return Lists.newArrayList();
		}
		DateFormat returndf = new SimpleDateFormat("yyyyMMdd");
		long l = System.currentTimeMillis();;
//		String timeFormat = returndf.format(System.currentTimeMillis() - 8 * 3600 * 1000);
		String timeFormat = returndf.format(System.currentTimeMillis());
		String index = "history*"+timeFormat;
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,index))
				.setExplain(true);

		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		BoolQueryBuilder queryBuilder1 = QueryBuilders.boolQuery();
		for (Map<String, Object> map: paramsList) {
			Object item = map.get("item");
			Object resourceId = map.get("resourceId");
			if (item == null || resourceId == null) {
				continue;
			}
			BoolQueryBuilder queryBuilder2 = QueryBuilders.boolQuery();
			queryBuilder2.must(QueryBuilders.matchPhraseQuery("resourceId", resourceId));
			queryBuilder2.must(QueryBuilders.matchPhraseQuery("item", item));
			queryBuilder1.should(queryBuilder2);
		}
		queryBuilder.must(queryBuilder1);
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("datetime");
		rangeQueryBuilder.lte(l).gte(l-3600000);
		queryBuilder.must(rangeQueryBuilder);
		TermsAggregationBuilder termsBuilder;
		termsBuilder = AggregationBuilders.terms("resourceId").field("resourceId.keyword").size(paramsList.size());
		termsBuilder.order(BucketOrder.aggregation("kpi_value", false));
		termsBuilder.subAggregation(AggregationBuilders.avg("kpi_value").field("value"));
//		termsBuilder.subAggregation(AggregationBuilders.topHits("expression_hit").size(1).sort("datetime", SortOrder.DESC));
		request.addAggregation(termsBuilder).setQuery(queryBuilder).setSize(0);
		log.info("查询设备利用率TOPN es索引： {}", Arrays.asList(request.request().indices()));
		log.info("查询设备利用率TOPN es脚本： {}", request.toString());
		SearchResponse response = request.execute().actionGet();
		Aggregations aggregations = response.getAggregations();
		List<Map<String, Object>> list = Lists.newArrayList();
		if (aggregations == null) {
			log.warn("aggregations are null!");
			return list;
		}
		List<Aggregation> aggregationList = aggregations.asList();
		for (Aggregation aggregation : aggregationList) {
			String s = aggregation.toString();
			JSONObject jsonObject = JSON.parseObject(s);
			generateAggs(jsonObject, list, null);
		}
		for (Map<String, Object> map: list) {
			Float kpi_value = MapUtils.getFloat(map, "value");
			if (kpi_value == null ) {
				continue;
			}
			map.put("value", (float)Math.round(kpi_value * 100)/ 100);
		}
		return list;
	}

	public List<Map<String, Object>> getKpiListForItem (@PathVariable("resourceId") String resourceId,@RequestBody List<String> paramsList) {
		DateFormat returndf = new SimpleDateFormat("yyyyMMdd");
		long l = System.currentTimeMillis();
//		String timeFormat = returndf.format(System.currentTimeMillis() - 8 * 3600 * 1000);
		String timeFormat = returndf.format(System.currentTimeMillis());
		String index = "history*"+timeFormat;
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,index))
				.setExplain(true);

		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		queryBuilder.must(QueryBuilders.matchPhraseQuery("resourceId", resourceId));
		if (!CollectionUtils.isEmpty(paramsList)) {
			queryBuilder.must(QueryBuilders.termsQuery("item.keyword", paramsList.toArray()));
		}
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("datetime");
		rangeQueryBuilder.lte(l).gte(l-3600000);
		queryBuilder.must(rangeQueryBuilder);
		TermsAggregationBuilder termsBuilder;
		int size = CollectionUtils.isEmpty(paramsList) ? 10000 : paramsList.size();
		termsBuilder = AggregationBuilders.terms("item").field("item.keyword").size(size);
//		termsBuilder.order(BucketOrder.aggregation("kpi_value", false));
//		termsBuilder.subAggregation(AggregationBuilders.avg("kpi_value").field("value"));
		termsBuilder.subAggregation(AggregationBuilders.topHits("expression_hit").size(1).sort("datetime", SortOrder.DESC));
		request.addAggregation(termsBuilder).setQuery(queryBuilder).setSize(0);
		log.info("查询设备利用率TOPN es索引： {}", Arrays.asList(request.request().indices()));
		log.info("查询设备利用率TOPN es脚本： {}", request.toString());
		SearchResponse response = request.execute().actionGet();
		Aggregations aggregations = response.getAggregations();
		List<Map<String, Object>> list = Lists.newArrayList();
		if (aggregations == null) {
			log.warn("aggregations are null!");
			return list;
		}
		List<Aggregation> aggregationList = aggregations.asList();
		for (Aggregation aggregation : aggregationList) {
			String s = aggregation.toString();
			JSONObject jsonObject = JSON.parseObject(s);
			getTopHis(jsonObject, list);
		}
		for (Map<String, Object> map: list) {
			Float kpi_value = MapUtils.getFloat(map, "value");
			if (kpi_value == null ) {
				continue;
			}
			map.put("value", (float)Math.round(kpi_value * 100)/ 100);
		}
		return list;
	}
}
