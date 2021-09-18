package com.aspire.mirror.elasticsearch.server.controller.zabbix;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregation;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.CompositeValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.composite.TermsValuesSourceBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.elasticsearch.api.dto.MonthBizSystemDayRequest;
import com.aspire.mirror.elasticsearch.api.dto.MonthBizSystemDayResponse;
import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IDay2MonthReportService;
import com.aspire.mirror.elasticsearch.server.biz.IKpiKeyConfigBiz;
import com.aspire.mirror.elasticsearch.server.controller.CommonController;
import com.aspire.mirror.elasticsearch.server.enums.Constants;
import com.aspire.mirror.elasticsearch.server.enums.KpiTypeEnum;
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
public class Day2MonthReportController extends CommonController implements IDay2MonthReportService {
	@Autowired
	private TransportClient transportClient;

	@Value("${INDEX_HISTORY_HOUR_PRE_NEW:kpi-hour_}")
	private String INDEX_HISTORY_HOUR_PRE;

	@Value("${INDEX_HISTORYUINT_HOUR_PRE_NEW:kpi-hour-uint_}")
	private String INDEX_HISTORYUINT_HOUR_PRE;

	@Value("${INDEX_HISTORY_DAY_PRE_NEW:kpi-day_}")
	private String INDEX_HISTORY_DAY_PRE;

	@Value("${INDEX_HISTORYUINT_DAY_PRE_NEW:kpi-hour-uint_}")
	private String INDEX_HISTORYUINT_DAY_PRE;

	@Value("${INDEX_VALUE_NEW:value_avg}")
	private String INDEX_VALUE;

	@Value("${INDEX_VALUE_MAX_NEW:value_max}")
	private String INDEX_VALUE_MAX;

	@Value("${NFJD_UNASSIGNED_BIZSYSTEM:bae24dd8679f4be897af8afff94c74bd,38c3e05b39374b6487831149875818d2,c9c8f37301d94ac6b068eeccbef696c3,5a91d0d2ea8d4c96b265dd858ac684e6}")
	private String NFJD_UNASSIGNED_BIZSYSTEM;
	@Value("${XXG_UNASSIGNED_BIZSYSTEM:bae24dd8679f4be897af8afff94c74bd,8919f848f220402a8f96a9f5dbd10aff}")
	private String XXG_UNASSIGNED_BIZSYSTEM;
	
	
	@Value("${term_key:host}")
	private String term_key;

	@Autowired
	private IKpiKeyConfigBiz iKpiKeyConfigBiz;
	
	@Value("${phyNodeTypes:计算节点,宿主机}")
	private String phyNodeTypes;

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
			String key = idcType + "_" + ip;
			Map<String, Object> rangeMap = new HashMap<>();
			if (idcTypeMap.containsKey(key)) {
				rangeMap = idcTypeMap.get(key);
			} else {
				rangeMap.put("ip", ip);
				rangeMap.put("idcType", idcType);
				rangeMap.put("deviceType", deviceType);
			}

			String max = InstantUtils.getAggValues("max", a, "avg_max");
			String avg = InstantUtils.getAggValues("avg", a, "avg_used");
			if (StringUtils.isBlank(max)) {
				max = null;
			}
			if (StringUtils.isBlank(avg)) {
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

		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);

		log.info("INDEX:{}", indexList);

