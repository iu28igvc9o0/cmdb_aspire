package com.aspire.mirror.scada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 组态服务启动类
 *
 * 项目名称:  mirror平台
 * 包:      com.aspire.mirror.scada
 * 类名称:    DhServiceApplication.java
 * 类描述：   模板服务启动类
 * 创建人：   pengfeng
 * 创建时间:  2019-5-24 16:02:34
 * 版本:      v1.0
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement(proxyTargetClass = true)
public class ScadaServiceApplication {
    /**
     * 服务启动入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(ScadaServiceApplication.class, args);
    }
}
