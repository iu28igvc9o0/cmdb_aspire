package com.aspire.mirror.elasticsearch.server.controller.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.elasticsearch.api.dto.monitor.MonitorCommonRequest;
import com.aspire.mirror.elasticsearch.api.service.monitor.IMonitorKpiService;
import com.aspire.mirror.elasticsearch.server.exception.BaseException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.server.controller.zabbix
 * @Author: baiwenping
 * @CreateTime: 2020-04-22 16:39
 * @Description: ${Description}
 */
@Slf4j
@RestController
public class MonitorKpiController extends MonitorCommonController implements IMonitorKpiService{
    @Autowired
    private TransportClient transportClient;

    /**
     * 插入数据
     *
     * @param index
     * @param type
     * @param map
     */
    @Override
    public void insert(@PathVariable("index") String index, @PathVariable("type") String type,
                       @RequestBody Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return;
        }
        log.info("索引：{}; 类型：{};新增ES数据，参数：{}", index, type, map);
        transportClient.prepareIndex(index, type).setSource(JSON.toJSONString(map), XContentType.JSON)
                .execute().actionGet();
        log.info("新增监控数据完成!");
    }

    /**
     * 插入数据
     *
     * @param index
     * @param type
     * @param list
     */
    @Override
    public void insertBatch(@PathVariable("index") String index, @PathVariable("type") String type,
                       @RequestBody List<Map<String, Object>> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        log.info("索引：{}; 类型：{};新增监控数据，数量：{}", index, type, list.size());
        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            bulkRequest.add(transportClient.prepareIndex(index, type).setSource(JSONObject.toJSONString(map), XContentType.JSON));
            if ((i + 1) % 500 == 0) {
                bulkRequest.execute().actionGet();
                bulkRequest = transportClient.prepareBulk();
            }
        }
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
        }
        log.info("新增监控数据完成!");
    }

    /**
     * 查询
     *
     * @param monitorCommonRequest
     * @return
     */
    @Override
    public List<Map<String, Object>> query(@RequestBody MonitorCommonRequest monitorCommonRequest) {
        String index = monitorCommonRequest.getIndex();
        JSONObject dsl = monitorCommonRequest.getDsl();
        if (StringUtils.isEmpty(index) || dsl == null) {
            log.error("index or dsl is null!");
            throw new BaseException("index or dsl is null!");
        }
        log.info("index is : {}; dsl is {}", index, dsl);

        if (checkAggs(dsl)) {
            return queryAggs(monitorCommonRequest);
        } else {
            return queryDate(monitorCommonRequest);
        }
    }

    /**
     * 校验是聚合还是查询
     * @param dsl
     * @return
     */
    private boolean checkAggs (JSONObject dsl) {
        Object aggs = dsl.get("aggs");
        Object aggregations = dsl.get("aggregations");
        if (aggs == null && aggregations == null ) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * 查询聚合
     *
     * @param monitorCommonRequest
     * @return
     */
    private List<Map<String, Object>> queryAggs(MonitorCommonRequest monitorCommonRequest) {
        String index = monitorCommonRequest.getIndex();
        JSONObject dsl = monitorCommonRequest.getDsl();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchModule searchModule = new SearchModule(Settings.EMPTY, false, Collections.emptyList());
        List<Map<String, Object>> list = Lists.newArrayList();
        try {
            XContentParser parser = XContentFactory.xContent(XContentType.JSON).
                    createParser(new NamedXContentRegistry(searchModule.getNamedXContents()), DeprecationHandler.THROW_UNSUPPORTED_OPERATION, dsl.toJSONString());
            searchSourceBuilder.parseXContent(parser);
            SearchResponse searchResponse = transportClient.prepareSearch(getClusterIndex(monitorCommonRequest,index)).setExplain(true)
                    .setSource(searchSourceBuilder).setSize(0).get();
            Aggregations aggregations = searchResponse.getAggregations();
            if (aggregations == null) {
                log.error("aggregations are null!");
                return list;
            }
            List<Aggregation> aggregationList = aggregations.asList();
            for (Aggregation aggregation : aggregationList) {
                String s = aggregation.toString();
                JSONObject jsonObject = JSON.parseObject(s);
                generateAggs(jsonObject, list, null);
            }
        } catch (Exception e) {
            log.error("", e);
            throw new BaseException(e);
        }
        return list;
    }
    /**
     * 查询数据
     *
     * @param monitorCommonRequest
     * @return
     */
    private List<Map<String, Object>> queryDate(MonitorCommonRequest monitorCommonRequest) {
        String index = monitorCommonRequest.getIndex();
        JSONObject dsl = monitorCommonRequest.getDsl();

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchModule searchModule = new SearchModule(Settings.EMPTY, false, Collections.emptyList());
        List<Map<String, Object>> list = Lists.newArrayList();
        try {
            XContentParser parser = XContentFactory.xContent(XContentType.JSON).
                    createParser(new NamedXContentRegistry(searchModule.getNamedXContents()), DeprecationHandler.THROW_UNSUPPORTED_OPERATION, dsl.toJSONString());
            searchSourceBuilder.parseXContent(parser);
            SearchResponse scrollResp = transportClient.prepareSearch(getClusterIndex(monitorCommonRequest,index)).setScroll(TimeValue.timeValueMinutes(1))
                    .setExplain(true).setSource(searchSourceBuilder).setSize(10000).get();
            log.info("共匹配到:" + scrollResp.getHits().getTotalHits() + "条记录!");
            //当searchHits的数组为空的时候结束循环，至此数据全部读取完毕
            while (scrollResp.getHits().getHits().length != 0) {
                for (SearchHit searchHit : scrollResp.getHits().getHits()) {
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                    list.add(sourceAsMap);
                }
                // 将scorllId循环传递
                scrollResp = transportClient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(TimeValue
                        .timeValueMinutes(1)).execute().actionGet();
            }

        } catch (Exception e) {
            log.error("", e);
            throw new BaseException(e);
        }
        log.info("查询到{}条数据", list.size());
        return list;
    }

    /**
     * 判断是否json格式
     *
     * @param str
     * @return
     */
    private boolean isJSON(String str) {
        boolean result = false;
        try {
            JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

	@Override
	public String[] getClusterIndexOut(@RequestBody net.sf.json.JSONObject jsonObject, String... indices) {
		return this.getClusterIndex(jsonObject, indices);
	}
}
