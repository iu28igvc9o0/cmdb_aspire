package com.aspire.mirror.elasticsearch.server.controller.network;

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
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.elasticsearch.api.dto.NetPerformanceAnalysis;
import com.aspire.mirror.elasticsearch.api.dto.NetSetDto;
import com.aspire.mirror.elasticsearch.api.service.zabbix.INetPerformanceAnalysisService;
import com.aspire.mirror.elasticsearch.server.controller.CommonController;
import com.aspire.mirror.elasticsearch.server.enums.Constants;
import com.aspire.mirror.elasticsearch.server.util.DateUtil;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @author longfeng
 * @title: NetworkStrategyController
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2015:53
 */
@RestController
@Slf4j
public class NetPerformanceAnalysisController extends CommonController implements INetPerformanceAnalysisService {
	
	@Value("${INDEX_HISTORY_DAY_PRE_NEW:kpi-day*_}")
	private   String INDEX_HISTORY_DAY_all_PRE;

	@Autowired
	private TransportClient transportClient;

	// 查询网络性能
	@Override
	public List<NetPerformanceAnalysis> getData(@RequestBody NetSetDto netSetDto) throws ParseException {
	
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		Date start = DateUtils.parseDate(netSetDto.getStartTime(), new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date end = DateUtils.parseDate(netSetDto.getEndTime(), new String[] { "yyyy-MM-dd HH:mm:ss" });
		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(start.getTime() / 1000);
		timeRange.lte(end.getTime() / 1000);
		queryBuilder.must(timeRange);
		
		
		 List<String> indexList = DateUtil.getIndexMonthList(start,end,INDEX_HISTORY_DAY_all_PRE);
			log.info("indexList:{}",indexList);
			SearchRequestBuilder	 request = transportClient.prepareSearch(getClusterIndex(netSetDto,indexList.toArray(new String[] {}))).setExplain(true);

		QueryBuilder queryClassId = QueryBuilders.termsQuery("deviceClass.keyword", "网络设备");
		queryBuilder.must(queryClassId);
		
		if (StringUtils.isNotBlank(netSetDto.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("idcType.keyword", netSetDto.getIdcType());
			queryBuilder.must(queryTermId);
		}
		if(null!=netSetDto.getIps()) {
			if (netSetDto.getIps().size()>1) {
				QueryBuilder queryIp = QueryBuilders
						.constantScoreQuery(QueryBuilders.termsQuery(Constants.DEVICE_IP + ".keyword", netSetDto.getIps()));
				queryBuilder.must(queryIp);
			} else {
				QueryBuilder queryIp = QueryBuilders
						.constantScoreQuery(QueryBuilders.termQuery(Constants.DEVICE_IP + ".keyword", netSetDto.getIps().get(0)));
				queryBuilder.must(queryIp);
			}
		}
		
		BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();
		QueryBuilder itemQb = QueryBuilders
				.constantScoreQuery(QueryBuilders.matchPhraseQuery("item", "cpuUsage"));
		shouldBuilder.should(itemQb);
		shouldBuilder.should( QueryBuilders
				.constantScoreQuery(QueryBuilders.matchPhraseQuery("item", "memUsage")));
		//queryBuilder.must(itemQb);
		queryBuilder.must(shouldBuilder);
		

		request.setQuery(queryBuilder).setSize(0);
		
		
		QueryBuilder cpuItemQb = QueryBuilders.matchPhraseQuery("item", "cpuUsage");
		
		QueryBuilder memItemQb = QueryBuilders.matchPhraseQuery("item", "memUsage");
		
		FilterAggregationBuilder  cpuFilter = AggregationBuilders.filter("cpuFilter", cpuItemQb);
		
		FilterAggregationBuilder  memFilter = AggregationBuilders.filter("memFilter", memItemQb);
		
		TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("host").field("host.keyword");
		if(null!=netSetDto.getSize()) {
			termsBuilder.size(netSetDto.getSize());
		}else {
			termsBuilder.size(100000);
		}
		//TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("item").field("item.keyword").size(100000);

		termsBuilder.subAggregation(AggregationBuilders.avg("avg_used").field("value_avg"));
		termsBuilder.subAggregation(AggregationBuilders.max("max_used").field("value_max"));
		termsBuilder.subAggregation(AggregationBuilders.min("min_used").field("value_min"));
		//termsBuilder.subAggregation(termsBuilder1);
		cpuFilter.subAggregation(termsBuilder);
		memFilter.subAggregation(termsBuilder);
		request.addAggregation(cpuFilter);
		request.addAggregation(memFilter);

		log.info("查询网络性能利用率es脚本： {}", request);
		SearchResponse response = request.get();
		
		Map<String,NetPerformanceAnalysis> mapNet = new HashMap<>();
		if (response.getHits().getTotalHits() > 0L) {
			final InternalFilter cpuTerms = response.getAggregations().get("cpuFilter");
				final Terms terms = cpuTerms.getAggregations().get("host");
				setData(mapNet,terms,true);
			final InternalFilter memTerms = response.getAggregations().get("memFilter");
				final Terms terms1 = memTerms.getAggregations().get("host");
				setData(mapNet,terms1,false);
		}
		
		List<NetPerformanceAnalysis> returnList = Lists.newArrayList();
		if(mapNet.size()>0) {
			returnList =new ArrayList<>(mapNet.values());
		}
		return returnList;
	}
	
	public void setData(Map<String,NetPerformanceAnalysis> mapNet,Terms terms,boolean isCpuFlag) {
		for (final Terms.Bucket a : terms.getBuckets()) {

			String ip = a.getKeyAsString();

			Max max = a.getAggregations().get("max_used");
			Avg avg = a.getAggregations().get("avg_used");
			Min min = a.getAggregations().get("min_used");
			NetPerformanceAnalysis net = new NetPerformanceAnalysis();
			if (mapNet.containsKey(ip)) {
				net = mapNet.get(ip);
			} else {
				net.setIp(ip);
				mapNet.put(ip, net);

			}
			if(isCpuFlag) {
				net.setCpuAvg((double)Math.round(avg.getValue()*100)/100);
				net.setCpuMax((double)Math.round(max.getValue()*100)/100);
				net.setCpuMin((double)Math.round(min.getValue()*100)/100);
			}else {
				net.setMemAvg((double) Math.round(avg.getValue() * 100) / 100);
				net.setMemMax((double) Math.round(max.getValue() * 100) / 100);
				net.setMemMin((double) Math.round(min.getValue() * 100) / 100);
			}
			
			// Terms terms1 = a.getAggregations().get("item");

		}
	}

	@Override
	public Map<String, Object> getTrendsData(@RequestParam(value = "startTime") String startTime,
			@RequestParam(value = "endTime") String endTime, @RequestParam(value = "ip") String ip,
			@RequestParam(value = "granularity") String granularity,
			@RequestParam(value = "monitorFlag") String monitorFlag,
			@RequestParam(value = "idcType", required = false) String idcType) throws ParseException {

		//SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setExplain(true);

		if (StringUtils.isBlank(ip)) {
			log.error("ip不能为空");
			return null;
		}

		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		Date start = DateUtils.parseDate(startTime, new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date end = DateUtils.parseDate(endTime, new String[] { "yyyy-MM-dd HH:mm:ss" });
		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(start.getTime() / 1000);
		timeRange.lt(end.getTime() / 1000);
		queryBuilder.must(timeRange);

		 List<String> indexList = DateUtil.getIndexMonthList(start,end,INDEX_HISTORY_DAY_all_PRE);
			log.info("indexList:{}",indexList);
			SearchRequestBuilder	 request = transportClient.prepareSearch(getClusterIndex(null,indexList.toArray(new String[] {}))).setExplain(true);

		
		
		if (StringUtils.isNotBlank(idcType)) {
			QueryBuilder queryTermId = QueryBuilders.termsQuery("idcType.keyword", idcType);
			queryBuilder.must(queryTermId);
		}

		QueryBuilder queryIp = QueryBuilders.constantScoreQuery(QueryBuilders.termQuery(Constants.DEVICE_IP, ip));
		queryBuilder.must(queryIp);
		String item = "";
		if (monitorFlag.equals("cpu")) {
			item = "cpuUsage";
		}else {
			item = "memUsage";
		}
		QueryBuilder itemQb = QueryBuilders.constantScoreQuery(QueryBuilders.matchPhraseQuery("item", item));
		queryBuilder.must(itemQb);

		request.setQuery(queryBuilder).setSize(0);

		DateHistogramAggregationBuilder dateAgg = AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
		String format = "";
		if (Constants.TIME_UNIT_DAY.equals(granularity)) {
			dateAgg.dateHistogramInterval(DateHistogramInterval.DAY);
			format = "yyyy-MM-dd";
		} else if (Constants.TIME_UNIT_WEEK.equals(granularity)) {
			dateAgg.dateHistogramInterval(DateHistogramInterval.WEEK);
			format = "yyyy-MM-dd";
		} else if (Constants.TIME_UNIT_MONTH.equals(granularity)) {
			dateAgg.dateHistogramInterval(DateHistogramInterval.MONTH);
			format = "yyyy-MM-dd";
		} else {
			throw new NullPointerException("无效的枚举类型");
		}
		dateAgg.format(format).timeZone(DateTimeZone.getDefault());
		dateAgg.extendedBounds(new ExtendedBounds(start.getTime(), end.getTime()));
		dateAgg.subAggregation(AggregationBuilders.avg("avg_used").field("value_avg"));
		dateAgg.subAggregation(AggregationBuilders.max("max_used").field("value_max"));
		dateAgg.subAggregation(AggregationBuilders.min("min_used").field("value_min"));
		request.addAggregation(dateAgg);
		log.info("查询网络性能es脚本:{}",  request);

		SearchResponse response = request.get();
		if (response.getHits().getTotalHits() > 0L) {
			Map<String, Object> tmpMap = new HashMap<String, Object>();

			Histogram h = response.getAggregations().get(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS);
			List<Bucket> bucketss = (List<Bucket>) h.getBuckets();
			List<String> intervalTime = new ArrayList<>();

			Map<String, List<Object>> valueMap = new HashMap<>();
			for (Bucket bt : bucketss) {
				// x轴
				if (StringUtils.isNotBlank(bt.getKeyAsString())) {
					intervalTime.add(bt.getKeyAsString());
				}
				getDateAggValues(valueMap, bt);
			}
			tmpMap.put("xAxis", intervalTime);
			tmpMap.put("series", valueMap);
			return tmpMap;
		}
		return null;
	}

	private Map<String, List<Object>> getDateAggValues(Map<String, List<Object>> temp, Bucket bt1) {
		Object maxValue = "";
		Object avgValue = "";
		Object minValue = "";

		Max max = bt1.getAggregations().get("max_used");
		Avg avg = bt1.getAggregations().get("avg_used");
		Min min = bt1.getAggregations().get("min_used");

		if (max != null && !"-Infinity".equals(max.getValueAsString()) && !"NaN".equals(max.getValueAsString())) {
			maxValue = (double)Math.round(max.getValue()*100)/100;
		}

		if (avg != null && !"-Infinity".equals(avg.getValueAsString()) && !"NaN".equals(avg.getValueAsString())) {
			avgValue = (double)Math.round(avg.getValue()*100)/100;
		}
		
		if (min != null && !"-Infinity".equals(min.getValueAsString()) && !"NaN".equals(min.getValueAsString()) && !"Infinity".equals(min.getValueAsString())) {
			minValue =(double)Math.round(min.getValue()*100)/100;
		}

		if (temp.containsKey("max")) {
			List<Object> avgList = temp.get("avg");
			List<Object> maxList = temp.get("max");
			List<Object> minList = temp.get("min");
			avgList.add(avgValue);
			maxList.add(maxValue);
			minList.add(minValue);
			temp.put("max", maxList);
			temp.put("avg", avgList);
			temp.put("min", minList);
		}else {
			List<Object> avgList = Lists.newArrayList();
			List<Object> maxList = Lists.newArrayList();
			List<Object> minList = Lists.newArrayList();
			avgList.add(avgValue);
			maxList.add(maxValue);
			minList.add(minValue);
			temp.put("max", maxList);
			temp.put("avg", avgList);
			temp.put("min", minList);
		}

		return temp;
	}

}
