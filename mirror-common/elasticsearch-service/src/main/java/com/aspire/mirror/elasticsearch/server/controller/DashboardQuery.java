package com.aspire.mirror.elasticsearch.server.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
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
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTimeZone;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.elasticsearch.api.dto.DashboardResponse;
import com.aspire.mirror.elasticsearch.api.dto.DataSet;
import com.aspire.mirror.elasticsearch.api.dto.DataSetNew;
import com.aspire.mirror.elasticsearch.api.dto.DataSetRequest;
import com.aspire.mirror.elasticsearch.api.dto.DataSetsDto;
import com.aspire.mirror.elasticsearch.api.dto.Serie;
import com.aspire.mirror.elasticsearch.api.dto.monitor.MonitorCommonRequest;
import com.aspire.mirror.elasticsearch.api.service.monitor.IMonitorKpiService;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IDashboardQueryService;
import com.aspire.mirror.elasticsearch.server.enums.Constants;
import com.aspire.mirror.elasticsearch.server.util.DateUtil;
import com.aspire.mirror.elasticsearch.server.util.DateUtils;
import com.aspire.mirror.elasticsearch.server.util.FormConditionUtil;
import com.aspire.mirror.elasticsearch.server.util.InstantUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

@RestController
@Slf4j
public class DashboardQuery extends CommonController implements IDashboardQueryService {
	@Value("${dashboard.conditionField}")
	private String conditionField;
	@Value("${dashboard.xlineColumn}")
	private String xlineColumn;
	@Value("${dashboard.countField}")
	private String countField;
	@Value("${dashboard.size:10000}")
	private int size;
	@Value("${dashboard.xlineTime:datetime}")
	private String xlineTime;
	

	private final static String HISTORY_INDEX_PRE = "history-*";
	private final static String HISTORY_UINT_INDEX_PRE = "history_uint-*";
	private final static String HISTORY_All_INDEX_PRE = "history*-*";
	@Autowired
	private TransportClient transportClient;
	
	@Autowired
	private IMonitorKpiService monitorKpiController;

	@Override
	public DashboardResponse getDashboardJsonList(@RequestBody DataSetsDto dataSetsDto) throws Exception {
		Long l = System.currentTimeMillis();
		Date from = new Date();
		log.info("输入getDashboardJsonList--dataSetsDto:{}", JSONObject.fromObject(dataSetsDto).toString());
		DashboardResponse resultMap = null;

		for (int i = 0; i < dataSetsDto.getDataSets().size(); i++) {
			DataSet dataSet = dataSetsDto.getDataSets().get(i);
			dataSet.setConditionField(conditionField);
			dataSet.setCountField(countField);
			dataSet.setXLineColumn(xlineColumn);
			// 时间值格式转换，text类型值转换,默认时间
			DashboardResponse currentMap = new DashboardResponse();
			// 设置聚合后最小值，防止聚合数为0时得到的结果没有该项 ,基线和多数据集设置为0
			currentMap = getData(dataSet);
			if (i == 0) {
				resultMap = currentMap;
				continue;
			} else {
				// 整合数据集
				resultMap = complexMap(dataSet, resultMap, currentMap);
			}
		}

		log.info("输出getDashboardJsonList--DashboardResponse:");
		Date to = new Date();
		long interval = (to.getTime() - from.getTime()) / 1000;
		if (interval >= 6) {
			log.info("超时6s--dataSetsDto:" + JSONUtils.valueToString(dataSetsDto));
		}
		log.info("请求接口花费时间：" + (System.currentTimeMillis() - l) + "ms");
		return resultMap;
	}

	public DashboardResponse complexMap(DataSet dataSet, DashboardResponse targetMap, DashboardResponse sourceMap)
			throws Exception {

		List<Serie> targetMap_series = targetMap.getSeries();
		List<Serie> sourceMap_series = sourceMap.getSeries();
		if (sourceMap_series != null) {
			targetMap_series.addAll(sourceMap_series);
			targetMap.setSeries(targetMap_series);
		}

		List targetMap_legend = targetMap.getLegend();
		List sourceMap_legend = sourceMap.getLegend();
		if (sourceMap_legend != null) {
			targetMap_legend.addAll(sourceMap_legend);
			targetMap.setLegend(targetMap_legend);
		}

		return targetMap;
	}

