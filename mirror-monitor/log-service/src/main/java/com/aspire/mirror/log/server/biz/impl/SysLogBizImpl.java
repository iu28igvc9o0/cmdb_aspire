package com.aspire.mirror.log.server.biz.impl;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.log.api.dto.*;
import com.aspire.mirror.log.server.biz.CommonBiz;
import com.aspire.mirror.log.server.biz.SysLogBiz;
import com.aspire.mirror.log.server.dao.LogAlertRuleDao;
import com.aspire.mirror.log.server.dao.LogFilterRuleDao;
import com.aspire.mirror.log.server.dao.po.*;
import com.aspire.mirror.log.server.helper.ElasticSearchHelper;
import com.aspire.mirror.log.server.util.DateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.net.util.IPAddressUtil;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.aspire.mirror.log.server.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 系统日志业务层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.server.biz.impl
 * 类名称:    SysLogBizImpl.java
 * 类描述:    系统日志业务层
 * 创建人:    JinSu
 * 创建时间:  2019/6/14 15:22
 * 版本:      v1.0
 */
@Service
public class SysLogBizImpl implements SysLogBiz {
    @Autowired
    private ElasticSearchHelper elasticSearchClient;
    Logger logger = LoggerFactory.getLogger(SysLogBizImpl.class);

    @Value("${syslog_index_name_new:log-network-*{yyyyMMdd}*}")
    String indexName;
    @Value("${syslog_index_name:log-network*}")
    String indexNameAll;

    @Autowired
    private LogFilterRuleDao logFilterRuleDao;

    @Autowired
    private LogAlertRuleDao logAlertRuleDao;

    @Autowired
    private CommonBiz commonBiz;
    @Override
    public PageResponse<SysLogResponse> getLogData(SysLogSearchRequest request) {
        return getSysLogResponse(request);
    }

