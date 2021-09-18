package com.migu.tsg.microservice.atomicservice.composite.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.migu.tsg.microservice.atomicservice.composite.controller.util.RequestWrapperFilter;

@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean bodyReaderFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        RequestWrapperFilter filter = new RequestWrapperFilter();
        registrationBean.setFilter(filter);

        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);

        return registrationBean;
    }
}
