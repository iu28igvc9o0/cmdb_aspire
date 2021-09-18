package com.aspire.mirror.elasticsearch.server.controller.zabbix;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.elasticsearch.api.dto.TrendsDto;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IZabbixTrendsService;
import com.aspire.mirror.elasticsearch.server.enums.DeviceUsedRateEnum;
import com.aspire.mirror.elasticsearch.server.util.DateUtil;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 * @author baiwp
 * @title: TrendsController
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2116:18
 */
@Slf4j
@RestController
public class TrendsController implements IZabbixTrendsService {
    private static final String INDEX = "trends*";
    private static final String TYPE = "trends_uint";
    @Autowired
    private TransportClient transportClient;

    @Value("${physical_pused_item_names:custom.cpu.avg5.pused,custom.memory.pused,diskpused}")
    private String pusedItemNames;

    @Value("${virtual_pused_item_names:cpu.usage_average,mem.usage_average,disk.usage_average}")
    private String virtralPusedItemNames;
    
    @Value("${cpu_pused_item_names:custom.cpu.avg5.pused,cpu.usage_average}")
    private String cpuPusedItemNames;
    
    @Value("${memory_pused_item_names:custom.memory.pused,mem.usage_average}")
    private String memoryPusedItemNames;
    
    @Value("${virtual_pused_item_names:cpu.usage_average,mem.usage_average,disk.usage_average}")
    private String storageItemNames;
    
    @Value("${virtralCputemNames:cpu.usage_average,custom.cpu.avg5.pused}")
	private String virtralCpuitemNames;
	@Value("${virtralMemitemNames:custom.memory.pused,mem.usage_average}")
	private String virtralMemitemNames;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
	

