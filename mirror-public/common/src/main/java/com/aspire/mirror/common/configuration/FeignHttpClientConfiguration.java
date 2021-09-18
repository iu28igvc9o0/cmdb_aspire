package com.aspire.mirror.common.configuration;

import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aspire.mirror.common.configuration.FeignHttpClientConfiguration.FeignProperties;

import feign.Request;
import feign.Retryer;

@Configuration
@EnableConfigurationProperties(FeignProperties.class)
public class FeignHttpClientConfiguration {
	@Bean
	public HttpClient client(FeignProperties feignProperties) {
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setMaxConnTotal(feignProperties.getMaxConnTotal());
		builder.setMaxConnPerRoute(feignProperties.getMaxConnPerRoute());
		builder.evictIdleConnections(feignProperties.getMaxIdleTime().longValue(), TimeUnit.SECONDS);
		return builder.build();
	}

	@Bean
	Request.Options feignOptions(FeignProperties feignProperties) {
		return new Request.Options(/** connectTimeoutMillis **/
				feignProperties.getConnectTimeoutMillis(), /** readTimeoutMillis **/
				feignProperties.getReadTimeoutMillis());
	}

	@Bean
	Retryer feignRetryer(FeignProperties feignProperties) {
		return feignProperties.retry ? new Retryer.Default() : Retryer.NEVER_RETRY;
	}

	@ConfigurationProperties(prefix = "feign.httpclient", ignoreUnknownFields = true)
	public static class FeignProperties {
		private Integer maxConnTotal = 1000;
		private Integer maxConnPerRoute = 50;
		private Long maxIdleTime = 60L;
		private Integer connectTimeoutMillis = 10 * 1000;
		private Integer readTimeoutMillis = 120 * 1000;
		private Boolean retry = true;

		public Integer getConnectTimeoutMillis() {
			return connectTimeoutMillis;
		}

		public void setConnectTimeoutMillis(Integer connectTimeoutMillis) {
			this.connectTimeoutMillis = connectTimeoutMillis;
		}

		public Integer getReadTimeoutMillis() {
			return readTimeoutMillis;
		}

		public void setReadTimeoutMillis(Integer readTimeoutMillis) {
			this.readTimeoutMillis = readTimeoutMillis;
		}

		public Boolean getRetry() {
			return retry;
		}

		public void setRetry(Boolean retry) {
			this.retry = retry;
		}

		public Integer getMaxConnTotal() {
			return maxConnTotal;
		}

		public void setMaxConnTotal(Integer maxConnTotal) {
			this.maxConnTotal = maxConnTotal;
		}

		public Integer getMaxConnPerRoute() {
			return maxConnPerRoute;
		}

		public void setMaxConnPerRoute(Integer maxConnPerRoute) {
			this.maxConnPerRoute = maxConnPerRoute;
		}

		public Long getMaxIdleTime() {
			return maxIdleTime;
		}

		public void setMaxIdleTime(Long maxIdleTime) {
			this.maxIdleTime = maxIdleTime;
		}

	}
}
