package com.aspire.mirror.log.server.helper;

import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.regex.Pattern;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.server.helper
 * 类名称:    ElasticSearchHelper.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/14 15:37
 * 版本:      v1.0
 */
@Component
public class ElasticSearchHelper {

    private static final Logger logger = Logger.getLogger(ElasticSearchHelper.class);

    @Value("${elasticsearch.ip}")
    private String es_host;

//    @Value("${elasticsearch.port}")
//    private Integer es_port;

    @Value("${elasticsearch.nodeType}")
    private String nodeType;

    @Value("${elasticsearch.clusterName}")
    private String clusterName;

    private static TransportClient client;

    public synchronized TransportClient getClient() {
        try {
            if (null == client) {
                System.setProperty("es.set.netty.runtime.available.processors", "false");
                if ("tribe".equals(nodeType.trim())) {
                    String[] hostPort = es_host.split(":");
                    String ip = hostPort[0].trim();
                    client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(
                            new TransportAddress(InetAddress.getByName(ip),  Integer.parseInt(hostPort[1])));
                } else {
                    Settings settings = Settings.builder().put("cluster.name", clusterName.trim()).build();
                    client = new PreBuiltTransportClient(settings);
                    String[] nodes = es_host.split(",");
                    for (String node : nodes) {
                        String[] hostPort = node.split(":");
                        String ip = hostPort[0].trim();
                        client.addTransportAddress( new TransportAddress(InetAddress.getByName(ip), Integer.parseInt(hostPort[1])));
                    }
                }
                logger.info("elasticsearch客户端新建成功！");
            }
        } catch (Exception e) {
            logger.error("创建elasticsearch客户端实例失败...", e);
        }
        return client;
    }
    

	/*
	 * private static String ipMatch(String ip){ if(ip.matches(
	 * "^([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|22[0-3])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}$"
	 * )) { return ip; } return "0"; }
	 */
}
