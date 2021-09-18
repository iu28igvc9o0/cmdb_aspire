package com.aspire.mirror.elasticsearch.server.controller.network;

import com.aspire.mirror.elasticsearch.api.dto.LldpData;
import com.aspire.mirror.elasticsearch.api.dto.LldpInfo;
import com.aspire.mirror.elasticsearch.api.dto.PeerDeviceInfo;
import com.aspire.mirror.elasticsearch.api.service.zabbix.ILLdpInfoService;
import com.aspire.mirror.elasticsearch.server.controller.CommonController;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.elasticsearch.server.controller.lldp
 * 类名称:    LldpInfoController.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/9/20 14:59
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class LldpInfoController extends CommonController implements ILLdpInfoService {
    @Value(value = "${lldp_index_name:lldp-network*}")
    private String indexName;

    @Autowired
    private TransportClient transportClient;

    @Override
    public List<LldpInfo> querylldpInfoByidcAndIp(@RequestParam(value = "idcType", required = false) String idcType,
                                                  @RequestParam(value = "ips", required = false) String ips) {
        List<LldpInfo> lldpInfoList = Lists.newArrayList();
        SearchRequestBuilder request = transportClient.prepareSearch(getClusterIndex(null,indexName)).setExplain(true);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("status", "true");
        queryBuilder.must(queryTermId);
        if (!StringUtils.isEmpty(idcType)) {
            queryBuilder.must(QueryBuilders.matchPhraseQuery("poolname", idcType));
        }
        if (!StringUtils.isEmpty(ips)) {
            queryBuilder.must(QueryBuilders.termsQuery("host", ips.trim().split(",")));
        }
        request.setQuery(queryBuilder).setSize(0);
        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms("host").field("host" +
                ".keyword").size(10000);
        termsBuilder.subAggregation(AggregationBuilders.topHits("top_sales_hits").sort("start_time", SortOrder.DESC).size(1).fetchSource(true));
        request.addAggregation(termsBuilder);
        log.info("LldpInfoController-querylldpInfoByidcAndIp es paramObject is：{}", request);
        SearchResponse resp = request.get();
        if(null == resp.getAggregations()) {
        	return lldpInfoList;
        }
        Terms terms = resp.getAggregations().get("host");
        if (terms != null) {
            for (final Terms.Bucket a : terms.getBuckets()) {
                final TopHits topHits = a.getAggregations().get("top_sales_hits");
                for (SearchHit searchHit : topHits.getHits().getHits()) {
                    searchHit.getSourceAsMap().put("id", searchHit.getId());
                    Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                    LldpInfo lldpInfo = new LldpInfo();
                    try {
                        BeanUtils.populate(lldpInfo, sourceAsMap);
                        lldpInfo.setDeviceBrand((String) sourceAsMap.get("device_brand"));
                        lldpInfo.setDeviceModel((String) sourceAsMap.get("device_model"));
                        lldpInfo.setDeviceType((String) sourceAsMap.get("device_type"));
                        List<Map<String, Object>> lldpDataMap = (List<Map<String, Object>>) sourceAsMap.get("lldp_data");
                        List<LldpData> lldpDataList = Lists.newArrayList();
                        for (Map<String, Object> data : lldpDataMap) {
                            LldpData lldpData = new LldpData();
                            lldpData.setPortIndex((String) data.get("port_index"));
                            lldpData.setPortDesc((String) data.get("port_desc"));

                            Map<String, Object> peerDeviceInfoMap = (Map<String, Object>) data.get("peer_devive_info");
                            PeerDeviceInfo peerDeviceInfo = new PeerDeviceInfo();
                            peerDeviceInfo.setPeerDeviceBrand((String) peerDeviceInfoMap.get("peer_device_brand"));
                            peerDeviceInfo.setPeerPortIndex((String) peerDeviceInfoMap.get("peer_port_index"));
                            peerDeviceInfo.setPeerDeviceModel((String) peerDeviceInfoMap.get("peer_device_model"));
                            peerDeviceInfo.setPeerDeviceType((String) peerDeviceInfoMap.get("peer_device_type"));
                            peerDeviceInfo.setPeerHost((String) peerDeviceInfoMap.get("peer_host"));
                            peerDeviceInfo.setPeerHostname((String) peerDeviceInfoMap.get("peer_hostname"));
                            peerDeviceInfo.setPeerPortDesc((String) peerDeviceInfoMap.get("peer_port_desc"));
                            lldpData.setPeerDeviveInfo(peerDeviceInfo);
                            lldpDataList.add(lldpData);
                        }
                        lldpInfo.setLldpData(lldpDataList);
                        lldpInfoList.add(lldpInfo);
                    } catch (Exception e) {
                        log.error("解析es返回错误", e);
                    }
                }
            }
        }
        return lldpInfoList;
    }
}
