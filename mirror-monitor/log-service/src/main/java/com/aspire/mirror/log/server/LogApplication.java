package com.aspire.mirror.log.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 类说明 日志服务引导 项目名称: 微服务 包: com.migu.tsg.microservice.monitor.log
 * 类名称: LogServiceApplication.java
 * 类描述: 日志服务引导 创建人:
 * jiangfuyi 创建时间: 2017年7月27日
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
@EnableScheduling
//@EnableFeignClients(basePackages = { "com.migu.tsg.microservice.monitor.log.client"})
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class LogApplication {

    /**
     * 日志服务引导
     * @param args
     *            启动参数
     */
    public static void main(final String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }
}
