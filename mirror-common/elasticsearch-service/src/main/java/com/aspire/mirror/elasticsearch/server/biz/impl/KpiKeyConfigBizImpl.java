package com.aspire.mirror.elasticsearch.server.biz.impl;

import com.alibaba.fastjson.JSONArray;
import com.aspire.mirror.common.entity.PageResult;
import com.aspire.mirror.elasticsearch.api.dto.ItemDto;
import com.aspire.mirror.elasticsearch.api.dto.monitor.KpiKeyConfig;
import com.aspire.mirror.elasticsearch.server.biz.IKpiKeyConfigBiz;
import com.aspire.mirror.elasticsearch.server.enums.KpiTypeEnum;
import com.aspire.mirror.elasticsearch.server.exception.BaseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.server.biz.impl
 * @Author: baiwenping
 * @CreateTime: 2020-06-11 15:30
 * @Description: ${Description}
 */
@Service
@Slf4j
public class KpiKeyConfigBizImpl implements IKpiKeyConfigBiz {

    @Autowired
    private TransportClient transportClient;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String INDEX = "monitor_key_config";
    private static final String TYPE = "doc";
    private static final String KPI_DIR_REDIS = "KPI:TYPE:";

    /**
     * 新增
     *
     * @param kpiKeyConfig
     */
    public void insert(KpiKeyConfig kpiKeyConfig) {
        if (kpiKeyConfig == null ) {
            throw new BaseException("kpiKeyConfig is null");
        }
        if (StringUtils.isEmpty(kpiKeyConfig.getKpiType())) {
            throw new BaseException("kpiType is null");
        }
        if (StringUtils.isEmpty(kpiKeyConfig.getKpiKey())) {
            throw new BaseException("kpiKey is null");
        }
        kpiKeyConfig.setKpiType(kpiKeyConfig.getKpiType().toUpperCase());
        log.info("新增指标配置数据：{}", kpiKeyConfig);
        transportClient.prepareIndex(INDEX, TYPE).setSource(JSONObject.fromObject(kpiKeyConfig)
                .toString(), XContentType.JSON).execute().actionGet();
        redisTemplate.delete(KPI_DIR_REDIS + kpiKeyConfig.getKpiType().toUpperCase());
    }

    /**
     * 新增
     *
     * @param list
     */
    @Override
    public void insert(List<KpiKeyConfig> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        log.info("新增指标配置数据，数量：{}", list.size());
        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();
        for (int i = 0; i < list.size(); i++) {
            KpiKeyConfig kpiKeyConfig = list.get(i);
            String kpiType = kpiKeyConfig.getKpiType();
            if (StringUtils.isEmpty(kpiType) || StringUtils.isEmpty(kpiKeyConfig.getKpiKey())) {
                continue;
            }
            kpiKeyConfig.setKpiType(kpiType.toUpperCase());
            bulkRequest.add(transportClient.prepareIndex(INDEX, TYPE).setSource(JSONObject.fromObject(kpiKeyConfig)
                    .toString(), XContentType.JSON));
            if ((i + 1) % 50 == 0) {
                bulkRequest.execute().actionGet();
                bulkRequest = transportClient.prepareBulk();
            }
        }
        if (bulkRequest.numberOfActions() > 0) {
            bulkRequest.execute().actionGet();
        }
        // 删除所有key
        redisTemplate.delete(redisTemplate.keys(KPI_DIR_REDIS));
    }