    public void insert(@RequestBody List<TrendsDto> trendsDtoList) {
        if (CollectionUtils.isEmpty(trendsDtoList)) {
            return;
        }
        log.info("新增zabbix趋势trends数据，数量：{}", trendsDtoList.size());
        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        for (int i = 0; i < trendsDtoList.size(); i++) {
            TrendsDto trendsDto = trendsDtoList.get(i);
            bulkRequest.add(transportClient.prepareIndex(INDEX, TYPE).setSource(JSONObject.fromObject(trendsDto).toString(), XContentType.JSON));
            if ((i + 1) % 50 == 0) {
                bulkRequest.execute().actionGet();
                bulkRequest=transportClient.prepareBulk();
            }

        }
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
        }
//        String source = JSONObject.fromObject(trends).toString();
//        IndexResponse response = transportClient.prepareIndex(INDEX, TYPE).setSource(source, XContentType.JSON).execute().actionGet();
    }

    public void deleteAll() {
        log.info("全量删除zabbix trends数据");
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(transportClient)
                .source(INDEX)
                .filter(QueryBuilders.matchAllQuery())
                .get();
    }

    public List<TrendsDto> queryTrends(@RequestParam(value = "itemId") String itemId,
                                       @RequestParam(value = "prefix", required = false) String prefix,
                                       @RequestParam(value = "startTime") Long startTime,
                                       @RequestParam(value = "endTime") Long endTime) {
        List<TrendsDto> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        QueryBuilder queryItemId = QueryBuilders.matchPhraseQuery("itemid", itemId);
        queryBuilder.must(queryItemId);
        QueryBuilder rangeQuery = QueryBuilders.rangeQuery("clock").from(startTime).to(endTime);
        queryBuilder.must(rangeQuery);
        if (StringUtils.isNotEmpty(prefix)) {
            QueryBuilder idcTerm = QueryBuilders.matchPhraseQuery("prefix", prefix);
            queryBuilder.must(idcTerm);
        }
        SearchResponse response = request.setSize(10000).setExplain(true).addSort("clock", SortOrder.ASC).setQuery(queryBuilder).get();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            TrendsDto trendsDto = new TrendsDto();
            try {
                BeanUtils.populate(trendsDto, sourceAsMap);
                list.add(trendsDto);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> deviceUsedRate(@RequestParam(value = "bizSystem", required = false) String bizSystem,
                                              @RequestParam(value = "deviceType", required = false) String deviceType) {
        Map<String, Object> resultMap = Maps.newHashMap();
        SearchRequestBuilder requestBuilder = transportClient.prepareSearch(INDEX);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date end = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date start = calendar.getTime();
        log.info("查询趋势数据时间范围为：start: [{}], end : [{}]", start, end);

        RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
        timeRange.gte(start.getTime()/1000);
        timeRange.lt(end.getTime()/1000);
        queryBuilder.must(timeRange);
        if (StringUtils.isNotEmpty(bizSystem)) {
//            queryBuilder.must(QueryBuilders.matchPhraseQuery("bizSystem", bizSystem));
            queryBuilder.must(QueryBuilders.termsQuery("bizSystem.keyword", bizSystem.trim().split("[\\,,\\，]")));
        }
        if (StringUtils.isNotEmpty(deviceType)) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("deviceType.keyword", deviceType));
            if (deviceType.equals("虚拟机")) {
                queryBuilder.must(QueryBuilders.termsQuery("item.keyword", virtralPusedItemNames.trim().split("[\\,,\\，]")));
            } else {
                queryBuilder.must(QueryBuilders.termsQuery("item.keyword", pusedItemNames.trim().split("[\\,,\\，]")));
            }
        }else {
            log.error("设备利用率设备类型不能为空");
            return resultMap;
        }

        requestBuilder.setQuery(queryBuilder);
        TermsAggregationBuilder termsBuilder;
        termsBuilder = AggregationBuilders.terms("item").field("item.keyword").size(1000);
        termsBuilder.subAggregation(AggregationBuilders.max("max_used").field("value_avg"));
        termsBuilder.subAggregation(AggregationBuilders.avg("avg_used").field("value_avg"));
        requestBuilder.addAggregation(termsBuilder);
        log.info("查询设备利用率es脚本： {}", requestBuilder);
        SearchResponse response = requestBuilder.get();
        if (response.getHits().getTotalHits() > 0L) {
            final Terms terms = response.getAggregations().get("item");
            for (final Terms.Bucket a : terms.getBuckets()) {
                Max max = a.getAggregations().get("max_used");
                Avg avg = a.getAggregations().get("avg_used");
                Map<String, Double> result = Maps.newHashMap();
                result.put("max", max.getValue());
                result.put("avg", avg.getValue());
                resultMap.put(DeviceUsedRateEnum.getItemAliasByItemName(String.valueOf(a.getKey())), result);
            }
        }
        return resultMap;
    }
    

    
    @Override
	public Map<String, Object> storageUsedRate(@RequestParam(value = "startTime",required=false) String startTime,
			@RequestParam(value = "endTime",required=false) String endTime,
			@RequestParam(value = "bizSystem", required = false) String bizSystem) throws ParseException {
		Map<String, Object> resultMap = Maps.newHashMap();
		//SearchRequestBuilder requestBuilder = transportClient.prepareSearch(INDEX);
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date end = calendar.getTime();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date start = calendar.getTime();
			if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
				start = DateUtils.parseDate(startTime, new String[] { "yyyy-MM-dd HH:mm:ss" });
				end = DateUtils.parseDate(endTime, new String[] { "yyyy-MM-dd HH:mm:ss" });
			}
		
		log.info("查询趋势数据时间范围为：start: [{}], end : [{}]", start, end);
		String indexName ="history-";
		List<String> indexList = DateUtil.getIndexList(start,end,indexName);
		
		SearchRequestBuilder requestBuilder = transportClient.prepareSearch(indexList.toArray(new String[] {})).setExplain(true);
		
		
		RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("clock");
		timeRange.gte(start.getTime() / 1000);
		timeRange.lt(end.getTime() / 1000);
		queryBuilder.must(timeRange);
		if (StringUtils.isNotEmpty(bizSystem)) {
			queryBuilder.must(QueryBuilders.termsQuery("bizSystem.keyword", bizSystem.trim().split("[\\,,\\，]")));
		}
		
		queryBuilder.must(
				QueryBuilders.termsQuery("item.keyword", storageItemNames.trim().split("[\\,,\\，]")));

		requestBuilder.setQuery(queryBuilder);
		/*
		 * TermsAggregationBuilder termsBuilder; termsBuilder =
		 * AggregationBuilders.terms("item").field("item.keyword").size(1000);
		 * termsBuilder.subAggregation(AggregationBuilders.max("max_used").field(
		 * "value_avg"));
		 * termsBuilder.subAggregation(AggregationBuilders.avg("avg_used").field(
		 * "value_avg"));
		 */
		requestBuilder.addAggregation(AggregationBuilders.max("max_used").field("value_avg"));
		requestBuilder.addAggregation(AggregationBuilders.avg("avg_used").field("value_avg"));
		log.info("查询设备利用率es脚本： {}", requestBuilder);
		SearchResponse response = requestBuilder.get();
		if (response.getHits().getTotalHits() > 0L) {
			final Terms terms = response.getAggregations().get("item");
			for (final Terms.Bucket a : terms.getBuckets()) {
				Max max = a.getAggregations().get("max_used");
				Avg avg = a.getAggregations().get("avg_used");
				Map<String, Double> result = Maps.newHashMap();
				result.put("max", max.getValue());
				result.put("avg", avg.getValue());
				resultMap.put(DeviceUsedRateEnum.getItemAliasByItemName(String.valueOf(a.getKey())), result);
			}
		}
		return resultMap;
	}

    public List<TrendsDto> query(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        List<TrendsDto> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setTypes(TYPE).setExplain(true);

        if(pageSize > 0) {
            int from = (pageNum - 1) * pageSize;
            from = from < 0 ? 0 : from;
            request.setFrom(from).setSize(pageSize);
        } else {
            //默认只返回10条，如果需要返回所有数据，设置10000
            request.setSize(10000);
        }
        SearchResponse response = request.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            TrendsDto trendsDto = new TrendsDto();
            try {
                BeanUtils.populate(trendsDto, sourceAsMap);
                list.add(trendsDto);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return list;
    }
    
    

    
    
}
