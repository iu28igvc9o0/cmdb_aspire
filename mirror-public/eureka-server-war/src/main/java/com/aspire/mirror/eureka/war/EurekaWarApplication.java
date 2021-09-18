package com.aspire.mirror.eureka.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 套了成皮 用来做war 哇
 *
 * @author pikaqiu
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaWarApplication extends SpringBootServletInitializer {
    /**
     * @param args 参数
     */
    public static void main(final String[] args) {
        SpringApplication.run(EurekaWarApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(EurekaWarApplication.class);
    }

}
