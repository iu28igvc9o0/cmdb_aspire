package com.aspire.mirror.theme.server.biz.process;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.theme.server.biz.handler.ElasticsearchHandler;
import com.aspire.mirror.theme.server.biz.handler.themeData.ThemeCacheHolder;
import com.aspire.mirror.theme.server.biz.helper.GrokHelper;
import com.aspire.mirror.theme.server.biz.helper.Reactor;
import com.aspire.mirror.theme.server.clientservice.cmdb.CmdbServiceClient;
import com.aspire.mirror.theme.server.clientservice.elasticsearch.ThemeDataServiceClient;
import com.aspire.mirror.theme.server.dao.LogThemeFlushTimeDao;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 日志主题数据处理
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.process
 * 类名称:    LogThemeDataProcess.java
 * 类描述:    日志主题数据处理
 * 创建人:    JinSu
 * 创建时间:  2018/12/21 16:37
 * 版本:      v1.0
 */
@Component
@ConditionalOnProperty(value = "middleware.configuration.switch.kafka", havingValue = "false")
public class ESLogThemeDataProcess {
    protected volatile String flushTime = new String();       // 系统中缓存的扫描游标

    @Autowired
    private GrokHelper grokHelper;

    @Autowired
    private ElasticsearchHandler elasticsearchHandler;

    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Autowired(required = false)
    private CmdbServiceClient cmdbServiceClient;

    @Autowired
    private ThemeCacheHolder themeCacheHolder;


//    @Autowired
//    private ElasticSearchHelper elasticSearchClient;

    @Autowired
    private ThemeDataServiceClient themeDataService;

    @Autowired
    private LogThemeFlushTimeDao logThemeFlushTimeDao;

    Logger logger = LoggerFactory.getLogger(ESLogThemeDataProcess.class);

    //    @KafkaListener(topics = "${log_theme_data_topic:topic_logThemeData}")

    @Scheduled(cron = "30 */1 * * * ?")
    @Transactional
    public void listen() throws Exception {
        if (StringUtils.isEmpty(flushTime)) {
            flushTime = logThemeFlushTimeDao.getFlushTime();
        }
        Date lastTime = null;
        String timeString = themeDataService.getLastNewLogTime();
        if (timeString != null) {
            lastTime = DateUtil.getDate(timeString, DateUtil.ELASTIC_TIME_FORMAT, "GMT");
        }
//        String indexName = "log-bizmoniter*";
//        Date startTime = DateUtil.getDate(flushTime, DateUtil.DATE_TIME_MS_FORMAT);
        List<String> themeDataList = themeDataService.getNewLogThemeData(flushTime, DateUtil.format(lastTime, DateUtil.DATE_TIME_MS_FORMAT));
        // 多线程处理

        for (String themeData : themeDataList) {
            Reactor.getInstance().submit(new LogThemeTask(cmdbServiceClient, themeCacheHolder, grokHelper,
                    elasticsearchHandler,
                    redissonClient, themeData));
        }
        flushTime = DateUtil.format(lastTime, DateUtil.DATE_TIME_MS_FORMAT);
        logThemeFlushTimeDao.setFlushTime(flushTime);
    }
}
