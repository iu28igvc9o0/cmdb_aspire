package com.aspire.mirror.elasticsearch.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.server.config
 * @Author: baiwenping
 * @CreateTime: 2020-05-19 10:27
 * @Description: ${Description}
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "elasticsearch.cluster")
public class ClusterProperties {
    private boolean open;
    private List<String> indexs;
    private Map<String, List<String>> queryKeywords;
    private Map<String, List<String>> indexKeywords;
}
