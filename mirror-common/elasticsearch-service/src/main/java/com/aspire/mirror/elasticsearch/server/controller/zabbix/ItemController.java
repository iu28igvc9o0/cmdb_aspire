package com.aspire.mirror.elasticsearch.server.controller.zabbix;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.elasticsearch.api.dto.ItemDto;
import com.aspire.mirror.elasticsearch.api.dto.ItemIndexDto;
import com.aspire.mirror.elasticsearch.api.dto.ItemRequest;
import com.aspire.mirror.elasticsearch.api.dto.MonitorKeyDto;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IZabbixItemService;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 * @author baiwp
 * @title: ItemController
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2015:53
 */
@RestController
@Slf4j
public class ItemController implements IZabbixItemService {
    private static final String INDEX = "zabbix_item";
    private static final String TYPE = "item";
    @Autowired
    private TransportClient transportClient;


    public void insert(@RequestBody List<ItemDto> itemDtoList) {
        if (CollectionUtils.isEmpty(itemDtoList)) {
            return;
        }
        log.info("新增zabbix item数据，数量：{}", itemDtoList.size());
        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        for (int i = 0; i < itemDtoList.size(); i++) {
            ItemDto itemDto = itemDtoList.get(i);
            bulkRequest.add(transportClient.prepareIndex(INDEX, TYPE).setSource(JSONObject.fromObject(itemDto)
                    .toString(), XContentType.JSON));
            if ((i + 1) % 50 == 0) {
                bulkRequest.execute().actionGet();
                bulkRequest = transportClient.prepareBulk();
            }
        }
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
        }
//        for (int i = 0; i < itemDtoList.size(); i++) {
//            ItemDto itemDto = itemDtoList.get(i);
//            IndexResponse response = transportClient.prepareIndex(INDEX, TYPE).setSource(JSONObject.fromObject
// (itemDto).toString(), XContentType.JSON).execute().actionGet();
//        }
    }

    public void deleteAll() {
        log.info("全量删除zabbix item数据");
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(transportClient)
                .source(INDEX)
                .filter(QueryBuilders.matchAllQuery())
                .get();
    }

    public PageResult<ItemDto> query(@RequestParam(value = "itemId", required = false) String itemId,
                                     @RequestParam(value = "idcType", required = false) String idcType,
                                     @RequestParam(value = "prefix", required = false) String prefix,
                                     @RequestParam(value = "ip", required = false) String ip,
                                     @RequestParam(value = "resourceId", required = false) String resourceId,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        List<ItemDto> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setTypes(TYPE).setExplain(true);
        if (pageSize > 0) {
            int from = (pageNum - 1) * pageSize;
            from = from < 0 ? 0 : from;
            request.setFrom(from).setSize(pageSize);
        } else {
            request.setSize(10000);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(itemId)) {
            QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("itemId", itemId);
            queryBuilder.must(queryTermId);
        }
        if (StringUtils.isNotEmpty(resourceId)) {
            QueryBuilder queryResourceId = QueryBuilders.matchPhraseQuery("resourceId", resourceId);
            queryBuilder.must(queryResourceId);
        }
        if (StringUtils.isNotEmpty(idcType)) {
            QueryBuilder queryIdc = QueryBuilders.matchPhraseQuery("idcType", idcType);
            queryBuilder.must(queryIdc);
        }
        if (StringUtils.isNotEmpty(prefix)) {
            QueryBuilder queryPrefix = QueryBuilders.matchPhraseQuery("prefix", prefix);
            queryBuilder.must(queryPrefix);
        }
        if (StringUtils.isNotEmpty(ip)) {
            QueryBuilder queryIp = QueryBuilders.matchPhraseQuery("ip", ip);
            queryBuilder.must(queryIp);
        }
        SearchResponse response = request.setQuery(queryBuilder).execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            ItemDto itemDto = new ItemDto();
            try {
                BeanUtils.populate(itemDto, sourceAsMap);
                list.add(itemDto);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        PageResult<ItemDto> result = new PageResult<>();
        result.setCount((int) searchHits.getTotalHits());
        result.setCurPage(pageNum);
        result.setPageSize(pageSize);
        result.setResult(list);
        return result;
    }

    public Map<String, Set<ItemIndexDto>> queryObject(@RequestBody ItemRequest itemRequest) {
        Map<String, Set<ItemIndexDto>> map = new HashMap<>();
        String[] fields = new String[4];
        fields[0] = "key"; // 字段1名称
        fields[1] = "moniObject"; // 字段2名称
        fields[2] = "units"; // 字段3名称
        fields[3] = "valueType"; // 字段4名称
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setTypes(TYPE);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(itemRequest.getBizSystem())) {
            QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("bizSystem", itemRequest.getBizSystem());
            queryBuilder.must(queryTermId);
        }
        if (StringUtils.isNotEmpty(itemRequest.getIdcType())) {
            QueryBuilder queryIdc = QueryBuilders.matchPhraseQuery("idcType", itemRequest.getIdcType());
            queryBuilder.must(queryIdc);
        }
        if (StringUtils.isNotEmpty(itemRequest.getDeviceClass())) {
            QueryBuilder queryIdc = QueryBuilders.matchPhraseQuery("deviceClass", itemRequest.getDeviceClass());
            queryBuilder.must(queryIdc);
        }
        if (StringUtils.isNotEmpty(itemRequest.getDeviceType())) {
            QueryBuilder queryIdc = QueryBuilders.matchPhraseQuery("deviceType", itemRequest.getDeviceType());
            queryBuilder.must(queryIdc);
        }
        if (StringUtils.isNotEmpty(itemRequest.getRoomId())) {
            QueryBuilder queryIdc = QueryBuilders.matchPhraseQuery("roomId", itemRequest.getRoomId());
            queryBuilder.must(queryIdc);
        }
        if (itemRequest.getIps() != null && itemRequest.getIps().length > 0) {
            QueryBuilder queryIp = QueryBuilders.constantScoreQuery(QueryBuilders.termsQuery("ip", itemRequest.getIps
                    ()));
            queryBuilder.must(queryIp);
        }
        QueryBuilder queryType = QueryBuilders.constantScoreQuery(QueryBuilders.termsQuery("valueType", "0", "3"));
        queryBuilder.must(queryType);

        request.setExplain(true).setQuery(queryBuilder)
                .setFetchSource(fields, null).setSize(0);
//                .setScroll(TimeValue.timeValueMinutes(1));
        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("moniObject.keyword").field("moniObject" +
                ".keyword").size(10000);
        termsBuilder.subAggregation(AggregationBuilders.terms("key.keyword").field("key.keyword").size(10000).subAggregation(AggregationBuilders.terms("units.keyword").field("units.keyword").subAggregation
                (AggregationBuilders.topHits("top_sales_hits").fetchSource(fields, null).sort
                        ("_uid", SortOrder.DESC).size(1))));
        request.addAggregation(termsBuilder);
        log.info("itemController-queryObject es paramObject is：{}", request);
        SearchResponse scrollResp = request.get();
//        log.info("共匹配到:" + scrollResp.getHits().getTotalHits() + "条记录!");
        //当searchHits的数组为空的时候结束循环，至此数据全部读取完毕
//        if (scrollResp.getHits().getHits().length != 0) {

        Terms terms = scrollResp.getAggregations().get("moniObject.keyword");
        log.info("查询到{}条数据", terms.getBuckets().size());
        for (final Terms.Bucket a : terms.getBuckets()) {
            Set<ItemIndexDto> set = map.get(a.getKey());
            if (set == null) {
                set = new HashSet<>();
                map.put((String) a.getKey(), set);
            }
            final Terms keyTerms = a.getAggregations().get("key.keyword");
            for (final Terms.Bucket keyBucket : keyTerms.getBuckets()) {
                final Terms subTerms = keyBucket.getAggregations().get("units.keyword");
                for (final Terms.Bucket b : subTerms.getBuckets()) {
                    final TopHits topHits = b.getAggregations().get("top_sales_hits");
                    for (SearchHit searchHit : topHits.getHits().getHits()) {
                        searchHit.getSourceAsMap().put("id", searchHit.getId());
                        Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                        ItemIndexDto itemIndexDto = new ItemIndexDto();
                        try {
                            BeanUtils.populate(itemIndexDto, sourceAsMap);
                            set.add(itemIndexDto);
                        } catch (Exception e) {
                            log.error("解析指标数据失败！{}", e);
                        }
                    }
                }
            }
        }
//            for (SearchHit searchHit : scrollResp.getHits().getHits()) {
//                searchHit.getSourceAsMap().put("id", searchHit.getId());
//                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
//                ItemIndexDto itemIndexDto = new ItemIndexDto();
//                try {
//                    BeanUtils.populate(itemIndexDto, sourceAsMap);
//                } catch (IllegalAccessException e) {
//                } catch (InvocationTargetException e) {
//                }
//                String key = itemIndexDto.getKey();
//                String moniObject = itemIndexDto.getMoniObject();
//                if (StringUtils.isEmpty(key) || StringUtils.isEmpty(moniObject)) {
//                    continue;
//                }
//                Set<ItemIndexDto> set = map.get(moniObject);
//                if (null == set) {
//                    set = new HashSet<>();
//                    map.put(moniObject, set);
//                }
//                set.add(itemIndexDto);
//            }
        //将scorllId循环传递
//            scrollResp = transportClient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(TimeValue
//                    .timeValueMinutes(1)).execute().actionGet();
//        }
        return map;
    }

    @Override
    public List<String> queryMoniObjects(@RequestBody ItemRequest itemRequest) {
        List<String> result = Lists.newArrayList();
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setTypes(TYPE);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(itemRequest.getBizSystem())) {
            QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("bizSystem", itemRequest.getBizSystem());
            queryBuilder.must(queryTermId);
        }
        if (StringUtils.isNotEmpty(itemRequest.getIdcType())) {
            QueryBuilder queryIdc = QueryBuilders.matchPhraseQuery("idcType", itemRequest.getIdcType());
            queryBuilder.must(queryIdc);
        }
        if (StringUtils.isNotEmpty(itemRequest.getDeviceClass())) {
            QueryBuilder queryIdc = QueryBuilders.matchPhraseQuery("deviceClass", itemRequest.getDeviceClass());
            queryBuilder.must(queryIdc);
        }
        if (StringUtils.isNotEmpty(itemRequest.getDeviceType())) {
            QueryBuilder queryIdc = QueryBuilders.matchPhraseQuery("deviceType", itemRequest.getDeviceType());
            queryBuilder.must(queryIdc);
        }
        if (StringUtils.isNotEmpty(itemRequest.getRoomId())) {
            QueryBuilder queryIdc = QueryBuilders.matchPhraseQuery("roomId", itemRequest.getRoomId());
            queryBuilder.must(queryIdc);
        }
        if (itemRequest.getIps() != null && itemRequest.getIps().length > 0) {
            QueryBuilder queryIp = QueryBuilders.constantScoreQuery(QueryBuilders.termsQuery("ip", itemRequest.getIps
                    ()));
            queryBuilder.must(queryIp);
        }
        QueryBuilder queryType = QueryBuilders.constantScoreQuery(QueryBuilders.termsQuery("valueType", "0", "3"));
        queryBuilder.must(queryType);

        request.setExplain(true).setQuery(queryBuilder)
                .setSize(0);
