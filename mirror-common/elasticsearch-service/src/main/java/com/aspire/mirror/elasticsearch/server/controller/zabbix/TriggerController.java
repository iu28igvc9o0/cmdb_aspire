package com.aspire.mirror.elasticsearch.server.controller.zabbix;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.elasticsearch.api.dto.TriggerDto;
import com.aspire.mirror.elasticsearch.api.service.zabbix.IZabbixTriggerService;
import com.aspire.mirror.elasticsearch.server.controller.monitor.MonitorCommonController;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
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
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: TriggerController
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2015:53
 */
@RestController
@Slf4j
public class TriggerController extends MonitorCommonController implements IZabbixTriggerService {
    private static final String INDEX = "zabbix_trigger";
    private static final String TYPE = "trigger";
    @Autowired
    private TransportClient transportClient;

    public void insert(@RequestBody List<TriggerDto> triggerList) {
        if (CollectionUtils.isEmpty(triggerList)) {
            return;
        }
        log.info("新增zabbix trigger数据，数量：{}", triggerList.size());
        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        for (int i = 0; i < triggerList.size(); i++) {
            TriggerDto triggerDto = triggerList.get(i);
            bulkRequest.add(transportClient.prepareIndex(INDEX, TYPE).setSource(JSONObject.fromObject(triggerDto).toString(), XContentType.JSON));
            if ((i + 1) % 50 == 0) {
                bulkRequest.execute().actionGet();
                bulkRequest=transportClient.prepareBulk();
            }
        }
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
        }
//        String source = JSONObject.fromObject(trigger).toString();
//        IndexResponse response = transportClient.prepareIndex(INDEX, TYPE).setSource(source, XContentType.JSON).execute().actionGet();
    }

    public void deleteAll() {
        log.info("全量删除zabbix trigger数据");
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(transportClient)
                .source(INDEX)
                .filter(QueryBuilders.matchAllQuery())
                .get();
    }

    public PageResult<TriggerDto> query(@RequestParam(value = "itemId", required = false) String itemId,
                                        @RequestParam(value = "idc", required = false) String idc,
                                        @RequestParam(value = "prefix", required = false) String prefix,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        List<TriggerDto> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setTypes(TYPE).setExplain(true);
        if(pageSize > 0) {
            int from = (pageNum - 1) * pageSize;
            from = from < 0 ? 0 : from;
            request.setFrom(from).setSize(pageSize);
        } else {
            request.setSize(10000);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(itemId)) {
            QueryBuilder term = QueryBuilders.matchPhraseQuery("itemId", itemId);
            queryBuilder.must(term);
        }
        if (StringUtils.isNotEmpty(idc)) {
            QueryBuilder term = QueryBuilders.matchPhraseQuery("idc", idc);
            queryBuilder.must(term);
        }
        if (StringUtils.isNotEmpty(prefix)) {
            QueryBuilder term = QueryBuilders.matchPhraseQuery("prefix", prefix);
            queryBuilder.must(term);
        }
        SearchResponse response = request.setQuery(queryBuilder).execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            TriggerDto triggerDto = new TriggerDto();
            try {
                BeanUtils.populate(triggerDto, sourceAsMap);
                list.add(triggerDto);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        PageResult<TriggerDto> result = new PageResult<>();
        result.setCount((int) searchHits.getTotalHits());
        result.setCurPage(pageNum);
        result.setPageSize(pageSize);
        result.setResult(list);
        return result;
    }

    public List<Map<String, Object>> queryTriggerThreshold(@RequestParam(value = "itemIds", required = false) List<String> itemIds,
                                                 @RequestParam(value = "idc", required = false) String idc,
                                                 @RequestParam(value = "prefix", required = false) String prefix) {
        if (CollectionUtils.isEmpty(itemIds)) {
            return Lists.newArrayList();
        }
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setTypes(TYPE).setExplain(true);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (!CollectionUtils.isEmpty(itemIds)) {
            QueryBuilder term = QueryBuilders.termsQuery("itemId.keyword", itemIds.toArray());
            queryBuilder.must(term);
        }
        if (StringUtils.isNotEmpty(prefix)) {
            QueryBuilder term = QueryBuilders.matchPhraseQuery("prefix", prefix);
            queryBuilder.must(term);
        }

        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("itemId").field("itemId.keyword").size(itemIds.size());
//        termsBuilder.order(BucketOrder.aggregation("kpi_value", false));
        termsBuilder.subAggregation(AggregationBuilders.topHits("expression_hit").size(1).sort("priority.keyword", SortOrder.DESC));
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
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(s);
            getTopHis(jsonObject, list);
        }
        List<Map<String, Object>> returnlist = Lists.newArrayList();
        for (Map<String, Object> map: list) {
            Map<String, Object> map1 = Maps.newHashMap();
            map1.put("itemId",map.get("itemId"));
            String expression = MapUtils.getString(map, "expression");
            if (StringUtils.isEmpty(expression)) {
                continue;
            }
            if (expression.indexOf("}") < 0) {
                continue;
            }
            expression = expression.substring(expression.lastIndexOf("}"));
            expression = expression.replaceFirst("[^\\d]+", "");
            map1.put("threshold",expression);
            returnlist.add(map1);
        }
        return returnlist;
    }
}
