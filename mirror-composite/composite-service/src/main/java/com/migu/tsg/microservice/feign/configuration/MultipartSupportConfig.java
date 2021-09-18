package com.migu.tsg.microservice.feign.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

/**
 * 
* Feign的局部配置，用于支持上传/下载文件
* ref: https://github.com/OpenFeign/feign-form
* Project Name:composite-service
* File Name:MultipartSupportConfig.java
* Package Name:com.migu.tsg.microservice.feign.configuration
* ClassName: MultipartSupportConfig <br/>
* date: 2017年10月31日 下午5:36:15 <br/>
* Feign的局部配置，用于支持上传/下载文件
* @author Administrator
* @version 
* @since JDK 1.8
 */
@Configuration
public class MultipartSupportConfig {
    @Bean
    @Primary
    @Scope("prototype")
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder();
    }
}