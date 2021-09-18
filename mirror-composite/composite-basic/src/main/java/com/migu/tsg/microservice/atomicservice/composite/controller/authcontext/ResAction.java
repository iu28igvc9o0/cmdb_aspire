package com.migu.tsg.microservice.atomicservice.composite.controller.authcontext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 资源和操作标记
* Project Name:composite-service
* File Name:ResAction.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.authContext
* ClassName: ResAction <br/>
* date: 2017年8月24日 下午6:56:43 <br/>
* 资源和操作标记
* @author pengguihua
* @version
* @since JDK 1.6
*/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResAction {
    String resType();
    String action();
    boolean loadResFilter() default false;
}
