package com.aspire.cdn.esdatawrap.anno;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: CdnReportCacheMark
 * <p/>
 *
 * 类功能描述: CDN运营报表缓存方法mark
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年8月7日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface CdnReportCacheMark {
	String value();						// 缓存键, 如果loopKeys存在值, 则此处的键为字符串模板, 最终会使用loopKeys里面的key替代模板占位生成最终的键
	String[] loopKeys() default {};		// 遍历的key, 此key会被作为参数, 传给此注解的方法
}
