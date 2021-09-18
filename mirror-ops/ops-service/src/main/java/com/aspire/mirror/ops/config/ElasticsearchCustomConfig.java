package com.aspire.mirror.ops.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aspire.mirror.ops.util.PropertyMapper;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: ElasticsearchCustomConfig
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年7月7日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Configuration
public class ElasticsearchCustomConfig {
	
	@Bean
	@ConfigurationProperties(prefix = "elasticsearch.rest")
	RestClientProperties loadRestClientProperties() {
		return new RestClientProperties();
	}
	
	@Bean
	public RestClientBuilder elasticsearchRestClientBuilder(RestClientProperties properties) {
		HttpHost[] hosts = properties.getUris().stream().map(HttpHost::create).toArray(HttpHost[]::new);
		RestClientBuilder builder = RestClient.builder(hosts);
		
		PropertyMapper map = PropertyMapper.get();
		map.from(properties::getUsername).whenHasText().to((username) -> {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			Credentials credentials = new UsernamePasswordCredentials(properties.getUsername(),
					properties.getPassword());
			credentialsProvider.setCredentials(AuthScope.ANY, credentials);
			});
		builder.setMaxRetryTimeoutMillis(RestClientBuilder.DEFAULT_MAX_RETRY_TIMEOUT_MILLIS * 10);
		builder.setRequestConfigCallback((requestConfigBuilder) -> {
			requestConfigBuilder.setConnectTimeout(properties.getConnectionTimeout());
			requestConfigBuilder.setSocketTimeout(properties.getReadTimeout());
			return requestConfigBuilder;
		});
		return builder;
	}
	
	@Bean
	public RestHighLevelClient elasticsearchRestHighLevelClient(RestClientBuilder restClientBuilder) {
		return new RestHighLevelClient(restClientBuilder);
	}
}
