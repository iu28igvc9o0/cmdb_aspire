package com.aspire.cdn.esdatawrap.config;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientBuilderCustomizer;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import de.dentrassi.crypto.pem.PemKeyStoreProvider;

/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: ElasticsearchCustomConfig
 * <p/>
 *
 * 类功能描述: ES 自定义配置
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月7日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Configuration(proxyBeanMethods=false)
class ElasticsearchCustomConfig {
	private static final String PEM_FILE = "root-ca.pem";

	@Bean
	public RestClientBuilder elasticsearchRestClientBuilder(RestClientProperties properties,
			ObjectProvider<RestClientBuilderCustomizer> builderCustomizers) {
		HttpHost[] hosts = properties.getUris().stream().map(HttpHost::create).toArray(HttpHost[]::new);
		RestClientBuilder builder = RestClient.builder(hosts);
		
		PropertyMapper map = PropertyMapper.get();
		map.from(properties::getUsername).whenHasText().to((username) -> {
			CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			Credentials credentials = new UsernamePasswordCredentials(properties.getUsername(),
					properties.getPassword());
			credentialsProvider.setCredentials(AuthScope.ANY, credentials);
			builder.setHttpClientConfigCallback((httpClientBuilder) -> 
				httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider).setSSLContext(loadTrustSsl()));
			});
		builder.setRequestConfigCallback((requestConfigBuilder) -> {
			map.from(properties::getConnectionTimeout).whenNonNull().asInt(Duration::toMillis)
					.to(requestConfigBuilder::setConnectTimeout);
			map.from(properties::getReadTimeout).whenNonNull().asInt(Duration::toMillis)
					.to(requestConfigBuilder::setSocketTimeout);
			return requestConfigBuilder;
		});
		builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
		return builder;
	}
	
	private SSLContext loadTrustSsl() {
		try {
			ClassPathResource pemFile = new ClassPathResource(PEM_FILE);
			KeyStore keyStore = KeyStore.getInstance("PEM", new PemKeyStoreProvider());
			try (InputStream fis = pemFile.getInputStream()) {
				keyStore.load(fis, null);
			}
			SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(keyStore, null);
			return sslBuilder.build();
		} catch (Exception e) {
			throw new RuntimeException("Error when load trust pem file.", e);
		}
	}
	
	@Bean 
	public RestClientBuilderCustomizer restClientCustomer() {
		return new RestClientBuilderCustomizer() {
			@Override
			public void customize(RestClientBuilder builder) {
//				HttpHeaders headers = new HttpHeaders();
//				headers.setBasicAuth("admin", "8UHLr8_86_gg");     
//				String auth = headers.getFirst(HttpHeaders.AUTHORIZATION);
//				Header[] defaultHeaders = new Header[] {new BasicHeader(HttpHeaders.AUTHORIZATION, auth)};
//				builder.setDefaultHeaders(defaultHeaders);
				builder.setMaxRetryTimeoutMillis(RestClientBuilder.DEFAULT_MAX_RETRY_TIMEOUT_MILLIS * 10);
			}
		}; 
	}
}
