package com.migu.tsg.microservice.atomicservice.composite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
* Composite服务main入口
* Project Name:composite-service
* File Name:CompositeServiceApplication.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite
* ClassName: CompositeServiceApplication <br/>
* date: 2017年9月2日 上午12:04:17 <br/>
* Composite服务main入口
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ServletComponentScan
public class CompositeServiceApplication extends SpringBootServletInitializer {
    @Autowired
    private ObjectMapper mapper;

    public static void main(String[] args) {
        SpringApplication.run(CompositeServiceApplication.class, args);
    }

    /**
     * restful client template
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    
    @EventListener(classes = ApplicationReadyEvent.class)
    public void init(ApplicationReadyEvent event) {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
    
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    	return builder.sources(CompositeServiceApplication.class);
    }
}
