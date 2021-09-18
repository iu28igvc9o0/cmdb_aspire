/**
 * 
 */
package com.aspire.mirror.theme.server.biz.helper;

import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * ElasticSearch工具类
 * 
 * @author lupeng
 *
 */
@Component
public class ElasticSearchHelper {

	private static final Logger logger = Logger.getLogger(ElasticSearchHelper.class);

	@Value("${elasticsearch.ip}")
	private String es_host;
	
	@Value("${elasticsearch.port}")
	private Integer es_port;
	
	@Value("${elasticsearch.nodeType}")
	private String nodeType;
	
	@Value("${elasticsearch.clusterName}")
	private String clusterName;

	private static TransportClient client;

	public synchronized TransportClient getClient() {
		try {
			if (null == client) {
//				System.setProperty("es.set.netty.runtime.available.processors", "false");
//				if ("tribe".equals(nodeType.trim())) { // 部落节点不需要指定集群名称
//					client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(
//							new InetSocketTransportAddress(InetAddress.getByName(es_host), es_port));
//				} else { // 单集群需要指定集群名称
//					Settings settings = Settings.builder().put("cluster.name", clusterName.trim()).build();
//					client = new PreBuiltTransportClient(settings).addTransportAddress(
//							new InetSocketTransportAddress(InetAddress.getByName(es_host.trim()), es_port));
//				}
//				logger.info("elasticsearch客户端新建成功！");
			}
		} catch (Exception e) {
			logger.error("创建elasticsearch客户端实例失败...", e);
		}
		return client;
	}

}
