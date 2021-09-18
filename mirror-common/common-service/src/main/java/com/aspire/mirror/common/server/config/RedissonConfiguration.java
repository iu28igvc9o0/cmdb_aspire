package com.aspire.mirror.common.server.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableConfigurationProperties(RedissonProperties.class)
@ConditionalOnProperty("redisson.config-file.yaml")
public class RedissonConfiguration {
	@Autowired
	private RedissonProperties redissonProps;

	public Config configJson() throws IOException {
		File file = ResourceUtils.getFile(redissonProps.getConfigFile().getJson());
		return Config.fromJSON(file);
	}
	
	public Config configYaml() throws IOException {
		File outConfigFile = new File(redissonProps.getConfigFile().getYaml());
		// 优先从当前目录下取配置
		Resource resource = null;
		if (outConfigFile.exists()) { 
			resource = new FileSystemResource(redissonProps.getConfigFile().getYaml());
		} else {
			resource = new ClassPathResource(redissonProps.getConfigFile().getYaml());
		}
		InputStream input = null;
		try {
			input = resource.getInputStream();
			return Config.fromYAML(input);
		} finally {
			if(null!=input) {
				input.close();
			}
			
		}
	}
	
	@Bean
	@ConditionalOnMissingBean
	public Config config() throws IOException {
		if (!StringUtils.isEmpty(redissonProps.getConfigFile().getJson())) {
			return configJson();
		} else if (!StringUtils.isEmpty(redissonProps.getConfigFile().getYaml())) {
			return configYaml();
		} else {
			throw new RuntimeException("please offer the config file by json/yaml");
		}
	}
	
	@Bean(destroyMethod="shutdown")
	@ConditionalOnMissingBean
	public RedissonClient redissonClient(Config config) throws IOException {
		log.info("create RedissonClient, config is : {}", config.toJSON());
		return Redisson.create(config);
	}
}
