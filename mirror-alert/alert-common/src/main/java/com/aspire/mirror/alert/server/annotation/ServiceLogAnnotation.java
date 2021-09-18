package com.aspire.mirror.alert.server.annotation;


import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.*;

@Target({ ElementType.METHOD })
@Retention(RUNTIME)
@Documented
public @interface ServiceLogAnnotation {

    // 操作目标
    String id() default "";

    // 操作人
    String operator() default "";

    // 操作内容
    String content() default "";

    // 操作类型
    String operateType() default "";

    // 操作类型描述
    String operateTypeDesc() default "";

    // 操作模型
    String operateModel() default "";

    // 操作模型描述
    String operateModelDesc() default "";

    // 备注
    String remark() default "";
}
