package com.aspire.mirror.indexadapt.adapt;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

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
@ConditionalOnProperty(name="middleware.configuration.switch.kafka", havingValue="false")
public class HttpStandardIndexDataListPublisher implements StandardIndexDataListPublisher {

    @Value("${proxy_standard_index_url}")
    private String proxyStandardIndexUrl;

    @Override
    public void publishStandardIndexDataList(List<IndexDataListAdapter.StandardIndex> indexDataList, String
            adapterIdentity) {
        RestTemplate template = new RestTemplate();
        if (CollectionUtils.isEmpty(indexDataList)) {
            log.info("As the apapted index dataList is empty for adapter {}, "
                    + "skip the publishing to http.", adapterIdentity);
            return;
        }
        log.info("Begin to publish {} standardIndexs to http url {}.", indexDataList.size(), proxyStandardIndexUrl);
//        for (IndexDataListAdapter.StandardIndex idxItem : indexDataList) {
//            template.postForEntity(proxyStandardIndexUrl, idxItem, Object.class);
//        }
        template.postForEntity(proxyStandardIndexUrl, indexDataList, Object.class);
}
}
