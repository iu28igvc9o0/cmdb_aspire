package com.aspire.mirror.alert.server.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.biz.monthReport.AlertsScheduleBiz;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.kafka
 * @Author: baiwenping
 * @CreateTime: 2020-07-27 17:19
 * @Description: ${Description}
 */
@Slf4j
@Component
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
public class CmdbInstanceSyncListener {
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private AlertsScheduleBiz alertScheduleBiz;

    @KafkaListener(topics = "${kafka.topic.topic_cmdb_all_ip:SYNC_ALL_INSTANCE_IP_TO_ALERT}")
    public void listen(ConsumerRecord<?, String> cr) throws Exception {
        log.info("sync cmdb start!");
        try {
            String value = cr.value();
            JSONObject jsonObject = JSON.parseObject(value);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (jsonArray == null || jsonArray.size() == 0) {
                log.error("同步cmdb数据错误，数据实体为空!");
                return;
            }
            log.info("设备总条数：{},当前批次设备条数：{}", jsonObject.get("totalSize"), jsonArray.size());
            List<Map<String, Object>> list = Lists.newArrayList();
            for (int k = 0; k < jsonArray.size(); k++) {
                list.add(jsonArray.getJSONObject(k).getInnerMap());
            }
            // 获取模型字段
            List<AlertFieldVo> modelFromRedis =
                    alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_DEVICE_INSTANCE);
            if (modelFromRedis.isEmpty()) {
                log.error("cmdb instance model is not exist!");
                return;
            }
            List<String> keys = modelFromRedis.stream().map(AlertFieldVo::getFieldCode).collect(Collectors.toList());
            Map<String, Object> ciMap = Maps.newHashMap();
            ciMap.put("fieldList", keys);
            alertScheduleBiz.insertCmdb(ciMap, list, modelFromRedis);
        } catch (Exception e) {
            log.error("sync cmdb instance error:", e);
        }
    }
}
