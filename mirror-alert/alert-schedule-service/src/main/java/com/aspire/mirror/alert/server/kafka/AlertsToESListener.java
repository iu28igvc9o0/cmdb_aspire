package com.aspire.mirror.alert.server.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.clientservice.elasticsearch.MonitorKpiServiceClient;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;

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
public class AlertsToESListener {
    @Autowired
    private MonitorKpiServiceClient monitorKpiServiceClient;

    /**
    * 历史告警数据入ES. <br/>
    * @auther baiwenping
    * @Description
    * @Date 16:51 2020/2/20
    * @Param [cr]
    * @return void
    **/
    @KafkaListener(topics = "topic_alert_datas",group = "consumer_alert_ES")
    public void listenAlertsHis(ConsumerRecord<?, String> cr) {
        String value = cr.value();
        try {
            JSONObject jsonObject = JSON.parseObject(value);
            if (jsonObject == null) {
                return;
            }
            String index = jsonObject.getString("index");
            if (StringUtils.isNotEmpty(index)) {
                jsonObject.remove("index");
                int year = DateUtil.getYear(new Date());
                monitorKpiServiceClient.insert(index + "_" + year, "doc", jsonObject);
            }
        } catch (Exception e) {
            log.error("插入es错误", e);
        }
    }
}
