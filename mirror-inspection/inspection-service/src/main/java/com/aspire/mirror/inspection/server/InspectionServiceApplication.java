package com.aspire.mirror.inspection.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.ltsopensource.spring.boot.annotation.EnableJobClient;

/**
 * 流水线服务启动类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.inspection.server
 * 类名称:    InspectionServiceApplication.java
 * 类描述：   巡检服务启动类
 * 创建人：   JinSu
 * 创建时间:  2018-7-26 16:02:34
 * 版本:      v1.0
 * @author   JinSu
 */
@EnableDiscoveryClient
@EnableJobClient
@EnableFeignClients
@EnableEurekaClient
@EnableTransactionManagement(proxyTargetClass=true)
@SpringBootApplication(exclude = {KafkaAutoConfiguration.class})
public class InspectionServiceApplication {
    /**
     * 服务启动入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(InspectionServiceApplication.class, args);
    }
}