//                .setScroll(TimeValue.timeValueMinutes(1));
        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("moniObject.keyword").field("moniObject" +
                ".keyword").size(10000);
        request.addAggregation(termsBuilder);
        log.info("itemController-queryMoniObjects es paramObject is：{}", request);
        SearchResponse scrollResp = request.get();
//        log.info("共匹配到:" + scrollResp.getHits().getTotalHits() + "条记录!");
        //当searchHits的数组为空的时候结束循环，至此数据全部读取完毕
//        if (scrollResp.getHits().getHits().length != 0) {

        Terms terms = scrollResp.getAggregations().get("moniObject.keyword");
        log.info("查询到{}条数据", terms.getBuckets().size());
        for (final Terms.Bucket a : terms.getBuckets()) {
            result.add((String) a.getKey());
        }
        return result;
    }
    
    @Override
    public MonitorKeyDto queryMonitorObjectList(@RequestBody ItemRequest itemRequest) {
        String[] fields = new String[5];
        fields[0] = "key"; // 字段1名称
        fields[1] = "moniObject"; // 字段2名称
        fields[2] = "units"; // 字段3名称
        fields[3] = "valueType"; // 字段4名称
        fields[4] = "itemId"; // 字段4名称
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setTypes(TYPE);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(itemRequest.getBizSystem())) {
            QueryBuilder queryTermId = QueryBuilders.termQuery("bizSystem.keyword", itemRequest.getBizSystem());
            queryBuilder.must(queryTermId);
        }
        if (StringUtils.isNotEmpty(itemRequest.getIdcType())) {
            QueryBuilder queryIdc = QueryBuilders.termQuery("idcType.keyword", itemRequest.getIdcType());
            queryBuilder.must(queryIdc);
        }
        if (StringUtils.isNotEmpty(itemRequest.getDeviceClass())) {
            QueryBuilder queryIdc = QueryBuilders.termQuery("deviceClass.keyword", itemRequest.getDeviceClass());
            queryBuilder.must(queryIdc);
        }
        if (StringUtils.isNotEmpty(itemRequest.getDeviceType())) {
            QueryBuilder queryIdc = QueryBuilders.termQuery("deviceType.keyword", itemRequest.getDeviceType());
            queryBuilder.must(queryIdc);
        }
        if (StringUtils.isNotEmpty(itemRequest.getRoomId())) {
            QueryBuilder queryIdc = QueryBuilders.termQuery("roomId.keyword", itemRequest.getRoomId());
            queryBuilder.must(queryIdc);
        }
        if (itemRequest.getIps() != null && itemRequest.getIps().length > 0) {
            QueryBuilder queryIp = QueryBuilders.constantScoreQuery(QueryBuilders.termsQuery("ip", itemRequest.getIps
                    ()));
            queryBuilder.must(queryIp);
        }
        
        if (StringUtils.isNotEmpty(itemRequest.getItem())) {
            QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("key", itemRequest.getItem());
            queryBuilder.must(queryTermId);
        }
        QueryBuilder queryType = QueryBuilders.constantScoreQuery(QueryBuilders.termsQuery("valueType", "0", "3"));
        queryBuilder.must(queryType);

        request.setExplain(true).setQuery(queryBuilder)
                .setFetchSource(fields, null).setSize(1);
