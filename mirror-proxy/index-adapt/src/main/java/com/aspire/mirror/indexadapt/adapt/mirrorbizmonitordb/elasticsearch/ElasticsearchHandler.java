package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.elasticsearch;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model.BizThemeDim;
import com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model.Items;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequestBuilder;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * es处理类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.handler
 * 类名称:    ElasticsearchHandler.java
 * 类描述:    es处理类
 * 创建人:    JinSu
 * 创建时间:  2018/10/25 15:56
 * 版本:      v1.0
 */
@Component
public class ElasticsearchHandler {
    public static final int MONTH_LIMIT = 10;
    public static final String SUB_INDEX_ITEM = "-item";
    public static final String TOP_HITS = "top_sales_hits";
    public static final String THEME_CONTENT = "theme_content";
    private Logger logger = LoggerFactory.getLogger(ElasticsearchHandler.class);

    @Value("${indexAdapter.mirrorbizmonitorDb.elasticsearch.index.name}")
    private String es_index;
    private static Set<String> indexSet = new HashSet<String>();

    @Value("${indexAdapter.mirrorbizmonitorDb.elasticsearch.index.fields.limit}")
    private Integer field_limit;

    @Autowired
    private MirrorElasticSearchClient elasticSearcClient;

    @Autowired
    private QueueHelper queueHelper;


    /**
     * 插入es指标计算数据
     *
     * @param item  监控项
     * @param value 计算结果值
     * @param date  日期
     */
    public void insertEs(Items item, String objectId, String groupKey, Object groupDesc, String remark,  Object value, Date date) {
        try {
            Map<String, Object> json = new HashMap<>();
            String indexName = getThemeItemIndex(json, null);
            if (null == indexName) {
                return;
            }
            json.put("@timestamp", new DateTime(date).toString());
            json.put("id", item.getItemId());
            json.put("triggerName", item.getName());
            json.put("keyName", item.getKey());
            json.put("remark", remark);
//            json.put("sysCode", item.getBizCode());
            json.put("objectId", objectId);
            json.put("groupKey", groupKey);
            json.put("groupDesc", groupDesc);
            json.put("themeId", item.getBizThemeId());
            json.put("logValue", value);
            IndexRequestBuilder lbr = elasticSearcClient.getClient().prepareIndex(indexName, "log").setSource(json);
            queueHelper.put(lbr);
        } catch (Exception e) {
            logger.error(
                    "es数据入库失败！id=" + item.getItemId() + ",value=" + value + ",date="
                            + date, e);
        }
    }

    /**
     * @param item
     * @param json
     * @param date
     * @param receiveTime
     */
    public void insertEs(Items item, Map<String, Object> json, Map<String, String> typeMap, Date date, Date
            receiveTime) {
        try {
            String indexName = getThemeItemIndex(json, typeMap);
            if (null == indexName) {
                return;
            }
            json.put("@timestamp", new DateTime(date).toString());
            json.put("id", item.getItemId());
            json.put("itemName", item.getName());
            json.put("keyName", item.getKey());
            json.put("objectId", item.getObjectIds().get(0));
            json.put("themeId", item.getBizThemeId());
            json.put("receiveTime", receiveTime);
            IndexRequestBuilder lbr = elasticSearcClient.getClient().prepareIndex(indexName, "log").setSource(json);
            queueHelper.put(lbr);
        } catch (Exception e) {
            logger.error(
                    "计算主题，es数据入库失败！id=" + item.getItemId() + ",json=" + json + ",date="
                            + date, e);
        }
    }

