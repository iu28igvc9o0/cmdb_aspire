package com.aspire.mirror.indexadapt.adapt.migubizmonitordb.elasticsearch;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexadapt.adapt.migubizmonitordb.model.BusinessTrigger;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j
@Component
public final class ElasticSearchHelper {
	@Autowired
    ElasticSearchClient elasticSearchClient;
	
	public Long count(String indexName, String query, List<Map<String, String>> mustList,
            List<Map<String, String>> mustNotList, Date startTime, Date endTime, String dateField) {
        if (null == indexName || null == startTime || null == endTime) {
            log.error("ElasticSearchServiceImpl(count),参数 indexName,startTime,endTime不能为空");
            return 0L;
        }
        if (null == query || query.trim().length() == 0) {
            query = "*";
        }

        if (null == dateField || dateField.trim().length() == 0) {
            dateField = "@timestamp";
        }

        SearchResponse response = null;
        try {
            // 过滤条件
            final BoolQueryBuilder bqb = QueryBuilders.boolQuery();
            // 拼接queryString
            bqb.must(QueryBuilders.queryStringQuery(query));

            if (null != mustList && !mustList.isEmpty()) {
                for (final Map<String, String> mustMap : mustList) {
                    for (final String key : mustMap.keySet()) {
                        bqb.mustNot(QueryBuilders.matchPhraseQuery(key, mustMap.get(key)));
                    }
                }
            }
            if (null != mustNotList && !mustNotList.isEmpty()) {
                for (final Map<String, String> mustnotMap : mustNotList) {
                    for (final String key : mustnotMap.keySet()) {
                        bqb.must(QueryBuilders.matchPhraseQuery(key, mustnotMap.get(key)));
                    }
                }
            }

            // 时间范围控制
            bqb.must(QueryBuilders.rangeQuery(dateField).gte(startTime.getTime()).lte(endTime.getTime()));
            // 请求
            final SearchRequestBuilder searchRequestBuilder = elasticSearchClient.getClient().prepareSearch(indexName)
                    .setQuery(bqb);
            response = searchRequestBuilder.setSize(0).setExplain(true).get();
            return response.getHits().getTotalHits();
        } catch (final Exception e) {
            log.error("ElasticSearchServiceImpl(search)异常", e);
            // elasticSearchClient.close();
        }
        return null;
    }
	
