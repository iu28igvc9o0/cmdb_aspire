package com.aspire.mirror.ops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.github.ltsopensource.spring.boot.annotation.EnableJobClient;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsServiceApplication
 * <p/>
 *
 * 类功能描述: OpsServiceApplication
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月18日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@EnableDiscoveryClient
@EnableScheduling
@EnableJobClient
@EnableFeignClients
@EnableTransactionManagement(proxyTargetClass = true)
@SpringBootApplication(exclude = {KafkaAutoConfiguration.class})
public class OpsServiceApplication {

	public static void main(String[] args) {
        SpringApplication.run(OpsServiceApplication.class, args);
    }
}
