package com.aspire.mirror.elasticsearch.server.controller.network;

import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.elasticsearch.api.dto.ItemIndexDto;
import com.aspire.mirror.elasticsearch.api.dto.NetworkFillWallDto;
import com.aspire.mirror.elasticsearch.api.dto.NetworkLoadBalancerDto;
import com.aspire.mirror.elasticsearch.api.dto.NetworkPublicNetDto;
import com.aspire.mirror.elasticsearch.api.service.zabbix.INetworkStrategyService;
import com.aspire.mirror.elasticsearch.server.controller.CommonController;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: NetworkStrategyController
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2015:53
 */
@RestController
@Slf4j
public class NetworkStrategyController extends CommonController implements INetworkStrategyService {

    @Value("${index.network.fillwall.rule:fw-rule-policy*}")
    private String indexFillwallRule;
    private static final String INDEX_LOADBALANCER = "network_loadbalancer";
    private static final String INDEX_PUBLICNET = "network_publicnet";
    private static final String TYPE = "network";
    @Autowired
    private TransportClient transportClient;

    public void insertFillWall(@RequestBody List<NetworkFillWallDto> networkStrategyDtoList) {
        if (CollectionUtils.isEmpty(networkStrategyDtoList)) {
            return;
        }
        log.info("新增network_strategy数据，数量：{}", networkStrategyDtoList.size());
        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        for (int i = 0; i < networkStrategyDtoList.size(); i++) {
            NetworkFillWallDto networkStrategyDto = networkStrategyDtoList.get(i);
            bulkRequest.add(transportClient.prepareIndex(indexFillwallRule, TYPE).setSource(JSONObject.fromObject(networkStrategyDto).toString(), XContentType.JSON));
            if ((i + 1) % 50 == 0) {
                bulkRequest.execute().actionGet();
                bulkRequest = transportClient.prepareBulk();
            }
        }
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
        }
    }

    public PageResult<Map<String, Object>> queryFillWall(@RequestParam(value = "keyword", required = false) String keyword,
                                                 @RequestParam(value = "name", required = false) String name,
                                                 @RequestParam(value = "sourceZone", required = false)
                                                         String sourceZone,
                                                 @RequestParam(value = "destinationZone", required = false)
                                                         String destinationZone,
                                                 @RequestParam(value = "action", required = false) String
                                                         action,
                                                 @RequestParam(value = "destinationAddress", required =
                                                         false) String destinationAddress,
                                                 @RequestParam(value = "sourceAddress", required = false)
                                                         String sourceAddress,
                                                 @RequestParam(value = "servicePort", required = false)
                                                         String servicePort,
                                                @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        List<Map<String, Object>> list = new ArrayList<>();
        String[] clusterIndex = getClusterIndex(null,indexFillwallRule);
        SearchRequestBuilder request = transportClient.prepareSearch(clusterIndex).setExplain(true);
        if (pageSize > 0) {
            int from = (pageNum - 1) * pageSize;
            from = from < 0 ? 0 : from;
            request.setFrom(from).setSize(pageSize);
        } else {
            request.setSize(10000);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(keyword)) {
//            QueryBuilder term = QueryBuilders.wildcardQuery("ip", "*"+keyword+"*");
            QueryBuilder term = QueryBuilders.multiMatchQuery(keyword,"ip");
            queryBuilder.must(term);
        }
        if (StringUtils.isNotEmpty(name)) {
            queryBuilder.must(QueryBuilders.wildcardQuery("security_rule.name.keyword", "*" + QueryParser.escape(name.trim()) + "*"));
        }

        if (StringUtils.isNotEmpty(sourceZone)) {
            queryBuilder.must(QueryBuilders.wildcardQuery("security_rule.source_zone.keyword", "*" + QueryParser.escape(sourceZone.trim()) + "*"));
        }
        if (StringUtils.isNotEmpty(destinationZone)) {
            queryBuilder.must(QueryBuilders.wildcardQuery("security_rule.destination_zone.keyword", "*" + QueryParser.escape(destinationZone.trim()) + "*"));
        }
        if (StringUtils.isNotEmpty(action)) {
            queryBuilder.must(QueryBuilders.wildcardQuery("security_rule.action.keyword", "*" + QueryParser.escape(action.trim()) + "*"));
        }
        if (StringUtils.isNotEmpty(sourceAddress)) {
            BoolQueryBuilder mustBoolBuilder = QueryBuilders.boolQuery();
            mustBoolBuilder.should(QueryBuilders.termsQuery("security_rule.source_address.address_sets.list.ip", sourceAddress));
            mustBoolBuilder.should(QueryBuilders.termsQuery("security_rule.source_address.default.ip", sourceAddress));
            BoolQueryBuilder subBoolBuilder = QueryBuilders.boolQuery();
            String newSourceAddress = sourceAddress.replace(".", "00");
            subBoolBuilder.must(QueryBuilders.rangeQuery("security_rule.source_address.range.from_num").lte(Long.valueOf(newSourceAddress)));
            subBoolBuilder.must(QueryBuilders.rangeQuery("security_rule.source_address.range.to_num").gte(Long.valueOf(newSourceAddress)));
            mustBoolBuilder.should(subBoolBuilder);
            queryBuilder.must(mustBoolBuilder);
        }
        if (StringUtils.isNotEmpty(destinationAddress)) {
            BoolQueryBuilder mustBoolBuilder = QueryBuilders.boolQuery();
            mustBoolBuilder.should(QueryBuilders.termsQuery("security_rule.destination_address.address_sets.list.ip", destinationAddress));
            mustBoolBuilder.should(QueryBuilders.termsQuery("security_rule.destination_address.default.ip", destinationAddress));
            BoolQueryBuilder subBoolBuilder = QueryBuilders.boolQuery();
            String newDestinationAddress = destinationAddress.replace(".", "00");
            subBoolBuilder.must(QueryBuilders.rangeQuery("security_rule.destination_address.range.from_num").lte(Long.valueOf(newDestinationAddress)));
            subBoolBuilder.must(QueryBuilders.rangeQuery("security_rule.destination_address.range.to_num").gte(Long.valueOf(newDestinationAddress)));
            mustBoolBuilder.should(subBoolBuilder);
            queryBuilder.must(mustBoolBuilder);
        }
        if (StringUtils.isNotEmpty(servicePort)) {
            BoolQueryBuilder mustBoolBuilder = QueryBuilders.boolQuery();
            mustBoolBuilder.should(QueryBuilders.termsQuery("security_rule.service.service_set.list.service_ports.ports", servicePort));
            BoolQueryBuilder subBoolBuilder = QueryBuilders.boolQuery();
            subBoolBuilder.must(QueryBuilders.rangeQuery("security_rule.service.service_set.list.service_ports.range_ports.from_num").lte(servicePort));
            subBoolBuilder.must(QueryBuilders.rangeQuery("security_rule.service.service_set.list.service_ports.range_ports.to_num").gte(servicePort));
            mustBoolBuilder.should(subBoolBuilder);
            queryBuilder.must(mustBoolBuilder);
        }
        QueryBuilder queryType = QueryBuilders.constantScoreQuery(queryBuilder);
        log.info("index is {}, query dsl is {}", clusterIndex, queryType);
        SearchResponse response = request.setQuery(queryType).execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            list.add(sourceAsMap);
        }
        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setCount((int) searchHits.getTotalHits());
        result.setCurPage(pageNum);
        result.setPageSize(pageSize);
        result.setResult(list);
        return result;
    }

    public List<Map<String, Object>> exportFillWall(@RequestParam(value = "keyword", required = false) String keyword) {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,indexFillwallRule)).setExplain(true);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(keyword)) {
//            QueryBuilder term = QueryBuilders.wildcardQuery("ip", "*"+keyword+"*");
            QueryBuilder term = QueryBuilders.multiMatchQuery(keyword,"ip");
            queryBuilder.must(term);
        }
        request.setSize(1000).setQuery(queryBuilder).setScroll(TimeValue.timeValueMinutes(1));
        SearchResponse scrollResp=request.get();
        log.info("\n{}", request);
        log.info("共匹配到:" + scrollResp.getHits().getTotalHits() + "条记录!");
        //当searchHits的数组为空的时候结束循环，至此数据全部读取完毕
        while (scrollResp.getHits().getHits().length != 0){
            log.info("查询到{}条数据", scrollResp.getHits().getHits().length);
            for (SearchHit searchHit : scrollResp.getHits().getHits()) {
                searchHit.getSourceAsMap().put("id", searchHit.getId());
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
//                ItemIndexDto itemIndexDto = new ItemIndexDto();
//                NetworkFillWallDto networkStrategyDto = new NetworkFillWallDto();
//                try {
//                    BeanUtils.populate(networkStrategyDto, sourceAsMap);
//                    list.add(networkStrategyDto);
//                } catch (IllegalAccessException e) {
//                } catch (InvocationTargetException e) {
//                }
                list.add(sourceAsMap);
            }
            //将scorllId循环传递
            scrollResp = transportClient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(TimeValue.timeValueMinutes(1)).execute().actionGet();
        }
        return list;
    }

    public void insertLoadBalancer(@RequestBody List<NetworkLoadBalancerDto> networkStrategyDtoList) {
        if (CollectionUtils.isEmpty(networkStrategyDtoList)) {
            return;
        }
        log.info("新增network_strategy数据，数量：{}", networkStrategyDtoList.size());
        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        for (int i = 0; i < networkStrategyDtoList.size(); i++) {
            NetworkLoadBalancerDto networkStrategyDto = networkStrategyDtoList.get(i);
            bulkRequest.add(transportClient.prepareIndex(INDEX_LOADBALANCER, TYPE).setSource(JSONObject.fromObject(networkStrategyDto).toString(), XContentType.JSON));
            if ((i + 1) % 50 == 0) {
                bulkRequest.execute().actionGet();
                bulkRequest = transportClient.prepareBulk();
            }
        }
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
        }
    }

    public PageResult<NetworkLoadBalancerDto> queryLoadBalancer(@RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        List<NetworkLoadBalancerDto> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,INDEX_LOADBALANCER)).setTypes(TYPE).setExplain(true);
        if (pageSize > 0) {
            int from = (pageNum - 1) * pageSize;
            from = from < 0 ? 0 : from;
            request.setFrom(from).setSize(pageSize);
        } else {
            request.setSize(10000);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(keyword)) {
//            QueryBuilder term = QueryBuilders.wildcardQuery("ip", "*"+keyword+"*");
            QueryBuilder term = QueryBuilders.multiMatchQuery(keyword,"loadIp","deviceIp");
            queryBuilder.must(term);
        }
        SearchResponse response = request.setQuery(queryBuilder).execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            NetworkLoadBalancerDto networkStrategyDto = new NetworkLoadBalancerDto();
            try {
                BeanUtils.populate(networkStrategyDto, sourceAsMap);
                list.add(networkStrategyDto);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        PageResult<NetworkLoadBalancerDto> result = new PageResult<>();
        result.setCount((int) searchHits.getTotalHits());
        result.setCurPage(pageNum);
        result.setPageSize(pageSize);
        result.setResult(list);
        return result;
    }

    public List<NetworkLoadBalancerDto> exportLoadBalancer(@RequestParam(value = "keyword", required = false) String keyword) {
        List<NetworkLoadBalancerDto> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,INDEX_LOADBALANCER)).setTypes(TYPE).setExplain(true);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(keyword)) {
//            QueryBuilder term = QueryBuilders.wildcardQuery("ip", "*"+keyword+"*");
            QueryBuilder term = QueryBuilders.multiMatchQuery(keyword,"loadIp","deviceIp");
            queryBuilder.must(term);
        }
        request.setSize(1000).setQuery(queryBuilder).setScroll(TimeValue.timeValueMinutes(1));
        SearchResponse scrollResp=request.get();
        log.info("\n{}", request);
        log.info("共匹配到:" + scrollResp.getHits().getTotalHits() + "条记录!");
        //当searchHits的数组为空的时候结束循环，至此数据全部读取完毕
        while (scrollResp.getHits().getHits().length != 0){
            log.info("查询到{}条数据", scrollResp.getHits().getHits().length);
            for (SearchHit searchHit : scrollResp.getHits().getHits()) {
                searchHit.getSourceAsMap().put("id", searchHit.getId());
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                ItemIndexDto itemIndexDto = new ItemIndexDto();
                NetworkLoadBalancerDto networkStrategyDto = new NetworkLoadBalancerDto();
                try {
                    BeanUtils.populate(networkStrategyDto, sourceAsMap);
                    list.add(networkStrategyDto);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
            //将scorllId循环传递
            scrollResp = transportClient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(TimeValue.timeValueMinutes(1)).execute().actionGet();
        }
        return list;
    }

    public void insertPublicNet(@RequestBody List<NetworkPublicNetDto> networkStrategyDtoList) {
        if (CollectionUtils.isEmpty(networkStrategyDtoList)) {
            return;
        }
        log.info("新增network_strategy数据，数量：{}", networkStrategyDtoList.size());
        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        for (int i = 0; i < networkStrategyDtoList.size(); i++) {
            NetworkPublicNetDto networkStrategyDto = networkStrategyDtoList.get(i);
            bulkRequest.add(transportClient.prepareIndex(INDEX_PUBLICNET, TYPE).setSource(JSONObject.fromObject(networkStrategyDto).toString(), XContentType.JSON));
            if ((i + 1) % 50 == 0) {
                bulkRequest.execute().actionGet();
                bulkRequest = transportClient.prepareBulk();
            }
        }
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
        }
    }

    public PageResult<NetworkPublicNetDto> queryPublicNet(@RequestParam(value = "keyword", required = false) String keyword,
                                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                        @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        List<NetworkPublicNetDto> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,INDEX_PUBLICNET)).setTypes(TYPE).setExplain(true);
        if (pageSize > 0) {
            int from = (pageNum - 1) * pageSize;
            from = from < 0 ? 0 : from;
            request.setFrom(from).setSize(pageSize);
        } else {
            request.setSize(10000);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(keyword)) {
//            QueryBuilder term = QueryBuilders.wildcardQuery("ip", "*"+keyword+"*");
            QueryBuilder term = QueryBuilders.multiMatchQuery(keyword,"fillWallIp","loadIp","targetIntranetIp");
            queryBuilder.must(term);
        }
        SearchResponse response = request.setQuery(queryBuilder).execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            NetworkPublicNetDto networkStrategyDto = new NetworkPublicNetDto();
            try {
                BeanUtils.populate(networkStrategyDto, sourceAsMap);
                list.add(networkStrategyDto);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        PageResult<NetworkPublicNetDto> result = new PageResult<>();
        result.setCount((int) searchHits.getTotalHits());
        result.setCurPage(pageNum);
        result.setPageSize(pageSize);
        result.setResult(list);
        return result;
    }

    public List<NetworkPublicNetDto> exportPublicNet(@RequestParam(value = "keyword", required = false) String keyword) {
        List<NetworkPublicNetDto> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,INDEX_PUBLICNET)).setTypes(TYPE).setExplain(true);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(keyword)) {
//            QueryBuilder term = QueryBuilders.wildcardQuery("ip", "*"+keyword+"*");
            QueryBuilder term = QueryBuilders.multiMatchQuery(keyword,"fillWallIp","loadIp","targetIntranetIp");
            queryBuilder.must(term);
        }
        request.setSize(1000).setQuery(queryBuilder).setScroll(TimeValue.timeValueMinutes(1));
        SearchResponse scrollResp=request.get();
        log.info("\n{}", request);
        log.info("共匹配到:" + scrollResp.getHits().getTotalHits() + "条记录!");
        //当searchHits的数组为空的时候结束循环，至此数据全部读取完毕
        while (scrollResp.getHits().getHits().length != 0){
            log.info("查询到{}条数据", scrollResp.getHits().getHits().length);
            for (SearchHit searchHit : scrollResp.getHits().getHits()) {
                searchHit.getSourceAsMap().put("id", searchHit.getId());
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                ItemIndexDto itemIndexDto = new ItemIndexDto();
                NetworkPublicNetDto networkStrategyDto = new NetworkPublicNetDto();
                try {
                    BeanUtils.populate(networkStrategyDto, sourceAsMap);
                    list.add(networkStrategyDto);
                } catch (IllegalAccessException e) {
                } catch (InvocationTargetException e) {
                }
            }
            //将scorllId循环传递
            scrollResp = transportClient.prepareSearchScroll(scrollResp.getScrollId()).setScroll(TimeValue.timeValueMinutes(1)).execute().actionGet();
        }
        return list;
    }
}
