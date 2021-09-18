package com.aspire.ums.cmdb.automate.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fanwenhui
 * @date 2020-08-25 10:58
 * @description 自定义字段名
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface FieldName {
    /**
     * 字段名称
     */
    String value() default "";

    /**
     * 是否忽略
     */
    boolean Ignore() default false;
}
