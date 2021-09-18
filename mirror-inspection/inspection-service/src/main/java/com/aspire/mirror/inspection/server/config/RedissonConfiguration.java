package com.aspire.mirror.inspection.server.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aspire.mirror.inspection.server.config.redisson.ClusterServersConfig;
import com.aspire.mirror.inspection.server.config.redisson.IRedisonConfigContentAware;
import com.aspire.mirror.inspection.server.config.redisson.MasterSlaveServersConfig;
import com.aspire.mirror.inspection.server.config.redisson.ReplicatedServersConfig;
import com.aspire.mirror.inspection.server.config.redisson.SentinelServersConfig;
import com.aspire.mirror.inspection.server.config.redisson.SingleServerConfig;

import lombok.extern.slf4j.Slf4j;

/**
* Redisson自动配置    <br/>
* Project Name:inspection-service
* File Name:RedissonConfiguration.java
* Package Name:com.aspire.mirror.inspection.server.config
* ClassName: RedissonConfiguration <br/>
* date: 2018年9月3日 下午4:20:35 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Configuration
//@ConditionalOnExpression("${middleware.configuration.switch.redis:false}")
public class RedissonConfiguration {
	@Autowired
	private IRedisonConfigContentAware redisonConfig;

	@Bean
	@ConditionalOnMissingBean
	public Config config() throws IOException {
		String redisJson = redisonConfig.formatConfig2JsonContent();
		log.info(redisJson);
		return Config.fromJSON(redisJson);
	}
	
	@Bean(destroyMethod="shutdown")
	@ConditionalOnMissingBean
	public RedissonClient redissonClient(Config config) throws IOException {
		log.info("create RedissonClient, config is : {}", config.toJSON());
		return Redisson.create(config);
	}
	
	@Bean
	@Qualifier("singleServerConfig")
	@ConfigurationProperties(prefix="redisson")
	@ConditionalOnProperty(name="redisson.activeConfig", havingValue="singleServerConfig")
	public SingleServerConfig singleServerConfig() {
		return new SingleServerConfig();
	}
	
	@Bean
	@Qualifier("clusterServersConfig")
	@ConfigurationProperties(prefix="redisson")
	@ConditionalOnProperty(name="redisson.activeConfig", havingValue="clusterServersConfig")
	public ClusterServersConfig clusterServersConfig() {
		return new ClusterServersConfig();
	}
	
	@Bean
	@Qualifier("masterSlaveServersConfig")
	@ConfigurationProperties(prefix="redisson")
	@ConditionalOnProperty(name="redisson.activeConfig", havingValue="masterSlaveServersConfig")
	public MasterSlaveServersConfig masterSlaveServersConfig() {
		return new MasterSlaveServersConfig();
	}
	
	@Bean
	@Qualifier("sentinelServersConfig")
	@ConfigurationProperties(prefix="redisson")
	@ConditionalOnProperty(name="redisson.activeConfig", havingValue="sentinelServersConfig")
	public SentinelServersConfig sentinelServersConfig() {
		return new SentinelServersConfig();
	}
	
	@Bean
	@Qualifier("replicatedServersConfig")
	@ConfigurationProperties(prefix="redisson")
	@ConditionalOnProperty(name="redisson.activeConfig", havingValue="replicatedServersConfig")
	public ReplicatedServersConfig replicatedServersConfig() {
		return new ReplicatedServersConfig();
	}
}
