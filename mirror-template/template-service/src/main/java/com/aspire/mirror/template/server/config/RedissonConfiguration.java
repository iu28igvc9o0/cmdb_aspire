package com.aspire.mirror.template.server.config;

import com.aspire.mirror.template.server.config.redisson.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "middleware.configuration.switch.redis", havingValue="true")
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