	public Map<String, Long> termsItemMap(BusinessTrigger businessTrigger, String indexName, String query,
            JSONArray fieldArray, JSONArray dateArray, String dateField, String aggField) {
        final Map<String, Long> map = new HashMap<String, Long>();
        if (null == indexName || null == fieldArray || null == dateArray || null == aggField) {
            log.error("ElasticSearchServiceImpl(termsItemMap),参数 indexName,fieldArray,dateArray,aggField不能为空");
            return map;
        }
        if (null == query || query.trim().length() == 0) {
            query = aggField + ":*";
        }

        if (null == dateField || dateField.trim().length() == 0) {
            dateField = "@timestamp";
        }

        SearchResponse response = null;
        try {
            // 过滤条件
            final BoolQueryBuilder bqb = QueryBuilders.boolQuery();

            // 拼接queryString
            bqb.must(QueryBuilders.queryStringQuery(query));

            boolean notItemFlag = false;// 如果都没有过滤字段，标识退出
            // 字段过滤查询
            for (int i = 0; i < fieldArray.size(); i++) {
                final JSONObject item = fieldArray.getJSONObject(i);
                if (StringUtils.isEmpty(item.getString("field"))) {
                    notItemFlag = true;
                    continue;
                } else {
                    notItemFlag = false;
                }
                final String itemField = "A_" + item.getString("field");
                final String fieldComparor = item.getString("fieldComparor");//
                final String fieldValue = item.getString("fieldValue").trim();

                if ("0".equals(fieldComparor)) {// >
                    bqb.must(QueryBuilders.rangeQuery(itemField).gt(fieldValue));
                } else if ("1".equals(fieldComparor)) {// <
                    bqb.must(QueryBuilders.rangeQuery(itemField).lt(fieldValue));
                } else if ("2".equals(fieldComparor)) {// =
                    bqb.must(QueryBuilders.matchPhraseQuery(itemField, fieldValue));
                } else if ("3".equals(fieldComparor)) {// 包含
                    bqb.must(QueryBuilders.termsQuery(itemField, fieldValue.trim().split("[\\,,\\，,\\;,\\；]")));
                }
            }

            // 时间范围控制
            for (int i = 0; i < dateArray.size(); i++) {
                final JSONObject itemDate = dateArray.getJSONObject(i);
                final String jkzbDimDateField = itemDate.getString("field");
                if (StringUtils.isNotEmpty(jkzbDimDateField) && !"10".equals(jkzbDimDateField)) {
                    dateField = "A_" + jkzbDimDateField;
                }
                // 0> 1< 2=
                final String dateComparor = itemDate.getString("dateComparor");

                if (StringUtils.isEmpty(dateComparor)) {
                    notItemFlag = true;
                    continue;
                } else {
                    notItemFlag = false;
                }

                // 0指标计算当前时间 1指标计算当日开始 2指标计算当日结束
                final String dateStaticsType = itemDate.getString("dateStaticsType");
                // + -
                final String dateOptionType = itemDate.getString("dateOptionType");
                // 值
                final String dateValue = itemDate.getString("dateValue");
                // 0天 1时 2分
                final String dateInterval = itemDate.getString("dateInterval");

                final Calendar c = Calendar.getInstance();
                if ("1".equals(dateStaticsType)) {
                    c.set(Calendar.HOUR_OF_DAY, 0);
                    c.set(Calendar.MINUTE, 0);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);
                } else if ("2".equals(dateStaticsType)) {
                    c.set(Calendar.HOUR, 23);
                    c.set(Calendar.MINUTE, 59);
                    c.set(Calendar.SECOND, 59);
                    c.set(Calendar.MILLISECOND, 999);
                }

                int removeTime = 0;
                if ("0".equals(dateOptionType)) {
                    removeTime = Integer.valueOf(dateValue);
                } else if ("1".equals(dateOptionType)) {
                    removeTime = -Integer.valueOf(dateValue);
                }

                if ("0".equals(dateInterval)) {
                    c.add(Calendar.DATE, removeTime);
                } else if ("1".equals(dateInterval)) {
                    c.add(Calendar.HOUR, removeTime);
                } else if ("2".equals(dateInterval)) {
                    c.add(Calendar.MINUTE, removeTime);
                }

                final Date searchTime = c.getTime();
                if ("0".equals(dateComparor)) {// >
                    bqb.must(QueryBuilders.rangeQuery(dateField).gt(searchTime.getTime()));
                } else if ("1".equals(dateComparor)) {// <
                    bqb.must(QueryBuilders.rangeQuery(dateField).lt(searchTime.getTime()));
                } else if ("2".equals(dateComparor)) {// =
                    bqb.must(QueryBuilders.matchPhraseQuery(dateField, searchTime.getTime()));
                }
            }

            if (notItemFlag) {
                // 没有过滤字段查询处理
                final Calendar c = Calendar.getInstance();
                // 数据到达时
                if ("1".equals(businessTrigger.getCalculationType())) {
                    c.add(Calendar.MINUTE, -1);// 数据到达时，前推一分钟
                } else {
                    if (businessTrigger.getCycleType().equals("0")) {// 分
                        c.add(Calendar.MINUTE, -Integer.valueOf(businessTrigger.getCycleValue()));
                    } else if (businessTrigger.getCycleType().equals("1")) {// 时
                        c.add(Calendar.HOUR, -Integer.valueOf(businessTrigger.getCycleValue()));
                    } else {// 天
                        c.add(Calendar.DATE, -Integer.valueOf(businessTrigger.getCycleValue()));
                    }
                }
                bqb.must(QueryBuilders.rangeQuery(dateField).gte(c.getTime().getTime()).lte(new Date().getTime()));
            }

            // 组装请求
            final SearchRequestBuilder searchRequestBuilder = elasticSearchClient.getClient().prepareSearch(indexName)
                    .setQuery(bqb);// 注意：filtered能过滤查询和聚合，而postFilter只能过滤查询，对聚合没影响

            // 聚合处理
            searchRequestBuilder.addAggregation(AggregationBuilders.terms("agg").field(aggField).size(200)
            // .shardSize(0)
                    );

            // 发送请求
            response = searchRequestBuilder.setSize(0).get();
            if (response.getHits().getTotalHits() > 0L) {
                final Terms terms = response.getAggregations().get("agg");
                for (final Bucket b : terms.getBuckets()) {
                    map.put(String.valueOf(b.getKey()), b.getDocCount());
                }
            }
            map.put("response.hits.total", response.getHits().getTotalHits());
        } catch (final Exception e) {
            log.error("ElasticSearchServiceImpl(termsMap)异常", e);
        }
        return map;
    }
}
