package com.aspire.mirror.elasticsearch.server.controller.theme.handler;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.handler
 * 类名称:    ThemeCodeThemeDataSearchHandler.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/12/14 14:04
 * 版本:      v1.0
 */
public class ThemeCodeThemeDataSearchHandler extends ThemeDataSearchHandler {

    Logger logger = LoggerFactory.getLogger(ThemeCodeThemeDataSearchHandler.class);

    @Override
    SearchResponse handlerRequest(TransportClient transportClient, String bizCode, String themeCode, String host, String indexName, RangeQueryBuilder
            range) {
        final BoolQueryBuilder bqb = QueryBuilders
                        .boolQuery()
                        .must(range)
                        .must(QueryBuilders.queryStringQuery("theme_code:" + themeCode
                                .toUpperCase().trim()
                                + " AND success:*"))
                        .must(QueryBuilders.queryStringQuery("host.keyword:"
                                + (null == host ? "*" : host)));
        logger.debug("ThemeCodeThemeDataSearchHandler elasticsearch operate BoolQueryBuilder is {}", bqb);
        if (StringUtils.isEmpty(bizCode) && !StringUtils.isEmpty(themeCode)) {
            return transportClient
                    .prepareSearch(indexName)
                    .setQuery(bqb).setSize(1)
                    .addSort("success.keyword", SortOrder.ASC).addSort("@timestamp", SortOrder.DESC).get();
        } else {
            return getHandler().handlerRequest(transportClient, bizCode, themeCode, host, indexName, range);
        }
    }
}
