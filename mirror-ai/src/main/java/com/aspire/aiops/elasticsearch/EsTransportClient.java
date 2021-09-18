package com.aspire.aiops.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aspire.aiops.utils.ResourceUtil;

import javax.annotation.PreDestroy;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Properties;

public class EsTransportClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsTransportClient.class);
    private static final Properties props = ResourceUtil.loadResource("es.properties");

    //@Value("${elasticsearch.host}")
    private String[] es_host = props.getProperty("es.host").split(",");
    //@Value("${elasticsearch.port}")
    private Integer es_port = Integer.parseInt(props.getProperty("es.port"));
    // @Value("${elasticsearch.node.type}")
    private String nodeType = props.getProperty("es.node.type");
    //@Value("${elasticsearch.cluster.name}")
    private String clusterName = props.getProperty("es.cluster.name");

    private org.elasticsearch.client.transport.TransportClient client;

    public synchronized TransportClient getClient() {
        try {
            if (null == client) {
                if ("tribe".equals(nodeType.trim())) {  // 部落节点不需要指定集群名称
                    client = new PreBuiltTransportClient(Settings.EMPTY);
                    for (String host:es_host){
                        client.addTransportAddress(new InetSocketTransportAddress(Inet4Address.getByName(host.trim()), es_port));
                    }
                    //.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(es_host), es_port));
                } else { // 单集群需要指定集群名称
                    final Settings settings = Settings.builder()
                            .put("cluster.name", clusterName.trim())
                            .build();
                    client = new PreBuiltTransportClient(settings);
                    //添加多节点
                    for (String host:es_host){
                        client.addTransportAddress(new InetSocketTransportAddress(Inet4Address.getByName(host.trim()), es_port));
                    }
                    //.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.12.70.42"), es_port));
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