		SearchRequestBuilder request = transportClient
				.prepareSearch(getClusterIndex(monthReportRequest, indexList.toArray(new String[] {})))
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
			QueryBuilder queryTermId = QueryBuilders.termsQuery("podName.keyword",
					monthReportRequest.getPod().split(","));
			queryBuilder.must(queryTermId);
		}
		queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
		String deviceType = monthReportRequest.getDeviceType();
		if (StringUtils.isNotBlank(deviceType)) {

			FormConditionUtil.getDeviceType(deviceType, monthReportRequest.getItem(), queryBuilder);
		} else {
			log.error("设备利用率设备类型不能为空");
			return null;
		}

		TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("host").field("host.keyword").size(100000);
		termsBuilder.subAggregation(AggregationBuilders.max("avg_max").field(INDEX_VALUE));
		termsBuilder.subAggregation(AggregationBuilders.avg("avg_used").field(INDEX_VALUE));
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

		/*
		 * DateFormat returnd = new SimpleDateFormat("yyyyMM"); INDEX = INDEX +
		 * returnd.format(startTime) + "*";
		 */

		/*
		 * Calendar calendar = Calendar.getInstance(); calendar.setTime(endTime);
		 * calendar.add(Calendar.DATE, 1);
		 */
		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
		log.info("index:{}", indexList);
		SearchRequestBuilder request = transportClient
				.prepareSearch(getClusterIndex(monthReportRequest, indexList.toArray(new String[] {})))
				.setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(startTime.getTime() / 1000);
		timeRange.lte(endTime.getTime() / 1000);
		queryBuilder.must(timeRange);
		queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
		if (StringUtils.isNotBlank(idcType)) {
			queryBuilder.must(QueryBuilders.termQuery("idcType.keyword", idcType));
		}
		if (StringUtils.isNotBlank(monthReportRequest.getPod())) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("podName.keyword",
					monthReportRequest.getPod().split(","));
			queryBuilder.must(queryTermId);
		}

		FormConditionUtil.getDeviceType(deviceType, monthReportRequest.getItem(), queryBuilder);

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
		dateAgg.subAggregation(
				AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(this.INDEX_VALUE));
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
		if (null == response.getAggregations() || null == response.getAggregations().get("bizSystem")) {
			return dataMap;
		}
		final Terms terms2 = response.getAggregations().get("bizSystem");
		for (final Terms.Bucket bucket2 : terms2.getBuckets()) {

			String biz = bucket2.getKeyAsString();
			String key = biz + deviceType;
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

				if (StringUtils.isBlank(max)) {
					max = null;
				}
				if (StringUtils.isBlank(avg)) {
					avg = null;
				}
				StringBuffer sb = new StringBuffer();

				sb.append(item).append("_max");
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
	public Map<String, Map<String, Object>> queryIdcTypeByDay(@RequestBody MonthReportRequest monthReportRequest)
			throws ParseException {
		log.info("**monthReport-queryIdcTypeByDay-begin***********************************");
		String start = monthReportRequest.getStartTime();
		String end = monthReportRequest.getEndTime();

		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		String deviceType = monthReportRequest.getDeviceType();
		String item = monthReportRequest.getItem();
		Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();

		/*
		 * DateFormat returnd = new SimpleDateFormat("yyyyMM"); INDEX = INDEX +
		 * returnd.format(startTime) + "*";
		 */

		/*
		 * Calendar calendar = Calendar.getInstance(); calendar.setTime(endTime);
		 * calendar.add(Calendar.DATE, 1);
		 */
		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
		log.info("index:{}", indexList);
		SearchRequestBuilder request = transportClient
				.prepareSearch(getClusterIndex(monthReportRequest, indexList.toArray(new String[] {})))
				.setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(startTime.getTime() / 1000);
		timeRange.lte(endTime.getTime() / 1000);
		queryBuilder.must(timeRange);

		if (StringUtils.isNotBlank(monthReportRequest.getPod())) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("podName.keyword",
					monthReportRequest.getPod().split(","));
			queryBuilder.must(queryTermId);
		}

		queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
		FormConditionUtil.getDeviceType(deviceType, monthReportRequest.getItem(), queryBuilder);

		request.setQuery(queryBuilder).setSize(0);

		// 时间分组
		String format = "yyyy-MM-dd HH:mm:ss";

		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
		dateAgg.format(format);// .timeZone(DateTimeZone.getDefault());

		dateAgg.extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()));

		TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("idcType").field("idcType.keyword").size(20);
		// dateAgg.subAggregation(AggregationBuilders.max(Constants.AGGREGATION_TYPE_MAX
		// + "_item").field("value"));
		dateAgg.subAggregation(
				AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(this.INDEX_VALUE));
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
		if (null == response.getAggregations() || null == response.getAggregations().get("idcType")) {
			return dataMap;
		}
		final Terms terms2 = response.getAggregations().get("idcType");
		for (final Terms.Bucket bucket2 : terms2.getBuckets()) {

			String idcType = bucket2.getKeyAsString();
			String key = idcType + deviceType;
			Map<String, Object> val = new HashMap<String, Object>();
			if (dataMap.containsKey(key)) {
				val = dataMap.get(key);
			} else {
				val.put("idcType", idcType);
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

				if (StringUtils.isBlank(max)) {
					max = null;
				}
				if (StringUtils.isBlank(avg)) {
					avg = null;
				}
				StringBuffer sb = new StringBuffer();

				sb.append(item).append("_max");
				val.put(sb.toString(), max);
				sb.setLength(0);
				sb.append(item).append("_avg");
				val.put(sb.toString(), avg);
			}

		}
		log.info("**monthReport-queryIdcTypeByDay-end***********************************");
		return dataMap;
	}

	@Override
	public Map<String, Map<String, Object>> idcTypeDeviceUsedRate(@RequestBody MonthReportRequest monthReportRequest)
			throws Exception {
		Map<String, Map<String, Object>> idcTypeMap = monthReportRequest.getDataMap();
		if (null == idcTypeMap) {
			idcTypeMap = Maps.newHashMap();
			return idcTypeMap;
		}
		Map<String, Map<String, Object>> dataMap = Maps.newHashMap();
		SearchRequestBuilder request = initeMonthRequst(monthReportRequest, false);

		// 组装composite
		List<CompositeValuesSourceBuilder<?>> sources = Lists.newArrayList();
		sources.add(new TermsValuesSourceBuilder("idcType").field("idcType.keyword").missingBucket(false)
				.order(SortOrder.ASC));
		sources.add(new TermsValuesSourceBuilder("ip").field("host.keyword").missingBucket(false).order(SortOrder.ASC));

		CompositeAggregationBuilder composite = new CompositeAggregationBuilder("composite", sources).size(5000);

		request.addAggregation(composite);
		composite.subAggregation(AggregationBuilders.max("avg_max").field(INDEX_VALUE));
		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.execute().actionGet();
		if (null == response.getAggregations()) {
			return idcTypeMap;
		}
		CompositeAggregation parsedComposite = response.getAggregations().get("composite");
		Map<String, Object> afterKey = parsedComposite.afterKey();

		String deviceType = monthReportRequest.getDeviceType();
		String item = monthReportRequest.getItem();

		getIdcTypeComposite(item, dataMap, deviceType, parsedComposite);

		while (null != afterKey) {
			composite.aggregateAfter(afterKey);
			log.info("查询设备利用率es脚本： {}", request);
			parsedComposite = request.execute().actionGet().getAggregations().get("composite");
			getIdcTypeComposite(item, dataMap, deviceType, parsedComposite);
			afterKey = parsedComposite.afterKey();
		}

		getRate(item, idcTypeMap, dataMap);
		dataMap.clear();
		return idcTypeMap;

	}

	public void getIdcTypeComposite(String item, Map<String, Map<String, Object>> idcTypeMap, String deviceType,
			CompositeAggregation parsedComposite) {
		List<? extends CompositeAggregation.Bucket> list = parsedComposite.getBuckets();
		for (int i = 0; i < list.size(); i++) {
			String key = deviceType;
			for (Map.Entry<String, Object> m : list.get(i).getKey().entrySet()) {
				String keyTemp = m.getKey();
				if (keyTemp.equals("idcType")) {
					key = m.getValue() == null ? null : m.getValue().toString() + key;
				}

			}
			Map<String, Object> val = Maps.newHashMap();
			if (idcTypeMap.containsKey(key)) {
				val = idcTypeMap.get(key);
			} else {
				idcTypeMap.put(key, val);
			}
			Max max = list.get(i).getAggregations().get("avg_max");
			formCountVal(max, item, val);

		}
	}

	@Override
	public Map<String, Map<String, Object>> bizSystemDeviceUsedRate(@RequestBody MonthReportRequest monthReportRequest)
			throws Exception {
		Map<String, Map<String, Object>> idcTypeMap = monthReportRequest.getDataMap();
		if (null == idcTypeMap) {
			idcTypeMap = Maps.newHashMap();
			return idcTypeMap;
		}
		Map<String, Map<String, Object>> dataMap = Maps.newHashMap();
		SearchRequestBuilder request = initeMonthRequst(monthReportRequest, true);

		// 组装composite
		List<CompositeValuesSourceBuilder<?>> sources = Lists.newArrayList();
		sources.add(new TermsValuesSourceBuilder("idcType").field("idcType.keyword").missingBucket(false)
				.order(SortOrder.ASC));
		sources.add(new TermsValuesSourceBuilder("ip").field("host.keyword").missingBucket(false).order(SortOrder.ASC));
		sources.add(new TermsValuesSourceBuilder("bizSystem").field("bizSystem.keyword").missingBucket(false)
				.order(SortOrder.ASC));

		CompositeAggregationBuilder composite = new CompositeAggregationBuilder("composite", sources).size(5000);

		request.addAggregation(composite);
		composite.subAggregation(AggregationBuilders.max("avg_max").field(INDEX_VALUE_MAX));
		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.execute().actionGet();
		if (null == response.getAggregations()) {
			return idcTypeMap;
		}
		CompositeAggregation parsedComposite = response.getAggregations().get("composite");
		Map<String, Object> afterKey = parsedComposite.afterKey();

		String deviceType = monthReportRequest.getDeviceType();
		String item = monthReportRequest.getItem();

		getBizSystemComposite(item, dataMap, deviceType, parsedComposite);

		while (null != afterKey) {
			composite.aggregateAfter(afterKey);
			log.info("查询设备利用率es脚本： {}", request);
			parsedComposite = request.execute().actionGet().getAggregations().get("composite");
			getBizSystemComposite(item, dataMap, deviceType, parsedComposite);
			afterKey = parsedComposite.afterKey();
		}

		getRate(item, idcTypeMap, dataMap);
		dataMap.clear();
		return idcTypeMap;

	}

	public void getBizSystemComposite(String item, Map<String, Map<String, Object>> idcTypeMap, String deviceType,
			CompositeAggregation parsedComposite) {
		List<? extends CompositeAggregation.Bucket> list1 = parsedComposite.getBuckets();
		for (int i = 0; i < list1.size(); i++) {

			String idcType = "";
			String bizSystem = "";
			for (Map.Entry<String, Object> m : list1.get(i).getKey().entrySet()) {
				String keyTemp = m.getKey();

				if (keyTemp.equals("idcType")) {
					idcType = m.getValue() == null ? null : m.getValue().toString();
				}
				if (keyTemp.equals("bizSystem")) {
					bizSystem = m.getValue() == null ? null : m.getValue().toString();
				}

			}
			String key = idcType + "_" + bizSystem + "_" + deviceType;

			Map<String, Object> val = Maps.newHashMap();
			if (idcTypeMap.containsKey(key)) {
				val = idcTypeMap.get(key);
			} else {
				idcTypeMap.put(key, val);
			}
			Max max = list1.get(i).getAggregations().get("avg_max");
			formCountVal(max, item, val);
		}
	}

	public void getRate(String item, Map<String, Map<String, Object>> idcTypeMap,
			Map<String, Map<String, Object>> dataMap) {
		// log.info("idcTypeMap： {}", idcTypeMap);
		for (Map.Entry<String, Map<String, Object>> m : dataMap.entrySet()) {
			String key = m.getKey();
			if (!idcTypeMap.containsKey(key)) {
				continue;
			}
			Map<String, Object> idcTypeVal = idcTypeMap.get(key);
			Map<String, Object> val = m.getValue();
			String keyStr1 = item + "_fifteen_ratio";
			int count1 = val.get(keyStr1) == null ? 0 : Integer.parseInt(val.get(keyStr1).toString());
			String keyStr2 = item + "_fourty_ratio";
			int count2 = val.get(keyStr2) == null ? 0 : Integer.parseInt(val.get(keyStr2).toString());
			String keyStr3 = item + "_eighty_ratio";
			int count3 = val.get(keyStr3) == null ? 0 : Integer.parseInt(val.get(keyStr3).toString());
			String keyStr4 = item + "_eighty_more_ratio";
			int count4 = val.get(keyStr4) == null ? 0 : Integer.parseInt(val.get(keyStr4).toString());
			int sum = count1 + count2 + count3 + count4;
			if (sum > 0) {
				float sum1 = (float) sum;
				float countff1 = (float) (count1 * 100) / sum1;
				double countf = new BigDecimal(countff1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				idcTypeVal.put(keyStr1, countf);

				float countff2 = (float) (count2 * 100) / sum1;
				double count1f = new BigDecimal(countff2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				idcTypeVal.put(keyStr2, count1f);

				float countff3 = (float) (count3 * 100) / sum1;
				double count3f = new BigDecimal(countff3).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				idcTypeVal.put(keyStr3, count3f);

				float countff4 = (float) (count4 * 100) / sum1;
				double count4f = new BigDecimal(countff4).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				idcTypeVal.put(keyStr4, count4f);
			}
		}
	}

	public void formCountVal(Max max, String item, Map<String, Object> val) {
		if (max == null || "-Infinity".equals(max.getValueAsString()) || "NaN".equals(max.getValueAsString())) {
			return;
		}
		double maxVal = max.getValue();
		if (maxVal < 15) {
			String keyStr = item + "_fifteen_ratio";
			int count = val.get(keyStr) == null ? 0 : Integer.parseInt(val.get(keyStr).toString());
			count++;
			val.put(keyStr, count);
		}
		if (15 <= maxVal && maxVal < 40) {
			String keyStr = item + "_fourty_ratio";
			int count = val.get(keyStr) == null ? 0 : Integer.parseInt(val.get(keyStr).toString());
			count++;
			val.put(keyStr, count);
		}
		if (40 <= maxVal && maxVal < 80) {
			String keyStr = item + "_eighty_ratio";
			int count = val.get(keyStr) == null ? 0 : Integer.parseInt(val.get(keyStr).toString());
			count++;
			val.put(keyStr, count);
		}
		if (maxVal >= 80) {
			String keyStr = item + "_eighty_more_ratio";
			int count = val.get(keyStr) == null ? 0 : Integer.parseInt(val.get(keyStr).toString());
			count++;
			val.put(keyStr, count);
		}
	}

	public SearchRequestBuilder initeMonthRequst(MonthReportRequest monthReportRequest, boolean bizSystemFlag)
			throws ParseException {

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		String startDate = monthReportRequest.getStartTime();
		// String endDate = monthReportRequest.getEndTime();
		Date startTime = DateUtils.parseDate(startDate, new String[] { "yyyy-MM-dd HH:mm:ss" });
		// Date endTime = DateUtils.parseDate(endDate, new String[] { "yyyy-MM-dd
		// HH:mm:ss" });

		DateFormat returnd = new SimpleDateFormat("yyyyMM");
		String INDEX = this.INDEX_HISTORY_DAY_PRE + returnd.format(startTime) + "*";

		log.info("INDEX:{}", INDEX);

		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest, INDEX))
				.setExplain(true);

		if (StringUtils.isNotBlank(monthReportRequest.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", monthReportRequest.getIdcType());
			queryBuilder.must(queryTermId);
		}
		String deviceType = monthReportRequest.getDeviceType();

		if (StringUtils.isNotBlank(deviceType)) {
			FormConditionUtil.getDeviceType(deviceType, monthReportRequest.getItem(), queryBuilder);
			if (deviceType.equals("X86服务器")) {
				int type = monthReportRequest.getType();
				if (type == 1 || type == 3) {// 已分配
					queryBuilder.mustNot(QueryBuilders.termsQuery("bizSystem.keyword",
							(this.NFJD_UNASSIGNED_BIZSYSTEM + "," + this.XXG_UNASSIGNED_BIZSYSTEM).split(",")));
				}
				if (type == 2 || type == 3) {
					queryBuilder.must(QueryBuilders.termQuery("nodeType.keyword",  Constants.NODE_TYPE_JSJD));
					//queryBuilder.mustNot(QueryBuilders.termQuery("nodeType.keyword", Constants.NODE_TYPE_SZJ));// 裸金属（含GPU），不含宿主机
				}
				if(type==1 || type==0) {
					queryBuilder.must(QueryBuilders.termsQuery("nodeType.keyword", phyNodeTypes.split(",")));
				}
				if (type == 4) {
					queryBuilder.must(QueryBuilders.termQuery("nodeType.keyword", Constants.NODE_TYPE_SZJ));// 宿主机
				}
				if (type == 5) {
					queryBuilder.must(QueryBuilders.termQuery("nodeType.keyword", Constants.NODE_TYPE_MANEGERNODE));// 管理节点
				}
			}
		} else {
			log.error("设备利用率设备类型不能为空");
			return null;
		}

		request.setQuery(queryBuilder).setSize(0);

		return request;
	}

	@Override
	public List<MonthBizSystemDayResponse> getBizSytemDataByDay(@RequestBody MonthBizSystemDayRequest monthRequest)
			throws ParseException {
		log.info("**monthReport-getBizSytemDayData-begin****");
		String day = monthRequest.getDay();
		String start = day + " 00:00:00";
		String end = day + " 23:59:59";

		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		String bizSystem = monthRequest.getBizSystem();
		String deviceType = monthRequest.getDeviceType();

		DateFormat returnd = new SimpleDateFormat("yyyyMM");
		String INDEX = this.INDEX_HISTORY_DAY_PRE + returnd.format(startTime) + "*";

		log.info("index:{}", INDEX);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthRequest, INDEX))
				.setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(startTime.getTime() / 1000);
		timeRange.lte(endTime.getTime() / 1000);
		queryBuilder.must(timeRange);

		if (StringUtils.isNotBlank(bizSystem)) {
			queryBuilder.must(QueryBuilders.termQuery("bizSystem.keyword", bizSystem));
		}

		if (StringUtils.isBlank(deviceType)) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", "云主机", "X86服务器");
			StringBuffer sb = new StringBuffer();
			sb.append(Constants.CPU_ITEM).append(",").append(Constants.MEMORY_ITEM);
			queryBuilder.must(QueryBuilders.termsQuery("item.keyword", sb.toString().split(",")));

			queryBuilder.must(queryTermId);
		} else {
			queryBuilder.must(QueryBuilders.termsQuery("item.keyword",
					(Constants.CPU_ITEM + "," + Constants.MEMORY_ITEM).split(",")));
			if (deviceType.equals("虚拟机") || deviceType.equals("云主机")) {

				QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", "云主机");
				queryBuilder.must(queryTermId);

			} else {
				QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", "X86服务器");
				queryBuilder.must(queryTermId);
			}
		}

		request.setQuery(queryBuilder).setSearchType(SearchType.DEFAULT).setSize(1000)
				.setScroll(TimeValue.timeValueMinutes(5));

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.execute().actionGet();
		Map<String, MonthBizSystemDayResponse> map = Maps.newLinkedHashMap();
		while (response.getHits().getHits().length > 0) {
			for (SearchHit searchHit : response.getHits().getHits()) {
				// System.out.println(searchHit.getSource().toString());
				Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
				MonthBizSystemDayResponse rs = new MonthBizSystemDayResponse();
				String idcType = sourceAsMap.get("idcType") == null ? "" : sourceAsMap.get("idcType").toString();
				String deviceType1 = sourceAsMap.get("deviceType") == null ? ""
						: sourceAsMap.get("deviceType").toString();

				String ip = sourceAsMap.get("host") == null ? "" : sourceAsMap.get("host").toString();
				String resourceId = sourceAsMap.get("resourceId") == null ? ""
						: sourceAsMap.get("resourceId").toString();
				String item = sourceAsMap.get("item") == null ? "" : sourceAsMap.get("item").toString();
				String key = idcType + ip;
				Float avg = sourceAsMap.get("value_avg") == null ? null
						: Float.parseFloat(sourceAsMap.get("value_avg").toString());
				Float max = sourceAsMap.get("value_max") == null ? null
						: Float.parseFloat(sourceAsMap.get("value_max").toString());
				if (null != avg) {
					avg = (float) Math.round(avg * 100) / 100;
					if (avg < 0) {
						avg = null;
					}
				}
				if (null != max) {
					max = (float) Math.round(max * 100) / 100;
					if (max < 0) {
						max = null;
					}
				}
				if (map.containsKey(key)) {
					rs = map.get(key);
				} else {
					rs.setDate(day);
					rs.setDeviceType(deviceType1);
					int tem = Integer.parseInt(day.substring(day.lastIndexOf("-") + 1));
					rs.setDateDisplsy(tem + "号");
					rs.setIdcType(idcType);
					rs.setIp(ip);
					rs.setIp(ip);
					rs.setResourceId(resourceId);
					map.put(key, rs);
				}
				if (item.equals("cpu.usage_average") || item.equals("custom.cpu.avg5.pused")
						|| item.equals("cpu.capacity_usagepct_average")) {
					rs.setCpuAvg(avg);
					rs.setCpuMax(max);
				} else {
					rs.setMemoryAvg(avg);
					rs.setMemoryMax(max);
				}

			}

			response = transportClient.prepareSearchScroll(response.getScrollId())
					.setScroll(TimeValue.timeValueMinutes(5)).execute().actionGet();
		}

		log.info("**monthReport-getBizSytemDayData-end****");
		return new ArrayList(map.values());
	}

	@Override
	public List<Map<String, Object>> getBizSytemDataByDayReturnMap(@RequestBody MonthBizSystemDayRequest monthRequest)
			throws ParseException {

		log.info("**monthReport-getBizSytemDataByDayReturnMap-begin****");
		String day = monthRequest.getDay();
		String start = day + " 00:00:00";
		String end = day + " 23:59:59";

		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		String bizSystem = monthRequest.getBizSystem();
		String deviceType = monthRequest.getDeviceType();

		DateFormat returnd = new SimpleDateFormat("yyyyMM");
		String INDEX = this.INDEX_HISTORY_DAY_PRE + returnd.format(startTime) + "*";

		log.info("index:{}", INDEX);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthRequest, INDEX))
				.setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(startTime.getTime() / 1000);
		timeRange.lte(endTime.getTime() / 1000);
		queryBuilder.must(timeRange);

		if (StringUtils.isNotBlank(bizSystem)) {
			queryBuilder.must(QueryBuilders.termQuery("bizSystem.keyword", bizSystem));
		}

		if (StringUtils.isBlank(deviceType)) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("deviceType.keyword", "云主机", "X86服务器");
			StringBuffer sb = new StringBuffer();
			sb.append(Constants.CPU_ITEM).append(",").append(Constants.MEMORY_ITEM);
			queryBuilder.must(QueryBuilders.termsQuery("item.keyword", sb.toString().split(",")));

			queryBuilder.must(queryTermId);
		} else {
			queryBuilder.must(QueryBuilders.termsQuery("item.keyword",
					(Constants.CPU_ITEM + "," + Constants.MEMORY_ITEM).split(",")));
			if (deviceType.equals("虚拟机") || deviceType.equals("云主机")) {
				QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", "云主机");
				queryBuilder.must(queryTermId);

			} else {
				QueryBuilder queryTermId = QueryBuilders.termQuery("deviceType.keyword", "X86服务器");
				queryBuilder.must(queryTermId);
			}
		}

		request.setQuery(queryBuilder).setSearchType(SearchType.DEFAULT).setSize(1000)
				.addSort("idcType.keyword", SortOrder.ASC).addSort("deviceType.keyword", SortOrder.ASC)
				.setScroll(TimeValue.timeValueMinutes(5));

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.execute().actionGet();
		Map<String, Map<String, Object>> map = Maps.newLinkedHashMap();
		while (response.getHits().getHits().length > 0) {
			for (SearchHit searchHit : response.getHits().getHits()) {
				// System.out.println(searchHit.getSource().toString());
				Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
				Map<String, Object> rs = Maps.newLinkedHashMap();
				String idcType = sourceAsMap.get("idcType") == null ? "" : sourceAsMap.get("idcType").toString();
				String deviceType1 = sourceAsMap.get("deviceType") == null ? ""
						: sourceAsMap.get("deviceType").toString();

				String ip = sourceAsMap.get("host") == null ? "" : sourceAsMap.get("host").toString();
				String item = sourceAsMap.get("item") == null ? "" : sourceAsMap.get("item").toString();
				String tag = sourceAsMap.get("tag") == null ? "" : sourceAsMap.get("tag").toString();
				String key = idcType + ip;
				Float avg = sourceAsMap.get("value_avg") == null ? null
						: Float.parseFloat(sourceAsMap.get("value_avg").toString());
				Float max = sourceAsMap.get("value_max") == null ? null
						: Float.parseFloat(sourceAsMap.get("value_max").toString());
				if (null != avg) {
					avg = (float) Math.round(avg * 100) / 100;
					if (avg < 0) {
						avg = null;
					}
				}
				if (null != max) {
					max = (float) Math.round(max * 100) / 100;
					if (max < 0) {
						max = null;
					}
				}
				if (map.containsKey(key)) {
					rs = map.get(key);
				} else {
					rs.put("date", day);
					rs.put("deviceType", deviceType1);
					rs.put("idcType", idcType);
					rs.put("ip", ip);
					rs.put("tag", tag);
					map.put(key, rs);
				}
				if (item.equals("cpu.usage_average") || item.equals("custom.cpu.avg5.pused")
						|| item.equals("cpu.capacity_usagepct_average")) {
					rs.put("cpu_avg", avg);
					rs.put("cpu_max", max);
				} else {
					rs.put("memory_avg", avg);
					rs.put("memory_max", max);
				}

			}

			response = transportClient.prepareSearchScroll(response.getScrollId())
					.setScroll(TimeValue.timeValueMinutes(5)).execute().actionGet();
		}

		log.info("**monthReport-getBizSytemDataByDayReturnMap-end****");
		return new ArrayList(map.values());

	}

