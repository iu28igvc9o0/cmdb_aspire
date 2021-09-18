package com.aspire.mirror.elasticsearch.server.controller.cmdb.service.impl;

import com.aspire.mirror.elasticsearch.api.dto.ItemDto;
import com.aspire.mirror.elasticsearch.api.dto.cmdb.Result;
import com.aspire.mirror.elasticsearch.server.controller.cmdb.service.ICmdbESService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbESServiceImpl
 * Author:   zhu.juwang
 * Date:     2020/2/14 17:28
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CmdbESServiceImpl implements ICmdbESService {

    @Autowired
    private TransportClient transportClient;

    public boolean checkExists(String index) {
        // 先判断index是否存在
        IndicesExistsRequest request = new IndicesExistsRequest(index);
        IndicesAdminClient adminClient = transportClient.admin().indices();
        IndicesExistsResponse response = adminClient.exists(request).actionGet();
        // 已经存在, 则跳过.
        if (response.isExists()) {
            return true;
        }
        return false;
    }

    public Map<String, String> createIndex(Map<String, Object> mappingSetting, String index) {
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("flag", "success");
        if (!checkExists(index)) {
            // 创建索引
            IndicesAdminClient adminClient = transportClient.admin().indices();
            CreateIndexRequestBuilder cib = adminClient.prepareCreate(index);
            XContentBuilder mapping = null;
            try {
                mapping = XContentFactory.jsonBuilder()
                        .startObject()//表示开始设置值
                        .startObject("properties");
                int loop = 0;
                for (String key : mappingSetting.keySet()) {
                    mapping.startObject(key).field("type", "keyword").endObject();
                    loop ++;
//                    if (loop > 10) {
//                        break;
//                    }
                }
                mapping.endObject();
                mapping.endObject();
            } catch (IOException e) {
                log.error("Setting es index [{}] mapping error.", index, e);
                returnMap.put("flag", "error");
                returnMap.put("msg", "Setting es index " + index + " mapping error." + e.getLocalizedMessage());
            }
            try {
                cib.addMapping(index, mapping);
                cib.execute().actionGet();
                try {
                    // 设置es index max_result_window 防止查询超过1W报错
                    JSONObject settings = JSONObject.fromObject("{\"max_result_window\": \"200000000\"}");
                    UpdateSettingsRequest request = Requests.updateSettingsRequest(index).settings(settings);
                    adminClient.updateSettings(request).actionGet();
                } catch (Exception e) {
                    log.error("Update index [{}] max_result_window error.", index, e);
                }
            } catch (Exception e) {
                log.error("Create es index [{}] error.", index, e);
                returnMap.put("flag", "error");
                returnMap.put("msg", "Setting es index " + index + " error." + e.getLocalizedMessage());
            }
        }
        return returnMap;
    }

    @Override
    public Map<String, String> insert(List<Map<String, Object>> data, String index, String type) {
        Map<String, String> returmMap = new HashMap<>();
        if (CollectionUtils.isEmpty(data)) {
            returmMap.put("flag", "error");
            returmMap.put("msg", "Data list can't empty.");
            return returmMap;
        }
        try {
//            if (!checkExists(index)) {
//                Map<String, String> createResult = createIndex(data.get(0), index);
//                if (createResult.get("flag").equals("error")) {
//                    return createResult;
//                }
//            }
            log.info("Insert data to es. index: --> {} type: -->{}", index, type);
            BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).containsKey("id")) {
                    bulkRequest.add(transportClient.prepareIndex(index, type).setId(data.get(i).get("id").toString())
                            .setSource(JSONObject.fromObject(data.get(i)).toString(), XContentType.JSON));
                } else {
                    bulkRequest.add(transportClient.prepareIndex(index, type)
                            .setSource(JSONObject.fromObject(data.get(i)).toString(), XContentType.JSON));
                }
                if ((i + 1) % 1000 == 0) {
                    bulkRequest.execute().actionGet();
                    bulkRequest = transportClient.prepareBulk();
                }
            }
            if (bulkRequest.numberOfActions() > 0) {
                bulkRequest.execute().actionGet();
            }
