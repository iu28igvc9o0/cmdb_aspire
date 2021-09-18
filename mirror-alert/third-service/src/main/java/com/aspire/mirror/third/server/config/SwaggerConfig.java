package com.aspire.mirror.third.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 项目名称: ldap-service <br>
 * 包: com.aspire.mirror.elasticsearch.server.config <br>
 * 类名称: SwaggerConfig.java <br>
 * 类描述: swagger 配置类<br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年9月8日下午2:03:50 <br>
 * 版本: v1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

    ApiInfo apiInfo() {
            return new ApiInfoBuilder().title("THIRD API Documentation").description("version v1").termsOfServiceUrl("")
                .version("1.0.0").contact(new Contact("", "", "")).build();
    }

    /**
     * 包扫描
     * @return Docket
     */
    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.aspire.mirror.third.server.controller"))
                .build().directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class).apiInfo(apiInfo());
    }

}
