package com.aspire.mirror.alert.server.kafka;

import com.aspire.mirror.alert.server.util.JsonUtil;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.helper.AlertsHandleV2Helper;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import com.aspire.mirror.alert.server.vo.alert.ZabbixAlertV2;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.kafka
 * @Author: baiwenping
 * @CreateTime: 2020-02-20 16:51
 * @Description: ${Description}
 */
@Slf4j
@Component
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
public class ZabbixSystemAlertV2Listener {
    @Autowired
    private AlertsHandleV2Helper alertHandleHelper;

    /**
    * 监听并处理系统告警. <br/>
    * @auther baiwenping
    * @Description
    * @Date 16:51 2020/2/20
    * @Param [cr]
    * @return void
    **/
    @KafkaListener(topics = "${kafka.topic.topic_system_alerts:TOPIC_SYSTEM_ALERTS}")
    public void listen(ConsumerRecord<?, String> cr) throws Exception {
        Long time1 = System.currentTimeMillis();
        ZabbixAlertV2 zbxAlert = JsonUtil.jacksonConvert(cr.value(), ZabbixAlertV2.class);
        AlertsV2Vo alert = alertHandleHelper.parseZabbix(zbxAlert);

        String alertType = "recover";
        //插入监控项和监控标准化描述
        if (AlertsV2Vo.ALERT_ACTIVE.equals(alert.getAlertType())) {
            alertType = "create";
        }

        if (alert.getObjectType().equals(AlertsV2Vo.OBJECT_TYPE_BIZ) && StringUtils.isEmpty(alert.getMoniObject())) {
            alert.setMoniObject("Application");
        }

        alertHandleHelper.handleAlert(alert);
        log.info("------------------consumer {} alert use : {} ms", alertType, (System.currentTimeMillis()-time1));
    }
}
