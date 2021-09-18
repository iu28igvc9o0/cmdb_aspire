package com.migu.tsg.microservice.atomicservice.composite.config;

import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 屏蔽Feign的URL映射
 * Project Name:composite-service
 * File Name:WebConfig.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.config
 * ClassName: FeignRequestMappingHandlerMapping <br/>
 * date: 2018年1月19日 下午2:44:33 <br/>
 * 屏蔽Feign的URL映射
 * @author pengguihua
 * @version WebConfig
 * @since JDK 1.6
 */
@Configuration
public class IgnoreFeignMappingConfig extends WebMvcRegistrationsAdapter {
    
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new IgnoreFeignRequestMappingHandlerMapping();
    }  
    
     private static class IgnoreFeignRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
         @Override
         protected boolean isHandler(Class<?> beanType) {
             return super.isHandler(beanType) 
                     && !AnnotatedElementUtils.hasAnnotation(beanType, FeignClient.class);
         }
     }
}