    public synchronized String getThemeItemIndex(Map<String, Object> json, Map<String, String> typeMap) {
        logger.info("ElasticsearchHandler[getThemeItemIndex] 参数json：{}， typeMap{}", json, typeMap);
        Date date = new Date();
        Integer year = DateUtil.getYear(date);
        Integer month = DateUtil.getMonth(date);
        String indexName = es_index + SUB_INDEX_ITEM + "-" + year + "." + (month < MONTH_LIMIT ? "0" : "") + month;
        if (!indexSet.contains(indexName)) {
            try {
                ClusterStateRequestBuilder request = elasticSearcClient.getClient().admin().cluster().prepareState()
                        .setIndices(indexName);
                ClusterStateResponse response = request.execute().actionGet();
                MetaData metaData = response.getState().metaData();
                // 不存在则创建索引
                if (metaData.getIndices().isEmpty()) {
                    CreateIndexRequestBuilder createIndexRequest = elasticSearcClient.getClient().admin().indices()
                            .prepareCreate(indexName);
                    CreateIndexResponse createIndexResponse = createIndexRequest.execute().actionGet();
                    if (!createIndexResponse.isAcknowledged()) {
                        logger.error("索引[" + indexName + "]创建失败!");
                        return null;
                    }
                    // 初始化字段类型
                    XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject("log")
                            .startObject("properties");
                    if (typeMap == null) {
                        builder.startObject("log_value").field("type", "double").endObject();
                    } else {
                        for (String fieldCode : json.keySet()) {
                            if (typeMap.get(fieldCode) != null && typeMap.get(fieldCode).equals("0")) {
                                builder.startObject(fieldCode).field("type", "double").endObject();
                            } else {
                                builder.startObject(fieldCode)
                                        .field("type", "keyword").endObject();
                            }
                        }
                    }
                    builder.endObject().endObject().endObject();
                    PutMappingRequest mapping = Requests.putMappingRequest(indexName).type("log").source(builder);
                    elasticSearcClient.getClient().admin().indices().putMapping(mapping).actionGet();
                }
                indexSet.add(indexName);
            } catch (Exception e) {
                logger.error("索引获取异常！", e);
            }
        } else {
            XContentBuilder builder = null;
            try {
                builder = XContentFactory.jsonBuilder().startObject()
                        .startObject("log").startObject("properties");
                for (String fieldCode : json.keySet()) {
                    if (typeMap != null && typeMap.get(fieldCode) != null && typeMap.get(fieldCode).equals("0")) {
                        builder.startObject(fieldCode)
                                .field("type", "double").endObject();
                    } else {
                        builder.startObject(fieldCode)
                                .field("type", "keyword").endObject();
                    }
                }
                builder.endObject().endObject().endObject();
                PutMappingRequest mapping = Requests.putMappingRequest(indexName).type("log")
                        .source(builder);
                elasticSearcClient.getClient().admin().indices().putMapping(mapping).actionGet();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return indexName;
    }

    public Map<String, Object> termsItemMap(Items items, String indexName, String query,
                                            JSONArray fieldArray, JSONArray dateArray, String dateField, String
                                                    aggField, String staticsField, JSONArray bizGroup) {
//        List<Map<String, Object>> list = Lists.newArrayList();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "关联对象");
//        jsonObject.put("code", "object_id");
//        bizGroup.add(jsonObject);
        JSONArray bizGroupFields = new JSONArray();
        if (bizGroup != null) {
            for (int i = 0; i < bizGroup.size(); i++) {
                bizGroupFields.add(bizGroup.getJSONObject(i).getString("code"));
            }
        }
        final Map<String, Object> map = Maps.newHashMap();
        if (null == indexName || null == fieldArray || null == dateArray || null == aggField) {
            logger.error("ElasticsearchHandler(termsItemMap),参数 indexName,fieldArray,dateArray,aggField不能为空");
            return map;
        }
        if (null == dateField || dateField.trim().length() == 0) {
            dateField = "@timestamp";
        }
        if (null == query || query.trim().length() == 0) {
            query = "node_code" + ":" + aggField + " AND " + staticsField + ":*" + " AND success: 0";
        }
        SearchResponse response = null;
        try {
            // 过滤条件
            final SearchRequestBuilder searchRequestBuilder = getSearchRequestBuilder(items, indexName, query,
                    fieldArray, dateArray, dateField, null);
            TermsAggregationBuilder termsBuilder;
            if (items.getMoniType().equals("1")) {
                termsBuilder = AggregationBuilders.terms("object_id").field("device_id.keyword").size(1000);
            } else {
                termsBuilder = AggregationBuilders.terms("object_id").field("biz_code.keyword").size(1000);
            }
            List<BizThemeDim> bizThemeDimList = items.getDimList();
            List<String> dimCodes = Lists.newArrayList();
            for (BizThemeDim bizThemeDim : bizThemeDimList) {
                dimCodes.add(bizThemeDim.getDimCode());
            }
            if (bizGroupFields != null && bizGroupFields.size() != 0) {
                TermsAggregationBuilder addBuilder = getTermsBuilder(0, bizGroupFields, staticsField, dimCodes);
                termsBuilder.subAggregation(addBuilder);
                searchRequestBuilder.addAggregation(termsBuilder);
            } else {
                termsBuilder.subAggregation(AggregationBuilders.topHits("top_sales_hits").fetchSource(
                        dimCodes.toArray(new String[dimCodes.size()]), null).sort("@timestamp", SortOrder.DESC).size(1));
                searchRequestBuilder.addAggregation(termsBuilder.subAggregation(AggregationBuilders.terms
                        (staticsField).field
                        (staticsField).size(1000)));
            }
            logger.debug("ElasticsearchHandler(termsItemMap), es查询参数：{}", searchRequestBuilder);

            // 发送请求
            response = searchRequestBuilder.get();
            if (response.getHits().getTotalHits() > 0L) {
                final Terms terms = response.getAggregations().get("object_id");
                for (final Terms.Bucket a : terms.getBuckets()) {

//                    Terms item = a.getAggregations().get("agg");
//                    Map<String, Long> itemMap = Maps.newHashMap();
//                    for (final Terms.Bucket b : item.getBuckets()) {
//                        itemMap.put(String.valueOf(b.getKey()), b.getDocCount());
//                    }
//                    itemMap.put("response.hits.total", a.getDocCount());
//                    map.put(String.valueOf(a.getKey()), itemMap);
                    List<Map<String, Object>> list = Lists.newArrayList();
                    Map<String, Object> mapItem = Maps.newHashMap();
                    if (bizGroupFields != null && bizGroupFields.size() != 0) {
                        final Terms termsItem = a.getAggregations().get(bizGroupFields.getString(0));
                        getResultMap(0, termsItem, bizGroupFields, staticsField, mapItem, list, items.getDimList());
                    } else {
                        final TopHits topHits = a.getAggregations().get(TOP_HITS);
                        for (SearchHit hit : topHits.getHits().getHits()){
                            Map<String, Object> sourceMap = hit.getSourceAsMap();
                            for (BizThemeDim bizThemeDim : items.getDimList()) {
                                bizThemeDim.setValue((String) sourceMap.get(bizThemeDim.getDimCode()));
                            }
                        }
                        map.put(THEME_CONTENT, JSON.toJSONString(items.getDimList()));
                        final Terms termsItem = a.getAggregations().get(staticsField);
                        getItemMapList(map, list, termsItem);
                        map.remove(THEME_CONTENT);
                    }
                    map.put(String.valueOf(a.getKey()), list);
                }
            }

//            map.put("response.hits.total", response.getHits().getTotalHits());
        } catch (final Exception e) {
            logger.error("ElasticSearchServiceImpl(termsMap)异常", e);
        }
        return map;
    }

    private void FieldFilterAdd(BoolQueryBuilder bqb, JSONObject item) {
        final String itemField = item.getString("field");
        final String fieldComparor = item.getString("fieldComparor");//
        final String fieldValue = item.getString("fieldValue").trim();
        if ("0".equals(fieldComparor)) {// >
            bqb.must(QueryBuilders.rangeQuery(itemField).gt(fieldValue));
        } else if ("1".equals(fieldComparor)) {// <
            bqb.must(QueryBuilders.rangeQuery(itemField).lt(fieldValue));
        } else if ("2".equals(fieldComparor)) {// =
            bqb.must(QueryBuilders.matchPhraseQuery(itemField, fieldValue));
        } else if ("3".equals(fieldComparor)) {// 包含
            bqb.must(QueryBuilders.termsQuery(itemField, fieldValue.toLowerCase().trim().split("[\\,,\\，,\\;,\\；]")));
        }
    }

    private void dateRangeAdd(String dateField, BoolQueryBuilder bqb, JSONObject itemDate, String dateComparor, Date
            groupTime) {
        // 0指标计算当前时间 1指标计算当日开始 2指标计算当日结束
        final String dateStaticsType = itemDate.getString("dateStaticsType");
        // + -
        final String dateOptionType = itemDate.getString("dateOptionType");
        // 值
        final String dateValue = itemDate.getString("dateValue");
        // 0天 1时 2分
        final String dateInterval = itemDate.getString("dateInterval");
        final Calendar c = Calendar.getInstance();
        if ("1".equals(dateStaticsType)) {
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        } else if ("2".equals(dateStaticsType)) {
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 999);
        } else if ("3".equals(dateStaticsType)) {
            c.setTime(groupTime);
        }
        int removeTime = 0;
        if ("0".equals(dateOptionType)) {
            removeTime = Integer.valueOf(dateValue);
        } else if ("1".equals(dateOptionType)) {
            removeTime = -Integer.valueOf(dateValue);
        }

        if ("0".equals(dateInterval)) {
            c.add(Calendar.DATE, removeTime);
        } else if ("1".equals(dateInterval)) {
            c.add(Calendar.HOUR, removeTime);
        } else if ("2".equals(dateInterval)) {
            c.add(Calendar.MINUTE, removeTime);
        }

        final Date searchTime = c.getTime();
        if ("0".equals(dateComparor)) {// >
            bqb.must(QueryBuilders.rangeQuery(dateField).gt(searchTime.getTime()));
        } else if ("1".equals(dateComparor)) {// <
            bqb.must(QueryBuilders.rangeQuery(dateField).lt(searchTime.getTime()));
        } else if ("2".equals(dateComparor)) {// =
            bqb.must(QueryBuilders.matchPhraseQuery(dateField, searchTime.getTime()));
        }
    }

    public List<Map<String, Object>> termsThemeCalcItemMap(Items items, String indexName, String query,
                                                           JSONArray fieldArray, JSONArray dateArray, String
                                                                   dateField, String
                                                                   aggField, String staticsField, JSONArray
                                                                   bizGroupArray, Date itemGroupTime) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (null == indexName || null == fieldArray || null == dateArray || null == aggField) {
            logger.error("ElasticsearchHandler(termsThemeCalcItemMap),参数 indexName,fieldArray,dateArray,aggField不能为空");
            return list;
        }
        if (null == dateField || dateField.trim().length() == 0) {
            dateField = "@timestamp";
        }
        if (null == query || query.trim().length() == 0) {
            query = "node_code" + ":" + aggField + " AND " + staticsField + ":*" + " AND success: 0";
        }
        SearchResponse response = null;
        try {
            final SearchRequestBuilder searchRequestBuilder = getSearchRequestBuilder(items, indexName, query,
                    fieldArray, dateArray, dateField, itemGroupTime);
            TermsAggregationBuilder termsBuilder;
            if (bizGroupArray != null && bizGroupArray.size() != 0) {
                termsBuilder = getTermsBuilder(0, bizGroupArray, staticsField, null);
            } else {
                termsBuilder = AggregationBuilders.terms(staticsField).field(staticsField);
            }
            searchRequestBuilder.addAggregation(termsBuilder);
            logger.debug("ElasticsearchHandler(termsThemeCalcItemMap), es查询参数：{}", searchRequestBuilder);
//                    // .shardSize(0)

            // 发送请求
            response = searchRequestBuilder.get();
            if (response.getHits().getTotalHits() > 0L) {
                Map<String, Object> map = Maps.newHashMap();
                if (bizGroupArray != null && bizGroupArray.size() != 0) {
                    final Terms terms = response.getAggregations().get(bizGroupArray.getString(0));
                    getResultMap(0, terms, bizGroupArray, staticsField, map, list, null);
                } else {
                    final Terms terms = response.getAggregations().get(staticsField);
                    getItemMapList(map, list, terms);
                }
            }
        } catch (final Exception e) {
            logger.error("ElasticSearchServiceImpl(termsMap)异常", e);
        }
        return list;
    }

