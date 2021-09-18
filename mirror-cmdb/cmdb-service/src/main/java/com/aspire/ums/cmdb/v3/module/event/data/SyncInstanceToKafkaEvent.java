package com.aspire.ums.cmdb.v3.module.event.data;

import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEventHandlerClass;
import com.aspire.ums.cmdb.cmic.payload.CmdbModuleKafkaEvent;
import com.aspire.ums.cmdb.cmic.service.CmdbModuleKafkaEventService;
import com.aspire.ums.cmdb.cmic.service.ICmdbModuleEventHandlerClassService;
import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SyncInstanceToKafkaEvent
 * Author:   hangfang
 * Date:     2020/9/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class SyncInstanceToKafkaEvent  extends AbstractModuleEvent {

    @Autowired
    private CmdbModuleKafkaEventService kafkaEventService;

    @Autowired
    private ICmdbModuleEventHandlerClassService handlerClassService;

    @Override
    public void initSpringBeans() {
        if (this.kafkaEventService == null) {
            this.kafkaEventService = SpringUtils.getBean(CmdbModuleKafkaEventService.class);
        }
        if (this.handlerClassService == null) {
            this.handlerClassService = SpringUtils.getBean(ICmdbModuleEventHandlerClassService.class);
        }
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("flag", "true");
        if (!handleData.containsKey("changeType")) {
            log.error("未传变更类型");
            return resultMap;
        }
//        List<String> changeTypeList = Arrays.asList(EventConst.EVENT_TYPE_DATA_INSERT, EventConst.EVENT_TYPE_DATA_UPDATE, EventConst.EVENT_TYPE_DATA_DELETE);
//        String changeType = handleData.get("changeType").toString();
//        if (!changeTypeList.contains(changeType)) {
//            log.error("变更类型[{}]不符合规范 （1-新增，2-更新，3-删除） ", changeType);
//            return resultMap;
//        }
        this.handlerEvent(moduleId, instanceId, handleData, "kafka");
        return resultMap;
    }


    private void handlerEvent(String moduleId, String instanceId, Map<String, Object> handleData, String eventType) {
        try {
            List<CmdbModuleKafkaEvent> kafkaEventList = kafkaEventService.listByModuleId(moduleId);
            if (kafkaEventList == null || kafkaEventList.size() == 0) {
                log.info("模型{}未绑定kafka事件");
                return;
            }
            for (CmdbModuleKafkaEvent event : kafkaEventList) {
                EventThreadUtils.FIXED_POOL.execute(() -> {
                    CmdbModuleEventHandlerClass handlerClass = handlerClassService.getById(event.getKafkaHandlerClassId());
                    log.info("开始执行事件[{}]", handlerClass.getHandlerName());
                    try {
                        AbstractModuleEvent abstractEvent;
                        Class clz = Class.forName(handlerClass.getHandlerClass());
                        Constructor constructor = clz.getConstructor();
                        abstractEvent = (AbstractModuleEvent) constructor.newInstance();
                        Map<String, Object> kafkaInfo = new HashMap<>();
                        kafkaInfo.put("changeType", handleData.get("changeType"));
                        kafkaInfo.put("topic", event.getTopic());
                        kafkaInfo.put("kafka_address", event.getKafkaAddress());
                        kafkaInfo.put("instanceDetail", handleData.get("instanceDetail"));
                        if (event.getPartition() != null) {
                            try {
                                Integer parsePartition = Integer.parseInt(event.getPartition());
                                if (parsePartition < 0 ) {
                                    throw new RuntimeException("分区值不应该为负值");
                                }
                                kafkaInfo.put("partition", parsePartition);
                            } catch (Exception e) {
                                log.error("分区[{}]不符合规范，必段为正整数");
                            }
                        }
                        abstractEvent.run(moduleId, instanceId, kafkaInfo);
                        log.info("执行事件[{}]结束", handlerClass.getHandlerName());
                    } catch (Exception e) {
                        e.getStackTrace();
                        log.info("执行事件[{}]异常，error：{}", handlerClass.getHandlerName(), e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            log.info("执行kafka事件异常，error：{}", e.getMessage());
        }

    }
}
