package com.aspire.mirror.alert.server.kafka;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.server.clientservice.elasticsearch.MonitorKpiServiceClient;
import com.aspire.mirror.alert.server.dao.kpi.KpiCommonMapper;
import com.aspire.mirror.alert.server.vo.kpi.MonitorKpiVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.kafka.monitor
 * @Author: baiwenping
 * @CreateTime: 2020-04-22 17:21
 * @Description: ${Description}
 */
@Slf4j
@Component
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
public class MonitorKpiConsumer {

    private static final String DB = "db";
    private static final String ES = "es";

    @Autowired
    private MonitorKpiServiceClient monitorKpiServiceClient;
    @Autowired
    private KpiCommonMapper kpiCommonMapper;

    @KafkaListener(topics = "${kafka.topic.topic_monitor_kpi:TOPIC_MONITOR_KPI}")
    public void listen(ConsumerRecord<?, String> cr) throws Exception {
        Long time1 = System.currentTimeMillis();
        String value = cr.value();
        MonitorKpiVo monitorKpiVo = JSON.parseObject(value, MonitorKpiVo.class);
        String dataType = monitorKpiVo.getDataType();
        String dataTable = monitorKpiVo.getDataTable();
        if (DB.equalsIgnoreCase(dataType)) {
            kpiCommonMapper.insert(dataTable, monitorKpiVo.getFieldList(), monitorKpiVo.getDataList());
        } else if (ES.equalsIgnoreCase(dataType)) {
            monitorKpiServiceClient.insertBatch(dataTable, "doc", monitorKpiVo.getDataList());
        }

        log.info("consumer monitor kpi size are : {}; use {} ms", monitorKpiVo.getDataList().size(), (System.currentTimeMillis() - time1));
    }
}
