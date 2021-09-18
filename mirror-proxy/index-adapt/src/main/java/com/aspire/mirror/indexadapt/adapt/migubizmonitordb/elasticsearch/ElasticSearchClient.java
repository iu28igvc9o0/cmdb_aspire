package com.aspire.mirror.indexadapt.adapt.migubizmonitordb.elasticsearch;

import java.net.InetAddress;

import javax.annotation.PreDestroy;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * ElasticSearch连接类
 * 
 * @author lupeng
 *
 */
@Component
public class ElasticSearchClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchClient.class);

    // public static AppConfig appConfig = AppConfig.getInstance();

    @Value("${indexAdapter.migubizmonitorDb.elasticsearch.host}")
    private String es_host;
//    @Value("${indexAdapter.migubizmonitorDb.elasticsearch.port}")
//    private Integer es_port;
    @Value("${indexAdapter.migubizmonitorDb.elasticsearch.node.type}")
    private String nodeType;
    @Value("${indexAdapter.migubizmonitorDb.elasticsearch.cluster.name}")
    private String clusterName;

    private TransportClient client;

    public synchronized TransportClient getClient() {
        try {
            if (null == client) {
                if ("tribe".equals(nodeType.trim())) {  // 部落节点不需要指定集群名称
                    String[] hostPort = es_host.split(":");
                    return new PreBuiltTransportClient(Settings.EMPTY)
                            .addTransportAddress(new TransportAddress(InetAddress.getByName(hostPort[0].trim()), Integer.parseInt(hostPort[1])));                    //.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(es_host), es_port));
                } else { // 单集群需要指定集群名称
                    Settings settings = Settings.builder().put("cluster.name", clusterName.trim()).build();
                    client = new PreBuiltTransportClient(settings);
                    String[] nodes = es_host.split(",");
                    //添加多节点
                    for (String node : nodes) {
                        String[] hostPort = node.split(":");
                        client.addTransportAddress( new TransportAddress(InetAddress.getByName(hostPort[0].trim()), Integer.parseInt(hostPort[1])));
                    }
                }
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
