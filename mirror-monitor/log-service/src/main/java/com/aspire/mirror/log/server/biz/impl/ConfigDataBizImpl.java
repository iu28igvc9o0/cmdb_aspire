package com.aspire.mirror.log.server.biz.impl;

import static com.aspire.mirror.log.server.util.PayloadParseUtil.jacksonBaseParse;

import java.text.SimpleDateFormat;
import java.util.*;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
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

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.log.api.dto.ConfigDataResponse;
import com.aspire.mirror.log.api.dto.ConfigDataSearchRequest;
import com.aspire.mirror.log.api.dto.ConfigFileUploadReq;
import com.aspire.mirror.log.server.biz.CommonBiz;
import com.aspire.mirror.log.server.biz.ConfigDataBiz;
import com.aspire.mirror.log.server.dao.ConfigFileDao;
import com.aspire.mirror.log.server.dao.po.ConfigFileUploadLogDTO;
import com.aspire.mirror.log.server.helper.ElasticSearchHelper;
import com.aspire.mirror.log.server.util.DateUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 配置数据业务层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.server.biz.impl
 * 类名称:    ConfigDataBizImpl.java
 * 类描述:    配置数据业务层
 * 创建人:    JinSu
 * 创建时间:  2019/6/19 14:41
 * 版本:      v1.0
 */
@Service
public class ConfigDataBizImpl implements ConfigDataBiz {
    @Autowired
    private ElasticSearchHelper elasticSearchClient;
    @Autowired
    private CommonBiz commonBiz;

    Logger logger = LoggerFactory.getLogger(SysLogBizImpl.class);

    @Value("${config_index_name_new:config-network-*{yyyyMMdd}*}")
    String indexName;
    
    @Value("${config_index_name:config-network*}")
    String indexNameAll;

    private static final String INDEX = "config-network-";
    private static final String TYPE = "doc";

    @Autowired
    private ConfigFileDao configFileDao;

    @Override
    public PageResponse<ConfigDataResponse> getConfigData(ConfigDataSearchRequest request) {
        return getConfigDataList(request);
    }

    private PageResponse<ConfigDataResponse> getConfigDataList(ConfigDataSearchRequest request) {
    	logger.info("SysLogBizImpl(getLogData), ConfigDataSearchRequest:{}", request);
    	PageResponse<ConfigDataResponse> resp = new PageResponse<>();
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
        logger.info("ConfigDataBizImpl(getConfigData), index：{}", index);
        if (StringUtils.isNotEmpty(request.getIp())) {
            bqb.must(QueryBuilders.queryStringQuery("host:*" + request.getIp().trim() + "*"));
        }
        if (StringUtils.isNotEmpty(request.getPool())) {
//            bqb.must(QueryBuilders.queryStringQuery("poolcode:" + request.getPool().trim()));
            bqb.must(QueryBuilders.matchQuery("poolname", request.getPool().trim()).operator(Operator.AND));
        }
        if (!CollectionUtils.isEmpty(request.getParams())) {
//            StringBuilder paramString = new StringBuilder("message.keyword:" + "*" + QueryParser.escape(request
//                    .getParams().get(0).trim()) + "*");
//            for (int i = 1; i < request.getParams().size(); i++) {
//                paramString.append(" AND message.keyword:*").append(QueryParser.escape(request.getParams().get(i)
//                        .trim())).append("*");
//            }
//            bqb.must(QueryBuilders.queryStringQuery(paramString.toString()));
            for (String param : request.getParams()) {
                bqb.must(QueryBuilders.wildcardQuery("message.keyword", "*" + QueryParser.escape(param.trim()) + "*"));
            }
        }
//        if (StringUtils.isNotBlank(request.getContent())) {
//            bqb.must(QueryBuilders.queryStringQuery("message:" + "*" + request.getContent().trim() + "*"));
//        }
        final SearchRequestBuilder searchRequestBuilder;
        List<ConfigDataResponse> listConfigData = Lists.newArrayList();
        if (request.getIsExport().equals("0")) {
            Integer pageNo = StringUtils.isNotBlank(request.getPageNo())
                    ? Integer.parseInt(request.getPageNo()) - 1 : 0;
            Integer pageSize = StringUtils.isNotBlank(request
                    .getPageSize()) ?
                    Integer.parseInt(request.getPageSize()) : 20;
            searchRequestBuilder = elasticSearchClient.getClient().prepareSearch(commonBiz.getClusterIndex(request, index))
                    .setQuery(bqb).setFrom(pageNo * pageSize).setSize(pageSize).addSort("log_create_time", SortOrder
                            .DESC).addSort("_uid", SortOrder.DESC);
            logger.info("ConfigDataBizImpl(getConfigData), indexs：{}", searchRequestBuilder.request().indices());
            logger.info("ConfigDataBizImpl(getConfigData), 分页查询 es查询参数：{}", searchRequestBuilder);
            SearchResponse response = searchRequestBuilder.get();
            if (response.getHits().getTotalHits() > 0L) {
                final SearchHits hits = response.getHits();
                resp.setCount((int) hits.totalHits);
                for (SearchHit searchHit : hits.getHits()) {
                    transferConfigResp(listConfigData, searchHit);
                }
            }
        } else {
            // scroll查询所有数据
            searchRequestBuilder = elasticSearchClient.getClient().prepareSearch(commonBiz.getClusterIndex(request, index))
                    .setQuery(bqb).addSort("log_create_time", SortOrder.DESC).setScroll(new TimeValue(20000));
            logger.info("ConfigDataBizImpl(getConfigData), indexs：{}", searchRequestBuilder.request().indices());
            logger.info("ConfigDataBizImpl(getConfigData), 导出 es查询参数：{}", searchRequestBuilder);
            SearchResponse response = searchRequestBuilder.setSize(1).get();
            if (response.getHits().getTotalHits() > 0) {
                resp.setCount((int) response.getHits().getTotalHits());
                do {
                    for (SearchHit searchHit : response.getHits().getHits()) {
                        transferConfigResp(listConfigData, searchHit);
                    }
                    response = elasticSearchClient.getClient().prepareSearchScroll(response.getScrollId()).setScroll
                            (new TimeValue(60000)).execute().actionGet();
                } while (response.getHits().getHits().length != 0);
            }
        }
        resp.setResult(listConfigData);
        return resp;
    }

