package com.aspire.ums.bills.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BillAnnotation
 * Author:   hangfang
 * Date:     2021/3/1
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BillLogAnnotation {
    String content();
    String obj();
    String type();
}
