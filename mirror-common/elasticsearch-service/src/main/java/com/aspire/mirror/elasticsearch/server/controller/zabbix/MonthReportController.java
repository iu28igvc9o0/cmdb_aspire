package com.aspire.mirror.elasticsearch.server.controller.zabbix;

import java.math.BigDecimal;
import java.text.DateFormat;
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
import org.elasticsearch.search.SearchHit;
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
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.pipeline.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.pipeline.bucketmetrics.InternalBucketMetricValue;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.elasticsearch.api.dto.MonthReportNetRequest;
import com.aspire.mirror.elasticsearch.api.dto.MonthReportRequest;
import com.aspire.mirror.elasticsearch.api.dto.NetConfig;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IMonthReportService;
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
public class MonthReportController extends CommonController implements IMonthReportService {
	@Autowired
	private TransportClient transportClient;
	
	@Value("${INDEX_HISTORY_DAY_PRE_NEW:kpi-day_}")
	private   String INDEX_HISTORY_DAY_PRE;
	
	@Value("${INDEX_HISTORYUINT_DAY_PRE_NEW:kpi-day-uint_}")
	private   String INDEX_HISTORYUINT_DAY_PRE;
	
	@Value("${INDEX_VALUE_NEW:value_avg}")
	private   String INDEX_VALUE;
	
	@Value("${INDEX_VALUE_MAX_NEW:value_max}")
	private   String INDEX_VALUE_MAX;
	
	@Value("${INDEX_HISTORY_HOUR_PRE_NEW:kpi-hour_}")
	private   String INDEX_HISTORY_HOUR_PRE;
	
	/*
	 * @Value(
	 * "${XXG_UNASSIGNED_BIZSYSTEM:私有云自用,待回收,未分配,Vmware虚拟化宿主机,已回收,存储节点,非业务（规划没有业务网，无法给业务使用）,NBU}")
	 * private String XXG_UNASSIGNED_BIZSYSTEM;
	 * 
	 * @Value("${NFJD_UNASSIGNED_BIZSYSTEM:未分配,公共存储,公共资源,虚拟化宿主机}") private String
	 * NFJD_UNASSIGNED_BIZSYSTEM;
	 */
	@Value("${NFJD_UNASSIGNED_BIZSYSTEM:bae24dd8679f4be897af8afff94c74bd,38c3e05b39374b6487831149875818d2,c9c8f37301d94ac6b068eeccbef696c3,5a91d0d2ea8d4c96b265dd858ac684e6}")
	private   String NFJD_UNASSIGNED_BIZSYSTEM;
	@Value("${XXG_UNASSIGNED_BIZSYSTEM:bae24dd8679f4be897af8afff94c74bd,8919f848f220402a8f96a9f5dbd10aff}")
	private   String XXG_UNASSIGNED_BIZSYSTEM;
	@Value("${IT_COMPANY_INTERNAL:6c1f1415-aa0d-11e9-995c-0242ac110002}")
	private   String IT_COMPANY_INTERNAL;
	
	@Value("${noIdcTypes:深圳池外,北京池外}")
	private String noIdcTypes;
	@Value("${phyNodeTypes:计算节点,宿主机}")
	private String phyNodeTypes;
	