    /**
     * 查询
     *
     * @param kpiType
     * @param kpiKey
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult<KpiKeyConfig> query(String kpiType, String kpiKey, int pageNum, int pageSize) {
        List<KpiKeyConfig> list = new ArrayList<>();
        SearchRequestBuilder request = transportClient.prepareSearch(INDEX).setExplain(true);
        if (pageSize > 0) {
            int from = (pageNum - 1) * pageSize;
            from = from < 0 ? 0 : from;
            request.setFrom(from).setSize(pageSize);
        } else {
            request.setSize(10000);
        }
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.isNotEmpty(kpiType)) {
            QueryBuilder queryTermId = QueryBuilders.matchPhraseQuery("kpiType", kpiType.toUpperCase());
            queryBuilder.must(queryTermId);
        }
        if (StringUtils.isNotEmpty(kpiKey)) {
            QueryBuilder queryIdc = QueryBuilders.matchPhraseQuery("kpiKey", kpiKey);
            queryBuilder.must(queryIdc);
        }
        SearchResponse response = request.setQuery(queryBuilder).execute().actionGet();
        SearchHits searchHits = response.getHits();
        log.info("共匹配到:" + searchHits.getTotalHits() + "条记录!");
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit searchHit : hits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            KpiKeyConfig kpiKeyConfig = new KpiKeyConfig();
            try {
                BeanUtils.populate(kpiKeyConfig, sourceAsMap);
                list.add(kpiKeyConfig);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        PageResult<KpiKeyConfig> result = new PageResult<>();
        result.setCount((int) searchHits.getTotalHits());
        result.setCurPage(pageNum);
        result.setPageSize(pageSize);
        result.setResult(list);
        return result;
    }

    /**
     * 查询
     *
     * @param kpiType
     * @return
     */
    @Override
    public BoolQueryBuilder query4Builder(String kpiType, String field) {
        if (StringUtils.isEmpty(kpiType)) {
            throw new BaseException("kpiType is null");
        }
        if (StringUtils.isEmpty(field)) {
            field = "item";
        }
        kpiType = kpiType.toUpperCase();
        String key = KPI_DIR_REDIS + kpiType;
        List<KpiKeyConfig> list;
        if (redisTemplate.hasKey(key)) {
            String o = (String) redisTemplate.opsForValue().get(key);
            list = JSONArray.parseArray(o, KpiKeyConfig.class);
        } else {
            PageResult<KpiKeyConfig> query = query(kpiType, null, -1, 20);
            list = query.getResult();
            log.info("type is {}, keys are : {}", kpiType, list);
            if (!CollectionUtils.isEmpty(list)) {
                String s = JSONArray.toJSONString(list);
                redisTemplate.opsForValue().set(key, s);
                redisTemplate.expire(key, 24, TimeUnit.HOURS);
            }
        }

        if (!CollectionUtils.isEmpty(list)) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            Set<String> set = Sets.newHashSet();
            for (KpiKeyConfig kpiKeyConfig: list) {
                if ("2".equals(kpiKeyConfig.getKpiKeyModel())) {
                    QueryBuilder queryTermId = QueryBuilders.matchPhrasePrefixQuery(field, kpiKeyConfig.getKpiKey());
                    queryBuilder.should(queryTermId);
                } else {
                    set.add(kpiKeyConfig.getKpiKey());
                }
            }
            if (set.size() > 0) {
                TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(field + ".keyword", set.toArray());
                queryBuilder.should(termsQueryBuilder);
            }
            log.info(queryBuilder.toString());
            return queryBuilder;
        }
        throw new BaseException("no available item key");
    }

    /**
     * 查询
     *
     * @param kpiTypes
     * @return
     */
    public BoolQueryBuilder query4Builder(String field, String... kpiTypes) {
        if (kpiTypes == null || kpiTypes.length == 0) {
            throw new BaseException("kpiTypes is null");
        }
        if (StringUtils.isEmpty(field)) {
            field = "item";
        }
        List<KpiKeyConfig> list = Lists.newArrayList();
        for (String kpiType:kpiTypes) {
            kpiType = kpiType.toUpperCase();
            String key = KPI_DIR_REDIS + kpiType;
            if (redisTemplate.hasKey(key)) {
                String o = (String) redisTemplate.opsForValue().get(key);
                list.addAll(JSONArray.parseArray(o, KpiKeyConfig.class));
            } else {
                PageResult<KpiKeyConfig> query = query(kpiType, null, -1, 20);
                List<KpiKeyConfig> list1 = query.getResult();
                list.addAll(list1);
                log.info("type is {}, keys are : {}", kpiType, list1);
                if (!CollectionUtils.isEmpty(list1)) {
                    String s = JSONArray.toJSONString(list1);
                    redisTemplate.opsForValue().set(key, s);
                    redisTemplate.expire(key, 24, TimeUnit.HOURS);
                }
            }
        }

        if (!CollectionUtils.isEmpty(list)) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
            Set<String> set = Sets.newHashSet();
            for (KpiKeyConfig kpiKeyConfig: list) {
                if ("2".equals(kpiKeyConfig.getKpiKeyModel())) {
                    QueryBuilder queryTermId = QueryBuilders.matchPhrasePrefixQuery(field, kpiKeyConfig.getKpiKey());
                    queryBuilder.should(queryTermId);
                } else {
                    set.add(kpiKeyConfig.getKpiKey());
                }
            }
            if (set.size() > 0) {
                TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery(field + ".keyword", set.toArray());
                queryBuilder.should(termsQueryBuilder);
            }
            log.info(queryBuilder.toString());
            return queryBuilder;
        }
        throw new BaseException("no available item key");
    }
}
