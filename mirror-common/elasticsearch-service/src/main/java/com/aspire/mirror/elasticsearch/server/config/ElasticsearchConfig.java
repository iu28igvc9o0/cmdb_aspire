package com.aspire.mirror.elasticsearch.server.config;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * @author baiwp
 * @title: ElasticsearchConfig
 * @projectName mirror-common
 * @description: TODO
 * @date 2019/6/2011:10
 */
@Configuration
public class ElasticsearchConfig {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${elasticsearch.cluster-name}")
    private String clusterName;
    @Value("${elasticsearch.cluster-nodes}")
    private String clusterNodes;
    @Value("${elasticsearch.sniff: false}")
    private boolean sniff;
    private static final String CLUSTER_NODES_SPLIT_SYMBOL = ",";
    private static final String HOST_PORT_SPLIT_SYMBOL = ":";

    @Bean
    public TransportClient getTransportClient() {
        logger.info("elasticsearch init.");
        logger.info("elasticsearch init.clusterName is {},  clusterNodes is :{}", clusterName, clusterNodes);
        if (StringUtils.isEmpty(clusterName)) {
            throw new RuntimeException("elasticsearch.cluster-name is empty.");
        }
        if (StringUtils.isEmpty(clusterNodes)) {
            throw new RuntimeException("elasticsearch.cluster-nodes is empty.");
        }
        try {
            Settings settings = Settings.builder().put("cluster.name", clusterName.trim())
                    .put("client.transport.sniff", sniff)
                    .put("transport.connections_per_node.recovery", 4)
                    .put("transport.connections_per_node.bulk", 6)
                    .put("transport.connections_per_node.reg", 12)
                    .put("transport.connections_per_node.state", 4)
                    .put("processors", 20)
                    .build();
            TransportClient transportClient = new PreBuiltTransportClient(settings);
            String[] clusterNodeArray = clusterNodes.trim().split(CLUSTER_NODES_SPLIT_SYMBOL);
            for (String clusterNode : clusterNodeArray) {
                String[] clusterNodeInfoArray = clusterNode.trim().split(HOST_PORT_SPLIT_SYMBOL);
                TransportAddress transportAddress = new TransportAddress(InetAddress.getByName(clusterNodeInfoArray[0]),
                        Integer.parseInt(clusterNodeInfoArray[1]));
                transportClient.addTransportAddress(transportAddress);
            }
            logger.info("elasticsearch init success.");
            return transportClient;
        } catch (Exception e) {
            logger.error("elasticsearch init fail.", e);
            throw new RuntimeException("elasticsearch init fail.");
        }
    }
}