	@Override
	public Map<String, Map<String, Object>> queryNetBandwidth(@RequestBody MonthReportNetRequest monthReportRequest) throws ParseException{

		log.info("************************************queryNetBandwidth begin***********************************");
		int size = monthReportRequest.getNetConfigs().get(0).getIps().split(",").length;
		SearchRequestBuilder request = initeNetRequst(monthReportRequest,this.INDEX_HISTORYUINT_DAY_PRE);
		TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("host").field("host.keyword").size(size);
		termsBuilder1.subAggregation(AggregationBuilders.topHits("Bandwidth").sort("clock", SortOrder.DESC).size(1).fetchSource(true));
		request.addAggregation(termsBuilder1);
		// request.addAggregation(termsBuilder1New);
		Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();
		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		
		if (null == dataMap || dataMap.size() == 0) {
			dataMap = new HashMap<>();
		}
		if(null == response.getAggregations()) {
			return dataMap;
		}
		
		
		String deviceType = monthReportRequest.getDeviceType();
		String idcType = monthReportRequest.getIdcType();
		String key = idcType + "_" + deviceType;
		Map<String, Object> val = new HashMap<String, Object>();
		if (dataMap.containsKey(key)) {
			val = dataMap.get(key);
		} else {
			val.put("idcType", idcType);
			// val.put("department2", de2);
			val.put("deviceType", deviceType);
			dataMap.put(key, val);
		}
		final Terms terms = response.getAggregations().get("host");
		double sum = 0;
		for (final Terms.Bucket hostBk : terms.getBuckets()) {
			 final TopHits topHits = hostBk.getAggregations().get("Bandwidth");
			 SearchHit searchHit = topHits.getHits().getHits()[0];
			 double value = Double.parseDouble(searchHit.getSourceAsMap().get(INDEX_VALUE_MAX).toString());
			 sum +=value;
		}
		sum = sum/1000000000;
		sum = new BigDecimal(sum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		val.put("outHighSpeed", sum);
		val.put("inHighSpeed", sum);

		log.info("************************************queryNetBandwidth end***********************************");
		return dataMap;
	}
	
	@Override
	public Map<String, Map<String, Object>> queryNetUseRatio(@RequestBody MonthReportNetRequest monthReportRequest) throws ParseException{

		log.info("************************************queryNetUseRatio begin***********************************");
		
		SearchRequestBuilder request = initeNetRequst(monthReportRequest,this.INDEX_HISTORY_DAY_PRE);
	
		String deviceType = monthReportRequest.getDeviceType();
		Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();
		
		
		TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("host").field("host.keyword").size(10);
		termsBuilder1.subAggregation(AggregationBuilders.avg("avg_item").field(this.INDEX_VALUE));
		termsBuilder1.subAggregation(AggregationBuilders.max("max_item").field(this.INDEX_VALUE_MAX));
		
		request.addAggregation(termsBuilder1);
		// request.addAggregation(termsBuilder1New);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		
		if (null == dataMap || dataMap.size() == 0) {
			dataMap = new HashMap<>();
		}
		if(null == response.getAggregations()||response.getHits().getTotalHits()==0) {
			return dataMap;
		}
		final Terms terms = response.getAggregations().get("host");
			
			String idcType =monthReportRequest.getIdcType();
			String key = idcType+"_"+deviceType;
			Map<String, Object> val = new HashMap<String, Object>();
			if (dataMap.containsKey(key)) {
				val = dataMap.get(key);
			} else {
				val.put("idcType", idcType);
				// val.put("department2", de2);
				val.put("deviceType", deviceType);
				dataMap.put(key, val);
			}
			
			double avg = 0;
			double max = 0;
			int num = 0;
			for (final Terms.Bucket hostBk : terms.getBuckets()) {
				Max maxVal = hostBk.getAggregations().get("max_item");
				Avg avgVal = hostBk.getAggregations().get("avg_item");
				avg += avgVal.getValue();
				max += maxVal.getValue();
				 num++;
			}
			//avg = 44.34234234;
			//max = 87.34234;
			avg = new BigDecimal(avg/num).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			max = new BigDecimal(max/num).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if(monthReportRequest.isIn()) {
				val.put("ifInOctetsAvg", avg);
				val.put("ifInOctetsMax", max);
			}else {
				val.put("ifOutOctetsAvg", avg);
				val.put("ifOutOctetsMax", max);
			}
			
		log.info("************************************queryNetUseRatio end***********************************");
		return dataMap;
	}
	
	@Override
	public Map<String, Map<String, Object>> queryNetUseTotal(@RequestBody MonthReportNetRequest monthReportRequest) throws ParseException{

		log.info("************************************queryNetUseTotal begin***********************************");
		
		SearchRequestBuilder request = initeNetRequst(monthReportRequest,this.INDEX_HISTORYUINT_DAY_PRE);
	
		String deviceType = monthReportRequest.getDeviceType();
		Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();
		
		
		//TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("host").field("host.keyword").size(10);
		//termsBuilder1.subAggregation(AggregationBuilders.sum("sum_item").field("value"));
		request.addAggregation(AggregationBuilders.sum("sum_item").field(this.INDEX_VALUE));
		// request.addAggregation(termsBuilder1New);

		log.info("查询设备利用率es脚本： {}", request);
		SearchResponse response = request.get();
		
		if (null == dataMap || dataMap.size() == 0) {
			dataMap = new HashMap<>();
		}
		if(null == response.getAggregations()) {
			return dataMap;
		}
	

			String idcType = monthReportRequest.getIdcType();
			String key = idcType+"_"+deviceType;
			Map<String, Object> val = new HashMap<String, Object>();
			
			Sum sumVal = response.getAggregations().get("sum_item");
			double sum = sumVal.getValue();
			if(sum==0) {
				return dataMap;
			}
			if (dataMap.containsKey(key)) {
				val = dataMap.get(key);
			} else {
				val.put("idcType", idcType);
				// val.put("department2", de2);
				val.put("deviceType", deviceType);
				dataMap.put(key, val);
			}
			sum = sum*24*12/1024/1024/1024;//用的是一天的均值
			//sum = sum/num;
			sum = new BigDecimal(sum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if(monthReportRequest.isIn()) {
				val.put("inTotal", sum);
			}else {
				val.put("outTotal", sum);
			}
			
		log.info("************************************queryNetUseTotal end***********************************");
		return dataMap;
	}
	
	
public SearchRequestBuilder initeNetRequst(MonthReportNetRequest monthReportRequest,String INDEX) throws ParseException {
	String start = monthReportRequest.getStartTime();
	//String end = monthReportRequest.getEndTime();

	Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
	//Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
	

	DateFormat returnd = new SimpleDateFormat("yyyyMM");
	INDEX = INDEX + returnd.format(startTime) + "*";

	log.info("index:{}", INDEX);
	SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest,INDEX)).setExplain(true);
	// 设置查询条件
	//BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

		/*
		 * RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		 * timeRange.gte(startTime.getTime() / 1000); timeRange.lt(endTime.getTime() /
		 * 1000); queryBuilder.must(timeRange);
		 */

	List<NetConfig> nets = monthReportRequest.getNetConfigs();
	//BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();
	
	
	NetConfig  net = nets.get(0);
		//for (NetConfig net : nets) {
			BoolQueryBuilder idcTypeBuilder = QueryBuilders.boolQuery();
			idcTypeBuilder.must(QueryBuilders.termQuery("idcType.keyword", net.getIdcType()));
			idcTypeBuilder.must(QueryBuilders.termsQuery("host.keyword", net.getIps().split(",")));
			String[] items = net.getPort().split(",");
			StringBuffer sb = new StringBuffer();
		
			for(int i=0;i<items.length;i++) {
				sb.setLength(0);
				sb.append(monthReportRequest.getItem()).append("[").append(items[i]).append("]");
				items[i] =  sb.toString();
			}
			idcTypeBuilder.must(QueryBuilders.termsQuery("item.keyword", items));
			//shouldBuilder.should(idcTypeBuilder);

		//}
		//queryBuilder.must(idcTypeBuilder);
		 
	
	request.setQuery(idcTypeBuilder).setSize(0);
	return request;
}

@Override
public Map<String, Map<String, Object>> queryIdcTypeUserRate(@RequestBody MonthReportRequest monthReportRequest)
		throws ParseException {
	log.info("**monthReport-queryIdcTypeUserRate-begin***********************************");
	String start = monthReportRequest.getStartTime();
	String end = monthReportRequest.getEndTime();

	Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
	Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
	String month = sdf1.format(startTime);
	
	String idcType = monthReportRequest.getIdcType();
	String deviceType = monthReportRequest.getDeviceType();
	String item = monthReportRequest.getItem();
	int type = monthReportRequest.getType();
	
	Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();
	if(null==dataMap) {
		dataMap = Maps.newHashMap();
	}
	List<String> indexList = DateUtil.getIndexMonthList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
	log.info("index:{}", indexList);
	SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest,indexList.toArray(new String[] {}))).setExplain(true);
	// 设置查询条件
	BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

	RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
	timeRange.gte(startTime.getTime() / 1000);
	timeRange.lte(endTime.getTime() / 1000);
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
	if(deviceType.equals("X86服务器")) {
		if(type==1 || type==3) {//已分配		
			queryBuilder.mustNot(QueryBuilders.termsQuery("bizSystem.keyword",  (this.NFJD_UNASSIGNED_BIZSYSTEM+","+this.XXG_UNASSIGNED_BIZSYSTEM).split(",")));		
		}
		if(type==2 || type==3) {
			queryBuilder.must(QueryBuilders.termQuery("nodeType.keyword",  Constants.NODE_TYPE_JSJD));
			//queryBuilder.mustNot(QueryBuilders.termQuery("nodeType.keyword",  Constants.NODE_TYPE_SZJ));//裸金属（含GPU），不含宿主机
		}
		
		if(type==1) {
			queryBuilder.must(QueryBuilders.termsQuery("nodeType.keyword", phyNodeTypes.split(",")));
		}
		if(type==4) {
			queryBuilder.must(QueryBuilders.termQuery("nodeType.keyword",  Constants.NODE_TYPE_SZJ));//宿主机
		}
		if(type==5) {
			queryBuilder.must(QueryBuilders.termQuery("nodeType.keyword",  Constants.NODE_TYPE_MANEGERNODE));//管理节点
		}
	}
	
	
	request.setQuery(queryBuilder).setSize(0);

	// 时间分组
	String format = "yyyy-MM-dd HH:mm:ss";

	DateHistogramAggregationBuilder dateAgg = AggregationBuilders
			.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
	dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
	dateAgg.format(format);// .timeZone(DateTimeZone.getDefault());

	//dateAgg.extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()));

	TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("col").field("idcType.keyword")
			.size(100000);
	
	dateAgg.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(this.INDEX_VALUE));
	termsBuilder2.subAggregation(dateAgg);

	termsBuilder2.subAggregation(PipelineAggregatorBuilders.statsBucket("stats_buckets3", "dateagg.avg_item"));

	request.addAggregation(termsBuilder2);
	
	log.info("查询设备利用率es脚本： {}", request);
	SearchResponse response = request.get();

	if(null == response.getAggregations() || null == response.getAggregations().get("col")) {
		return dataMap;
	}
	final Terms terms2 = response.getAggregations().get("col");
	for (final Terms.Bucket bucket2 : terms2.getBuckets()) {
		
		String col = bucket2.getKeyAsString();
		String key = col+deviceType;
		
		Map<String, Object> val = new HashMap<String, Object>();
		if(dataMap.containsKey(key)) {
			val = dataMap.get(key);
		}else {
			val.put("idcType", col);
			val.put("type", type);
			val.put("month", month);
			val.put("deviceType", deviceType);
			dataMap.put(key, val);
		}
			
		
		Stats stats = bucket2.getAggregations().get("stats_buckets3");
		if (null != stats) {
			
			String avg = InstantUtils.getStatsValues("avg", stats);
			String max = InstantUtils.getStatsValues("max", stats);
		
			if(StringUtils.isBlank(max)) {
				max = null;
			}
			if(StringUtils.isBlank(avg)) {
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
	log.info("**monthReport-queryIdcTypeUserRate-end***********************************");
	return dataMap;
}

@Override
public Map<String, Map<String, Object>> queryPhyUserRate(@RequestBody MonthReportRequest monthReportRequest)
		throws ParseException {
	log.info("**monthReport-queryIdcTypeUserRate-begin***********************************");
	String start = monthReportRequest.getStartTime();
	String end = monthReportRequest.getEndTime();

	Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
	Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
	String month = sdf1.format(startTime);
	
	//String idcType = monthReportRequest.getIdcType();
	String deviceType = monthReportRequest.getDeviceType();
	int type = monthReportRequest.getType();
	
	Map<String, Map<String, Object>> dataMap =Maps.newHashMap();

	
	List<String> indexList = DateUtil.getIndexMonthList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
	log.info("index:{}", indexList);
	SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest,indexList.toArray(new String[] {}))).setExplain(true);
	// 设置查询条件
	BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

	RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
	timeRange.gte(startTime.getTime() / 1000);
	timeRange.lte(endTime.getTime() / 1000);
	queryBuilder.must(timeRange);
	
	if (StringUtils.isNotBlank(phyNodeTypes)) {
		//queryBuilder.must(QueryBuilders.termQuery("idcType.keyword", idcType));
		queryBuilder.must(QueryBuilders.termsQuery("nodeType.keyword", phyNodeTypes.split(",")));
	}
	if (StringUtils.isNotBlank(noIdcTypes)) {
		//queryBuilder.must(QueryBuilders.termQuery("idcType.keyword", idcType));
		queryBuilder.mustNot(QueryBuilders.termsQuery("idcType.keyword", noIdcTypes.split(",")));
	}
	if (StringUtils.isNotBlank(monthReportRequest.getPod())) {
		QueryBuilder queryTermId = QueryBuilders.termsQuery("podName.keyword", monthReportRequest.getPod().split(","));
		queryBuilder.must(queryTermId);
	}

	queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
	FormConditionUtil.getDeviceType(deviceType,monthReportRequest.getItem(),queryBuilder);
	if(deviceType.equals("X86服务器")) {
		if(type==1 || type==3) {//已分配		
			queryBuilder.mustNot(QueryBuilders.termsQuery("bizSystem.keyword",  (this.NFJD_UNASSIGNED_BIZSYSTEM+","+this.XXG_UNASSIGNED_BIZSYSTEM).split(",")));		
		}
		if(type==2 || type==3) {
			queryBuilder.mustNot(QueryBuilders.termQuery("nodeType.keyword",  Constants.NODE_TYPE_SZJ));//裸金属（含GPU），不含宿主机
		}
		
		if(type==4) {
			queryBuilder.must(QueryBuilders.termQuery("nodeType.keyword",  Constants.NODE_TYPE_SZJ));//宿主机
		}
		if(type==5) {
			queryBuilder.must(QueryBuilders.termQuery("nodeType.keyword",  Constants.NODE_TYPE_MANEGERNODE));//管理节点
		}
	}
	
	
	request.setQuery(queryBuilder).setSize(0);

	// 时间分组
	String format = "yyyy-MM-dd HH:mm:ss";

	DateHistogramAggregationBuilder dateAgg = AggregationBuilders
			.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
	dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
	dateAgg.format(format);// .timeZone(DateTimeZone.getDefault());

	//dateAgg.extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()));


	BoolQueryBuilder distributeBuilder = QueryBuilders.boolQuery();
	QueryBuilder distributionQb = QueryBuilders.termsQuery("bizSystem.keyword",  (this.NFJD_UNASSIGNED_BIZSYSTEM+","+this.XXG_UNASSIGNED_BIZSYSTEM).split(","));
	distributeBuilder.mustNot(distributionQb);
	
	FilterAggregationBuilder  distributeFilter = AggregationBuilders.filter("distributeFilter", distributeBuilder);	
	
	dateAgg.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(this.INDEX_VALUE));
	distributeFilter.subAggregation(dateAgg);

	distributeFilter.subAggregation(PipelineAggregatorBuilders.maxBucket("stats_buckets3", "dateagg.avg_item"));

	request.addAggregation(distributeFilter);
	request.addAggregation(dateAgg);
	request.addAggregation(PipelineAggregatorBuilders.maxBucket("stats_buckets3", "dateagg.avg_item"));
	
	log.info("查询设备利用率es脚本： {}", request);
	SearchResponse response = request.get();

	if(null == response.getAggregations()) {
		return dataMap;
	}
	InternalBucketMetricValue max = response.getAggregations().get("stats_buckets3");
	Double value = InstantUtils.getNumericValue(max);
	if (null != value) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("idcType", "已上线物理服务器CPU均峰值");
		map.put("comment", "一级TI资源池已交维的裸金属（含GPU）、宿主机、管理节点");
		map.put("sort", 0);
		map.put("cpu_max", value);
		map.put("type", type);
		map.put("month", month);
		map.put("deviceType", deviceType);
		dataMap.put(map.get("idcType").toString(), map);
	}
	
	final InternalFilter terms2 = response.getAggregations().get("distributeFilter");
	if(null!=terms2) {
		InternalBucketMetricValue disMax = terms2.getAggregations().get("stats_buckets3");
		Double value1 = InstantUtils.getNumericValue(disMax);
		if (null != max) {
			Map<String,Object> map = Maps.newHashMap();
			map.put("idcType", "已分配的物理服务器CPU均峰值");
			map.put("comment", "一级TI资源池已分配给租户的裸金属（含GPU）、宿主机，且租户资源开通时间达3个月以上");
			map.put("sort", 1);
			map.put("cpu_max", value1);
			map.put("type", type);
			map.put("month", month);
			map.put("deviceType", deviceType);
			dataMap.put(map.get("idcType").toString(), map);
		}
	}
			
	log.info("**monthReport-queryIdcTypeUserRate-end***********************************");
	
	return dataMap;
}

