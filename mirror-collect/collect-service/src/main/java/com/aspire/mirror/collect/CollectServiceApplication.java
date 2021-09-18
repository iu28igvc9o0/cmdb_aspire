package com.aspire.mirror.collect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 应用入口    <br/>
 * Project Name:collect-service
 * File Name:CollectServiceApplication.java
 * Package Name:com.aspire.mirror.collect
 * ClassName: CollectServiceApplication <br/>
 * date: 2018年9月6日 下午3:41:03 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class CollectServiceApplication {
    /**
     * 服务启动入口
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(CollectServiceApplication.class, args);
    }
}