//直真接口p1.1.4
	@Override
	public Map<String, Map<String, Object>> queryIdcTypeByMonth(@RequestBody MonthReportRequest monthReportRequest)
			throws ParseException {
		log.info("**monthReport-queryIdcTypeByMonth-begin***********************************");
		String start = monthReportRequest.getStartTime();
		String end = monthReportRequest.getEndTime();

		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		String deviceType = monthReportRequest.getDeviceType();
		String item = monthReportRequest.getItem();
		Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();

		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
		log.info("index:{}", indexList);
		SearchRequestBuilder request = transportClient
				.prepareSearch(getClusterIndex(monthReportRequest, indexList.toArray(new String[] {})))
				.setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		/*
		 * RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		 * timeRange.gte(startTime.getTime() / 1000); timeRange.lte(endTime.getTime() /
		 * 1000); queryBuilder.must(timeRange);
		 */

		if (StringUtils.isNotBlank(monthReportRequest.getPod())) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("podName.keyword",
					monthReportRequest.getPod().split(","));
			queryBuilder.must(queryTermId);
		}
		if (StringUtils.isNotBlank(monthReportRequest.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", monthReportRequest.getIdcType());
			queryBuilder.must(queryTermId);
		}

		queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
		FormConditionUtil.getDeviceType(deviceType, monthReportRequest.getItem(), queryBuilder);

		request.setQuery(queryBuilder).setSize(0);

		// 时间分组
		String format = "yyyy-MM-dd HH:mm:ss";

		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
		dateAgg.format(format);// .timeZone(DateTimeZone.getDefault());

		dateAgg.extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()));

		TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("idcType").field("idcType.keyword").size(20);
		// dateAgg.subAggregation(AggregationBuilders.max(Constants.AGGREGATION_TYPE_MAX
		// + "_item").field("value"));
		dateAgg.subAggregation(
				AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(this.INDEX_VALUE));
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
		if (null == response.getAggregations() || null == response.getAggregations().get("idcType")) {
			return dataMap;
		}
		final Terms terms2 = response.getAggregations().get("idcType");
		for (final Terms.Bucket bucket2 : terms2.getBuckets()) {

			String idcType = bucket2.getKeyAsString();
			String key = idcType + deviceType;
			Map<String, Object> val = new HashMap<String, Object>();
			if (dataMap.containsKey(key)) {
				val = dataMap.get(key);
			} else {
				val.put("idcType", idcType);
				val.put("type", monthReportRequest.getType());
				// val.put("department2", de2);
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

				if (StringUtils.isBlank(max)) {
					max = null;
				}
				if (StringUtils.isBlank(avg)) {
					avg = null;
				}
				StringBuffer sb = new StringBuffer();

				sb.append(item).append("_max");
				val.put(sb.toString(), max);
				sb.setLength(0);
				sb.append(item).append("_avg");
				val.put(sb.toString(), avg);
			}

		}
		log.info("**monthReport-queryIdcTypeByMonth-end***********************************");
		return dataMap;
	}

