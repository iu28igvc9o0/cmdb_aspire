package com.aspire.mirror.common.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {
	private ConfigFile configFile = new ConfigFile();
	
	@Data
	public class ConfigFile {
		private String	json;	// json格式配置文件
		private String	yaml;	// yaml格式配置文件
	}
}
