package com.aspire.mirror.zabbixintegrate.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @BelongsProject: mirror-common
 * @BelongsPackage: com.aspire.mirror.elasticsearch.server.config
 * @Author: longfeng
 * @CreateTime: 2020-05-19 10:27
 * @Description: ${Description}
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "HwMonitorSync")
public class HWProperties {
	private int tag_type;
	private String url;
	//private String om_ip;

	//private int port;

	private String token_url;
	private String monitor_type_url;
	private String monitor_indicatorsrel_url;

	private String monitor_indicators_url;


	private String region_url;

	private String device_url;

	private String monitor_data_url;

	private int regionPageSize;

	private int devicePageSize;

	private String oc_username;
	private String oc_password;

	private String interval;

	private int intervalMinute;

	private int delayMinute;

	private String tokenHeaderName;

	private int indicatorCount;

	private int deviceCount;

	private String idcType;
	
	private String regionClassName;
	
	private String deviceClassName;


	private String toKafkaTopic;
	private String toKafkaTopic_uint;
	private int reMinute;//重跑任务的查询时间
	
	private int syncMinute;//重跑任务的间隔时间
	
	private String device_source;
	
	
}