	/**
	 * 查询单个数据集
	 */
	public DashboardResponse getData(@RequestBody DataSet dataSet) {
		// String TYPE = Constants.monitor_index_0;
		// String INDEX = "history-*";
		String INDEX_PRE = this.HISTORY_INDEX_PRE;
		if (StringUtils.isNotBlank(dataSet.getMointerIndex())) {
			if (dataSet.getMointerIndex().equals("3")) {
				INDEX_PRE = this.HISTORY_UINT_INDEX_PRE;
			} else {
				INDEX_PRE = this.HISTORY_INDEX_PRE;
			}
		}
		// log.info("DashboardQuery索引:{}",INDEX);
		// SearchRequestBuilder request =
		// transportClient.prepareSearch(INDEX).setExplain(true);
		Long rangeNowDate = null;
		Long rangeComDate = null;
		Date now = new Date();
		// 计算设置时间范围
		if (null == dataSet.getStartTime()) {
			rangeNowDate = new Date().getTime() / 1000;
			if (Constants.TIME_UNIT_HOUR.equals(dataSet.getMinTimeUnit())) {
				rangeComDate = DateUtils.addHour(now, -Integer.parseInt(dataSet.getMinTimeValue())).getTime() / 1000;
				// 分钟
			} else if (Constants.TIME_UNIT_DAY.equals(dataSet.getMinTimeUnit())) {
				rangeComDate = DateUtils.addDate(now, -Integer.parseInt(dataSet.getMinTimeValue())).getTime() / 1000;

			} else {
				throw new NullPointerException("无效的枚举类型");
			}
		} else {
			rangeComDate = dataSet.getStartTime();
			rangeNowDate = dataSet.getEndTime();
		}

		List<String> indexList = DateUtil.getIndexList(new Date(rangeComDate * 1000), new Date(rangeNowDate * 1000),
				INDEX_PRE);
		log.info("indexList:{}", indexList);
		SearchRequestBuilder request = transportClient
				.prepareSearch(getClusterIndex(dataSet, indexList.toArray(new String[] {}))).setExplain(true);

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		/*
		 * queryBuilder.must(QueryBuilders.queryStringQuery(dataSet.getCountField().
		 * replace(".keyword", "") + ":*") .analyzeWildcard(true));
		 */
		if (StringUtils.isNotBlank(dataSet.getSubItemIds())) {
			String[] items = dataSet.getSubItemIds().split("///,");
			QueryBuilder queryTermId = QueryBuilders.termsQuery(dataSet.getConditionField() + ".keyword", items);
			queryBuilder.must(queryTermId);
		} else {
			QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery(dataSet.getConditionField(), dataSet.getItemId());
			queryBuilder.must(queryTermId);
		}

		/*
		 * if (StringUtils.isNotBlank(dataSet.getIdcType())) { QueryBuilder queryTermId
		 * = QueryBuilders.termQuery("idcType.keyword", dataSet.getIdcType());
		 * queryBuilder.must(queryTermId); }
		 */

		setResource(dataSet.getResourceType(), dataSet.getResourceValue(), queryBuilder);
		// 设置查询时间范围
		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery(dataSet.getXLineColumn());
		timeRange.gte(rangeComDate);
		timeRange.lt(rangeNowDate);
		queryBuilder.must(timeRange);

		request.setQuery(queryBuilder).setTimeout(new TimeValue(1, TimeUnit.MINUTES));
		request.setSize(0);// .execute().actionGet();SearchResponse response =

		// 组装分组
		HistogramAggregationBuilder dateAgg = AggregationBuilders
				.histogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS);
		// 定义分组的日期字段
		dateAgg.field(dataSet.getXLineColumn());
		String format = "";
		DateFormat returndf = null;

		if (Constants.TIME_UNIT_HOUR.equals(dataSet.getIntervalUnit())) {
			Date comDate = DateUtils.addHour(now, -Integer.parseInt(dataSet.getIntervalTime()));

			dateAgg.interval((now.getTime() - comDate.getTime()) / 1000);
			format = "yyyy-MM-dd HH:mm:ss";
			// 分钟
		} else if (Constants.TIME_UNIT_MINUTE.equals(dataSet.getIntervalUnit())) {
			Date comDate = DateUtils.addSecond(now, -Integer.parseInt(dataSet.getIntervalTime()) * 60);

			dateAgg.interval((now.getTime() - comDate.getTime()) / 1000);
			format = "yyyy-MM-dd HH:mm";
		} else {
			throw new NullPointerException("无效的枚举类型");
		}
		returndf = new SimpleDateFormat(format);
		// 日期最大最小
		dateAgg.extendedBounds(rangeComDate, rangeNowDate);
		dateAgg.minDocCount(1);
		dateAgg = (HistogramAggregationBuilder) addAggregations(dataSet.getCountType(), dataSet.getCountField(),
				dateAgg);
		request.addAggregation(dateAgg);
		log.info("index:{},输入getData--request:{}", request.request().indices(), request);
		SearchResponse response = request.execute().actionGet();
		DashboardResponse re = new DashboardResponse();
		List<String> intervalTime = new ArrayList<>();
		List<Object> vaues = new ArrayList<>();

		List<Serie> series = new ArrayList<>();
		List<String> legend = new ArrayList<String>();
		legend.add(dataSet.getItemViewName());
		if (null != response.getAggregations()) {
			Histogram h = response.getAggregations().get(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS);
			// 得到一级聚合结果里面的分桶集合
			List<Bucket> bucketss = (List<Bucket>) h.getBuckets();
			log.info("输出bucketss size:" + bucketss.size());

			for (Bucket bt : bucketss) {
				// x轴
				if (StringUtils.isNotBlank(bt.getKeyAsString())) {
					if (bt.getKey().toString().contains("E")) {
						Double dd = (Double) bt.getKey();
						BigDecimal bd1 = new BigDecimal(dd * 1000);
						intervalTime.add(returndf.format(new Date(new Long(bd1.toPlainString()))));
					} else {
						intervalTime.add(returndf.format(bt.getKey().toString()));
					}
				}
				getDateAggValues(dataSet, vaues, bt);
			}
			// Y轴

			// return re;
		}
		Serie serie = new Serie();
		serie.setName(dataSet.getItemViewName());
		serie.setData(vaues);
		series.add(serie);
		re.setSeries(series);
		re.setLegend(legend);
		re.setXAxis(intervalTime);

