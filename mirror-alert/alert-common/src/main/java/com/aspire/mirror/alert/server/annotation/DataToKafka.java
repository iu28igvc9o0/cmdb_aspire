package com.aspire.mirror.alert.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.METHOD })
@Retention(RUNTIME)
@Documented
public @interface DataToKafka {

    // es的index
    String index();

    /**
     * 是否发送所有参数，默认为false
     * false：只发送第一个非基础类型的参数，结构为jsonString
     * true：发送所有参数，结构为jsonArray
     * @return
     */
    boolean allArgs() default false;
}