//                .setScroll(TimeValue.timeValueMinutes(1));
        if(itemRequest.isMultiQuery()) {
        	TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("key_type").field("key" +
                    ".keyword").subAggregation((AggregationBuilders.topHits("top_sales_hits").fetchSource(fields, null).size(1))).size(10000);
//            (AggregationBuilders.topHits("top_sales_hits").fetchSource(fields, null).sort
//                    ("_uid", SortOrder.DESC).size(1))
            request.addAggregation(termsBuilder);
        }
        
        
        log.info("itemController-queryMonitorObject es paramObject is：{}", request);
        
        SearchResponse searchResponse = request.get();
        
        SearchHits hits = searchResponse.getHits();
        long totalHits = hits.getTotalHits();
        SearchHit[] hits2 = hits.getHits();
        log.info("查询到{}条数据", totalHits);
        
        if(hits2.length>0) {
        	Map<String, Object> sourceAsMap = hits2[0].getSourceAsMap();
        	MonitorKeyDto itemIndexDto = new MonitorKeyDto();
             try {
                 BeanUtils.populate(itemIndexDto, sourceAsMap);
             } catch (Exception e) {
                 log.error("解析指标数据失败！{}", e);
             }
			if (itemRequest.isMultiQuery()) {
				List keys = Lists.newArrayList();
				StringTerms h = (StringTerms) searchResponse.getAggregations().get("key_type");
                List<MonitorKeyDto> moliList = Lists.newArrayList();
				for (StringTerms.Bucket bucket : h.getBuckets()) {
                    final TopHits topHits = bucket.getAggregations().get("top_sales_hits");
                    for (SearchHit searchHit : topHits.getHits().getHits()) {
                        Map<String, Object> sourceMap = searchHit.getSourceAsMap();
                        MonitorKeyDto subItem = new MonitorKeyDto();
                        try {
                            BeanUtils.populate(subItem, sourceMap);
                        } catch (Exception e) {
                            log.error("解析指标数据失败！{}", e);
                        }
                        moliList.add(subItem);
                    }
					if (StringUtils.isNotBlank(bucket.getKeyAsString())) {
						keys.add(bucket.getKey().toString());
					}
				}
                itemIndexDto.setMoniList(moliList);
				itemIndexDto.setKeys(keys);
			}
             return itemIndexDto;
        }
        
		/*
		 * for(SearchHit h : hits2) { h.getSourceAsMap().put("id", h.getId());
		 * Map<String, Object> sourceAsMap = h.getSourceAsMap(); ItemIndexDto
		 * itemIndexDto = new ItemIndexDto(); try { BeanUtils.populate(itemIndexDto,
		 * sourceAsMap); set.add(itemIndexDto); } catch (Exception e) {
		 * log.error("解析指标数据失败！{}", e); } //System.out.println(h.getSourceAsString()); }
		 */

        return null;
    }
}
