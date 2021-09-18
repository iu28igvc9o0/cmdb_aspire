package com.migu.tsg.microservice.atomicservice.composite.controller.authcontext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 匿名调用注解
 * Project Name:composite-service
 * File Name:Authentication.java
 * Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.authcontext
 * ClassName: Authentication <br/>
 * date: 2017年12月6日 下午5:45:09 <br/>
 * TODO 详细描述这个类的功能等
 *
 * @author baiwp
 * @since JDK 1.6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Authentication {
    boolean anonymous() default false;

    String anonymousName() default "anonymous";
}
