package com.aspire.mirror.alert.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 告警服务启动类
 * <p>
 * 项目名称:  mirror平台
 * 包:      com.aspire.mirror.alert.server
 * 类名称:    TemplateServiceApplication.java
 * 类描述：   告警服务启动类
 * 创建人：   JinSu
 * 创建时间:  2018-7-26 16:02:34
 * 版本:      v1.0
 */
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication(exclude = {KafkaAutoConfiguration.class})
@EnableAsync
public class AlertAppApplication extends SpringBootServletInitializer {
    /**
     * 服务启动入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(AlertAppApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AlertAppApplication.class);
    }
}
