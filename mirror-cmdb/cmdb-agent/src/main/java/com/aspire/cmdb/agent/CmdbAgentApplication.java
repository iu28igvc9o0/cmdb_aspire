package com.aspire.cmdb.agent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.aspire.ums.cmdb.**", "com.aspire.cmdb.agent.**"},
        exclude = {KafkaAutoConfiguration.class})
@EnableFeignClients(basePackages = {"com.aspire.ums.cmdb.**.client", "com.aspire.cmdb.agent.**.client"})
@EnableDiscoveryClient
@MapperScan({"com.aspire.cmdb.agent.**.mapper", "com.aspire.ums.cmdb.**.mapper"})
public class CmdbAgentApplication {
    public static void main(final String[] args) {
        SpringApplication.run(CmdbAgentApplication.class, args);
    }
}