package com.aspire.mirror.zabbixintegrate.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.server.config
 * @Author: baiwenping
 * @CreateTime: 2020-05-19 10:27
 * @Description: ${Description}
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "CloudSysSyncTask")
public class IdcTypeProperties {
    private String toKafkaTopic;
    private String toKafkaUintTopic;
    private String fromKafkaTopic;
    private String defaultTopic;
    private Map<String, String> kafkaTopicIdcTypeMap;
   
}