    private PageResponse<SysLogResponse> getSysLogResponse(SysLogSearchRequest request) {
        logger.info("SysLogBizImpl(getLogData), SysLogSearchRequest:{}", request);
        List<SysLogResponse> listSysLog = Lists.newArrayList();
        PageResponse<SysLogResponse> resp = new PageResponse<>();
        // 过滤条件
        final BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        boolean start = StringUtils.isNotBlank(request.getCreateTimeStart());
        boolean end = StringUtils.isNotBlank(request.getCreateTimeEnd());
        Date endDate = new Date();
        Date startDate = null;
        if (start || end) {
            RangeQueryBuilder timeRange = QueryBuilders.rangeQuery("log_create_time");
            if (start) {
                Date startTime = DateUtil.getDate(request.getCreateTimeStart(), DateUtil.DATE_TIME_CH_FORMAT);
                timeRange.gte(startTime.getTime());
                startDate = startTime;
            }
            if (end) {
                Date endTime = DateUtil.getDate(request.getCreateTimeEnd(), DateUtil.DATE_TIME_CH_FORMAT);
                timeRange.lte(endTime.getTime());
                endDate = endTime;
            }
            bqb.must(timeRange);
        }
        String index = this.indexNameAll;
        if(null!=startDate) {
            index = DateUtils.getExecIndexs(indexName, startDate, endDate);
        }
        logger.info("SysLogBizImpl(getLogData), index:{}", index);
        if (StringUtils.isNotEmpty(request.getIp())) {
//            bqb.must(QueryBuilders.queryStringQuery("ip:" + request.getIp()));
            String ip = request.getIp().trim();
            if (IPAddressUtil.isIPv4LiteralAddress(ip) || IPAddressUtil.isIPv6LiteralAddress(ip)) {
                bqb.must(QueryBuilders.matchPhraseQuery("host", ip));
            } else {
                bqb.must(QueryBuilders.queryStringQuery("host:*" + ip + "*"));
            }
        }
        if (StringUtils.isNotEmpty(request.getPool())) {
//            bqb.must(QueryBuilders.queryStringQuery("poolcode:" + request.getPool().trim()));
            bqb.must(QueryBuilders.matchQuery("poolcode", request.getPool().trim()).operator(Operator.AND));
        }
        if (!CollectionUtils.isEmpty(request.getParams())) {
//            StringBuilder paramString = new StringBuilder("message.keyword:" + "*" + QueryParser.escape(request.getParams().get(0).trim()) + "*");
//            for (int i = 1; i < request.getParams().size(); i++) {
//                paramString.append(" AND message.keyword:*").append(QueryParser.escape(request.getParams().get(i).trim())).append("*");
//            }
//            bqb.must(QueryBuilders.queryStringQuery(paramString.toString()));
            for (String param : request.getParams()) {
                bqb.must(QueryBuilders.wildcardQuery("message.keyword", "*" + QueryParser.escape(param.trim()) + "*"));
            }
        }
        if (StringUtils.isNotBlank(request.getIncludeKey())) {
            for (String param : request.getIncludeKey().split(",")) {
                bqb.must(QueryBuilders.wildcardQuery("message.keyword", "*" + QueryParser.escape(param.trim()) + "*"));
            }
        }
        if (StringUtils.isNotBlank(request.getNoIncludeKey())) {
            for (String param : request.getNoIncludeKey().split(",")) {
                bqb.mustNot(QueryBuilders.wildcardQuery("message.keyword", "*" + QueryParser.escape(param.trim()) + "*"));
//                bqb.mustNot(getQueryKey(param.trim()));
            }
        }
        final SearchRequestBuilder searchRequestBuilder;
        if (request.getIsExport().equals("0")) {
            Integer pageNo = StringUtils.isNotBlank(request.getPageNo())
                    ? Integer.parseInt(request.getPageNo()) - 1 : 0;
            Integer pageSize = StringUtils.isNotBlank(request.getPageSize()) ?
                    Integer.parseInt(request.getPageSize()) : 50;
            searchRequestBuilder = elasticSearchClient.getClient().prepareSearch(commonBiz.getClusterIndex(request, index))
                    .setQuery(bqb).setFrom(pageNo*pageSize).setSize(pageSize).setTrackScores(true).addSort("log_create_time", SortOrder.DESC);
            logger.info("SysLogBizImpl(getLogData), indexs:{}", JSON.toJSONString(searchRequestBuilder.request().indices()));
            logger.info("SysLogBizImpl(getLogData), es查询参数：{}", searchRequestBuilder);
            SearchResponse response = searchRequestBuilder.get();
            if (response.getHits().getTotalHits() > 0L) {
                final SearchHits hits = response.getHits();
                resp.setCount((int) hits.totalHits);
                for (SearchHit searchHit : hits.getHits()) {
                    transferResp(listSysLog, searchHit);
                }
            }
        } else {
            // scroll查询所有数据
            searchRequestBuilder = elasticSearchClient.getClient().prepareSearch(commonBiz.getClusterIndex(request, index))
                    .setQuery(bqb).addSort("log_create_time", SortOrder.DESC).setScroll(new TimeValue(20000));
            SearchResponse response = searchRequestBuilder.setSize(1).get();
            if (response.getHits().getTotalHits() > 0) {
                resp.setCount((int) response.getHits().getTotalHits());
                do {
                    for (SearchHit searchHit : response.getHits().getHits()) {
                        transferResp(listSysLog, searchHit);
                    }
                    response = elasticSearchClient.getClient().prepareSearchScroll(response.getScrollId()).setScroll
                            (new TimeValue(60000)).execute().actionGet();
                } while (response.getHits().getHits().length != 0);
            }
        }
        logger.info("SysLogBizImpl(getLogData), get syslog size is :{}", resp.getCount());
        resp.setResult(listSysLog);
        return resp;
    }

    /**
     *
     * @param key
     * @return
     */
    private QueryBuilder getQueryKey (String key) {
        key = key.trim();
        if (key.indexOf(" ") > -1) {
            return QueryBuilders.wildcardQuery("message.keyword", "*" + QueryParser.escape(key) + "*");
        } else {
            return QueryBuilders.queryStringQuery("message.keyword:*" + QueryParser.escape(key) + "*").defaultOperator(Operator.AND);
        }
    }

    private void transferResp(List<SysLogResponse> listSysLog, SearchHit searchHit) {
        final Map<String, Object> resMap = searchHit.getSourceAsMap();
        SysLogResponse sysLogResponse = new SysLogResponse();
        sysLogResponse.setIp((String) resMap.get("host"));
        sysLogResponse.setLogCreateTime(DateUtil.getDate((String) resMap.get("log_create_time"), DateUtil
                .ELASTIC_TIME_FORMAT, "GMT"));
        sysLogResponse.setMassage((String) resMap.get("message"));
        sysLogResponse.setPool((String) resMap.get("poolcode"));
        sysLogResponse.setPoolName((String) resMap.get("poolname"));
        sysLogResponse.setProxyip((String) resMap.get("proxyip"));
        sysLogResponse.setProxyport((String) resMap.get("proxyport"));
        listSysLog.add(sysLogResponse);
    }