    private void getResultMap(int index, Terms terms, JSONArray bizGroupArray, String staticsField, Map<String,
            Object> map, List<Map<String, Object>> mapList, List<BizThemeDim> dimList) {
        if (bizGroupArray.size() == index + 1) {
            for (final Terms.Bucket a : terms.getBuckets()) {
                map.put(bizGroupArray.getString(index), String.valueOf(a.getKey()));
                //维度列表不为空，表示需要初始化各维度值
                if (dimList != null) {
                    final TopHits topHits = a.getAggregations().get("top_sales_hits");
                    for (SearchHit hit : topHits.getHits().getHits()){
                        Map<String, Object> sourceMap = hit.getSourceAsMap();
                        for (BizThemeDim bizThemeDim : dimList) {
                            bizThemeDim.setValue((String) sourceMap.get(bizThemeDim.getDimCode()));
                        }
                    }
                    map.put("theme_content", JSON.toJSONString(dimList));
                }
                final Terms subTerms = a.getAggregations().get(staticsField);
                getItemMapList(map, mapList, subTerms);
            }
        } else {
            index++;
            for (final Terms.Bucket b : terms.getBuckets()) {
                map.put(bizGroupArray.getString(index - 1), String.valueOf(b.getKey()));
                final Terms subTerms = b.getAggregations().get(bizGroupArray.getString(index));
                getResultMap(index, subTerms, bizGroupArray, staticsField, map, mapList, dimList);
            }
        }
    }

