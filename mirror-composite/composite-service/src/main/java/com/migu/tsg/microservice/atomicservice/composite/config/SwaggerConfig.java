package com.migu.tsg.microservice.atomicservice.composite.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
* Swagger配置
* Project Name:composite-service
* File Name:SwaggerConfig.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.config
* ClassName: SwaggerConfig <br/>
* date: 2017年9月4日 下午8:16:34 <br/>
* Swagger配置
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {
	
	/*
	 * 默认情况下Spring Boot 使用WebMvcAutoConfiguration中配置的各种属性。
	 * 因为
	 *  spring: #配置mvc,resource用来处理请求404抛出NoHandlerFoundException异常
	 *	  mvc: 
	 *	    throw-exception-if-no-handler-found: true #当出现404错误时,是否直接抛出异常
	 *	  resources:  
	 *	    add-mappings: false #是否开启默认的资源处理
	 *	所以swagger-ui.html没法映射到spring boot默认提供静态资源目录位置，
	 *	因此需要继承WebMvcAutoConfiguration类,添加注册swagger资源到spring boot默认提供静态资源目录位置.
	 */
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

	ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Composite API").description("Composite服务API")
				.termsOfServiceUrl("").version("1.0.0")
				.contact(new Contact("", "", "")).build();
	}

	/**
	* customImplementation:(这里用一句话描述这个方法的作用). <br/>
	*
	* 作者： yangshilei
	* @return
	*/
	@Bean
	public Docket customImplementation() {
		ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> paramList = new ArrayList<Parameter>();
        Parameter headOrgAcccout = tokenPar.name("head_orgAccount").description("根帐号").modelRef(
        		new ModelRef("string")).parameterType("header").required(true).build();
        Parameter headUsername = tokenPar.name("head_userName").description("用户名").modelRef(
        		new ModelRef("string")).parameterType("header").required(true).build();
        Parameter headIsAdmin = tokenPar.name("head_isAdmin").description("是否管理员").modelRef(
        		new ModelRef("Boolean")).parameterType("header").required(false).build();
        Parameter headIsSuperUser = tokenPar.name("head_isSuperUser").description("用户名").modelRef(
        		new ModelRef("Boolean")).parameterType("header").required(false).build();
        paramList.add(headOrgAcccout);
        paramList.add(headUsername);
        paramList.add(headIsAdmin);
        paramList.add(headIsSuperUser);

		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors
						.basePackage("com.migu.tsg.microservice.atomicservice.composite.controller"))
				.build()
				.globalOperationParameters(paramList)
				.directModelSubstitute(org.joda.time.LocalDate.class,
						java.sql.Date.class)
				.directModelSubstitute(org.joda.time.DateTime.class,
						java.util.Date.class).apiInfo(apiInfo());
	}

}