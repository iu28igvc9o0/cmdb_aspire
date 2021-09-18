package com.aspire.ums.cmdb.collect.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryRuleEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Data
@Slf4j
public class DiscoveryFromKafka implements Runnable {

    private AutoDiscoveryRuleEntity ruleEntity;
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void run() {
        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("rule_id", ruleEntity.getId());
            sendObj.put("start_scan_ip", ruleEntity.getStartScanIp());
            sendObj.put("end_scan_ip", ruleEntity.getEndScanIp());
            sendObj.put("room", ruleEntity.getRoom());
            sendObj.put("discovery_type", ruleEntity.getDiscoveryType());
            sendObj.put("discovery_param", ruleEntity.getDiscoveryParam());
            String requestMessage = JSON.toJSONString(sendObj, SerializerFeature.WriteMapNullValue);
            if (log.isInfoEnabled()) {
                log.info("Send kafka content -> {}", requestMessage);
            }
            kafkaTemplate.send("cmdb_discovery_info_request", requestMessage);
            if (log.isInfoEnabled()) {
                log.info("Send kafka ok.");
            }
        } catch (Exception e) {
            log.error("Make discovery task error. -> {}", e.getMessage() ,e);
        }

    }

    public DiscoveryFromKafka(KafkaTemplate<String, String> kafkaTemplate, AutoDiscoveryRuleEntity ruleEntity) {
        this.kafkaTemplate = kafkaTemplate;
        this.ruleEntity = ruleEntity;
    }
}
