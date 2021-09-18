package com.migu.tsg.microservice.atomicservice.composite.controller.logcontext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 日志记录码定义注解，可以在类和方法上定义， 日志记录码 = 类的定义码值 + 方法定义码值 + 最后一位错误码值(在抛出异常时传入)
* Project Name:composite-service
* File Name:LogCodeDefine.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.logcontext
* ClassName: LogCodeDefine <br/>
* date: 2017年12月08日 下午1:56:43 <br/>
* 资源和操作标记
* @author pengguihua
* @version
* @since JDK 1.6
*/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LogCodeDefine {
    String value();
    String desc() default "";
}