    private void getItemMapList(Map<String, Object> map, List<Map<String, Object>> mapList, Terms subTerms) {
        Map<String, Long> mapValue = Maps.newHashMap();
        for (final Terms.Bucket c : subTerms.getBuckets()) {
            mapValue.put(String.valueOf(c.getKey()), c.getDocCount());
        }
        Map<String, Object> targetMap = Maps.newHashMap();
        targetMap.putAll(map);
        targetMap.put("value", mapValue);
        mapList.add(targetMap);
    }

    private TermsAggregationBuilder getTermsBuilder(int index, JSONArray bizGroupArray, String staticsField,
                                                    List<String> dimCodes) {
        if (bizGroupArray.size() == index + 1) {
            TermsAggregationBuilder lastBuild = AggregationBuilders.terms(bizGroupArray.getString(index)).field
                    (bizGroupArray.getString(index))
                    .size(1000)
                    .subAggregation(AggregationBuilders.terms(staticsField).field(staticsField).size(1000));

            // 主题类型指标dimCode存在，主题计算类型指标dimCode不存在
            if (!CollectionUtils.isEmpty(dimCodes)) {
                lastBuild.subAggregation(AggregationBuilders.topHits("top_sales_hits").fetchSource(
                        dimCodes.toArray(new String[dimCodes.size()]), null).sort("@timestamp", SortOrder.DESC).size(1));
            }
            return lastBuild;
        } else {
            return AggregationBuilders.terms(bizGroupArray.getString(index)).field(bizGroupArray.getString(index))
                    .size(1000)
                    .subAggregation(getTermsBuilder(++index, bizGroupArray, staticsField, dimCodes));
        }
    }

