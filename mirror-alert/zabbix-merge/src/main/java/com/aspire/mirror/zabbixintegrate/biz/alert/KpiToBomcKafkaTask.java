package com.aspire.mirror.zabbixintegrate.biz.alert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.zabbixintegrate.util.SuyanMonitorHelper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class KpiToBomcKafkaTask {

    @Value("${KpiToBomcKafka.toKafkaTopic}")
    private String toKafkaTopic;
    @Autowired
    private SuyanMonitorHelper suyanMonitorHelper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "${KpiToBomcKafka.fromKafkaTopic}",containerFactory = "batchFactory")
    private void consumeHistoryMonitor(List<ConsumerRecord<?, String>> list) {
        Map<String,Object> map;
        long l = System.currentTimeMillis();
        long ls = l / 1000;
        for (ConsumerRecord<?, String> record : list) {
            try {
                String value = record.value();
                JSONObject obj = JSON.parseObject(value);
                if(null==obj) {
                    continue;
                }
                String item = obj.getString("item");
                if (StringUtils.isEmpty(item)) {
                    continue;
                }
                if (!obj.containsKey("value")) {
                    continue;
                }
                map = suyanMonitorHelper.getmetricNameByZabbixKey(item, obj);
                if (map == null || map.isEmpty()) {
                    continue;
                }
                map.put("datetime", ls);
                kafkaTemplate.send(toKafkaTopic,JSONObject.toJSONString(map));
            }catch(Exception e) {
                log.error("解析kafka消息报错:{}",record);
                log.error("解析kafka消息报错",e);
            }

        }
        log.info("transfer kpi to bomc format size are : {}, use {}ms.", list.size(), (System.currentTimeMillis() - l));
    }
}
