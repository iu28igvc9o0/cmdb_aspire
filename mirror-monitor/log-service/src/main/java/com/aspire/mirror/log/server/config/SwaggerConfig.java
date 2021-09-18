package com.aspire.mirror.log.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
* 类说明 Swagger配置
* 项目名称:  微服务
* 包:        com.aspire.mirror.log.server.config
* 类名称:    SwaggerConfig.java
* 创建人:    jiangfuyi
* 创建时间:  2017年7月27日
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("logserviceAPI").description("logserviceAPI").termsOfServiceUrl("")
                .version("v1.0.0").contact(new Contact("", "", "")).build();
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aspire.mirror.log.server.controller"))
                .build().directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class).apiInfo(apiInfo());
    }

}