    private SearchRequestBuilder getSearchRequestBuilder(Items items, String indexName, String query, JSONArray
            fieldArray, JSONArray dateArray, String dateField, Date groupTime) {
        // 过滤条件
        final BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        // 拼接queryString
        bqb.must(QueryBuilders.queryStringQuery(query));
        boolean notItemFlag = true;// 如果都没有过滤字段，标识退出
        // 字段过滤查询
        for (int i = 0; i < fieldArray.size(); i++) {
            final JSONObject item = fieldArray.getJSONObject(i);
            if (!org.apache.commons.lang.StringUtils.isEmpty(item.getString("field"))) {
                notItemFlag = false;
                FieldFilterAdd(bqb, item);
            }
        }
        // 时间范围控制
        for (int i = 0; i < dateArray.size(); i++) {
            final JSONObject itemDate = dateArray.getJSONObject(i);
            final String jkzbDimDateField = itemDate.getString("field");
            if (org.apache.commons.lang.StringUtils.isNotEmpty(jkzbDimDateField) && !"@timestamp".equals
                    (jkzbDimDateField)) {
                dateField = jkzbDimDateField;
            } else {
                dateField = "@timestamp";
            }
            // 0> 1< 2=
            final String dateComparor = itemDate.getString("dateComparor");
            // 是否有查询条件控制
            if (!org.apache.commons.lang.StringUtils.isEmpty(dateComparor)) {
                notItemFlag = false;
                dateRangeAdd(dateField, bqb, itemDate, dateComparor, groupTime);
            }
        }
        if (notItemFlag) {
            // 没有过滤字段查询处理
            final Calendar c = Calendar.getInstance();
            // 数据到达时
            if ("1".equals(items.getCalcType())) {
                c.add(Calendar.MINUTE, -1);// 数据到达时，前推一分钟
            } else {
                c.add(Calendar.MINUTE, -Integer.valueOf(items.getDelay()));
            }
            bqb.must(QueryBuilders.rangeQuery(dateField).gte(c.getTime().getTime()).lte(new Date().getTime()));
        }
        // 组装请求
        return elasticSearcClient.getClient().prepareSearch(indexName)
                .setQuery(bqb);
    }

    public void deleteEsData(String itemId, Date itemGroupTime) {
        try {
            String indexName = es_index + "-*";
            if (null == indexName) {
                return;
            }
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            queryBuilder.must(QueryBuilders.matchPhraseQuery("@timestamp", new DateTime(itemGroupTime).toString()))
                    .must(QueryBuilders.queryStringQuery("id:" + itemId));
            BulkByScrollResponse response =
                    DeleteByQueryAction.INSTANCE.newRequestBuilder(elasticSearcClient
                            .getClient())
                            .filter(queryBuilder)
                            .source(indexName)
                            .get();
            long deleted = response.getDeleted();
            logger.info("es 删除数据条数：{}", deleted);
        } catch (Exception e) {
            logger.error(
                    "es删除数据失败！id=" + itemId + ",time=" + itemGroupTime, e);
        }
    }
}
