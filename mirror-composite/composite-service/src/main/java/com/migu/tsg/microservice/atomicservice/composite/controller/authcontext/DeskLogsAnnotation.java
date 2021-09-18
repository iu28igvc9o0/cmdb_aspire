package com.migu.tsg.microservice.atomicservice.composite.controller.authcontext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统日志注解
 * <pre>
 * 系统日志注解
 * </pre>
 *
 * @company 卓望技术有限公司
 * @author menglinjie
 * @date 2020年11月16日
 */
@Target({ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME) 
public @interface DeskLogsAnnotation {
    String value();

    String httpMethod() default "GET";

    String OpType() default "View";

    String AppType() default "系统登录";


}
