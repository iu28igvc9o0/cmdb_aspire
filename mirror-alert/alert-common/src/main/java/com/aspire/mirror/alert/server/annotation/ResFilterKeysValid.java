package com.aspire.mirror.alert.server.annotation;

import java.lang.annotation.*;


/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: ResFilterKeysValid
 * <p/>
 *
 * 类功能描述: 验证数据过滤权限键值
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月16日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResFilterKeysValid {
    String[] notAllEmpty();		// 指定的键，至少有一个存在值
}
