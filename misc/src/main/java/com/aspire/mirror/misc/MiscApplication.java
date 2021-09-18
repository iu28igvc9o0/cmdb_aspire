package com.aspire.mirror.misc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@EnableEurekaClient
@EnableScheduling
@SpringBootApplication
public class MiscApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiscApplication.class, args);
	}

}