@Override
public Map<String, Map<String, Object>> queryDepartment2UserRate(@RequestBody MonthReportRequest monthReportRequest
		,@RequestParam(value = "department1Flag", required = false) boolean department1Flag)
		throws ParseException {
	log.info("**monthReport-queryDepartment2UserRate-begin***********************************");
	String start = monthReportRequest.getStartTime();
	String end = monthReportRequest.getEndTime();

	Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
	Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
	String month = sdf1.format(startTime);
	
	String idcType = monthReportRequest.getIdcType();
	String deviceType = monthReportRequest.getDeviceType();
	String item = monthReportRequest.getItem();
	int type = monthReportRequest.getType();
	Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();
	if(null==dataMap) {
		dataMap = Maps.newHashMap();
	}

	List<String> indexList = DateUtil.getIndexMonthList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
	log.info("index:{}", indexList);
	SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest,indexList.toArray(new String[] {}))).setExplain(true);
	// 设置查询条件
	BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

	RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
	timeRange.gte(startTime.getTime() / 1000);
	timeRange.lte(endTime.getTime() / 1000);
	queryBuilder.must(timeRange);

	if (StringUtils.isNotBlank(idcType)) {
		queryBuilder.must(QueryBuilders.termQuery("idcType.keyword", idcType));
	}
	if (StringUtils.isNotBlank(monthReportRequest.getPod())) {
		QueryBuilder queryTermId = QueryBuilders.termsQuery("podName.keyword", monthReportRequest.getPod().split(","));
		queryBuilder.must(queryTermId);
	}
	if (StringUtils.isNotBlank(deviceType)) {
		FormConditionUtil.getDeviceType(deviceType,monthReportRequest.getItem(),queryBuilder);
	}else {
		FormConditionUtil.getDeviceTypeAll(monthReportRequest.getItem(),queryBuilder);
	}
	queryBuilder.must(QueryBuilders.rangeQuery(this.INDEX_VALUE).gte(0));
	//it公司内部
	if(department1Flag) {
		queryBuilder.must(QueryBuilders.termQuery("department1.keyword",  this.IT_COMPANY_INTERNAL));//裸金属（含GPU），不含宿主机
	}else {
		queryBuilder.mustNot(QueryBuilders.termQuery("department1.keyword",  this.IT_COMPANY_INTERNAL));//裸金属（含GPU），不含宿主机
	}
	
	
	if(type==1 || type==3) {//已分配
		queryBuilder.mustNot(QueryBuilders.termsQuery("bizSystem.keyword",  (this.XXG_UNASSIGNED_BIZSYSTEM+","+this.NFJD_UNASSIGNED_BIZSYSTEM).split(",")));
	}
	if(type==2 || type==3) {
		queryBuilder.mustNot(QueryBuilders.termQuery("nodeType.keyword",  1));//裸金属（含GPU），不含宿主机
	}
	
	
	
	
	request.setQuery(queryBuilder).setSize(0);

	// 时间分组
	String format = "yyyy-MM-dd HH:mm:ss";

	DateHistogramAggregationBuilder dateAgg = AggregationBuilders
			.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime");
	dateAgg.dateHistogramInterval(DateHistogramInterval.hours(1));
	dateAgg.format(format);// .timeZone(DateTimeZone.getDefault());

	//dateAgg.extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()));

	TermsAggregationBuilder termsBuilder2 = AggregationBuilders.terms("col").field("department2.keyword")
			.size(10000);
	if(!department1Flag) {
		 termsBuilder2 = AggregationBuilders.terms("col").field("department1.keyword")
					.size(10000);
	}
	
	dateAgg.subAggregation(AggregationBuilders.avg(Constants.AGGREGATION_TYPE_AVG + "_item").field(this.INDEX_VALUE));
	termsBuilder2.subAggregation(dateAgg);

	termsBuilder2.subAggregation(PipelineAggregatorBuilders.statsBucket("stats_buckets3", "dateagg.avg_item"));

	request.addAggregation(termsBuilder2);
	
	log.info("查询设备利用率es脚本： {}", request);
	SearchResponse response = request.get();

	if(null == response.getAggregations() || null == response.getAggregations().get("col")) {
		return dataMap;
	}
	final Terms terms2 = response.getAggregations().get("col");
	for (final Terms.Bucket bucket2 : terms2.getBuckets()) {

		String col = bucket2.getKeyAsString();
		Map<String, Object> val = new HashMap<String, Object>();
		if(dataMap.containsKey(col)) {
			val = dataMap.get(col);
		}else {
			val.put("department2", col);
			val.put("month", month);
			val.put("type", type);
			dataMap.put(col, val);
		}
			
		
		Stats stats = bucket2.getAggregations().get("stats_buckets3");
		if (null != stats) {
			
			String avg = InstantUtils.getStatsValues("avg", stats);
			String max = InstantUtils.getStatsValues("max", stats);
		
			if(StringUtils.isBlank(max)) {
				max = null;
			}
			if(StringUtils.isBlank(avg)) {
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
	log.info("**monthReport-queryDepartment2UserRate-end***********************************");
	return dataMap;
}


@Override
public Map<String, Map<String, Object>> queryBizSystemUserRate(@RequestBody MonthReportRequest monthReportRequest)
		throws ParseException {
	log.info("**monthReport-queryBizSystemUserRate-begin***********************************");
	// 设置查询条件
	BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	String start = monthReportRequest.getStartTime();
	String end = monthReportRequest.getEndTime();

	Date startTime = DateUtils.parseDate(start, new String[] { "yyyy-MM-dd HH:mm:ss" });
	Date endTime = DateUtils.parseDate(end, new String[] { "yyyy-MM-dd HH:mm:ss" });
	String idcType = monthReportRequest.getIdcType();
	String deviceType = monthReportRequest.getDeviceType();
	int type = monthReportRequest.getType();
	Map<String, Map<String, Object>> dataMap = monthReportRequest.getDataMap();
	if(null==dataMap) {
		Maps.newHashMap();
	}
			

	List<String> indexList = DateUtil.getIndexMonthList(startTime, endTime, this.INDEX_HISTORY_HOUR_PRE);
	log.info("index:{}", indexList);
	SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(monthReportRequest,indexList.toArray(new String[] {}))).setExplain(true);
	

	RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
	timeRange.gte(startTime.getTime() / 1000);
	timeRange.lte(endTime.getTime() / 1000);
	queryBuilder.must(timeRange);

	if (StringUtils.isNotBlank(idcType)) {
		queryBuilder.must(QueryBuilders.termQuery("idcType.keyword", idcType));
	}
	if (StringUtils.isNotBlank(monthReportRequest.getPod())) {
		QueryBuilder queryTermId = QueryBuilders.termsQuery("podName.keyword", monthReportRequest.getPod().split(","));
		queryBuilder.must(queryTermId);
	}
	if (StringUtils.isNotBlank(deviceType)) {
		queryBuilder.must(QueryBuilders.termQuery("deviceType.keyword", deviceType));
	}
	
	
	if(deviceType.equals("X86服务器")) {
		if(type==1 || type==3) {//已分配
			queryBuilder.mustNot(QueryBuilders.termsQuery("bizSystem.keyword",  (this.XXG_UNASSIGNED_BIZSYSTEM+","+this.NFJD_UNASSIGNED_BIZSYSTEM).split(",")));
		}
		if(type==2 || type==3) {
			queryBuilder.mustNot(QueryBuilders.termQuery("nodeType.keyword",  1));//裸金属（含GPU），不含宿主机
		}
	}
	
	
	
	
	request.setQuery(queryBuilder).setSize(0);

	//组装composite
	  List<CompositeValuesSourceBuilder<?>> sources = Lists.newArrayList();
	  //sources.add(new TermsValuesSourceBuilder("idcType").field("idcType.keyword").missingBucket(false).order(SortOrder.ASC));
	  sources.add(new TermsValuesSourceBuilder("bizSystem").field("bizSystem.keyword").missingBucket(false));

	  String format = "yyyy-MM-dd HH:mm:ss";
DateHistogramValuesSourceBuilder datetime = new DateHistogramValuesSourceBuilder(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS)
          .field("datetime")
          .dateHistogramInterval(DateHistogramInterval.hours(1))
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
		return dataMap;
	}
	CompositeAggregation  parsedComposite = response.getAggregations().get("composite");
	Map<String, Object> afterKey = parsedComposite.afterKey();
	getBizSystemComposite(monthReportRequest,dataMap,parsedComposite);
	while(null!=afterKey) {
		composite.aggregateAfter(afterKey);
		log.info("查询设备利用率es脚本： {}", request);
		 parsedComposite = request.execute().actionGet().getAggregations().get("composite");
		 getBizSystemComposite(monthReportRequest,dataMap,parsedComposite);
		afterKey = parsedComposite.afterKey();
	}
	
	
	//syncDate(dataMap);
	return dataMap;
}