//直真接口p1.1.4
	@Override
	public Map<String, Object> queryIdcTypeAllByMonth(@RequestBody MonthReportRequest monthReportRequest)
			throws ParseException {
		log.info("**monthReport-queryIdcTypeAllByMonth-begin***********************************");
		String start = monthReportRequest.getStartTime();
		String end = monthReportRequest.getEndTime();

		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		String deviceType = monthReportRequest.getDeviceType();
		String item = monthReportRequest.getItem();
		Map<String, Object> dataMap = Maps.newHashMap();

		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
		log.info("index:{}", indexList);
		SearchRequestBuilder request = transportClient
				.prepareSearch(getClusterIndex(monthReportRequest, indexList.toArray(new String[] {})))
				.setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		if (StringUtils.isNotBlank(monthReportRequest.getPod())) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("podName.keyword",
					monthReportRequest.getPod().split(","));
			queryBuilder.must(queryTermId);
		}
		if (StringUtils.isNotBlank(monthReportRequest.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", monthReportRequest.getIdcType());
			queryBuilder.must(queryTermId);
		}

		queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
		FormConditionUtil.getDeviceType(deviceType, item, queryBuilder);

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
		dateAgg.subAggregation(
				AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(this.INDEX_VALUE));

		request.addAggregation(dateAgg);
		request.addAggregation(PipelineAggregatorBuilders.statsBucket("stats_buckets3", "dateagg.avg_item"));
		// request.addAggregation(termsBuilder1New);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		/*
		 * if(response.getHits().getTotalHits() ==0) { return null; }
		 */
		if (null == dataMap || dataMap.size() == 0) {
			dataMap = new HashMap<>();
		}
		if (null == response.getAggregations() || null == response.getAggregations().get("stats_buckets3")) {
			return dataMap;
		}

		Stats stats = response.getAggregations().get("stats_buckets3");
		if (null != stats) {

			String avg = InstantUtils.getStatsValues("avg", stats);
			String max = InstantUtils.getStatsValues("max", stats);

			/*
			 * Max max = bt1.getAggregations().get(name); if (max == null ||
			 * "-Infinity".equals(max.getValueAsString()) ||
			 * "NaN".equals(max.getValueAsString())) { value = ""; } else { value =
			 * max.getValueAsString(); }
			 */

			if (StringUtils.isBlank(max)) {
				max = null;
			}
			if (StringUtils.isBlank(avg)) {
				avg = null;
			}
			StringBuffer sb = new StringBuffer();

			sb.append(item).append("_max");
			dataMap.put(sb.toString(), max);
			sb.setLength(0);
			sb.append(item).append("_avg");
			dataMap.put(sb.toString(), avg);
			// dataMap.put("type", monthReportRequest.getType());

		}
		log.info("**monthReport-queryIdcTypeAllByMonth-end***********************************");
		return dataMap;
	}

	public Map<String, Object> queryDepartment2Rate(@RequestBody MonthReportRequest monthReportRequest)
			throws ParseException {
		log.info("**monthReport-queryDepartment2Rate-begin***********************************");
		String start = monthReportRequest.getStartTime();
		String end = monthReportRequest.getEndTime();

		Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
		String deviceType = monthReportRequest.getDeviceType();
		String item = monthReportRequest.getItem();
		Map<String, Object> dataMap = Maps.newHashMap();

		List<String> indexList = DateUtil.getIndexMonthList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
		log.info("index:{}", indexList);
		SearchRequestBuilder request = transportClient
				.prepareSearch(getClusterIndex(monthReportRequest, indexList.toArray(new String[] {})))
				.setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(startTime.getTime() / 1000);
		timeRange.lt(endTime.getTime() / 1000);
		queryBuilder.must(timeRange);

		if (StringUtils.isNotBlank(monthReportRequest.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", monthReportRequest.getIdcType());
			queryBuilder.must(queryTermId);
		}

		FormConditionUtil.getDeviceType(deviceType, item, queryBuilder);

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
		dateAgg.subAggregation(
				AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(this.INDEX_VALUE));

		request.addAggregation(dateAgg);
		request.addAggregation(PipelineAggregatorBuilders.statsBucket("stats_buckets3", "dateagg.avg_item"));
		// request.addAggregation(termsBuilder1New);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		/*
		 * if(response.getHits().getTotalHits() ==0) { return null; }
		 */
		if (null == dataMap || dataMap.size() == 0) {
			dataMap = new HashMap<>();
		}
		if (null == response.getAggregations() || null == response.getAggregations().get("stats_buckets3")) {
			return dataMap;
		}

		Stats stats = response.getAggregations().get("stats_buckets3");
		if (null != stats) {

			String avg = InstantUtils.getStatsValues("avg", stats);
			String max = InstantUtils.getStatsValues("max", stats);

			/*
			 * Max max = bt1.getAggregations().get(name); if (max == null ||
			 * "-Infinity".equals(max.getValueAsString()) ||
			 * "NaN".equals(max.getValueAsString())) { value = ""; } else { value =
			 * max.getValueAsString(); }
			 */

			if (StringUtils.isBlank(max)) {
				max = null;
			}
			if (StringUtils.isBlank(avg)) {
				avg = null;
			}
			StringBuffer sb = new StringBuffer();

			sb.append(item).append("_max");
			dataMap.put(sb.toString(), max);
			sb.setLength(0);
			sb.append(item).append("_avg");
			dataMap.put(sb.toString(), avg);
			dataMap.put("type", monthReportRequest.getType());

		}
		log.info("**monthReport-queryIdcTypeAllByMonth-end***********************************");
		return dataMap;
	}

	@Override
	public List<Map<String, Object>> getPoorEfficiencyDeviceMonthData(@RequestBody MonthReportRequest monthRequest)
			throws ParseException {

		log.info("**monthReport-getPoorEfficiencyDeviceData-begin****");
		String day = monthRequest.getStartTime();
		String[] indexDays = day.split(" ")[0].split("-");
		String item = monthRequest.getItem();
		String INDEX = this.INDEX_HISTORY_HOUR_PRE + indexDays[0] + indexDays[1] + "*";

		log.info("index:{}", INDEX);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthRequest, INDEX))
				.setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		if (StringUtils.isNotBlank(item)) {
			if (item.equals("cpu")) {
				queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(),"item"));
			} else {
				queryBuilder.must(iKpiKeyConfigBiz.query4Builder( KpiTypeEnum.MEMORY_PUSED.name(),"item"));
			}

		}
		request.setQuery(queryBuilder).setSize(0);
		TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("resourceId").field(term_key+".keyword")
				.size(100000);
		termsBuilder.subAggregation(AggregationBuilders.avg("avg_value").field("value_avg"));
		request.addAggregation(termsBuilder);
		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.execute().actionGet();
		List<Map<String, Object>> list = Lists.newArrayList();
		if(null==response.getAggregations()) {
			return null;
		}
		final Terms terms = response.getAggregations().get("resourceId");
		for (final Terms.Bucket a : terms.getBuckets()) {
			String resourceId = a.getKeyAsString();
			Map<String, Object> rangeMap = new HashMap<>();
			rangeMap.put("date", day);
			rangeMap.put("item", item);
			rangeMap.put("type", 2);
			InternalAvg avgInter = a.getAggregations().get("avg_value");
			Double avg = InstantUtils.getNumericValue(avgInter);
			rangeMap.put("avg_value_month", avg);
			rangeMap.put("resourceId", resourceId);
			list.add(rangeMap);
		}

		log.info("**monthReport-getPoorEfficiencyDeviceMonthData-end****");
		return list;

	}
	
	
	@Override
	public List<Map<String, Object>>  getPoorEfficiencyDeviceWeekData(@RequestBody MonthReportRequest monthRequest) throws ParseException{

		log.info("**monthReport-getPoorEfficiencyDeviceData-begin****");

		Date startTime = DateUtils.parseDate(monthRequest.getStartTime(), new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date endTime = DateUtils.parseDate(monthRequest.getEndTime(), new String[] { "yyyy-MM-dd HH:mm:ss" });
		String item = monthRequest.getItem();

		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
		
		log.info("index:{}", indexList);
		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthRequest,indexList.toArray(new String[] {})))
				.setExplain(true);
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		

		if (StringUtils.isNotBlank(item)) {
			if(item.equals("cpu")) {
				queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(),"item" ));
			}else {
				queryBuilder.must(iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.MEMORY_PUSED.name(),"item" ));
			}
		
		}
		request.setQuery(queryBuilder).setSize(0);
		TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("resourceId").field(term_key+".keyword").size(100000);
		termsBuilder.subAggregation(AggregationBuilders.avg("avg_value").field("value_avg"));
		request.addAggregation(termsBuilder);
		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.execute().actionGet();
		List<Map<String, Object>> list = Lists.newArrayList();
		if(null==response.getAggregations()) {
			return null;
		}
		final Terms terms = response.getAggregations().get("resourceId");
		for (final Terms.Bucket a : terms.getBuckets()) {
			String resourceId = a.getKeyAsString();
			Map<String, Object> rangeMap = new HashMap<>();
			rangeMap.put("date", monthRequest.getStartTime());
			rangeMap.put("item", item);
			rangeMap.put("type", 1);
			InternalAvg avgInter = a.getAggregations().get("avg_value");
			Double avg = InstantUtils.getNumericValue(avgInter);
			rangeMap.put("avg_value_week", avg);
			rangeMap.put("resourceId", resourceId);
			list.add(rangeMap);
		}
		
	   log.info("**monthReport-getPoorEfficiencyDeviceMonthData-end****");
		return list;

	}

}