		return re;
	}

	private void setResource(int type, String rs, BoolQueryBuilder queryBuilder) {
		if (type == 1) {
			QueryBuilder queryTermId = QueryBuilders.termQuery(Constants.BIZ_SYSTEM + ".keyword", rs);
			queryBuilder.must(queryTermId);
		} else if (type == 2) {
			QueryBuilder queryTermId = QueryBuilders.termQuery(Constants.IDC_TYPE + ".keyword", rs);
			queryBuilder.must(queryTermId);
		} else if (type == 3) {
			QueryBuilder queryTermId = QueryBuilders.termQuery(Constants.ROOM_ID + ".keyword", rs);
			queryBuilder.must(queryTermId);
		} else if (type == 4) {
			QueryBuilder queryTermId = QueryBuilders.termQuery(Constants.DEVICE_CLASS + ".keyword", rs);
			queryBuilder.must(queryTermId);
		} else if (type == 5) {
			QueryBuilder queryTermId = QueryBuilders.termQuery(Constants.DEVICE_TYPE + ".keyword", rs);
			queryBuilder.must(queryTermId);
		} else {
			String[] ips = rs.split(",");
			QueryBuilder queryIp = QueryBuilders.termsQuery(Constants.DEVICE_IP, ips);
			queryBuilder.must(queryIp);
		}
	}

	private List getDateAggValues(DataSet dataSet, List temp, Bucket bt1) {
		String aggregationType = dataSet.getCountType();
		String column = dataSet.getCountField();
		Object value = null;
		Map<String, Object> tmpMap = new HashMap<String, Object>();
		long count = bt1.getDocCount();

		tmpMap.put(Constants.AGGREGATION_TYPE_COUNT, count);
		if (Constants.AGGREGATION_TYPE_MAX.equals(aggregationType)) {

			Max max = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_MAX + "_" + column);
			if (max == null || "-Infinity".equals(max.getValueAsString())) {
				value = "";
			} else {
				value = max.getValue();
			}

		} else if (Constants.AGGREGATION_TYPE_MIN.equals(aggregationType)) {
			Min min = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_MIN + "_" + column);
			if (min == null || "-Infinity".equals(min.getValueAsString())) {
				value = "";
			} else {
				value = min.getValue();
			}

		} else if (Constants.AGGREGATION_TYPE_AVG.equals(aggregationType)) {
			Avg avg = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_AVG + "_" + column);
			if (avg == null || "-Infinity".equals(avg.getValueAsString())) {
				value = "";
			} else {
				value = avg.getValue();
			}

		} else if (Constants.AGGREGATION_TYPE_SUM.equals(aggregationType)) {
			Sum sum = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_SUM + "_" + column);
			if (sum == null || "-Infinity".equals(sum.getValueAsString())) {
				value = "";
			} else {
				value = sum.getValue();
			}

		} else if (Constants.AGGREGATION_TYPE_UNIQUECOUNT.equals(aggregationType)) {
			Cardinality cardinality = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_UNIQUECOUNT + "_" + column);
			if ("-Infinity".equals(cardinality.getValueAsString()) || cardinality == null) {
				value = "";
			} else {
				value = cardinality.getValue();
			}

		} else if (Constants.AGGREGATION_TYPE_COUNT.equals(aggregationType)) {
			value = count;
		}
		if (count == 0) {
			value = null;
		}
		if (value instanceof Double) {
			Boolean isok = Double.isNaN((double) value) || Double.isInfinite((double) value);
			if (!isok) {
				// 科学计数法转string
				if (value.toString().contains("E")) {
					Double dd = (Double) value;
					BigDecimal bd1 = new BigDecimal(dd);
					value = bd1.toPlainString();
				}
			}
		}
		temp.add(value);

		return temp;
	}

	/**
	 * 添加聚合类型
	 */
	private AggregationBuilder addAggregations(String aggregationType, String column, AggregationBuilder aggregation) {
		if (Constants.AGGREGATION_TYPE_MAX.equals(aggregationType)) {

			aggregation.subAggregation(
					AggregationBuilders.max(Constants.AGGREGATION_TYPE_MAX + "_" + column).field(column));
		} else if (Constants.AGGREGATION_TYPE_MIN.equals(aggregationType)) {

			aggregation.subAggregation(
					AggregationBuilders.min(Constants.AGGREGATION_TYPE_MIN + "_" + column).field(column));
		} else if (Constants.AGGREGATION_TYPE_AVG.equals(aggregationType)) {

			aggregation.subAggregation(
					AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_" + column).field(column));
		} else if (Constants.AGGREGATION_TYPE_SUM.equals(aggregationType)) {

			aggregation.subAggregation(
					AggregationBuilders.sum(Constants.AGGREGATION_TYPE_SUM + "_" + column).field(column));
		} else if (Constants.AGGREGATION_TYPE_COUNT.equals(aggregationType)) {

			aggregation.subAggregation(
					AggregationBuilders.count(Constants.AGGREGATION_TYPE_COUNT + "_" + column).field(column));
		} else if (Constants.AGGREGATION_TYPE_UNIQUECOUNT.equals(aggregationType)) {

			aggregation.subAggregation(AggregationBuilders
					.cardinality(Constants.AGGREGATION_TYPE_UNIQUECOUNT + "_" + column).field(column));
		}

		return aggregation;
	}

	/*
	 * 将时间转换为时间戳
	 */
	public String stampTodate(Date date, String dateFormat) throws ParseException {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);

		res = String.valueOf(simpleDateFormat.format(date));
		return res;
	}

	@Override
	public DashboardResponse queryDataList(@RequestBody DataSetRequest dataSetRequest) throws ParseException {
		String itemType = dataSetRequest.getItermType();
		DataSetNew dataSet = new DataSetNew();
		BeanUtils.copyProperties(dataSetRequest, dataSet);
		// dataSet.setCountType(dataSetRequest.getCountType());
		String deviceClass = dataSetRequest.getDeviceClass();
		dataSet.setType("terms");
		if (deviceClass.equals("服务器")) {
			if (itemType.equals("cpuUse")) {
				dataSet.setItemList(new ArrayList<>(Arrays.asList(Constants.CPU_ITEM.split(","))));
				dataSet.setMointerIndex("0");
			}
			if (itemType.equals("memoryUse")) {
				dataSet.setItemList(new ArrayList<>(Arrays.asList(Constants.MEMORY_ITEM.split(","))));
				dataSet.setMointerIndex("0");
			}
			if (itemType.equals("processLoad")) {
				dataSet.setItemList(new ArrayList<>(Arrays.asList(Constants.SERVER_PROCESS_LOAD.split("///,"))));
				dataSet.setMointerIndex("0");
			}
			if (itemType.equals("netIn")) {
				dataSet.setItem(Constants.NET_IN_ITEM);
				dataSet.setType("match");
				dataSet.setMointerIndex("3");
				dataSet.setOperator("/");
				dataSet.setOperatorValue(1000 * 1000 * 125l);
			}
			if (itemType.equals("netOut")) {
				dataSet.setItem(Constants.NET_OUT_ITEM);
				dataSet.setType("match");
				dataSet.setMointerIndex("3");
				dataSet.setOperator("/");
				dataSet.setOperatorValue(1000 * 1000 * 125l);
			}
			if (itemType.equals("diskUse")) {
				dataSet.setItem(Constants.SERVER_DISK_USAGE);
				dataSet.setSuffixItem(Constants.SERVER_DISK_SUFFIX_USAGE);
				dataSet.setType("wildcard");
				dataSet.setMointerIndex("0");
			}
		}
		if (deviceClass.equals("网络设备")) {
			if (itemType.equals("netOut")) {
				dataSet.setItem(Constants.NET_OUT_ITEM_NET);
				dataSet.setMointerIndex("3");
				dataSet.setType("match");
				dataSet.setOperator("/");
				dataSet.setOperatorValue(1000 * 1000 * 125l);
			}
			
			if (itemType.equals("netIn")) {
				dataSet.setItem(Constants.NET_IN_ITEM_NET);
				dataSet.setMointerIndex("3");
				dataSet.setType("match");
				dataSet.setOperator("/");
				dataSet.setOperatorValue(1000 * 1000 * 125l);
			}
			
			if (itemType.equals("inError")) {
				dataSet.setItem(Constants.IF_IN_ERRORS);
				dataSet.setMointerIndex("3");
				dataSet.setType("match");
				//dataSet.setIntervalTime(10+"");
			}
			
			if (itemType.equals("outError")) {
				dataSet.setItem(Constants.IF_OUT_ERRORS);
				dataSet.setMointerIndex("3");
				dataSet.setType("match");
				//dataSet.setIntervalTime(10+"");
			}
		}
		
		return getDataNew2(dataSet);
	}
	
	@Override
	public Map<String,Object> queryLatestData(@RequestBody DataSetRequest dataSetRequest) throws ParseException {
		String itemType = dataSetRequest.getItermType();
		DataSetNew dataSet = new DataSetNew();
		BeanUtils.copyProperties(dataSetRequest, dataSet);
		// dataSet.setCountType(dataSetRequest.getCountType());
		String deviceClass = dataSetRequest.getDeviceClass();
		if (deviceClass.equals("服务器")) {
			if (itemType.equals("sysTime")) {
				dataSet.setItemList(new ArrayList<>(Arrays.asList(Constants.SYS_RUNTIME.split("///,"))));
				dataSet.setMointerIndex("3");
				dataSet.setType("terms");
			}
			
			if (itemType.equals("memorySize")) {
				dataSet.setItem(Constants.MEMORY_SIZE);
				dataSet.setMointerIndex("3");
				dataSet.setType("term");
				dataSet.setOperator("/");
				dataSet.setOperatorValue(1024 *1024*1024l);
			}
			
		}
		if (deviceClass.equals("网络设备")) {
			if (itemType.equals("sysTime")) {
				dataSet.setItem(Constants.SYS_RUNTIME_NET);
				dataSet.setMointerIndex("3");
				dataSet.setType("term");
			}
			if (itemType.equals("pingTime")) {
				dataSet.setItem(Constants.SYS_ICMPPINGSEC_NET);
				dataSet.setMointerIndex("0");
				dataSet.setType("match");
			}
			
		}

		return getLatestData(dataSet);
	}

	public Map<String,Object> getLatestData(@RequestBody DataSetNew dataSet) throws ParseException {

		String INDEX_PRE = this.HISTORY_INDEX_PRE;
		if (StringUtils.isNotBlank(dataSet.getMointerIndex())) {
			if (dataSet.getMointerIndex().equals("3")) {
				INDEX_PRE = this.HISTORY_UINT_INDEX_PRE;
			} else {
				INDEX_PRE = this.HISTORY_INDEX_PRE;
			}
		}
		Long rangeNowDate = dataSet.getEndTime();
		Long rangeComDate = dataSet.getStartTime();

		List<String> indexList = DateUtil.getIndexList(new Date(rangeComDate), new Date(rangeNowDate), INDEX_PRE);
		log.info("indexList:{}", indexList);
		// SearchRequestBuilder request =
		// transportClient.prepareSearch(getClusterIndex(dataSet,indexList.toArray(new
		// String[] {}))).setExplain(true);

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		String type = dataSet.getType();
		if (type.equals("terms")) {
			List<String> items = dataSet.getItemList();
			QueryBuilder queryTermId = QueryBuilders.termsQuery("item.keyword", items);
			queryBuilder.must(queryTermId);
		} else if (type.equals("term")) {
		    String item = dataSet.getItem();
			QueryBuilder queryTermId = QueryBuilders.termQuery("item.keyword", item);
			queryBuilder.must(queryTermId);
		}else if (type.equals("wildcard")) {
			QueryBuilder queryTermId = QueryBuilders.wildcardQuery("item.keyword",
					dataSet.getItem() + "*" + dataSet.getSuffixItem());
			queryBuilder.must(queryTermId);
		} else {
			QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("item", dataSet.getItem());
			queryBuilder.must(queryTermId);
		}

		if (StringUtils.isNotBlank(dataSet.getInstanceId())) {
			queryBuilder.must(QueryBuilders.termQuery("resourceId.keyword", dataSet.getInstanceId()));
		}

		// 设置查询时间范围
		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("datetime");
		timeRange.gte(rangeComDate);
		timeRange.lt(rangeNowDate );
		queryBuilder.must(timeRange);

		
		Map<String,Object> map = Maps.newHashMap();
		SearchRequestBuilder request = transportClient
				.prepareSearch(getClusterIndex(dataSet, indexList.toArray(new String[] {})))
				.setSearchType(SearchType.DEFAULT).setQuery(queryBuilder).setSize(1)
				.addSort("clock", SortOrder.DESC);
		log.info("index:{}", JSON.toJSONString(request.request().indices()));
		log.info("输入getData--request:{}", request);
		SearchResponse scrollResponse = request.execute().actionGet();
		log.info("get es Data--return");
	
		
		if(scrollResponse.getHits().getHits().length > 0) {
			Map<String, Object> sourceAsMap = scrollResponse.getHits().getHits()[0].getSourceAsMap();
			String datetime = sourceAsMap.get(this.xlineTime).toString();
			String time = formateTime(datetime);
			String item = sourceAsMap.get("item").toString();
			Object value = sourceAsMap.get("value");
			value = getValue(value, dataSet);
			map.put("value", value);
			map.put("time", time);
			map.put("item", item);
		}
		
		
		return map;
	}

	
	
	public DashboardResponse getDataNew(@RequestBody DataSetNew dataSet) throws ParseException {

		String INDEX_PRE = this.HISTORY_INDEX_PRE;
		if (StringUtils.isNotBlank(dataSet.getMointerIndex())) {
			if (dataSet.getMointerIndex().equals("3")) {
				INDEX_PRE = this.HISTORY_UINT_INDEX_PRE;
			} else {
				INDEX_PRE = this.HISTORY_INDEX_PRE;
			}
		}
		Long rangeNowDate = dataSet.getEndTime();
		Long rangeComDate = dataSet.getStartTime();

		List<String> indexList = DateUtil.getIndexList(new Date(rangeComDate), new Date(rangeNowDate), INDEX_PRE);
		log.info("indexList:{}", indexList);
		// SearchRequestBuilder request =
		// transportClient.prepareSearch(getClusterIndex(dataSet,indexList.toArray(new
		// String[] {}))).setExplain(true);

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		String type = dataSet.getType();
		if (type.equals("terms")) {
			List<String> items = dataSet.getItemList();
			QueryBuilder queryTermId = QueryBuilders.termsQuery("item.keyword", items);
			queryBuilder.must(queryTermId);
		} else if (type.equals("term")) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("item.keyword", dataSet.getItem());
			queryBuilder.must(queryTermId);
		} else if (type.equals("wildcard")) {
			QueryBuilder queryTermId = QueryBuilders.wildcardQuery("item.keyword",
					dataSet.getItem() + "*" + dataSet.getSuffixItem());
			queryBuilder.must(queryTermId);
		} else {
			QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("item", dataSet.getItem());
			queryBuilder.must(queryTermId);
		}

		if (StringUtils.isNotBlank(dataSet.getInstanceId())) {
			queryBuilder.must(QueryBuilders.termQuery("resourceId.keyword", dataSet.getInstanceId()));
		}

		// 设置查询时间范围
		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("datetime");
		timeRange.gte(rangeComDate);
		timeRange.lt(rangeNowDate);
		queryBuilder.must(timeRange);

		
		

		SearchRequestBuilder request = transportClient
				.prepareSearch(getClusterIndex(dataSet, indexList.toArray(new String[] {}))).setExplain(true);
		request.setQuery(queryBuilder);
		  TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("item").field("item" +
	                ".keyword").size(10000);
	        DateHistogramAggregationBuilder dateAgg =  AggregationBuilders
					.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime")
					.dateHistogramInterval(DateHistogramInterval.minutes(5)).format("yyyy-MM-dd HH:mm:ss").timeZone(DateTimeZone.getDefault());
	        dateAgg.extendedBounds(new ExtendedBounds(rangeComDate, rangeNowDate));
	        dateAgg.minDocCount(0);
	        dateAgg.subAggregation(AggregationBuilders.avg("avg").field("value"));
	        termsBuilder.subAggregation(dateAgg);
	        if(dataSet.isCountTypeFlag()) {
				termsBuilder.subAggregation(AggregationBuilders.avg("avg_used").field("value"));
				termsBuilder.subAggregation(AggregationBuilders.max("max_used").field("value"));
				
			}
	        request.addAggregation(termsBuilder).setSize(0);
	        
		
		
		log.info("index:{}", JSON.toJSONString(request.request().indices()));
		log.info("输入getData--request:{}", request);
		SearchResponse scrollResponse = request.execute().actionGet();
		log.info("get es Data--return");
		
		//设置返回
		DashboardResponse re = new DashboardResponse();
		List<String> legend = Lists.newArrayList();
		List<String> xAxis = Lists.newArrayList();
		re.setSeries(Lists.newArrayList());
		re.setLegend(legend);
		re.setXAxis(xAxis);
		if (null == scrollResponse.getAggregations()) {
			return re;
		}
		Map<String, Serie> tmpMap = new HashMap<>();
			final Terms terms = scrollResponse.getAggregations().get("item");
			
			for (final Terms.Bucket a : terms.getBuckets()) {
				String item = a.getKeyAsString();
				Serie s = new Serie();
				s.setName(item);
				s.setData(Lists.newArrayList());
				tmpMap.put(item, s);
				Histogram h = a.getAggregations().get(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS);
	            for (Bucket b : h.getBuckets()) {
	            	String time = b.getKeyAsString();
	            	InternalAvg avg = b.getAggregations().get("avg");
	            	Double avgValue = InstantUtils.getNumericValue(avg);
	            	Object value = avgValue;
	            	if(null!=avgValue) {
	            		value = getValue(value, dataSet);
	            	}
	            	setListData(xAxis, tmpMap, item, value, time);
	            }
				
	            if(dataSet.isCountTypeFlag()) {
	            	Double max = InstantUtils.getValue(a.getAggregations().get("max_used"));
					Double avg = InstantUtils.getValue(a.getAggregations().get("avg_used"));
					Object max1 = getValue(max, dataSet);
					Object avg1 = getValue(avg, dataSet);
					s.setMax(max1);
					s.setAvg(avg1);
	            }
				tmpMap.put(item, s);
				legend.add(item);
			}
		//mergeValue(tmpMap,xAxis);
		re.setSeries(new ArrayList(tmpMap.values()));
		return re;
	}
	
	
	public DashboardResponse getDataNew2(@RequestBody DataSetNew dataSet) throws ParseException {

		String INDEX_PRE = this.HISTORY_INDEX_PRE;
		if (StringUtils.isNotBlank(dataSet.getMointerIndex())) {
			if (dataSet.getMointerIndex().equals("3")) {
				INDEX_PRE = this.HISTORY_UINT_INDEX_PRE;
			} else {
				INDEX_PRE = this.HISTORY_INDEX_PRE;
			}
		}
		Long rangeNowDate = dataSet.getEndTime();
		Long rangeComDate = dataSet.getStartTime();

		List<String> indexList = DateUtil.getIndexList(new Date(rangeComDate), new Date(rangeNowDate), INDEX_PRE);
		log.info("indexList:{}", indexList);
		

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		String type = dataSet.getType();
		if (type.equals("terms")) {
			List<String> items = dataSet.getItemList();
			QueryBuilder queryTermId = QueryBuilders.termsQuery("item.keyword", items);
			queryBuilder.must(queryTermId);
		} else if (type.equals("term")) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("item.keyword", dataSet.getItem());
			queryBuilder.must(queryTermId);
		} else if (type.equals("wildcard")) {
			QueryBuilder queryTermId = QueryBuilders.wildcardQuery("item.keyword",
					dataSet.getItem() + "*" + dataSet.getSuffixItem());
			queryBuilder.must(queryTermId);
		} else {
			QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("item", dataSet.getItem());
			queryBuilder.must(queryTermId);
		}

		if (StringUtils.isNotBlank(dataSet.getInstanceId())) {
			queryBuilder.must(QueryBuilders.termQuery("resourceId.keyword", dataSet.getInstanceId()));
		}

		// 设置查询时间范围
		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("datetime");
		timeRange.gte(rangeComDate);
		timeRange.lt(rangeNowDate);
		queryBuilder.must(timeRange);

		
		

		SearchRequestBuilder request = transportClient
				.prepareSearch(getClusterIndex(dataSet, indexList.toArray(new String[] {}))).setExplain(true);
		request.setQuery(queryBuilder);
		/*
		 * int granularity = 5; if(StringUtils.isNotBlank(dataSet.getIntervalTime())) {
		 * granularity = Integer.parseInt(dataSet.getIntervalTime()); }
		 */
	        DateHistogramAggregationBuilder dateAgg =  AggregationBuilders
					.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime")
					.dateHistogramInterval(DateHistogramInterval.minutes(10)).format("yyyy-MM-dd HH:mm:ss").timeZone(DateTimeZone.getDefault());
	        dateAgg.extendedBounds(new ExtendedBounds(rangeComDate, rangeNowDate));
	        dateAgg.minDocCount(1);
	        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("item").field("item" +
	                ".keyword").size(10000);
	        termsBuilder.subAggregation(AggregationBuilders.avg("avg").field("value"));
	        dateAgg.subAggregation(termsBuilder);
	        if(dataSet.isCountTypeFlag()) {
	        	 TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("itemBuckts").field("item" +
	 	                ".keyword").size(10000);
	        	 termsBuilder1.subAggregation(AggregationBuilders.avg("avg_used").field("value"));
	        	 termsBuilder1.subAggregation(AggregationBuilders.max("max_used").field("value"));
	        	  request.addAggregation(termsBuilder1);
			}
	        request.addAggregation(dateAgg).setSize(0);
	        
		
		
		log.info("index:{}", JSON.toJSONString(request.request().indices()));
		log.info("输入getData--request:{}", request);
		SearchResponse scrollResponse = request.execute().actionGet();
		log.info("get es Data--return");
		
		//设置返回
		DashboardResponse re = new DashboardResponse();
		List<String> legend = Lists.newArrayList();
		List<String> xAxis = Lists.newArrayList();
		re.setSeries(Lists.newArrayList());
		re.setLegend(legend);
		re.setXAxis(xAxis);
		if (null == scrollResponse.getAggregations()) {
			return re;
		}
		Map<String, Serie> tmpMap = new HashMap<>();
			 
	Histogram h = scrollResponse.getAggregations().get(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS);
            for (Bucket b : h.getBuckets()) {
            	String time = b.getKeyAsString();
            	xAxis.add(time);
            	 Terms terms = b.getAggregations().get("item");
            	 for (final Terms.Bucket a : terms.getBuckets()) {
            		 String item = a.getKeyAsString();
            		 InternalAvg avg = a.getAggregations().get("avg");
                 	Double avgValue = InstantUtils.getNumericValue(avg);
                 	Object value = avgValue;
                 	if(null!=avgValue) {
                 		value = getValue(value, dataSet);
                 	}
                 	setListData2(xAxis, tmpMap, item, value);
                 	if(!legend.contains(item)) {
                 		legend.add(item);
                 	}
            	 }
            	
            	
            }
			
            if(dataSet.isCountTypeFlag()) {
            	 Terms terms = scrollResponse.getAggregations().get("itemBuckts");
            	 for (final Terms.Bucket a : terms.getBuckets()) {
            		 String item = a.getKeyAsString();
            		 Double max = InstantUtils.getValue(a.getAggregations().get("max_used"));
     				Double avg = InstantUtils.getValue(a.getAggregations().get("avg_used"));
     				Object max1 = getValue(max, dataSet);
     				Object avg1 = getValue(avg, dataSet);
     				if(tmpMap.containsKey(item)) {
     					tmpMap.get(item).setMax(max1);
     					tmpMap.get(item).setAvg(avg1);
     				}
            	 }
            	
            }
		
		mergeValue(tmpMap,xAxis);
		re.setSeries(new ArrayList(tmpMap.values()));
		/*
		 * Collections.sort(re.getSeries(), new Comparator<Serie>(){
		 * 
		 * @Override public int compare(Serie o1, Serie o2) { if(null==o1.getAvg() &&
		 * null==o2.getAvg()) { return 0; } if(null==o1.getAvg()) { return 1; }
		 * if(null==o2.getAvg()) { return -1; } double a1 =
		 * Double.parseDouble(o2.getAvg().toString()); double a2 =
		 * Double.parseDouble(o1.getAvg().toString()); if(a1>a2) { return 1; }else
		 * if(a1==a2) { return 0; }else { return -1; } } });
		 */

		return re;
	}

	
	private void mergeValue(Map<String, Serie> tmpMap, List<String> xAxis) {
		int size = xAxis.size();
		if(xAxis.size()==0) {
			return;
		}
		
		for(Map.Entry<String, Serie> en:tmpMap.entrySet()) {
			List<Object> data = en.getValue().getData();
			if(data.size()<size) {
				for(int i=data.size();i<size;i++) {
					data.add(null);
				}
			}
		}
		
		
	}

	Object getValue(Object value, DataSetNew dataSet) {
		if (null == value) {
			return null;
		}
		Double value1 = Double.parseDouble(value.toString());

		if (StringUtils.isNotBlank(dataSet.getOperator())) {
			if (dataSet.getOperator().equals("/")) {
				value1 = value1 / dataSet.getOperatorValue();
			} else {
				value1 = value1 * dataSet.getOperatorValue();
			}
		}
		value = FormConditionUtil.formatDouble(value1);
		// 科学计数法转string
		if (value.toString().contains("E")) {
			Double dd = (Double) value;
			BigDecimal bd1 = new BigDecimal(dd);
			value = bd1.toPlainString();
		}
		return value;
	}

	String formateTime(String timeStr) throws ParseException {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
		// SDF.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		Date dateTime = SDF.parse(timeStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateTime);
		cal.add(Calendar.HOUR, 8);
		SimpleDateFormat Time3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String starttime = Time3.format(cal.getTime());
		return starttime;
	}
	private void setListData2(List<String> xAxis, Map<String, Serie> tmpMap, String item, Object value) {
		if(tmpMap.containsKey(item)) {
			Serie s = tmpMap.get(item);
			 List<Object> data = s.getData();
			if(data.size()==xAxis.size()-1) {
				data.add(value);
			}else {
				int dataSize = data.size();
				for(int i=0;i<xAxis.size()-1;i++) {
					if(dataSize<=i) {
						data.add(null);
					}
				}
				data.add(value);
			}
		}else {
			Serie s = new Serie();
			s.setName(item);
			s.setData(Lists.newArrayList());
			for(int i=0;i<xAxis.size()-1;i++) {
					s.getData().add(null);
			}
			s.getData().add(value);
			tmpMap.put(item, s);
		}
	}
	private void setListData(List<String> xAxis, Map<String, Serie> tmpMap, String item, Object value, String time) {
		
		  if(!xAxis.contains(time)) { xAxis.add(time); }
		  tmpMap.get(item).getData().add(value);

	}

	private List getDateAggValuesNew(String aggregationType, List temp, Bucket bt1) {
		Object value = null;
		Map<String, Object> tmpMap = new HashMap<String, Object>();
		long count = bt1.getDocCount();

		tmpMap.put(Constants.AGGREGATION_TYPE_COUNT, count);
		if (Constants.AGGREGATION_TYPE_MAX.equals(aggregationType)) {

			Max max = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_MAX);
			if (max == null || "-Infinity".equals(max.getValueAsString())) {
				value = "";
			} else {
				value = max.getValue();
			}

		} else if (Constants.AGGREGATION_TYPE_MIN.equals(aggregationType)) {
			Min min = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_MIN);
			if (min == null || "-Infinity".equals(min.getValueAsString())) {
				value = "";
			} else {
				value = min.getValue();
			}

		} else if (Constants.AGGREGATION_TYPE_AVG.equals(aggregationType)) {
			Avg avg = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_AVG);
			if (avg == null || "-Infinity".equals(avg.getValueAsString())) {
				value = "";
			} else {
				value = avg.getValue();
			}

		} else if (Constants.AGGREGATION_TYPE_SUM.equals(aggregationType)) {
			Sum sum = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_SUM);
			if (sum == null || "-Infinity".equals(sum.getValueAsString())) {
				value = "";
			} else {
				value = sum.getValue();
			}

		} else if (Constants.AGGREGATION_TYPE_UNIQUECOUNT.equals(aggregationType)) {
			Cardinality cardinality = bt1.getAggregations().get(Constants.AGGREGATION_TYPE_UNIQUECOUNT);
			if ("-Infinity".equals(cardinality.getValueAsString()) || cardinality == null) {
				value = "";
			} else {
				value = cardinality.getValue();
			}

		} else if (Constants.AGGREGATION_TYPE_COUNT.equals(aggregationType)) {
			value = count;
		}
		if (count == 0) {
			value = null;
		}
		if (value instanceof Double) {
			Boolean isok = Double.isNaN((double) value) || Double.isInfinite((double) value);
			if (!isok) {
				// 科学计数法转string
				if (value.toString().contains("E")) {
					Double dd = (Double) value;
					BigDecimal bd1 = new BigDecimal(dd);
					value = bd1.toPlainString();
				} else {
					Double dd = (Double) value;
					value = FormConditionUtil.formatDouble(dd);
				}
			}
		}
		temp.add(value);

		return temp;
	}
	
	@Override
	public List<Map<String, Object>> queryTermDatas(@RequestBody DataSetRequest dataSetRequest) throws ParseException {
		String itemType = dataSetRequest.getItermType();
		DataSetNew dataSet = new DataSetNew();
		BeanUtils.copyProperties(dataSetRequest, dataSet);
		// dataSet.setCountType(dataSetRequest.getCountType());
		String deviceClass = dataSetRequest.getDeviceClass();
		if (deviceClass.equals("网络设备")) {
			if (itemType.equals("interfaceCount")) {
				dataSet.setItem(Constants.SYS_INTERFACE_NET);
				dataSet.setMointerIndex("3");
				dataSet.setType("match");
			}
			
			if (itemType.equals("itemCount")) {
				dataSet.setMointerIndex("2");
			}
		}

		return getTermData(dataSet);
	}
	
	public List<Map<String, Object>> getTermData(@RequestBody DataSetNew dataSet){
		String INDEX_PRE = this.HISTORY_INDEX_PRE;
		if (StringUtils.isNotBlank(dataSet.getMointerIndex())) {
			if (dataSet.getMointerIndex().equals("3")) {
				INDEX_PRE = this.HISTORY_UINT_INDEX_PRE;
			} else if(dataSet.getMointerIndex().equals("0")){
				INDEX_PRE = this.HISTORY_INDEX_PRE;
			}else {
				INDEX_PRE = this.HISTORY_All_INDEX_PRE;
			}
		}
		Long rangeNowDate = dataSet.getEndTime();
		Long rangeComDate = dataSet.getStartTime();
		//String itemType = dataSet.getItemType();

		List<String> indexList = DateUtil.getIndexList(new Date(rangeComDate), new Date(rangeNowDate), INDEX_PRE);
		log.info("indexList:{}", indexList);
		// SearchRequestBuilder request =
		// transportClient.prepareSearch(getClusterIndex(dataSet,indexList.toArray(new
		// String[] {}))).setExplain(true);

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		String type = dataSet.getType();
		if(StringUtils.isNotBlank(type)) {
			if (type.equals("terms")) {
				List<String> items = dataSet.getItemList();
				QueryBuilder queryTermId = QueryBuilders.termsQuery("item.keyword", items);
				queryBuilder.must(queryTermId);
			} else if (type.equals("term")) {
			    String item = dataSet.getItem();
				QueryBuilder queryTermId = QueryBuilders.termQuery("item.keyword", item);
				queryBuilder.must(queryTermId);
			}else if (type.equals("wildcard")) {
				QueryBuilder queryTermId = QueryBuilders.wildcardQuery("item.keyword",
						dataSet.getItem() + "*" + dataSet.getSuffixItem());
				queryBuilder.must(queryTermId);
			} else {
				QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("item", dataSet.getItem());
				queryBuilder.must(queryTermId);
			}
		}
		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("datetime");
		timeRange.gte(rangeComDate );
		timeRange.lt(rangeNowDate);
		queryBuilder.must(timeRange);

		if (StringUtils.isNotBlank(dataSet.getInstanceId())) {
			queryBuilder.must(QueryBuilders.termQuery("resourceId.keyword", dataSet.getInstanceId()));
		}
		
		CardinalityAggregationBuilder termsBuilder = AggregationBuilders.cardinality("name").field("item.keyword");
	
		
		com.alibaba.fastjson.JSONObject jo = new com.alibaba.fastjson.JSONObject();
		jo.put("query", com.alibaba.fastjson.JSONObject.parse(queryBuilder.toString()));
		jo.put("aggs", com.alibaba.fastjson.JSONObject.parse(termsBuilder.toString()));
		MonitorCommonRequest monitorCommonRequest = new MonitorCommonRequest();
		monitorCommonRequest.setDsl(jo);
		monitorCommonRequest.setIndex(String.join(",",indexList));
		return monitorKpiController.query(monitorCommonRequest);
		
	}
}
