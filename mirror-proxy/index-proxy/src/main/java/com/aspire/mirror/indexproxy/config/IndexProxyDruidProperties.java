package com.aspire.mirror.indexproxy.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "indexProxyDb")
public class IndexProxyDruidProperties {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int initialSize;
    private int minIdle;
    private int maxActive;
    private long maxWait;
    private String validationQuery;
    private boolean testOnBorrow;
    private List<String> mapperLocations;
}
