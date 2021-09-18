package com.aspire.mirror.theme.server.config;

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
 * swagger配置
 * <p>
 * 项目名称:  mirror平台-模板
 * 包:       com.aspire.mirror.alert.server.config
 * 类名称:    SwaggerConfig.java
 * 类描述:    swaggerConfig
 * 创建人:    JinSu
 * 创建时间:  2018/07/27 11:56
 * 版本:      v1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * @return ApiInfo
     */
    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Theme API").description("Theme API").termsOfServiceUrl("").version("1.0.0")
                .contact(new Contact("", "", "")).build();
    }

    /**
     * @return Docket
     */
    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.aspire.mirror.theme.server.controller"))
                .build().directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class).apiInfo(apiInfo());
    }

}