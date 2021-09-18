package com.aspire.mirror.indexadapt.adapt;

import static com.aspire.mirror.indexadapt.adapt.IndexAdaptConst.STANDARD_INDEX_TOPIC;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.indexadapt.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;
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
@Component
@Primary
@ConditionalOnExpression("${middleware.configuration.switch.kafka:true}")
class KafkaStandardIndexDataListPublisher implements StandardIndexDataListPublisher {

    @Value("${local.kafka.partionCount:3}")
    private Integer kafkaPartionCount;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publishStandardIndexDataList(List<IndexDataListAdapter.StandardIndex> indexDataList, String adapterIdentity) {
        if (CollectionUtils.isEmpty(indexDataList)) {
            log.info("As the apapted index dataList is empty for adapter {}, "
                    + "skip the publishing to kafka topic.", adapterIdentity);
            return;
        }
        log.info("Begin to publish {} standardIndexs to kafka topic {}.", indexDataList.size(), STANDARD_INDEX_TOPIC);
        for (IndexDataListAdapter.StandardIndex idxItem : indexDataList) {
            int partion = Math.abs(idxItem.getItemId().hashCode()) % kafkaPartionCount;
            this.kafkaTemplate.send(STANDARD_INDEX_TOPIC, partion, JsonUtil.toJacksonJson(idxItem));
        }
    }
}