//            BulkProcessor bulkProcessor = BulkProcessor.builder(transportClient, new BulkProcessor.Listener() {
//                @Override
//                public void beforeBulk(long l, BulkRequest bulkRequest) {
//                    log.info("Start bulk data to es at {}.", new Date().getTime());
//                }
//                @Override
//                public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
//                    log.info("Bulk data to es success at {}.", new Date().getTime());
//                }
//                @Override
//                public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
//                    log.error("Bulk data to es error at {}.", new Date().getTime());
//                }
//            }).setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
//                    .setBulkActions(1000)
//                    .setFlushInterval(TimeValue.timeValueSeconds(5))
//                    .build();
//            for (Map<String, Object> d : data) {
//                if (d.containsKey("id")) {
//                    bulkProcessor.add(new IndexRequest(index, type, String.valueOf(d.get("id"))).source(d));
//                } else {
//                    bulkProcessor.add(new IndexRequest(index, type).source(d));
//                }
//            }
            returmMap.put("flag", "success");
        } catch (Exception e) {
            log.error("Insert data to es error", e);
            returmMap.put("flag", "error");
            returmMap.put("msg", e.getMessage());
        }
        return returmMap;
    }

    @Override
    public Map<String, Object> deleteIndex(String index) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            log.info("开始删除索引 {}", index);
            String[] idxs = index.split(",");
            // 删除索引
            for (String idx : idxs) {
                IndicesExistsResponse response = transportClient.admin().indices().prepareExists(idx).execute().actionGet();
                if (response.isExists()) {
                    transportClient.admin().indices().prepareDelete(idx).execute().actionGet();
                }
            }
            resultMap.put("success", true);
            resultMap.put("message", "删除索引成功");
            log.info("删除索引 {} 成功", index);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "删除索引失败" + e.getMessage());
            log.info("删除索引 {} 成功", index);
        }
        return resultMap;
    }

    /**
     * 验证查询信息
     * @param querySetting
     */
    private void valid(Map<String, Object> querySetting) {
        if (!querySetting.containsKey("index")) {
            throw new RuntimeException("Query parameter[index] is required.");
        }
        if (!querySetting.containsKey("type")) {
            throw new RuntimeException("Query parameter[type] is required.");
        }
    }

    /**
     * 设置分页数据
     * @param request
     * @param querySetting
     */
    private void setPage(SearchRequestBuilder request, Map<String, Object> querySetting) {
        Integer pageSize = 100, currentPage = 1;
        if (querySetting.containsKey("pageSize")) {
            try {
                pageSize = Integer.parseInt(querySetting.get("pageSize").toString());
            } catch (Exception e) {
                throw new RuntimeException("Query parameter[pageSize] is invalid.");
            }
        }
        if (querySetting.containsKey("currentPage")) {
            try {
                currentPage = Integer.parseInt(querySetting.get("currentPage").toString());
            } catch (Exception e) {
                throw new RuntimeException("Query parameter[currentPage] is invalid.");
            }
        }
        if (currentPage != null && pageSize != null) {
            currentPage = currentPage < 1 ? 1 : currentPage;
            int from = (currentPage - 1) * pageSize;
            request.setFrom(from).setSize(pageSize);
        }
    }

    /**
     * 设置返回字典结果
     * @param request
     * @param querySetting
     */
    private void setResultFiled(SearchRequestBuilder request, Map<String, Object> querySetting) {
        Map<String, String> resultMap = null;
        if (querySetting.containsKey("result")) {
            try {
                resultMap = (Map<String, String>) querySetting.get("result");
            } catch (Exception e) {
                throw new RuntimeException("Format params error. querySetting.result is must List<String>.");
            }
        }
        if (null!=resultMap && !resultMap.isEmpty()) {
            String[] fields = new String[resultMap.keySet().size()];
            request.setFetchSource(true);
            request.setFetchSource(resultMap.keySet().toArray(fields), null);
        }
    }

    /**
     * 设置查询条件
     * @param request
     * @param querySetting
     */
    private BoolQueryBuilder setQuery(SearchRequestBuilder request, Map<String, Object> querySetting) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 设置查询条件
        List<Map<String, String>> queryParams = null;
        if (querySetting.containsKey("params")) {
            try {
                queryParams = (List<Map<String, String>>) querySetting.get("params");
            } catch (Exception e) {
                throw new RuntimeException("Format params error. querySetting.params is must List<Map<String,Object>>.");
            }
        }
        // 组装查询条件
        if (queryParams != null) {
        	//Locale.setDefault(Locale.ENGLISH);
            for (Map<String, String> param : queryParams) {
                if (!param.containsKey("filed")) {
                    throw new RuntimeException("Format params error. querySetting.params every member must has filed、operator、value properties.");
                }
                if (!param.containsKey("operator")) {
                    throw new RuntimeException("Format params error. querySetting.params every member must has filed、operator、value properties.");
                }
                if (!param.containsKey("value")) {
                    throw new RuntimeException("Format params error. querySetting.params every member must has filed、operator、value properties.");
                }
                String filed = param.get("filed").trim();
                String operator = param.get("operator").trim();
                String filedType = param.get("filed_type") == null ? "" : param.get("filed_type").toLowerCase(Locale.ENGLISH);
                Object value = parseValue(param.get("value"), filedType);
                if (!isEmpty(value)) {
                    if (!filedType.equals("datetime")) {
                        filed = filed + ".keyword";
                    }
                    if (("like").equalsIgnoreCase(operator)) {
                        WildcardQueryBuilder wildQueryBuilder = QueryBuilders.wildcardQuery(filed, "*" + value + "*");
                        queryBuilder.must(wildQueryBuilder);
                    } else if ("<".equalsIgnoreCase(operator) || "<=".equalsIgnoreCase(operator)) {
                        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(filed).lt(value);
                        queryBuilder.must(rangeQueryBuilder);
                    } else if (">".equalsIgnoreCase(operator) || ">=".equalsIgnoreCase(operator)) {
                        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(filed).gt(value);
                        queryBuilder.must(rangeQueryBuilder);
                    } else if ("=".equalsIgnoreCase(operator)) {
                        QueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery(filed, value);
                        queryBuilder.must(matchPhraseQuery);
                    } else if ("in".equalsIgnoreCase(operator)) {
                        List<String> inList = (List<String>) value;
                        String[] values = new String[inList.size()];
                        QueryBuilder matchQuery = QueryBuilders.termsQuery(filed, inList.toArray(values));
                        queryBuilder.must(matchQuery);
                    } else if ("not in".equalsIgnoreCase(operator)) {
                        List<String> notInList = (List<String>) value;
                        String[] values = new String[notInList.size()];
                        QueryBuilder matchQuery = QueryBuilders.multiMatchQuery(filed, values);
                        queryBuilder.mustNot(matchQuery);
                    } else if ("between".equalsIgnoreCase(operator)) {
                        List<String> betweenList = (List<String>) value;
                        if (betweenList.size() <= 2) {
                            betweenList.add(betweenList.get(0));
                        }
                        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(filed).gt(betweenList.get(0)).lt(betweenList.get(1));
                        queryBuilder.must(rangeQueryBuilder);
                    }
                }
            }
            request.setQuery(queryBuilder);
        }
        return queryBuilder;
    }

    /**
     * 解析结果值
     * @param value 值
     * @param filedType 字段类型
     * @return
     */
    private Object parseValue(Object value, String filedType) {
        // 对字段是日期类型的特殊处理
        if (filedType != null && filedType.equalsIgnoreCase("datetime")) {
            if (value instanceof String) {
                try {
                    Date date = DateUtils.parseDate(value.toString(), new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
                    return date.getTime();
                } catch (ParseException e) {
                    throw new RuntimeException("Params.value parse to date error. Params.value = " + value);
                }
            }
            if (value instanceof List) {
                List<String> valueList = (List<String>) value;
                List<String> longList = new LinkedList<>();
                for (String vl : valueList) {
                    try {
                        Date date = DateUtils.parseDate(vl, new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
                        longList.add(String.valueOf(date.getTime()));
                    } catch (ParseException e) {
                        throw new RuntimeException("Params.value parse to date error. Params.value = " + value);
                    }
                }
                return longList;
            }
        }
        return value;
    }

    private boolean isEmpty(Object value) {
        if (value != null) {
            if (value instanceof List) {
                return ((List) value).size() == 0;
            }
            if (value instanceof String) {
                return StringUtils.isEmpty(value.toString());
            }
            return false;
        }
        return true;
    }

    /**
     * 设置排序信息
     * @param request
     * @param querySetting
     */
    private void setSort(SearchRequestBuilder request, Map<String, Object> querySetting) {
        List<Map<String, String>> sortList = null;
        // 设置排序条件
        if (querySetting.containsKey("sort")) {
            try {
                sortList = (List<Map<String, String>>) querySetting.get("sort");
            } catch (Exception e) {
                throw new RuntimeException("Format params error. querySetting.sort is must List<Map<String,Object>>.");
            }
        }
        if (sortList != null) {
            for (Map<String, String> sort : sortList) {
                if (!sort.containsKey("filed")) {
                    throw new RuntimeException("Format params error. querySetting.sort every member must has filed、type properties.");
                }
                if (!sort.containsKey("type")) {
                    throw new RuntimeException("Format params error. querySetting.params every member must has filed、type properties.");
                }
                String filed = sort.get("filed").trim();
                String sortType = sort.get("type").trim();
                if (!sortType.toLowerCase().equalsIgnoreCase("asc") && !sortType.toLowerCase().equalsIgnoreCase("desc")) {
                    throw new RuntimeException("Format params error. querySetting.sort.type only support asc/desc.");
                }
                if (sortType.toLowerCase().equalsIgnoreCase("asc")) {
                    request.addSort(filed + ".keyword", SortOrder.ASC);
                }
                if (sortType.toLowerCase().equalsIgnoreCase("desc")) {
                    request.addSort(filed + ".keyword", SortOrder.DESC);
                }
            }
        }
    }

    /**
     * 创建查询链接
     * @param querySetting
     * @return
     */
    private SearchRequestBuilder createRequestBuilder(Map<String, Object> querySetting) {
        String index = querySetting.get("index").toString();
        String type = querySetting.get("type").toString();
        return transportClient.prepareSearch(index).setTypes(type).setExplain(true);
    }

    @Override
    public Result<Map<String, Object>> list(Map<String, Object> querySetting) {
        this.valid(querySetting);
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder request = createRequestBuilder(querySetting);

        this.setPage(request, querySetting);
        this.setResultFiled(request, querySetting);
        this.setQuery(request, querySetting);
        this.setSort(request, querySetting);

        log.info("ES request params -> {}", request);
        SearchResponse response = request.execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("Total find {} records.", searchHits.getTotalHits());
        SearchHit[] hits = searchHits.getHits();
        Map<String, String> filedListMap = null;
        if (querySetting.containsKey("filed_list")) {
            try {
                filedListMap = (Map<String, String>) querySetting.get("filed_list");
            } catch (Exception e) {
                throw new RuntimeException("Format params error. querySetting.result is must List<String>.");
            }
        }
        for (SearchHit searchHit : hits) {
            Map<String, Object> objectMap = searchHit.getSourceAsMap();
            list.add(format(objectMap, filedListMap));
        }
        Result<Map<String, Object>> result = new Result<>();
        result.setCount((int) searchHits.getTotalHits());
        result.setData(list);
        return result;
    }

    private List<Map<String, Object>> searchAll(Map<String, Object> querySetting) {
        SearchRequestBuilder searchRequestBuilder = this.createRequestBuilder(querySetting);
        this.setQuery(searchRequestBuilder, querySetting);
        this.setResultFiled(searchRequestBuilder, querySetting);
        this.setSort(searchRequestBuilder, querySetting);
        //设置每批读取的数据量
        searchRequestBuilder.setSize(10000);
        //设置 search context 维护1分钟的有效期
        searchRequestBuilder.setScroll(TimeValue.timeValueMinutes(2L));
        //获得首次的查询结果
        SearchResponse scrollResp=searchRequestBuilder.get();
        log.info("Request ES params -> {}", searchRequestBuilder);
        long totalHits = scrollResp.getHits().getTotalHits();
        log.info("Total find {} records.", totalHits);
        List<Map<String, Object>> returnList = new LinkedList();
        int i = 0;
        do {
            log.info("Read hits record -> {} - {}", (i) * 100, ( i + 1) * 100);
            for (SearchHit searchHit : scrollResp.getHits().getHits()) {
                returnList.add(format(searchHit.getSourceAsMap(), null));
            }
            //将scrollId循环传递
            scrollResp = transportClient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(TimeValue.timeValueMinutes(1)).execute().actionGet();
            i ++ ;
            //当searchHits的数组为空的时候结束循环，至此数据全部读取完毕
        } while(scrollResp.getHits().getHits().length != 0);
        //删除scroll
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollResp.getScrollId());
        transportClient.clearScroll(clearScrollRequest).actionGet();
        return returnList;
    }

    @Override
    public Map<String, Object> getById(String id, String index, String type) {
        // 创建查询链接
        SearchResponse response = transportClient.prepareSearch(index).setTypes(type)
                .setQuery(QueryBuilders.idsQuery().addIds(id)).get();
        SearchHits searchHits = response.getHits();
        log.info("Total find {} records.", searchHits.getTotalHits());
        SearchHit[] hits = searchHits.getHits();
        Map<String, Object> returnData = null;
        for (SearchHit searchHit : hits) {
            returnData = searchHit.getSourceAsMap();
        }
        return returnData;
    }

    @Override
    public List<Map<String, Object>> stats(Map<String, Object> statsSetting, String index, String type) {
        // 先检查配置是否合规
        Assert.isTrue(checkStatsSetting(statsSetting), "Stats params.params is require.");
        SearchRequestBuilder requestBuilder = transportClient.prepareSearch(index).setTypes(type).setExplain(true);
        this.setQuery(requestBuilder, statsSetting);
        requestBuilder.setTimeout(new TimeValue(5, TimeUnit.MINUTES)); //设置5分钟超时
        requestBuilder.setSize(0); // 不显示详细列表内容
        // 设置分组信息
        Map<String, Object> aggsObject = (Map<String, Object>) statsSetting.get("aggs");
        List<String> fields = (List<String>) aggsObject.get("field");
        StringBuilder scriptBuilder = new StringBuilder();
        for (String field : fields) {
            scriptBuilder.append("doc['").append(field + ".keyword").append("'].values").append("+'####'+");
        }
        String scriptString = scriptBuilder.toString();
        scriptString = scriptBuilder.substring(0, scriptString.length() - 8);
        Script script = new Script(scriptString);

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("custom_group_stats")
                .script(script);
//        if (aggsObject.containsKey("order")) {
//            boolean isAsc = "asc".equalsIgnoreCase(aggsObject.getString("order"));
//            termsAggregationBuilder.order(BucketOrder.aggregation(null, isAsc));
//        } else {
//            termsAggregationBuilder.order(BucketOrder.key(true));
//        }
        requestBuilder.addAggregation(termsAggregationBuilder);
        log.info("Stats es params -> {}", JSONObject.fromObject(requestBuilder));
        log.info("Stats es aggregationBuilder -> {}", JSONObject.fromObject(termsAggregationBuilder));
        long startTime = new Date().getTime();
        SearchResponse response = requestBuilder.get();
        List<Map<String, Object>> list = Lists.newArrayList();
        if (response.getHits().getTotalHits() == 0L) {
            return list;
        }
        log.info("###1. 获取ES统计数据, 耗时{} ms.", (new Date().getTime()) - startTime);
        startTime = new Date().getTime();
        Terms terms = response.getAggregations().get("custom_group_stats");
        List<Map<String, Object>> returnList = new LinkedList<>();
        for (Terms.Bucket bucket : terms.getBuckets()) {
            String key = bucket.getKey().toString();
            long count = bucket.getDocCount();
            String[] keys = key.split("####");
            if (keys.length != fields.size()) {
                throw new RuntimeException("Stats result keys length must equals params.aggs.field length. Except "
                        + fields.size() + " But " + keys.length);
            }
            Map<String, Object> dataMap = new LinkedHashMap<>();
            for (int i = 0; i < keys.length; i++) {
                String v = keys[i].equals("[]") ? "" : keys[i].substring(1, keys[i].length() - 1);
                dataMap.put(fields.get(i), v);
            }
            dataMap.put("count", count);
            returnList.add(dataMap);
        }
        log.info(JSONArray.fromObject(returnList).toString());
        log.info("###2. 解析ES统计结果, 转化为List<Map>, 耗时{} ms.", (new Date().getTime()) - startTime);
        startTime = new Date().getTime();
        // 对结果集进行排序. 按照为 空的放到最后
        for (Object key : fields) {
            Collections.sort(returnList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                    Object value = o2.get(key.toString());
                    if (value == null || ("").equals(value.toString())) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
        log.info("###3. 对转化的List<Map>, 按照空字段值排序, 耗时{} ms.", (new Date().getTime()) - startTime);
        return returnList;
    }

    @Override
    public List<Map<String, Object>> queryDeviceByIpAndIdcType(Map<String, Object> querySetting) {
        List<String> instanceIdList = new ArrayList<>();
        // 设置查询条件
        List<Map<String, Object>> queryParams = null;
        if (querySetting.containsKey("params")) {
            try {
                queryParams = (List<Map<String, Object>>) querySetting.get("params");
            } catch (Exception e) {
                throw new RuntimeException("Format params error. querySetting.params is must List<Map<String,Object>>.");
            }
        }
        // 组装查询条件
        String ip = "";
        String idcType = "";
        if (queryParams != null) {
            for (Map<String, Object> param : queryParams) {
                if (!param.containsKey("filed")) {
                    throw new RuntimeException("Format params error. querySetting.params every member must has filed、operator、value properties.");
                }
                if (!param.containsKey("operator")) {
                    throw new RuntimeException("Format params error. querySetting.params every member must has filed、operator、value properties.");
                }
                if (!param.containsKey("value")) {
                    throw new RuntimeException("Format params error. querySetting.params every member must has filed、operator、value properties.");
                }
                String filed = param.get("filed").toString().trim();
                Object value = param.get("value");
                if (value != null) {
                    if (filed.equals("ip")) {
                        ip = value.toString();
                    }
                    if (filed.equals("idcType")) {
                        idcType = value.toString();
                    }
                }
            }
        }
        if (StringUtils.isEmpty(ip)) {
            throw new RuntimeException("Query parameter[ip] is require.");
        }
        // 创建查询链接
        SearchResponse response = transportClient.prepareSearch("cmdb_instance_ip_manager")
                .setTypes("ip_manager")
                .setQuery(QueryBuilders.matchPhraseQuery("ip.keyword", ip))
                .get();
        SearchHits searchHits = response.getHits();
        log.info("Total find {} records.", searchHits.getTotalHits());
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> data = searchHit.getSourceAsMap();
            if (!instanceIdList.contains(data.get("instance_id").toString())) {
                instanceIdList.add(data.get("instance_id").toString());
            }
        }
        String index = "cmdb_instance", type = "instance";
        if (querySetting.containsKey("index")) {
            index = querySetting.get("index").toString();
        }
        if (querySetting.containsKey("type")) {
            type = querySetting.get("type").toString();
        }
        List<Map<String, Object>> returnData = new LinkedList<>();
        SearchRequestBuilder requestBuilder = transportClient.prepareSearch(index).setTypes(type).setExplain(true);
        this.setResultFiled(requestBuilder, querySetting);
        if (instanceIdList.size() > 0) {
            String[] ids = new String[instanceIdList.size()];
            instanceIdList.toArray(ids);
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (StringUtils.isNotEmpty(idcType)) {
                boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("idcType.keyword", idcType));
            }
            boolQueryBuilder.must(QueryBuilders.idsQuery().addIds(ids));
            requestBuilder.setQuery(boolQueryBuilder);
            SearchResponse listResponse = requestBuilder.get();
            SearchHits instanceHits = listResponse.getHits();
            log.info("Total find {} records.", instanceHits.getTotalHits());
            Map<String, String> filedListMap = null;
            if (querySetting.containsKey("filed_list")) {
                try {
                    filedListMap = (Map<String, String>) querySetting.get("filed_list");
                } catch (Exception e) {
                    throw new RuntimeException("Format params error. querySetting.result is must List<String>.");
                }
            }
            SearchHit[] dataHits = instanceHits.getHits();
            for (SearchHit hit : dataHits) {
                Map<String, Object> dataMap = hit.getSourceAsMap();
                returnData.add(format(dataMap, filedListMap));
            }
        }
        return returnData;
    }

    @Override
    public Map<String, Object> queryDetail(Map<String, Object> querySetting) {
        this.valid(querySetting);
        SearchRequestBuilder request = createRequestBuilder(querySetting);
        this.setResultFiled(request, querySetting);
        this.setQuery(request, querySetting);
        this.setSort(request, querySetting);
        log.info("ES request params -> {}", request);
        SearchResponse response = request.execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("Total find {} records.", searchHits.getTotalHits());
        if (searchHits.getTotalHits() > 1) {
            throw new RuntimeException("Find too many record. Except one, But find " + searchHits.getTotalHits());
        }
        Map<String, String> filedListMap = null;
        if (querySetting.containsKey("filed_list")) {
            try {
                filedListMap = (Map<String, String>) querySetting.get("filed_list");
            } catch (Exception e) {
                throw new RuntimeException("Format params error. querySetting.result is must List<String>.");
            }
        }
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> dataMap = searchHit.getSourceAsMap();
            return format(dataMap, filedListMap);
        }
        return new HashMap<>();
    }

    private Map<String, Object> format(Map<String, Object> dataMap, Map<String, String> resultMap) {
        for (String key : dataMap.keySet()) {
            if (resultMap != null && resultMap.containsKey(key) && StringUtils.isNotEmpty(resultMap.get(key))
                    && resultMap.get(key).equalsIgnoreCase("datetime")) {
                Object value = dataMap.get(key);
                if (value != null && !"".equals(String.valueOf(value))) {
                    dataMap.put(key, formatDate(value));
                }
            }
            if (key.equalsIgnoreCase("insert_time") || key.equalsIgnoreCase("update_time")) {
                Object value = dataMap.get(key);
                if (value != null && !"".equals(String.valueOf(value))) {
                    dataMap.put(key, formatDate(value));
                }
            }
        }
        return dataMap;
    }

    private Object formatDate(Object time) {
        if (time instanceof Long) {
            return DateFormatUtils.format((Long) time, "yyyy-MM-dd HH:mm:ss");
        }
        return time;
    }

    private boolean checkStatsSetting(Map<String, Object> statsSetting) {
        if (!statsSetting.containsKey("aggs")) {
            return false;
        }
        return true;
    }
}
