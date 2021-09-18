/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.common.annotation;

import java.lang.annotation.*;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.common.annotation <br>
* 类名称: ResultCode.java <br>
* 类描述: 封装供日志模块使用的resultCode值<br>
*        目前只有ERROR级别使用该resultCode值<br>
*        resultCode值为10位数,具体详情请参考对应文档<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月1日下午12:51:57 <br>
* 版本: v1.0
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultCode {

    /**
     * 供日志模块使用的resultCode值
     * @return resultCode值
     */
    public String value() default "";

}
