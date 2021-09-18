package com.aspire.cdn.esdatawrap.anno;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: EmptyCheckWithConds
 * <p/>
 *
 * 类功能描述: 非null验证注解
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
@Target(FIELD)
public @interface EmptyCheckWithConds {
	boolean chkNotNull() default true;		// 验证对象值非null，默认开启
	boolean chkNotBlank() default true;		// 验证值为非"空",默认开启： 对于字符串，会验证notBlank; 对于Map, 会验证Map对象是否包含键; 
											// 对于List或Array，会验证是否包含元素, 其它对象直接忽略
	String tipKey() default "";				// 验证不通过时的提示key, 如果此配置值为空，则使用注解对应的属性名
	String[] conds() default "*";			// 验证条件key,  默认为*，标识任何条件下均验证，如果配置为其它值，则只有匹配到了该key，才会验证该字段
}
