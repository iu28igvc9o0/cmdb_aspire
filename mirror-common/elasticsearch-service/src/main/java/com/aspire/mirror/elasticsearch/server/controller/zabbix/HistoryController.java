package com.aspire.mirror.elasticsearch.server.controller.zabbix;

import com.aspire.mirror.elasticsearch.api.dto.HistorySearchRequest;
import com.aspire.mirror.elasticsearch.api.dto.ItemIndexDto;
import com.aspire.mirror.elasticsearch.api.dto.NetPerformanceAnalysis;
import com.aspire.mirror.elasticsearch.api.dto.NetSetDto;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IZabbixHistoryService;
import com.aspire.mirror.elasticsearch.server.biz.IKpiKeyConfigBiz;
import com.aspire.mirror.elasticsearch.server.controller.CommonController;
import com.aspire.mirror.elasticsearch.server.enums.Constants;
import com.aspire.mirror.elasticsearch.server.enums.KpiTypeEnum;
import com.aspire.mirror.elasticsearch.server.util.DateUtil;
import com.aspire.mirror.elasticsearch.server.util.DateUtils;
import com.aspire.mirror.elasticsearch.server.util.InstantUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.InternalFilter;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.aggregations.pipeline.InternalSimpleValue;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.elasticsearch.server.controller.zabbix
 * 类名称:    HistoryController.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/24 20:23
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class HistoryController extends CommonController implements IZabbixHistoryService {
    private static final String INDEX = "history*";
    private final static String HISTORY_INDEX_PRE = "history-*";
    
    private final static String HISTORY_ALL_INDEX_PRE = "history*-*";
    @Autowired
    private TransportClient transportClient;
    
    @Autowired
	private IKpiKeyConfigBiz iKpiKeyConfigBiz;

    @Override
    public Map<String, Object> getMonitorValue(@RequestBody @Validated HistorySearchRequest historySearchRequest) {
        SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(historySearchRequest,INDEX)).setExplain(true).setSize(0);
        Map<String, List<String>> ipMap = historySearchRequest.getIpMap();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        String[] itemArray = new String[historySearchRequest.getItemList().size()];

        Long rangeNowDate = null;
        Long rangeComDate = null;
        Calendar nowTime = Calendar.getInstance();
        rangeNowDate = nowTime.getTime().getTime();
        nowTime.add(Calendar.MINUTE, -60);
        rangeComDate = nowTime.getTime().getTime();
        // 时间范围定义在最近10分钟
        queryBuilder.must(QueryBuilders.rangeQuery("@timestamp").gt(rangeComDate).lte(rangeNowDate));
        // 指标条件添加
        queryBuilder.must(QueryBuilders.termsQuery("item.keyword", historySearchRequest.getItemList().toArray
                (itemArray)));
        // 资源池和ip条件添加
        BoolQueryBuilder itemQueryBuilder = QueryBuilders.boolQuery();
        for (String idcType : ipMap.keySet()) {
            BoolQueryBuilder subqueryBuilder = QueryBuilders.boolQuery();
            subqueryBuilder.must(QueryBuilders.matchPhraseQuery("idcType", idcType));
            subqueryBuilder.must(QueryBuilders.termsQuery("host", ipMap.get(idcType)));
            itemQueryBuilder.should(subqueryBuilder);
        }
        queryBuilder.must(itemQueryBuilder);
        request.setQuery(queryBuilder);

        String[] fields = new String[4];
        fields[0] = "item"; // 字段1名称
        fields[1] = "host"; // 字段2名称
        fields[2] = "@timestamp";
        fields[3] = "value";
        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("host").field("host" +
                ".keyword").size(1000);
        termsBuilder.subAggregation(AggregationBuilders.terms
                ("item").field("item" +
                ".keyword").size(1000).subAggregation(AggregationBuilders.topHits("top_sales_hits").fetchSource
                (fields, null).sort("@timestamp", SortOrder.DESC).size(1)));
        request.addAggregation(termsBuilder);
        log.info("historyController[getMonitorValue] es query paramObject is {}", request);
        SearchResponse response = request.get();
        Terms terms = response.getAggregations().get("host");
        List<Map<String, Object>> resultList = Lists.newArrayList();
        for (final Terms.Bucket a : terms.getBuckets()) {
            final Terms itemTerms = a.getAggregations().get("item");
            for (final Terms.Bucket b : itemTerms.getBuckets()) {
                final TopHits topHits = b.getAggregations().get("top_sales_hits");
                for (SearchHit searchHit : topHits.getHits().getHits()) {
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                    resultList.add(sourceAsMap);
                }
            }
        }
        log.info("historyController[getMonitorValue] es query result is {}", resultList);
        Double value = parseValue(historySearchRequest.getCountType(), resultList);
        Map<String, Object> result = Maps.newHashMap();
        result.put("value", value);
        return result;
    }

    private Double parseValue(String countType, List<Map<String, Object>> result) {
        if (result.size() == 0) {
            return null;
        }
        if (Constants.AGGREGATION_TYPE_MAX.equals(countType)) {
            // 最大值
            return result.stream().mapToDouble(map -> getValue(map)).max().getAsDouble();
        } else if (Constants.AGGREGATION_TYPE_MIN.equals(countType)) {
            //最小值
            return result.stream().mapToDouble(map -> getValue(map)).min().getAsDouble();
        } else if (Constants.AGGREGATION_TYPE_AVG.equals(countType)) {
            //平均
            return result.stream().mapToDouble(map -> getValue(map)).average().getAsDouble();

        } else if (Constants.AGGREGATION_TYPE_SUM.equals(countType)) {
//            return result.stream().mapToDouble(map -> map.get("value")).sum();
            //求和
            return result.stream().mapToDouble(map -> getValue(map)).sum();
        }
        return null;
    }

    private double getValue(Map<String, Object> map) {
        if (map.get("value") instanceof Integer) {
            Integer value = (Integer) map.get("value");
            return (double) value;
        } else if (map.get("value") instanceof Double) {
            Double value = (Double) map.get("value");
            return value;
        } else if (map.get("value") instanceof Long) {
            Long value = (Long) map.get("value");
            return (double) value;
        } else {
            return 0.0;
        }
    }
    
    @Override
    public  Map<String,List<Map<String,Object>>> getInstanceMonitorValue(@RequestBody @Validated HistorySearchRequest historySearchRequest) {
    	String start = historySearchRequest.getStartTime();
		String end = historySearchRequest.getEndTime();
		Date startTime = DateUtils.parseDate(start,  "yyyy-MM-dd HH:mm:ss" );
		Date endTime = DateUtils.parseDate(end, "yyyy-MM-dd HH:mm:ss" );
		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.HISTORY_INDEX_PRE);
    	
		log.info("INDEX:{}", indexList);

		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(historySearchRequest,indexList.toArray(new String[] {})))
				.setExplain(true);
		
        List<String> idList = historySearchRequest.getIdList();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

       
        
        queryBuilder.must(QueryBuilders.rangeQuery("datetime").gte(startTime.getTime()).lte(endTime.getTime()));
        // 指标条件添加
        queryBuilder.must(iKpiKeyConfigBiz.query4Builder(historySearchRequest.getKpi(), "item"));
        
        queryBuilder.must(QueryBuilders.termsQuery("resourceId", idList));
       
       
        request.setQuery(queryBuilder);

        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("resourceId").field("resourceId" +
                ".keyword").size(10000);
        DateHistogramAggregationBuilder dateAgg =  AggregationBuilders
				.dateHistogram(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS).field("datetime")
				.dateHistogramInterval(DateHistogramInterval.minutes(5)).format("yyyy-MM-dd HH:mm:ss").timeZone(DateTimeZone.getDefault());
        dateAgg.extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()));
        dateAgg.subAggregation(AggregationBuilders.avg("avg").field("value"));
        termsBuilder.subAggregation(dateAgg);
        request.addAggregation(termsBuilder).setSize(0);
        
        log.info("查询es脚本： {}", request);
        SearchResponse response = request.get();
        Map<String,List<Map<String,Object>>> resultMap = Maps.newHashMap();
        if(null==response.getAggregations()) {
        	return resultMap;
        }
        Terms terms = response.getAggregations().get("resourceId");
       
        for (final Terms.Bucket a : terms.getBuckets()) {
        	String resourceId = a.getKeyAsString();
        	List<Map<String,Object>> valueList = Lists.newArrayList();
        	Histogram h = a.getAggregations().get(Constants.AGGREGATION_GROUP_INTERTIME_ALIAS);
            for (Bucket b : h.getBuckets()) {
            	Map<String,Object> data = Maps.newHashMap();
            	String time = b.getKeyAsString();
            	InternalAvg avg = b.getAggregations().get("avg");
            	Double avgValue = InstantUtils.getNumericValue(avg);
            	data.put("time", time);
            	data.put("value", avgValue);
            	valueList.add(data);
            }
            resultMap.put(resourceId, valueList);
        }
       
        return resultMap;
    }
    
    @Override
    public  List<String> getNullValueDeviceList(@RequestBody @Validated HistorySearchRequest historySearchRequest) {
    	String start = historySearchRequest.getStartTime();
		String end = historySearchRequest.getEndTime();
		Date startTime = DateUtils.parseDate(start,  "yyyy-MM-dd HH:mm:ss" );
		Date endTime = DateUtils.parseDate(end, "yyyy-MM-dd HH:mm:ss" );
		List<String> indexList = DateUtil.getIndexList(startTime, endTime, this.HISTORY_ALL_INDEX_PRE);
    	
		log.info("INDEX:{}", indexList);

		SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(historySearchRequest,indexList.toArray(new String[] {})))
				.setExplain(true);
		
        List<String> idList = historySearchRequest.getIdList();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        
        queryBuilder.must(QueryBuilders.termsQuery("resourceId", idList));
       
       
        request.setQuery(queryBuilder);

        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("resourceId").field("resourceId" +
                ".keyword").size(10000);
       
        request.addAggregation(termsBuilder).setSize(0);
        
        log.info("查询es脚本： {}", request);
        SearchResponse response = request.get();
        if(null==response.getAggregations()) {
        	return idList;
        }
        Terms terms = response.getAggregations().get("resourceId");
        List<String> valueList = Lists.newArrayList();
        for (final Terms.Bucket a : terms.getBuckets()) {
        	String resourceId = a.getKeyAsString();
        	valueList.add(resourceId);
           
        }
		List<String> deviceList = idList.stream().filter(item -> !valueList.contains(item))
				.collect(Collectors.toList());
      
        return deviceList;
    }
    /**
     * 资源池的设备性能分布
     * @param historySearchRequest
     * @return
     * @throws ParseException
     */
    @Override
    public Map<String,Map<String,Object>> getIdcTypePerformanceData(@RequestBody @Validated HistorySearchRequest historySearchRequest) throws ParseException {
    	
		// 设置查询条件
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		Date start = org.apache.commons.lang.time.DateUtils.parseDate(historySearchRequest.getStartTime(), new String[] { "yyyy-MM-dd HH:mm:ss" });
		Date end = org.apache.commons.lang.time.DateUtils.parseDate(historySearchRequest.getEndTime(), new String[] { "yyyy-MM-dd HH:mm:ss" });
		/*
		 * RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("datatime");
		 * timeRange.gte(start.getTime() ); timeRange.lte(end.getTime());
		 * queryBuilder.must(timeRange);
		 */
		
		
		 List<String> indexList = DateUtil.getIndexList(start, end, this.HISTORY_INDEX_PRE);
			log.info("indexList:{}",indexList);
			SearchRequestBuilder	 request = transportClient.prepareSearch(getClusterIndex(historySearchRequest,indexList.toArray(new String[] {}))).setExplain(true);

		
		if (StringUtils.isNotBlank(historySearchRequest.getIdcType())) {
			QueryBuilder queryTermId = QueryBuilders.termQuery("idcType.keyword", historySearchRequest.getIdcType());
			queryBuilder.must(queryTermId);
		}
		queryBuilder.must(QueryBuilders.termsQuery("deviceType.keyword","X86服务器","云主机"));
		queryBuilder.must(iKpiKeyConfigBiz.query4Builder("item",KpiTypeEnum.CPU_PUSED.name(),KpiTypeEnum.MEMORY_PUSED.name() ));
		request.setQuery(queryBuilder).setSize(0);
		
		
		BoolQueryBuilder cpuItemQb =iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.CPU_PUSED.name(), "item");
		BoolQueryBuilder memItemQb = iKpiKeyConfigBiz.query4Builder(KpiTypeEnum.MEMORY_PUSED.name(), "item");
		//QueryBuilder memItemQb = QueryBuilders.matchPhraseQuery("item", "memUsage");
		
		FilterAggregationBuilder  cpuFilter = AggregationBuilders.filter("cpuFilter", cpuItemQb);
		
		FilterAggregationBuilder  memFilter = AggregationBuilders.filter("memFilter", memItemQb);
		
		TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("deviceType").field("deviceType.keyword");
	
		//TermsAggregationBuilder termsBuilder1 = AggregationBuilders.terms("item").field("item.keyword").size(100000);

		termsBuilder.subAggregation(AggregationBuilders.range("rate").field("value")
				.addRange(0, 15).addRange(15, 30).addRange(30, 60).addRange(60, 85)
				.addRange(85, 100));
		
		//termsBuilder.subAggregation(termsBuilder1);
		cpuFilter.subAggregation(termsBuilder);
		memFilter.subAggregation(termsBuilder);
		request.addAggregation(cpuFilter);
		request.addAggregation(memFilter);

		log.info("查询资源池的设备性能分布es脚本： {}", request);
		SearchResponse response = request.get();
		Map<String,Map<String,Object>> map = Maps.newHashMap();
		if (response.getHits().getTotalHits() > 0L) {
			final InternalFilter cpuTerms = response.getAggregations().get("cpuFilter");
				final Terms terms = cpuTerms.getAggregations().get("deviceType");
				formData(terms, map, "cpu");
			final InternalFilter memTerms = response.getAggregations().get("memFilter");
				final Terms terms1 = memTerms.getAggregations().get("deviceType");
				formData(terms1, map, "memory");
		}
		
		return map;
	}
    
    public void formData(Terms terms,Map<String,Map<String,Object>> map,String item){
    	 for (final Terms.Bucket a : terms.getBuckets()) {
			 Map<String,Object> m = Maps.newHashMap();
			 m.put("item", item);
	        	String deviceType = a.getKeyAsString();
	        	m.put("deviceType", deviceType);
	        	Range  range = a.getAggregations().get("rate");
	        	long sum = 0l;
	        	 for (final Range.Bucket r : range.getBuckets()) {
	        		 long count = r.getDocCount();
		        		sum += count;
		        		String to = r.getToAsString();
		        		if(to.equals("15")||to.equals("15.0")) {
		        			m.put("fifteen_count", count);
		        		}
		        		if(to.equals("30")||to.equals("30.0")) {
		        			m.put("thirty_count", count);
		        		}
		        		if(to.equals("60")||to.equals("60.0")) {
		        			m.put("sixty_count", count);
		        		}
		        		if(to.equals("85")||to.equals("85.0")) {
		        			m.put("eighty_five_count", count);
		        		}
		        		if(to.equals("100")||to.equals("100.0")) {
		        			m.put("hundred_count", count);
		        		}
	        	 }
	        	 float sumRatio = 0f;
	        	 sumRatio += setRatioValue(m,"hundred_count",sum,sumRatio);
	        	 sumRatio +=setRatioValue(m,"thirty_count",sum,sumRatio);
	        	 sumRatio += setRatioValue(m,"sixty_count",sum,sumRatio);
	        	 sumRatio += setRatioValue(m,"eighty_five_count",sum,sumRatio);
	        	 m.put("fifteen_ratio", (float)Math.round((100-sumRatio)*100)/100);
	        	 
	        	
	        	 map.put(item+"_"+deviceType, m);
	        }
    }
  public float  setRatioValue(Map<String,Object> m,String key,long sum,float sumRatio){
	  long val = Long.parseLong(m.get(key).toString());
	  float ratio = InstantUtils.getOperValue(val, sum);
	  if((sumRatio+ratio)>100) {
		  ratio = 100 -sumRatio;
	  }
	  m.put(key.substring(0, key.lastIndexOf("_"))+"_ratio", ratio);
	  
	  return ratio;
    }
}
