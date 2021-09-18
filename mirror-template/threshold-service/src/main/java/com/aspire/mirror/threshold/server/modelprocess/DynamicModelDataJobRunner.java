package com.aspire.mirror.threshold.server.modelprocess;

import com.aspire.mirror.threshold.server.domain.DhDynamicMode;
import com.aspire.mirror.threshold.server.domain.StandardDynamicModel;
import com.aspire.mirror.threshold.server.domain.StandardDynamicModelExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    DynamicModelDataJobRunner
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/4 15:04
 * 版本:      v1.0
 */
@Component
@Slf4j
public class DynamicModelDataJobRunner {
    @Autowired
    private DynamicModelDataProcessor dynamicModelDataProcessor;

    // 默认每天玩8点开始同步
    @Scheduled(cron = "${dynamicmodel.sync.cron:0 11 5 * * ?}")
//    @Scheduled(cron = "30 */1 * * * ?")
    public void runJob() {
        log.info("DynamicModelDataJobRunner[runJob] is beginning");
        // 获取动态阈值对象列表  解析动态阈值对象 入库动态阈值对象
        dynamicModelDataProcessor.handlerDynamicModel();
//        // 解析动态阈值对象
//        Map<String, List<StandardDynamicModelExt>> modelListMap = dynamicModelDataProcessor.parseDynamicDataList(modelMap);
//        // 入库动态阈值对象
//        dynamicModelDataProcessor.publishStandardDynamicModel(modelListMap);
    }
}
