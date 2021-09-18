package com.aspire.mirror.thirdparty.alertnotify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class RtzAlertNotifyApplication {
	
    public static void main(String[] args) throws Exception {
    	SpringApplication.run(RtzAlertNotifyApplication.class, args);
    }
}
