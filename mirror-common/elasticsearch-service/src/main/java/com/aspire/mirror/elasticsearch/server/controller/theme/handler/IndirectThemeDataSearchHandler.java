package com.aspire.mirror.elasticsearch.server.controller.theme.handler;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.handler
 * 类名称:    IndirectThemeDataSearchHandler.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/12/14 14:57
 * 版本:      v1.0
 */
public class IndirectThemeDataSearchHandler extends ThemeDataSearchHandler {
    Logger logger = LoggerFactory.getLogger(IndirectThemeDataSearchHandler.class);

    @Override
    SearchResponse handlerRequest(TransportClient transportClient, String bizCode, String themeCode, String host, String indexName,
                                  RangeQueryBuilder
                                          range) {
        final BoolQueryBuilder bqb = QueryBuilders
                        .boolQuery()
                        .must(range)
                        .must(QueryBuilders.queryStringQuery("theme_code:" + themeCode
                                .toUpperCase().trim()
                                + " AND success:*"))
                        .must(QueryBuilders.queryStringQuery("biz_code.keyword:"
                                + bizCode.toUpperCase().trim()))
                        .must(QueryBuilders.queryStringQuery("host.keyword:"
                                + (null == host ? "*" : host)));
        logger.debug("IndirectThemeDataSearchHandler elasticsearch operate BoolQueryBuilder is {}", bqb);
        return transportClient
                .prepareSearch(indexName)
                .setQuery(bqb).setSize(1)
                .addSort("success.keyword", SortOrder.ASC).addSort("@timestamp", SortOrder.DESC).get();
    }
}
