package com.aspire.mirror.threshold.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    ThresholdServiceApplication
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/11/4 14:15
 * 版本:      v1.0
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
@EnableScheduling
public class ThresholdServiceApplication {
    /**
     * 服务启动入口
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(ThresholdServiceApplication.class, args);
    }
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(TemplateServiceApplication.class);
//    }
}
