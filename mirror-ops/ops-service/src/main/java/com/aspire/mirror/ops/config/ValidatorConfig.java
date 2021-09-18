package com.aspire.mirror.ops.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * 注解校验
 * <p>
 * 项目名称:  mirror平台-模板
 * 包:       com.aspire.mirror.alert.server.config
 * 类名称:    ValidatorConfig.java
 * 类描述:    注解校验
 * 创建人:    JinSu
 * 创建时间:  2018/07/27 11:56
 * 版本:      v1.0
 */
@Configuration
@EnableAutoConfiguration
public class ValidatorConfig {

    /**
     * 获取一个MethodValidationPostProcessor类
     * @return MethodValidationPostProcessor
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}