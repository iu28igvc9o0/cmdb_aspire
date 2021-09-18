package com.aspire.mirror.elasticsearch.server.controller.theme.handler;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.RangeQueryBuilder;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.handler
 * 类名称:    ThemeDataSearchHandler.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/12/14 13:35
 * 版本:      v1.0
 */
public abstract class ThemeDataSearchHandler {

    private ThemeDataSearchHandler handler;

//    protected ElasticSearchHelper elasticSearchClient;

    public ThemeDataSearchHandler getHandler() {
        return handler;
    }

    public void setNextHandler(ThemeDataSearchHandler nextHandler) {
        handler = nextHandler;
    }

    abstract SearchResponse handlerRequest(TransportClient transportClient, String bizCode, String themeCode, String host, String indexName,
                                           RangeQueryBuilder range);
}
