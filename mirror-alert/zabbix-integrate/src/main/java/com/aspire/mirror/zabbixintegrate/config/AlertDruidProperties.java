package com.aspire.mirror.zabbixintegrate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "alertDruid")
public class AlertDruidProperties {
    private String url;
    private String username;
    private String password;
    private int initialSize;
    private int minIdle;
    private int maxActive;
    private long maxWait;
    private String validationQuery;
    private boolean testOnBorrow;
    private List<String> mapperLocations;
}
