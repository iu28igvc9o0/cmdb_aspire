package com.aspire.mirror.template.server.biz.helper;

import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetAddress;

/**
 * ElasticSearch连接类
 * 
 * @author lupeng
 *
 */
@Component
public class ElasticSearchClient {

    private static final Logger LOGGER = Logger.getLogger(ElasticSearchClient.class);

    // public static AppConfig appConfig = AppConfig.getInstance();

    @Value("${elasticsearch.host}")
    private String es_host;
    @Value("${elasticsearch.port}")
    private Integer es_port;
    @Value("${elasticsearch.node.type}")
    private String nodeType;
    @Value("${elasticsearch.cluster.name}")
    private String clusterName;

    private TransportClient client;

    public synchronized TransportClient getClient() {
        try {
            if (null == client) {

//                if ("tribe".equals(nodeType.trim())) { // 部落节点不需要指定集群名称
//                    client = new PreBuiltTransportClient(Settings.EMPTY)
//                            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(es_host), es_port));
//                } else { // 单集群需要指定集群名称
//                    final Settings settings = Settings.builder().put("cluster.name", clusterName.trim()).build();
//                    client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(
//                            InetAddress.getByName(es_host.trim()), es_port));
//                }
            }
        } catch (final Exception e) {
            LOGGER.error("创建elasticsearch客户端实例失败", e);
        }
        return client;
    }
    
    @PreDestroy
    public void close() {
        if (null != client) {
            try {
                client.close();
            } catch (final Exception e) {
                LOGGER.error("elasticsearch客户端连接关闭失败", e);
            }
            client = null;
        }
    }
}