    @Override
    public ConfigDataResponse getConfigById(String index, String id) {
        String[] clusterIndex = commonBiz.getClusterIndex(null, index);
        SearchResponse response = elasticSearchClient.getClient().prepareSearch(clusterIndex)
                .setQuery(QueryBuilders.matchPhraseQuery("_id", id)).get();
        if (response.getHits().getTotalHits() > 0L) {
            final SearchHit searchHit = response.getHits().getHits()[0];
            final Map<String, Object> resMap = searchHit.getSourceAsMap();
            resMap.put("index", searchHit.getIndex());
            resMap.put("id", searchHit.getId());
            ConfigDataResponse configDataResponse = new ConfigDataResponse();
            transferRespDetail(resMap, configDataResponse);
            return configDataResponse;
        }
        return null;
    }

    /**
     * 根据索引和ip精确查找配置文件并返回一条数据
     * @param index
     * @param ip
     * @return
     */
    public ConfigDataResponse getConfigByIp(String index, String idcType, String ip) {
        if (StringUtils.isEmpty(index) || StringUtils.isEmpty(ip)) {
            return null;
        }
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        bqb.must(QueryBuilders.matchPhraseQuery("host", ip));
        if (StringUtils.isNotEmpty(idcType)) {
            bqb.must(QueryBuilders.matchPhraseQuery("poolname", idcType));
        }
        String[] clusterIndex = commonBiz.getClusterIndex(null, index);
        logger.info("index is : {}", Arrays.toString(clusterIndex));
        SearchResponse response = elasticSearchClient.getClient().prepareSearch(clusterIndex).setQuery(bqb).get();
        if (response.getHits().getTotalHits() > 0L) {
            final SearchHit searchHit = response.getHits().getHits()[0];
            final Map<String, Object> resMap = searchHit.getSourceAsMap();
            resMap.put("index", searchHit.getIndex());
            resMap.put("id", searchHit.getId());
            ConfigDataResponse configDataResponse = new ConfigDataResponse();
            transferRespDetail(resMap, configDataResponse);
            return configDataResponse;
        }
        return null;
    }

    /**
     *
     * @param idcType
     * @param ip
     * @return
     */
    public Set<String> getIndexByIp(String idcType, String ip) {
        Set<String> list = Sets.newHashSet();
        if (StringUtils.isEmpty(ip)) {
            return list;
        }
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        bqb.must(QueryBuilders.matchPhraseQuery("host", ip));
        if (StringUtils.isNotEmpty(idcType)) {
            bqb.must(QueryBuilders.matchPhraseQuery("poolname", idcType));
        }
        Date endDate = new Date(System.currentTimeMillis());
        Date startDate = new Date(System.currentTimeMillis() - 7 * 24 * 3600 * 1000);
        String index = DateUtils.getExecIndexs(indexName, startDate, endDate);
        SearchResponse response = elasticSearchClient.getClient().prepareSearch(commonBiz.getClusterIndex(null, index)).setQuery(bqb).get();
        if (response.getHits().getTotalHits() > 0L) {
            for (SearchHit searchHit: response.getHits()) {
                list.add(searchHit.getIndex());
            }
        }
        return list;
    }