public void getBizSystemComposite(MonthReportRequest monthReportRequest,Map<String, Map<String, Object>> idcTypeMap,
		CompositeAggregation parsedComposite){
	String startTime = monthReportRequest.getStartTime();
	String month = startTime.split("-")[0]+"-"+startTime.split("-")[1];
	String deviceType = monthReportRequest.getDeviceType();
	String namePre = "vm_";
	if(deviceType.equals("X86服务器")) {
		namePre = "phy_";
	}
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
			val.put("bizSystem", bizSystem);
			val.put("deviceType", deviceType);
			val.put("month", month);
			val.put("type", monthReportRequest.getType());
		}
		final InternalFilter cpuTerms = list1.get(i).getAggregations().get("cpuFilter");
		final InternalFilter memTerms = list1.get(i).getAggregations().get("memFilter");
		if(null!=cpuTerms) {
			Avg max = cpuTerms.getAggregations().get("value_avg");
			Double value = InstantUtils.getNumericValue(max);
			String name = namePre+"cpu_max";
			if(null!=value) {
				double maxVal = val.get(name)==null?0:Double.parseDouble(val.get(name).toString());
				val.put(name, value>maxVal?value:maxVal);
			}
			
		}
		
		if(null!=memTerms) {
			Avg memMax = memTerms.getAggregations().get("value_avg");
			Double value = InstantUtils.getNumericValue(memMax);
			String name = namePre+"memory_max";
			if(null!=value) {
				double maxVal = val.get(name)==null?0:Double.parseDouble(val.get(name).toString());
				val.put(namePre+"memory_max", value>maxVal?value:maxVal);
			}
			
		}
		
		
	}
}


}
