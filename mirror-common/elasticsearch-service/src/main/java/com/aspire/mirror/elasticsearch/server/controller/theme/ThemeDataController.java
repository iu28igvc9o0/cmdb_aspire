package com.aspire.mirror.elasticsearch.server.controller.theme;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.elasticsearch.api.dto.theme.BizTheme;
import com.aspire.mirror.elasticsearch.api.dto.theme.BizThemeDimData;
import com.aspire.mirror.elasticsearch.api.service.theme.IThemeDataService;
import com.aspire.mirror.elasticsearch.server.controller.theme.handler.ThemeDataSearchFacade;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.admin.cluster.state.ClusterStateRequestBuilder;
import org.elasticsearch.action.admin.cluster.state.ClusterStateResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 主题数据控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.elasticsearch.server.controller.theme
 * 类名称:    ThemeDataController.java
 * 类描述:    主题数据控制层
 * 创建人:    JinSu
 * 创建时间:  2019/12/11 11:14
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class ThemeDataController implements IThemeDataService {
    @Autowired
    private TransportClient transportClient;

    @Autowired
    private ThemeDataSearchFacade themeDataSearchFacade;


    @Value("${elasticsearch.index.fields.limit:10000}")
    private Integer field_limit;

    @Value("${elasticsearch.logMonitorIndexName:log-bizmoniter*}")
    private String logMonitorIndex;

    @Value("${elasticsearch.itemIndexName:mirror-theme}")
    private String indexName;

    public static final String SUB_INDEX_ITEM = "-item";

    @Override
    public void updateMappingByIndexName(@RequestBody Map<String, Object> param) {
        String indexName = (String) param.get("indexName");
        List<Map<String, Object>> bizThemeDimDataList = (List<Map<String, Object>>) param.get("dimList");
        ClusterStateRequestBuilder request = transportClient.admin().cluster().prepareState().setIndices(indexName);
        ClusterStateResponse indexResponse = request.execute().actionGet();
        MetaData metaData = indexResponse.getState().metaData();
        // 不存在则创建索引
        if (metaData.getIndices().isEmpty()) {
            Settings settings = Settings.builder().put("index.mapping.total_fields.limit", field_limit).build();
            CreateIndexRequestBuilder createIndexRequest = transportClient.admin().indices().prepareCreate(indexName)
                    .setSettings(settings);
            CreateIndexResponse createIndexResponse = createIndexRequest.execute().actionGet();
            if (!createIndexResponse.isAcknowledged()) {
                log.error("索引[" + indexName + "]创建失败!");
            }
        }
        for (Map<String, Object> map : bizThemeDimDataList) {
            BizThemeDimData dimDto = new BizThemeDimData();
            try {
                BeanUtils.populate(dimDto, map);
            } catch (Exception e) {
                log.error("ThemeDataController[updateMappingByIndexName]解析参数出错！");
                return;
            }
            Boolean dimExist = false;
            ImmutableOpenMap<String, MappingMetaData> mappings = transportClient.admin().cluster().prepareState()
                    .execute()
                    .actionGet().getState().getMetaData().getIndices().get(indexName)
                    .getMappings();
            if (mappings.containsKey("log")) {
                JSONObject log = JSONObject.parseObject(mappings.get("log").source().toString());
                JSONObject properties = log.getJSONObject("log").getJSONObject("properties");
                if (properties.containsKey(dimDto.getDimCode())) {
                    dimExist = true;
                }
            }
            if (!dimExist) {
                XContentBuilder builder = null;
                try {
                    builder = XContentFactory.jsonBuilder().startObject()
                            .startObject("log").startObject("properties");
                    if (dimDto.getDimType().equals("1")) {
                        builder.startObject(dimDto.getDimCode())
                                .field("type", "double").endObject();
                    } else if (dimDto.getDimType().equals("3")) {
                        builder.startObject(dimDto.getDimCode())
                                .field("type", "date").endObject();
                    } else {
                        builder.startObject(dimDto.getDimCode())
                                .field("type", "keyword").endObject();
                    }
                    builder.endObject().endObject().endObject();
                    PutMappingRequest mapping = Requests.putMappingRequest(indexName).type("log")
                            .source(builder);
                    transportClient.admin().indices().putMapping(mapping).actionGet();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void createThemeEsData(@RequestBody List<Map<String, Object>> maps) {
        try {
            if (maps.isEmpty()) {
                return;
            }
            long startTime = System.currentTimeMillis();
            BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
            for (Map<String, Object> map : maps) {
                Map<String, Object> json = (Map<String, Object>) map.get("json");
                String idFieldName = null;
                if (map.get("idFieldName") != null) {
                    idFieldName = map.get("idFieldName").toString();
                }
                IndexRequestBuilder lrb = transportClient.prepareIndex(map.get("indexName").toString(), "log",
                        idFieldName).setSource(json);
                bulkRequest.add(lrb);
            }
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                log.error("es批量入库主题数据失败 :" + bulkResponse.getItems().toString());
            }
            long endTime = System.currentTimeMillis();
            float excTime = (float) (endTime - startTime) / 1000;
            log.info("批处理[" + bulkRequest.numberOfActions() + "]条主题数据提交到es成功 ！,耗时：" + excTime + "秒");
        } catch (Exception e) {
            log.error("es批量入库主题数据异常！", e);
        }
    }

    @Override
    public List<String> getNewLogThemeData(@RequestParam(value = "flushTime") String flushTime, @RequestParam(value =
            "lastTime") String lastTime) {
        final BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("@timestamp");
        timeRange.gt(DateUtil.getDate(flushTime, DateUtil.DATE_TIME_MS_FORMAT).getTime());
        timeRange.lte(DateUtil.getDate(lastTime, DateUtil.DATE_TIME_MS_FORMAT).getTime());
        bqb.must(timeRange);

        SearchRequestBuilder searchRequestBuilder = transportClient.prepareSearch(logMonitorIndex)
                .setQuery(bqb).addSort("@timestamp", SortOrder.DESC).setScroll(new TimeValue(20000));
        log.info("es方式查询主题数据解析参数： {}", searchRequestBuilder);
        SearchResponse response = searchRequestBuilder.setSize(1).get();
        List<String> result = Lists.newArrayList();
        if (response.getHits().getTotalHits() > 0) {
            do {
                for (SearchHit searchHit : response.getHits().getHits()) {
                    final Map<String, Object> resMap = searchHit.getSourceAsMap();
                    String themeData = JSON.toJSONString(resMap);
                    result.add(themeData);
                }
                response = transportClient.prepareSearchScroll(response.getScrollId()).setScroll
                        (new TimeValue(60000)).execute().actionGet();
            } while (response.getHits().getHits().length != 0);
        }
        return result;
    }

    @Override
    public Map<String, Object> getThemeDataHis(@RequestBody BizTheme bizTheme) {
        try {
            // 分步查询
            SearchResponse response = null;
            // 0:成功状态
            if (null != bizTheme.getUpStatus() && bizTheme.getUpStatus().equals("0")) {
                if (null == bizTheme.getLastUpTime()) {
                    return null;
                }
                final long startTime = bizTheme.getLastUpTime().getTime() - 100 * 1000;
                final long endTime = bizTheme.getLastUpTime().getTime() + 100 * 1000;
                final RangeQueryBuilder range = QueryBuilders.rangeQuery("@timestamp").gte(startTime).lte(endTime);
                response = transportClient
                        .prepareSearch(bizTheme.getIndexName())
                        .setQuery(
                                QueryBuilders
                                        .boolQuery()
                                        .must(range)
                                        .must(QueryBuilders.queryStringQuery("theme_code:" + bizTheme.getThemeCode()
                                                .toUpperCase().trim()
                                                + " AND success:0"))).setSize(1)
                        .addSort("@timestamp", SortOrder.DESC).get();
            } else {
                if (null == bizTheme.getLastFailTime()) {
                    return null;
                }
                final long startTime = bizTheme.getLastFailTime().getTime() - 1;
                final long endTime = bizTheme.getLastFailTime().getTime() + 1;
                final RangeQueryBuilder range = QueryBuilders.rangeQuery("@timestamp").gte(startTime).lte(endTime);
                // 第一步查（指标编码）,没有数据则进行第二步查询
                response = transportClient
                        .prepareSearch(bizTheme.getIndexName())
                        .setQuery(
                                QueryBuilders
                                        .boolQuery()
                                        .must(range)
                                        .must(QueryBuilders.queryStringQuery("theme_code:" + bizTheme.getThemeCode()
                                                .toUpperCase().trim()
                                                + " AND success:1"))).setSize(1)
                        .addSort("@timestamp", SortOrder.DESC).get();
            }
            if (response.getHits().getTotalHits() > 0) {
                final SearchHit[] hits = response.getHits().getHits();
                final Map<String, Object> resMap = hits[0].getSourceAsMap();
                try {
                    final Date transfTimestamp = DateUtil.getDate(resMap.get("@timestamp").toString(),
                            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    resMap.put("timestamp", DateUtil.format(transfTimestamp, "yyyy.MM.dd HH:mm:ss"));

                    final Date receiveTime = DateUtil.getDate(resMap.get("receive_time").toString(),
                            "yyyyMMddHHmmss");
                    resMap.put("receive_time", DateUtil.format(receiveTime, "yyyy.MM.dd HH:mm:ss"));
                } catch (final Exception e) {
                    log.error("thematicDataHistory type conversion error:", e);
                }
                if (resMap.containsKey("theme_code")) {
                    resMap.put("jkzb_code", bizTheme.getThemeCode().toUpperCase().trim());
                }
                return resMap;
            }
        } catch (final Exception e) {
            log.error("ThemeDataController-->getThemeDataHis:ES themeCode:" + bizTheme.getThemeCode().toUpperCase()
                    + " error.", e);
        }
        return null;
    }

    //暂时没有用到，未调试
    @Override
    public List<Object[]> themeHistoryInfo(@RequestParam(value = "indexName") String indexName, @RequestParam(value =
            "themeCode") String themeCode, @RequestParam(value = "startTime") Long startTime, @RequestParam(value =
            "endTime") Long endTime) {
        final List<Object[]> itemValuesList = new ArrayList<>();
        try {
            final RangeQueryBuilder range = QueryBuilders.rangeQuery("@timestamp").gte(startTime).lte(endTime);
            final SearchResponse response = transportClient
                    .prepareSearch(indexName)
                    .setQuery(
                            QueryBuilders.boolQuery().must(range)
                                    .must(QueryBuilders.queryStringQuery("theme_code:" + themeCode.toUpperCase().trim
                                            ())))
                    .setSize(9999).addSort("@timestamp", SortOrder.ASC).get();
            if (response.getHits().getTotalHits() > 0) {
                final SearchHit[] hits = response.getHits().getHits();
                for (final SearchHit searchHit : hits) {
                    final Map<String, Object> map = searchHit.getSourceAsMap();

                    final Date transfTimestamp = DateUtil.getDate(map.get("@timestamp").toString(),
                            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

                    final Object[] item = new Object[2];
                    item[0] = transfTimestamp.getTime();
                    try {
                        item[1] = Double.valueOf((String) map.get("num_value"));
                    } catch (final Exception e) {
                        item[1] = 0;
                        log.error(
                                "themeHistoryInfo type conversion error:" + map.get("theme_code"),
                                e);
                    }
                    itemValuesList.add(item);
                }
            }

        } catch (final Exception e) {
            log.error("ThemeDataController-->themeHistoryInfo:ES jkzbCode:" + themeCode
                    + " startTime: " + startTime + "endTime: " + endTime + "error.", e);
        }
        return itemValuesList;
    }

    @Override
    public Map<String, Object> getThemeData(@RequestParam(value = "indexName") String indexName, @RequestParam(value =
            "startTime") long startTime, @RequestParam(value = "endTime") long endTime, @RequestParam(value =
            "host", required = false) String host, @RequestParam(value = "bizCode", required = false) String bizCode,
                                            @RequestParam(value = "themeCode") String
                                                    themeCode) {
        try {
            ClusterStateRequestBuilder request = transportClient.admin().cluster().prepareState().setIndices(indexName);
            ClusterStateResponse indexResponse = request.execute().actionGet();
            MetaData metaData = indexResponse.getState().metaData();
            // 不存在则创建索引
            if (metaData.getIndices().isEmpty()) {
                return null;
            }
            // 分步查询
            final RangeQueryBuilder range = QueryBuilders.rangeQuery("@timestamp").gte(startTime).lte(endTime);
            // 查询处理
            SearchResponse response = themeDataSearchFacade.handlThemeDataSearch(bizCode, themeCode, host, indexName,
                    range);
            if (response.getHits().getTotalHits() > 0) {
                final SearchHit[] hits = response.getHits().getHits();
                final Map<String, Object> resMap = hits[0].getSourceAsMap();
                try {
                    final Date transfTimestamp = DateUtil.getDate(resMap.get("@timestamp").toString(),
                            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    resMap.put("timestamp", DateUtil.format(transfTimestamp, "yyyy.MM.dd HH:mm:ss"));

                    final Date receiveTime = DateUtil.getDate(resMap.get("receive_time").toString(),
                            "yyyyMMddHHmmss");
                    resMap.put("receive_time", DateUtil.format(receiveTime, "yyyy.MM.dd HH:mm:ss"));
                } catch (final Exception e) {
                    log.error("thematicDataInfo type conversion error:", e);
                }
                if (resMap.containsKey("theme_code")) {
                    resMap.put("jkzb_code", themeCode.toUpperCase().trim());
                }
                return resMap;
            }
        } catch (final Exception e) {
            log.error("ThemeDataController-->getThemeData:ES jkzbCode:" + themeCode.toUpperCase()
                    + " error.", e);
        }
        return null;
    }

    @Override
    public String getItemValueByItemId(@RequestParam("itemId") String itemId, @RequestParam(value = "sysCode", required = false) String sysCode) {
        try {
            SearchResponse response = null;
            final BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            String query = "id" + ":" + itemId;
            if (!StringUtils.isEmpty(sysCode)) {
                query = query + " AND objectId" + ":" + sysCode;
            }
            bqb.must(QueryBuilders.queryStringQuery(query));
            response = transportClient.prepareSearch(indexName + SUB_INDEX_ITEM + "*")
                    .setQuery(bqb).setSize(1).addSort("@timestamp", SortOrder.DESC).get();
            if (response.getHits().getTotalHits() > 0) {
                final SearchHit[] hits = response.getHits().getHits();
                final Map<String, Object> resMap = hits[0].getSourceAsMap();
                return (String) resMap.get("logValue");
            }
        } catch (Exception e) {
            log.error("ThemeDataController-->getItemValueByItemId:ES itemId:" + itemId
                    + " error.", e);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getThemeCalcResult(@RequestParam("itemId") String itemId, @RequestParam("themeId")
            String themeId, @RequestParam("startTime") long startTime, @RequestParam("endTime") long endTime,
                                                        @RequestParam("sortName") String sortName, @RequestParam("nameMap")
                                                                    String nameMapString) {
        Map<String,String> nameMap = JSON.parseObject(nameMapString, Map.class);
        final RangeQueryBuilder range = QueryBuilders.rangeQuery("@timestamp").gte(startTime).lte(endTime);
        SearchResponse response;
        List<Map<String, Object>> result = Lists.newArrayList();
        SearchRequestBuilder srb = transportClient
                .prepareSearch(indexName + SUB_INDEX_ITEM + "*")
                .setQuery(
                        QueryBuilders
                                .boolQuery()
                                .must(range)
                                .must(QueryBuilders.queryStringQuery("id:" + itemId + " AND " + "themeId:" + themeId)
                                )).setScroll(new TimeValue(20000));
        srb.addSort(sortName, SortOrder.DESC);
        response = srb.setSize(1).get();
        if (response.getHits().getTotalHits() > 0) {
            do {
                for (SearchHit searchHit : response.getHits().getHits()) {
                    Map<String, Object> itemMap = Maps.newLinkedHashMap();
                    final Map<String, Object> resMap = searchHit.getSourceAsMap();
                    for (String key : nameMap.keySet()) {
                        if (resMap.containsKey(key)) {
                            itemMap.put(nameMap.get(key), resMap.get(key));
                        }
                    }
                    result.add(itemMap);
                }
                response = transportClient.prepareSearchScroll(response.getScrollId()).setScroll
                        (new TimeValue(60000)).execute().actionGet();
            } while (response.getHits().getHits().length != 0);
        }
        return result;
    }

    @Override
    public String getLastNewLogTime() {
        SearchResponse resp = transportClient.prepareSearch(logMonitorIndex).addSort("@timestamp", SortOrder.DESC)
                .setSize(1).get();
        if (resp.getHits().getTotalHits() > 0) {
            Map<String, Object> result = resp.getHits().getHits()[0].getSourceAsMap();
            return (String) result.get("@timestamp");
        }
        return null;
    }
}
