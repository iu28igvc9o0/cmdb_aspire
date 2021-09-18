package com.aspire.mirror.theme.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 业务主题服务启动类
 *
 * 项目名称:  mirror平台
 * 包:      com.aspire.mirror.alert.server
 * 类名称:    TemplateServiceApplication.java
 * 类描述：   业务主题服务启动类
 * 创建人：   JinSu
 * 创建时间:  2018-7-26 16:02:34
 * 版本:      v1.0
 */
@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class, KafkaAutoConfiguration.class})
@EnableScheduling
public class BizThemeServiceApplication extends SpringBootServletInitializer {
    /**
     * 服务启动入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(BizThemeServiceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(BizThemeServiceApplication.class);
    }
}
