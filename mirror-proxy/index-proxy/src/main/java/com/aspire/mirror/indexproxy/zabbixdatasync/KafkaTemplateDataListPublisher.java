package com.aspire.mirror.indexproxy.zabbixdatasync;

import com.aspire.mirror.indexproxy.domain.MonitorHost;
import com.aspire.mirror.indexproxy.domain.MonitorItemDetailResponse;
import com.aspire.mirror.indexproxy.domain.MonitorTemplateRecord;
import com.aspire.mirror.indexproxy.util.ExecuteUtil;
import com.aspire.mirror.indexproxy.util.JsonUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.indexadapt.adapt
 * 类名称:    KafkaStandardIndexDataListPublisher.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/7/2 20:01
 * 版本:      v1.0
 */
@Slf4j
//@Component
@Primary
//@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
class KafkaTemplateDataListPublisher implements TemplateDataListPublisher {
    public static final String			TEMPALTE_SYNC_INDEX_TOPIC	= "topic_templateSync";

    public static final String			HOST_SYNC_INDEX_TOPIC	= "topic_hostSync";

    public static final String			ITEM_SYNC_INDEX_TOPIC	= "topic_itemSync";
    @Value("${local.kafka.partionCount:3}")
    private Integer kafkaPartionCount;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publishStandardIndexDataList(List<MonitorTemplateRecord> indexDataList, String proxyIdentity) {
        if (CollectionUtils.isEmpty(indexDataList)) {
            log.info("As the template dataList is empty for adapter {}, "
                    + "skip the publishing to kafka topic.", proxyIdentity);
            return;
        }
        log.info("Begin to publish {} template to kafka topic {}.", indexDataList.size(), TEMPALTE_SYNC_INDEX_TOPIC);
        for (MonitorTemplateRecord idxItem : indexDataList) {
            int partion = Math.abs(idxItem.getTemplateId().hashCode()) % kafkaPartionCount;
            this.kafkaTemplate.send(TEMPALTE_SYNC_INDEX_TOPIC, partion, JsonUtil.toJacksonJson(idxItem));
        }
    }

    @Override
    public void publishHostDataList(List<MonitorHost> hostList, String proxyIdentity) {
        if (CollectionUtils.isEmpty(hostList)) {
            log.info("As the host dataList is empty for adapter {}, "
                    + "skip the publishing to kafka topic.", proxyIdentity);
            return;
        }
        log.info("Begin to publish {} host to kafka topic {}.", hostList.size(), HOST_SYNC_INDEX_TOPIC);
        for (MonitorHost idxItem : hostList) {
            int partion = Math.abs(idxItem.getHostid().hashCode()) % kafkaPartionCount;
            this.kafkaTemplate.send(HOST_SYNC_INDEX_TOPIC, partion, JsonUtil.toJacksonJson(idxItem));
        }
    }

    @Override
    public void publishItemData(List<MonitorItemDetailResponse> itemList, String proxyIdentity) {
        if (CollectionUtils.isEmpty(itemList)) {
            log.info("As the  itemList is empty for adapter {}, "
                    + "skip the publishing to kafka topic.", proxyIdentity);
            return;
        }
        log.info("Begin to publish {} host to kafka topic {}.", itemList.size(), ITEM_SYNC_INDEX_TOPIC);
        ExecuteUtil.partitionRun(itemList, 1000, (eachPair) -> this.publishItemPartitonList(eachPair, proxyIdentity));
//        for (MonitorItemDetailResponse idxItem : itemList) {
//            int partion = Math.abs(idxItem.getItemId().hashCode()) % kafkaPartionCount;
//
//            this.kafkaTemplate.send(ITEM_SYNC_INDEX_TOPIC, partion, JsonUtil.toJacksonJson(idxItem));
//        }
    }

    private void publishItemPartitonList(Pair<String, List<MonitorItemDetailResponse>> eachPair, String proxyIdentity) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(proxyIdentity, eachPair.getRight().stream().collect(Collectors.toMap(MonitorItemDetailResponse::getItemId, MonitorItemDetailResponse::getTemplateId, (key1, key2) -> key2)));
        this.kafkaTemplate.send(ITEM_SYNC_INDEX_TOPIC, JsonUtil.toJacksonJson(map));
    }

}
