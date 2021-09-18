package com.aspire.mirror.elasticsearch.server.controller.cmdb;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.elasticsearch.api.service.cmdb.ICmdbApprovalService;
import com.aspire.mirror.elasticsearch.server.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbApprovalController
 * Author:   hangfang
 * Date:     2019/9/17
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbApprovalController implements ICmdbApprovalService {
    private static final String INDEX = "cmdb_es";
    private static final String TYPE = "approval";
    @Autowired
    private TransportClient transportClient;

    @Override
    public void insert(@RequestBody List<Map<String, Object>> approvals){
        if (CollectionUtils.isEmpty(approvals)) {
            return;
        }
        log.info("新增cmdb_es 审核数据，数量：{}", approvals.size());
        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        for (int i = 0; i < approvals.size(); i++) {
            Map<String, Object> approval = approvals.get(i);
            bulkRequest.add(transportClient.prepareIndex(INDEX, TYPE).setSource(JSONObject.toJSONString(approval), XContentType.JSON));
            if ((i + 1) % 50 == 0) {
                bulkRequest.execute().actionGet();
                bulkRequest = transportClient.prepareBulk();
            }
        }
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
        }
    }

    @Override
    public Map<String, Object> query(@RequestBody Map<String, Object> query) {
        List<Map<String, Object>> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setTypes(TYPE).setExplain(true);
        int pageSize = query.get("pageSize") == null ? 0 : Integer.parseInt(query.get("pageSize").toString());
        int pageNum = query.get("pageNum") == null ? 0 : Integer.parseInt(query.get("pageNum").toString());
        String instanceId = query.get("instanceId").toString();
        if (pageSize > 0) {
            int from = (pageNum - 1) * pageSize;
            from = from < 0 ? 0 : from;
            request.setFrom(from).setSize(pageSize);
        } else {
            request.setSize(10000);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(instanceId)) {
            QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("instanceId", instanceId);
            queryBuilder.must(queryTermId);
        }
        if (StringUtils.isNotEmpty(query.get("startTime")) && StringUtils.isNotEmpty(query.get("endTime"))) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime;
            Date endTime;
            try {
                startTime = simpleDateFormat.parse(query.get("startTime").toString());
                endTime = simpleDateFormat.parse(query.get("endTime").toString());
            } catch (ParseException e) {
                throw new RuntimeException("时间输入有误无法正常解析" + e);
            }
            QueryBuilder queryTime = QueryBuilders.rangeQuery("operatorTime").gte(startTime.getTime()).lte(endTime.getTime());
            queryBuilder.must(queryTime);
        }
        if (StringUtils.isNotEmpty(query.get("approvalStatus"))) {
            QueryBuilder queryStatus= QueryBuilders.matchPhraseQuery("approvalStatus", query.get("approvalStatus"));
            queryBuilder.must(queryStatus);
        }
        if (StringUtils.isNotEmpty(query.get("operaterType"))) {
            QueryBuilder queryOpType= QueryBuilders.matchPhraseQuery("operaterType", query.get("operaterType"));
            queryBuilder.must(queryOpType);
        }
        if (StringUtils.isNotEmpty(query.get("codeFiledName"))) {
            QueryBuilder queryCodeName = QueryBuilders.matchPhraseQuery("code.filedName", query.get("codeFiledName"));
            queryBuilder.must(queryCodeName);
        }
        SearchResponse response = request.setQuery(queryBuilder).addSort("operatorTime", SortOrder.DESC).execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            JSONObject app = JSONObject.parseObject(searchHit.getSourceAsString());
//            try {
//                if (null != app.get("operatorTime")) {
//                    app.put("operatorTime", new Date((Long) app.get("operatorTime")));
//                }
//                if (null != app.get("approvalTime")) {
//                    app.put("approvalTime", new Date((Long) app.get("approvalTime")));
//                }
//            } catch (Exception e) {
//                throw new RuntimeException("日期格式转换异常" + e);
//            }
//            Map<String, Object> a = (Map<String, Object>)app;
            list.add(app);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("count", searchHits.getTotalHits());
        result.put("data", list);
        return result;
    }

    @Override
    public int getApproval(@RequestBody Map<String, Object> query) {
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setTypes(TYPE).setExplain(true);
        int pageSize = query.get("pageSize") == null ? 0 : Integer.parseInt(query.get("pageSize").toString());
        int pageNum = query.get("pageNum") == null ? 0 : Integer.parseInt(query.get("pageNum").toString());
        String instanceId = query.get("instanceId").toString();
        if (pageSize > 0) {
            int from = (pageNum - 1) * pageSize;
            from = from < 0 ? 0 : from;
            request.setFrom(from).setSize(pageSize);
        } else {
            request.setSize(10000);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(instanceId)) {
            QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("instanceId.keyword", instanceId);
            queryBuilder.must(queryTermId);
        }
        if (StringUtils.isNotEmpty(query.get("startTime")) && StringUtils.isNotEmpty(query.get("endTime"))) {
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String startTime;
            String endTime;
//            try {
                startTime =query.get("startTime").toString();
                endTime = query.get("startTime").toString();
//            } catch (ParseException e) {
//                throw new RuntimeException("时间输入有误无法正常解析" + e);
//            }
            QueryBuilder queryTime = QueryBuilders.rangeQuery("operatorTime").gte(startTime).lte(endTime);
            queryBuilder.must(queryTime);
        }
        if (StringUtils.isNotEmpty(query.get("approvalStatus"))) {
            QueryBuilder queryStauts= QueryBuilders.matchPhraseQuery("approvalStatus", query.get("approvalStatus"));
            queryBuilder.must(queryStauts);
        }
        if (StringUtils.isNotEmpty(query.get("operaterType"))) {
            QueryBuilder queryOpType= QueryBuilders.matchPhraseQuery("operaterType", query.get("operaterType"));
            queryBuilder.must(queryOpType);
        }
        if (StringUtils.isNotEmpty(query.get("codeFiledName"))) {
            QueryBuilder queryCodeName = QueryBuilders.matchPhraseQuery("code.filedName", query.get("codeFiledName"));
            queryBuilder.must(queryCodeName);
        }
        SearchResponse response = request.setQuery(queryBuilder).addSort("approvalTime", SortOrder.DESC).execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        return (int) searchHits.getTotalHits();
    }
}
