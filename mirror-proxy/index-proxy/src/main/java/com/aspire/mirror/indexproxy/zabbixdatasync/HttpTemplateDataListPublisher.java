package com.aspire.mirror.indexproxy.zabbixdatasync;

import com.aspire.mirror.indexproxy.domain.*;
import com.aspire.mirror.indexproxy.util.ExecuteUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.indexadapt.adapt
 * 类名称:    HttpStandardIndexDataListPublisher.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/7/2 20:08
 * 版本:      v1.0
 */
@Slf4j
@Component
@Qualifier("http")
//@ConditionalOnProperty(name = "middleware.configuration.switch.kafka", havingValue = "false")
public class HttpTemplateDataListPublisher implements TemplateDataListPublisher {

    @Value("${template_sync_url:}")
    private String templateSyncUrl;

    @Value("${host_sync_url:}")
    private String hostSyncUrl;

    @Value("${item_sync_url:}")
    private String itemSyncUrl;

    @Override
    public void publishStandardIndexDataList(List<MonitorTemplateRecord> indexDataList, String
            proxyIdentity) {
        RestTemplate template = new RestTemplate();
        if (CollectionUtils.isEmpty(indexDataList)) {
            log.info("As the template dataList is empty for proxy {}, "
                    + "skip the publishing to http.", proxyIdentity);
            return;
        }
        log.info("Begin to publish {} template to http url {}.", indexDataList.size(), templateSyncUrl);
        for (int i=0; i< indexDataList.size(); i++) {
            MonitorTemplateRecord idxItem = indexDataList.get(i);
            template.postForEntity(templateSyncUrl, idxItem, Object.class);
        }

//        template.postForEntity(templateSyncUrl, indexDataList, Object.class);
    }

    @Override
    public void publishHostDataList(List<MonitorHost> hostList, String proxyIdentity) {
        RestTemplate template = new RestTemplate();
        if (CollectionUtils.isEmpty(hostList)) {
            log.info("As the host dataList is empty for proxy {}, "
                    + "skip the publishing to http.", proxyIdentity);
            return;
        }
        log.info("Begin to publish {} host to http url {}.", hostList.size(), hostSyncUrl);
//        for (MonitorHostResponse idxItem : hostList) {
        ExecuteUtil.partitionRun(hostList, 500, (eachPair) -> this.publishPartitonList(eachPair));
//        }
    }

    @Override
    public void publishItemData(List<MonitorItemDetailResponse> itemList, String proxyIdentity) {
        if (itemList.isEmpty()) {
            log.info("As the item dataList is empty for proxy {}, "
                    + "skip the publishing to http.", proxyIdentity);
            return;
        }
        log.info("Begin to publish {} item to http url {}.", itemList.size(), itemSyncUrl);

        ExecuteUtil.partitionRun(itemList, 10000, (eachPair) -> this.publishItemPartitonList(eachPair, proxyIdentity));
    }

    private void publishItemPartitonList(Pair<String, List<MonitorItemDetailResponse>> itemPair, String proxyIdentity) {
        RestTemplate template = new RestTemplate();
        ItemSyncRequest itemSyncRequest = new ItemSyncRequest();
        itemSyncRequest.setItemIdMap(itemPair.getRight().stream().collect(Collectors.toMap(MonitorItemDetailResponse::getItemId, MonitorItemDetailResponse::getTemplateId, (key1, key2) -> key2)));
        itemSyncRequest.setIndexType(itemPair.getLeft());
        itemSyncRequest.setProxyIdentity(proxyIdentity);
        template.postForEntity(itemSyncUrl, itemSyncRequest, Object.class);
    }

    private void publishPartitonList(Pair<String, List<MonitorHost>> eachPair) {
        RestTemplate template = new RestTemplate();
        HostBatchCreateRequest batchCreateRequest = new HostBatchCreateRequest();
        List<MirrorHostVO> mirrorHostVOList =  MirrorHostVO.parseFromZabbixHost(eachPair.getRight());
        batchCreateRequest.setHostList(mirrorHostVOList);
        batchCreateRequest.setIndexType(eachPair.getLeft());
        template.postForEntity(hostSyncUrl, batchCreateRequest, Object.class);
    }
}
