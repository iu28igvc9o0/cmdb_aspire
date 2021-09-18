package com.aspire.cdn.esdatawrap.config;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

import com.aspire.cdn.esdatawrap.util.JsonUtil;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnExpression("!'none'.equals('${spring.main.web-application-type}')")
public class SwaggerConfig {

    /**
     * @return ApiInfo
     */
    ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Esdatawrap Manage API").description("Esdatawrap Manage API")
        		.termsOfServiceUrl("").version("1.0.0").contact(new Contact("", "", "")).build();
    }

    /**
     * @return Docket
     */
    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage("com.aspire.cdn.esdatawrap.controller"))
                .build().directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class).apiInfo(apiInfo());
    }
    
    
    public static void main(String[] args) throws UnsupportedEncodingException {
		Map<String, List<String>> resFilterConfig = new HashMap<>();
		resFilterConfig.put("authScriptIdList", Collections.singletonList("212"));
		System.out.println(Base64Utils.encodeToUrlSafeString(JsonUtil.toJacksonJson(resFilterConfig).getBytes("UTF-8")));
	}
}