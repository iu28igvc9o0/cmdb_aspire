package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.service;

//import com.migu.tsg.microservice.atomicservice.composite.clientservice.resmgr.RegionServiceClient;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.config.EsProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Es 连接service 项目名称: 微服务运维平台（log-service 模块） 包:
 * com.migu.tsg.microservice.monitor.log.biz.impl 类名称: JestService.java 类描述: Es
 * 连接service 创建人: sunke 创建时间: 2017年8月12日 上午10:10:49
 */
@Service
@EnableConfigurationProperties(EsProperties.class)
public class JestService {

    private static final Logger LOG = LoggerFactory.getLogger(JestService.class);

    private final JestClientFactory factory = new JestClientFactory();

    private static final String type = "es";

    private final ConcurrentHashMap<String ,JestClient> clients = new ConcurrentHashMap<String ,JestClient>();

    @Autowired
    private EsProperties espro;

    /*@Autowired
    private RegionServiceClient regionService;*/

    /**
     * 获取es连接客户端
     * @return 返回es连接客户端
     */
    public JestClient getInstance(String regionId, boolean isManager) {
    	String keyName = StringUtils.isEmpty(regionId) ? "default" : regionId;
        synchronized (factory) {
            if (!clients.containsKey(keyName)) {
                factory.setHttpClientConfig(initBuilder(regionId, isManager));
                JestClient client = factory.getObject();
                clients.putIfAbsent(keyName, client);
                return client;
            }
        }
        return clients.get(keyName);
    }

    /**
     * initBuilder:初始化builder. <br/>
     * 作者： jiangfuyi
     * @return
     */
    private HttpClientConfig initBuilder(String regionId, boolean isManager) {

        final HttpClientConfig.Builder builder = new HttpClientConfig.Builder(formatUri(regionId, isManager));
        if (Boolean.valueOf(espro.getNeedAuth())) {
            builder.credentialsProvider(doAuth());
        }
        builder.multiThreaded(Boolean.valueOf(espro.getMultiThreaded()))
                .defaultMaxTotalConnectionPerRoute(Integer.parseInt(espro.getMaxTotalConnectionPerRoute()))
                .maxTotalConnection(Integer.parseInt(espro.getMaxTotalConnection()))
                .readTimeout(Integer.parseInt(espro.getReadTimeout()));
        return builder.build();
    }

    /**
     * formatUri:(这里用一句话描述这个方法的作用). <br/>
     * 作者： jiangfuyi
     * @return
     */
//    private List<String> formatUri() {
//        List<String> nodes = new ArrayList<>();
//        if (espro.isCluster()) {
//            for (final String host : espro.getHosts().split(EsProperties.HOST_CLUSTER_SPLIT_CHAR)) {
//                nodes.add(EsProperties.HOST_SUPPORT_PROTOCOL + host);
//            }
//        } else {
//            nodes.add(EsProperties.HOST_SUPPORT_PROTOCOL + espro.getHosts());
//        }
//        
//        LOG.info("JestService isCluster:{}, nodes:{}", espro.isCluster(), nodes);
//        return nodes;
//    }

    /**
     * doAuth:(这里用一句话描述这个方法的作用). <br/>
     * 作者： jiangfuyi
     * @return
     */
    private BasicCredentialsProvider doAuth() {
        final BasicCredentialsProvider basicAuth = new BasicCredentialsProvider();
        if (espro.isCluster()) {
            for (final String host : espro.getHosts().split(EsProperties.HOST_CLUSTER_SPLIT_CHAR)) {
                genBasicAuthInfo(basicAuth, host, espro.getUserName(), espro.getPassword());
            }
        } else {
            genBasicAuthInfo(basicAuth, espro.getHosts(), espro.getUserName(), espro.getPassword());
        }
        
        LOG.info("JestService basicAuth:{}", basicAuth);
        return basicAuth;
    }

    /**
     * genBasicAuthInfo:(这里用一句话描述这个方法的作用). <br/>
     * 作者： jiangfuyi
     * @param basicAuth 权限
     * @param hostInfo 地址
     * @param userName 用户名
     * @param password 密码
     */
    private void genBasicAuthInfo(BasicCredentialsProvider basicAuth, final String hostInfo, final String userName,
            final String password) {
        final String[] authInfo = hostInfo.split(EsProperties.HOST_SPLIT_CHAR);
        basicAuth.setCredentials(new AuthScope(authInfo[0], Integer.parseInt(authInfo[1])),
                new UsernamePasswordCredentials(userName, password));
    }

    /**
     * getEspro:(这里用一句话描述这个方法的作用). <br/>
     * 作者： jiangfuyi
     * @return EsProperties
     */
    public EsProperties getEspro() {
        return espro;
    }

    /**
     * formatUri:格式化uri. <br/>
     * 作者： jiangfuyi
     * @return
     */
    private List<String> formatUri(String regionId, boolean isManager) {
        final List<String> nodes = new ArrayList<>();
        if (isManager || StringUtils.isEmpty(regionId)) {
            if (espro.isCluster()) {
                for (final String host : espro.getHosts().split(EsProperties.HOST_CLUSTER_SPLIT_CHAR)) {
                    nodes.add(EsProperties.HOST_SUPPORT_PROTOCOL + host);
                }
            } else {
                nodes.add(EsProperties.HOST_SUPPORT_PROTOCOL + espro.getHosts());
            }
        } /*else {
            JSONObject urlInfo = regionService.getRegionMetaInfo(regionId, type);
            String regionHosts = urlInfo.getString("hosts");
            LOG.info("regionService.getRegionMetaInfo(regionId, type) returns : {}, hosts is : {}", urlInfo, regionHosts);
            if (StringUtils.isNotBlank(regionHosts) && StringUtils.contains(regionHosts, EsProperties.HOST_CLUSTER_SPLIT_CHAR)) {
                for (final String host : regionHosts.split(EsProperties.HOST_CLUSTER_SPLIT_CHAR)) {
                    nodes.add(EsProperties.HOST_SUPPORT_PROTOCOL + host);
                }
            } else {
                nodes.add(EsProperties.HOST_SUPPORT_PROTOCOL + regionHosts);
            }
        }*/
        LOG.info("es nodes is : {}", nodes);
        return nodes;
    }

}