package com.aspire.mirror.template.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 流水线服务启动类
 *
 * 项目名称:  mirror平台
 * 包:      com.aspire.mirror.template.server
 * 类名称:    TemplateServiceApplication.java
 * 类描述：   模板服务启动类
 * 创建人：   JinSu
 * 创建时间:  2018-7-26 16:02:34
 * 版本:      v1.0
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
public class TemplateServiceApplication extends SpringBootServletInitializer {
    /**
     * 服务启动入口
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(TemplateServiceApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(TemplateServiceApplication.class);
    }
}
