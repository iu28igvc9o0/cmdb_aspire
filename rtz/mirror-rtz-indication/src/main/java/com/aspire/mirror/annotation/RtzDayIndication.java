package com.aspire.mirror.annotation;

import java.lang.annotation.*;

/**
 * 软探针工厂类注解
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RtzDayIndication {
    String value() default "";
}
