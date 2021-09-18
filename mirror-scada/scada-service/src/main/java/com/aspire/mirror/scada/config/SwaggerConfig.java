package com.aspire.mirror.scada.config;

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
 * 包:       com.aspire.mirror.dh.config
 * 类名称:    SwaggerConfig
 * 类描述:    SwaggerConfig
 * 创建人:    吕伟
 * 创建时间:   2018-12-21 09:03:28
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * @return ApiInfo
     */
    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("组态 API").description("组态 API").termsOfServiceUrl("").version("1.0.0")
                .contact(new Contact("", "", "")).build();
    }

    /**
     * @return Docket
     */
    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.aspire.mirror.scada.controller"))
                .build().directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class).apiInfo(apiInfo());
    }

}
