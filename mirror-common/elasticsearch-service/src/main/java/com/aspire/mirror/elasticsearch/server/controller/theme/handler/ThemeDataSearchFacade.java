package com.aspire.mirror.elasticsearch.server.controller.theme.handler;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.impl
 * 类名称:    ThemeDataSearchFacade.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/12/14 15:07
 * 版本:      v1.0
 */
@Component
public class ThemeDataSearchFacade {

    @Autowired
    private TransportClient transportClient;


    public SearchResponse handlThemeDataSearch(String bizCode, String themeCode, String host, String indexName, RangeQueryBuilder range) {
        ThemeDataSearchHandler directHandler = new DirectThemeDataSearchHandler();
        ThemeDataSearchHandler bizCodeHandler = new BizCodeThemeDataSearchHandler();
        ThemeDataSearchHandler themeCodeHandler = new ThemeCodeThemeDataSearchHandler();
        ThemeDataSearchHandler indirectHandler = new IndirectThemeDataSearchHandler();

        directHandler.setNextHandler(bizCodeHandler);
        bizCodeHandler.setNextHandler(themeCodeHandler);
        themeCodeHandler.setNextHandler(indirectHandler);

        return directHandler.handlerRequest(transportClient, bizCode, themeCode, host, indexName, range);

    }
}
