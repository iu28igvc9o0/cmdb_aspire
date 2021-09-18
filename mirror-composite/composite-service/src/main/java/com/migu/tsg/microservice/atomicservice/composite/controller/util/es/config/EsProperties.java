/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
 */
package com.migu.tsg.microservice.atomicservice.composite.controller.util.es.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * elastisearch 配置文件 项目名称: 微服务运维平台（log-service 模块） 包:
 * com.migu.tsg.microservice.monitor.log.config 类名称: EsProperties.java 类描述:
 * elastisearch 配置文件 创建人: sunke 创建时间: 2017年8月31日 上午10:18:36
 */
@ConfigurationProperties(prefix = "log")
public class EsProperties {

    private String hosts;

    private String maxTotalConnectionPerRoute;

    private String maxTotalConnection;

    private String multiThreaded;

    private String readTimeout;

    private String userName;

    private String password;

    private String needAuth;
    /**
     * 分隔符
     */
    public static final String HOST_CLUSTER_SPLIT_CHAR = ",";
    /**
     * 开头
     */
    public static final String HOST_SUPPORT_PROTOCOL = "http://";
    /**
     * 分隔符
     */
    public static final String HOST_SPLIT_CHAR = ":";
    /**
    * isCluster:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @return boolean
     */
    public boolean isCluster() {
        return StringUtils.isNotBlank(hosts) && StringUtils.contains(hosts, HOST_CLUSTER_SPLIT_CHAR);
    }
    /**
    * getHosts:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @return String
     */
    public String getHosts() {
        return hosts;
    }
    /**
    * setHosts:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @param hosts 地址
     */
    public void setHosts(String hosts) {
        this.hosts = hosts;
    }
    /**
    * getMaxTotalConnectionPerRoute:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @return String
     */
    public String getMaxTotalConnectionPerRoute() {
        return maxTotalConnectionPerRoute;
    }
    /**
    * setMaxTotalConnectionPerRoute:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @param maxTotalConnectionPerRoute 最大连接数
     */
    public void setMaxTotalConnectionPerRoute(String maxTotalConnectionPerRoute) {
        this.maxTotalConnectionPerRoute = maxTotalConnectionPerRoute;
    }
    /**
    * getMaxTotalConnection:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @return String
     */
    public String getMaxTotalConnection() {
        return maxTotalConnection;
    }
    /**
    * setMaxTotalConnection:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @param maxTotalConnection 最大总连接数
     */
    public void setMaxTotalConnection(String maxTotalConnection) {
        this.maxTotalConnection = maxTotalConnection;
    }
    /**
    * getMultiThreaded:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @return String
     */
    public String getMultiThreaded() {
        return multiThreaded;
    }
    /**
    * setMultiThreaded:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @param multiThreaded 总线程
     */
    public void setMultiThreaded(String multiThreaded) {
        this.multiThreaded = multiThreaded;
    }
    /**
    * getReadTimeout:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @return String
     */
    public String getReadTimeout() {
        return readTimeout;
    }
    /**
    * setReadTimeout:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @param readTimeout 读取超时时间
     */
    public void setReadTimeout(String readTimeout) {
        this.readTimeout = readTimeout;
    }
    /**
    * getUserName:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @return String
     */
    public String getUserName() {
        return userName;
    }
    /**
    * setUserName:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
    * getPassword:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @return String
     */
    public String getPassword() {
        return password;
    }
    /**
    * setPassword:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
    * getNeedAuth:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @return String
     */
    public String getNeedAuth() {
        return needAuth;
    }
    /**
    * setNeedAuth:(这里用一句话描述这个方法的作用). <br/>
    * 作者： jiangfuyi
    * @param needAuth 是否需要校验
     */
    public void setNeedAuth(String needAuth) {
        this.needAuth = needAuth;
    }
}