    @Override
    public String createLogFilterRule(CreateLogFilterRuleReq request) {
        String uuid = StringUtils.isEmpty(request.getUuid()) ? UUID.randomUUID().toString() : request.getUuid();
        request.setUuid(uuid);
        logFilterRuleDao.createLogFilterRule(jacksonBaseParse(CreateLogFilterRuleReqDTO.class, request));
        return uuid;
    }

    @Override
    public LogFilterRuleDetail getLogFilterRuleDetail(String uuid) {
        return jacksonBaseParse(LogFilterRuleDetail.class,logFilterRuleDao.getLogFilterRuleDetail(uuid));
    }

    @Override
    public List<LogFilterRuleDetail> getLogFilterRuleList(String ruleType) {
        return jacksonBaseParse(LogFilterRuleDetail.class, logFilterRuleDao.getLogFilterRuleList(ruleType));
    }

    @Override
    public void deleteLogFilterRule(String uuid) {
        logFilterRuleDao.deleteLogFilterRule(uuid);
    }

    @Override
    public PageResponse<SysLogResponse> getLogDataByFilterRule(String uuid, String pageNo, String pageSize) {
        LogFilterRuleDetailDTO logFilterRuleDetail = logFilterRuleDao.getLogFilterRuleDetail(uuid);
        SysLogSearchRequest sysLogSearchRequest = new SysLogSearchRequest();
        sysLogSearchRequest.setIp(logFilterRuleDetail.getIp());
        sysLogSearchRequest.setPool(logFilterRuleDetail.getIdcType());
        sysLogSearchRequest.setCreateTimeStart(logFilterRuleDetail.getStartTime());
        sysLogSearchRequest.setCreateTimeEnd(logFilterRuleDetail.getEndTime());
//        sysLogSearchRequest.setParams(Arrays.asList(logFilterRuleDetail.getParam().split(",")));
        sysLogSearchRequest.setIncludeKey(logFilterRuleDetail.getIncludeKey());
        sysLogSearchRequest.setNoIncludeKey(logFilterRuleDetail.getNoIncludeKey());
        sysLogSearchRequest.setPageNo(pageNo);
        sysLogSearchRequest.setPageSize(pageSize);
        return getSysLogResponse(sysLogSearchRequest);
    }

    @Override
    public LogFilterRuleDetail getLogFilterRuleDetailByName(String name) {
        return jacksonBaseParse(LogFilterRuleDetail.class,logFilterRuleDao.getLogFilterRuleDetailByName(name));
    }

    @Override
    public String createLogAlertRule(CreateLogAlertRuleReq request) {
        String uuid = UUID.randomUUID().toString();
        request.setUuid(uuid);
        logAlertRuleDao.createLogAlertRule(jacksonBaseParse(CreateLogAlertRuleReqDTO.class, request));
        return uuid;
    }

    @Override
    public LogAlertRuleDetail getLogAlertRuleDetail(String uuid) {
        return jacksonBaseParse(LogAlertRuleDetail.class,logAlertRuleDao.getLogAlertRuleDetail(uuid));
    }

    @Override
    public PageResponse<LogAlertRuleDetail> getLogAlertRuleList(LogAlertRuleListReq request) {
        PageResponse<LogAlertRuleDetail> response = new PageResponse<LogAlertRuleDetail>();
        response.setCount(logAlertRuleDao.getLogAlertRuleListCount(jacksonBaseParse(LogAlertRuleListReqDTO.class,request)));
        response.setResult(
                jacksonBaseParse(LogAlertRuleDetail.class,
                        logAlertRuleDao.getLogAlertRuleListData(jacksonBaseParse(LogAlertRuleListReqDTO.class,request))));
        return response;
    }

    @Override
    public void deleteLogAlertRule(List<String> uuidList) {
        logAlertRuleDao.deleteLogAlertRule(uuidList);
        logAlertRuleDao.deleteLogAlertLinkedById(uuidList);
    }

    @Override
    public LogAlertRuleDetail getLogAlertRuleDetailByName(String name) {
        return jacksonBaseParse(LogAlertRuleDetail.class,logAlertRuleDao.getLogAlertRuleDetailByName(name));
    }

    @Override
    public void updateRunStatusByUuid(Map<String, Object> request) {
        logAlertRuleDao.updateRunStatusByUuid(request);
    }

    @Override
    public void updateLogAlertRule(CreateLogAlertRuleReq request) {
        logAlertRuleDao.updateLogAlertRule(jacksonBaseParse(CreateLogAlertRuleReqDTO.class, request));
    }

    @Override
    public void insertLogAlertLinked(CreateLogAlertLinkedReq request) {
        logAlertRuleDao.insertLogAlertLinked(jacksonBaseParse(CreateLogAlertLinkedReqDTO.class, request));
    }
}
