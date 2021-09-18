/**
 *
 * 项目名： ums-cmdb-cdn 
 * <p/> 
 *
 * 文件名:  aaa.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月6日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.ums.cdn.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: CmdbTableColumn
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月6日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(CmdbTableColumns.class)
public @interface CmdbTableColumn {
    String tag();
    String colName();
}
