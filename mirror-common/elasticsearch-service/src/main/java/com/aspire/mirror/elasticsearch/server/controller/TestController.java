package com.aspire.mirror.elasticsearch.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author baiwp
 * @title: TestController
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2014:59
 */
@RestController
@Slf4j
@RequestMapping("/v1/test")
public class TestController {

    private static final String INDEX = "xxg";
    private static final String TYPE = "test";

    @Autowired
    private TransportClient transportClient;

    @GetMapping("/query")
    public List<Map<String, Object>> query(@RequestParam(value="ip", required = false) String ip) {
        long l = System.currentTimeMillis();
        List<Map<String, Object>> list = new ArrayList<>();

        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setTypes(TYPE);
        request.setFrom(0).setSize(20).setExplain(true);
        if (StringUtils.isNotEmpty(ip)) {
            QueryBuilder term = QueryBuilders.termQuery("ipaddr", ip);
            request.setQuery(term);
        }
        SearchResponse response = request.execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            list.add(sourceAsMap);
            Set<String> keySet = sourceAsMap.keySet();
            for (String string : keySet) {
                //key value 值对应关系
                log.info(string + ":" + sourceAsMap.get(string));
            }
        }
        return list;
    }
}