    private void transferConfigResp(List<ConfigDataResponse> listConfigData, SearchHit searchHit) {
        final Map<String, Object> resMap = searchHit.getSourceAsMap();
        resMap.put("index", searchHit.getIndex());
        resMap.put("id", searchHit.getId());
        ConfigDataResponse configDataResponse = new ConfigDataResponse();
        transferRespDetail(resMap, configDataResponse);
        String message = configDataResponse.getMassage();
        if (message != null && message.length() > 100) {
            configDataResponse.setMassage(message.substring(0, 100) + "...");
        }
        listConfigData.add(configDataResponse);
    }

    private void transferRespDetail(Map<String, Object> resMap, ConfigDataResponse configDataResponse) {
        configDataResponse.setId((String) resMap.get("id"));
        configDataResponse.setIndex((String) resMap.get("index"));
        configDataResponse.setIp((String) resMap.get("host"));
        configDataResponse.setLogCreateTime(DateUtil.getDate((String) resMap.get("log_create_time"),
                DateUtil.ELASTIC_CONFIG_TIME_FORMAT));
        configDataResponse.setMassage((String) resMap.get("message"));
        configDataResponse.setPool((String) resMap.get("poolcode"));
        configDataResponse.setPoolName((String) resMap.get("poolname"));
        configDataResponse.setCreateTime(DateUtil.getDate((String) resMap.get("@timestamp"),
                DateUtil.ELASTIC_CONFIG_TIME_FORMAT));
    }

    //private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
    //private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final Map<String,String> IDC_TYPE_CODE = new HashMap<String, String>() {{
        put("信息港资源池","xxgzyc");
        put("呼和浩特资源池","hhhtzyc");
        put("哈尔滨资源池","hebzyc");
        put("南方基地","nfjd");
        put("信息港池外","xxgcw");
        put("深圳池外","szcw");
    }};

    @Override
    public void uploadConfigFile(List<ConfigFileUploadReq> request) {

        try {
        	 SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            List<Map<String,Object>> insertConfigFileReqMaps = Lists.newArrayList();
            sdf2.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            Date date = new Date();
            String dateStr = sdf2.format(date);
            for (ConfigFileUploadReq con : request) {
//                ConfigDataSearchRequest configDataSearchRequest = new ConfigDataSearchRequest();
//                configDataSearchRequest.setIp(con.getDeviceIp());
//                configDataSearchRequest.setPool(con.getIdcType());
//                PageResponse<ConfigDataResponse> configDataList = getConfigDataList(configDataSearchRequest);
//                if (configDataList.getCount() > 0) {
//                    request.remove(con);
//                    continue;
//                }
                Map<String,Object> map = Maps.newHashMap();
                map.put("host",con.getDeviceIp());
                map.put("proxyip",con.getDeviceIp());
                map.put("cmd","");
                map.put("poolcode",con.getIdcType());
                map.put("poolname",con.getIdcType());
                map.put("log_create_time", con.getUploadTime());
                map.put("message",con.getUploadContent());
                map.put("@timestamp",dateStr);
                insertConfigFileReqMaps.add(map);
            }
            insertConfigFile2(insertConfigFileReqMaps);
            configFileDao.insertConfigFileUploadLog(jacksonBaseParse(ConfigFileUploadLogDTO.class,request));

        } catch (Exception e) {
            logger.info("配置文件上传失败: {}",e);
            throw new RuntimeException("配置文件上传失败: {}",e);
        }
    }

    private void insertConfigFile2(List<Map<String,Object>> request) {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(request)) return;
        logger.info("新增配置文件数据，数量：{}", request.size());
        BulkRequestBuilder bulkRequest = elasticSearchClient.getClient().prepareBulk();
        Date date = new Date();
        for (Map<String, Object> map : request) {
            logger.info("BulkRequestBuilder >>> {}", map.toString());
            String idc = String.valueOf(map.get("poolname"));
            String idcTypeCode = StringUtils.isEmpty(IDC_TYPE_CODE.get(idc)) ? "pool" : IDC_TYPE_CODE.get(idc);
            String str = INDEX + idcTypeCode;
            bulkRequest.add(elasticSearchClient.getClient().prepareIndex(str, TYPE ).setSource(map));
        }
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
        }
    }
}
