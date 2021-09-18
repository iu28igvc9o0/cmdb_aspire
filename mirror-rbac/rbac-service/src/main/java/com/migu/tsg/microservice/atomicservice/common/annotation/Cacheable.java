/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.common.annotation;


import java.lang.annotation.*;


/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.common.annotation <br>
* 类名称: Cacheable.java <br>
* 类描述: 写入缓存注解<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年10月1日下午12:51:57 <br>
* 版本: v1.0
*/
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
public @interface Cacheable {
    
    /**
     * 缓存KEY
     * @return KEY
     */
    public String value() default "";  
     
}
