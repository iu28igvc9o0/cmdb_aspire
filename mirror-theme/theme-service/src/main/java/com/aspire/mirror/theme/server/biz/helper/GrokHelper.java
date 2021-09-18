package com.aspire.mirror.theme.server.biz.helper;

import io.krakens.grok.api.Grok;
import io.krakens.grok.api.GrokCompiler;
import io.krakens.grok.api.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.util
 * 类名称:    GrokHelper.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/12/26 15:42
 * 版本:      v1.0
 */
@Component
public class GrokHelper {
    Logger logger = LoggerFactory.getLogger(GrokHelper.class);

    GrokCompiler grokCompiler = GrokCompiler.newInstance();


    public Map<String, Object> parsePattern(String pattern, String logMsg) {
        logger.debug("GrokHelper[parsePattern] param pattern value is {}, logMsg value is {}", pattern, logMsg);
        grokCompiler.registerDefaultPatterns();
        final Grok grok = grokCompiler.compile(pattern);
        Match grokMatch = grok.match(logMsg);
        Map<String, Object> result = grokMatch.capture();
        logger.debug("GrokHelper[parsePattern] result value is {} ", result);
        return result;
    }

}
