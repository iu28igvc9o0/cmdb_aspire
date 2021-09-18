package com.aspire.mirror.theme.server.biz.process;

import com.aspire.mirror.theme.server.biz.handler.ElasticsearchHandler;
import com.aspire.mirror.theme.server.biz.handler.themeData.ThemeCacheHolder;
import com.aspire.mirror.theme.server.biz.helper.GrokHelper;
import com.aspire.mirror.theme.server.biz.helper.Reactor;
import com.aspire.mirror.theme.server.clientservice.cmdb.CmdbServiceClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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
@ConditionalOnProperty(value = "middleware.configuration.switch.kafka", havingValue = "true")
public class LogThemeDataProcess {
//    @Autowired
//    private List<IMonitorEventProcessor> processorList;

//    @Autowired
//    private BizThemeDao bizThemeDao;
//
//    @Autowired
//    private BizThemeDimDao bizThemeDimDao;

    @Autowired
    private GrokHelper grokHelper;

    @Autowired
    private ElasticsearchHandler elasticsearchHandler;

    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Autowired(required = false)
    private CmdbServiceClient cmdbService;

    @Autowired
    private ThemeCacheHolder themeCacheHolder;

    Logger logger = LoggerFactory.getLogger(LogThemeDataProcess.class);

    @KafkaListener(topics = "${log_theme_data_topic:topic_logThemeData}")
    public void listen(ConsumerRecord<?, String> cr) throws Exception {
        logger.info("LogThemeDataProcess got kafka msg with offset {}", cr.offset());
        String themeData = cr.value();

        //多线程处理
        Reactor.getInstance().submit(new LogThemeTask(cmdbService, themeCacheHolder, grokHelper, elasticsearchHandler,
                redissonClient,themeData));

    }
//        MonitorEventRecord event = JsonUtil.jacksonConvert(, MonitorEventRecord.class);
//        for (IMonitorEventProcessor processor : processorList) {
//            try {
//                if (processor.isAware(event)) {
//                    processor.processEvent(event);
//                }
//            } catch (Throwable e) {
//                log.error("Error when process the monitor event.", e);
//            }
//        }
}
