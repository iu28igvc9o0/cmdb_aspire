package com.aspire.ums.cmdb.config;

/**
 * @Title: DruidProperties.java
 * @Package com.migu.tsg.microservice.atomicservice.order.config
 * @Description: Druid db configuration 
 * Copyright: Copyright (c) 2017
 * Company:咪咕文化 tsg
 * 
 * @author tsg-frank
 * @date 2017年5月23日 下午3:32:38
 * @version V1.0
 */

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "druid")
@Data
public class DruidProperties {

    private String url;

    private String username;

    private String password;

    private int initialSize;

    private int minIdle;

    private int maxActive;

    private long maxWait;

    private String validationQuery;

    private boolean testOnBorrow;

    private boolean removeAbandoned;

    private int removeAbandonedTimeout;

    private boolean logAbandoned;

    private boolean poolPreparedStatements;

    private int maxPoolPreparedStatementPerConnectionSize;

    private String filters;

    private String connectionProperties;
}
