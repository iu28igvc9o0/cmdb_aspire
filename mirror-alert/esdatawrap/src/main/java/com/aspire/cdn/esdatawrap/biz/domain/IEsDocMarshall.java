package com.aspire.cdn.esdatawrap.biz.domain;

import com.aspire.cdn.esdatawrap.util.JsonUtil;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: IEsDocMarshall
 * <p/>
 *
 * 类功能描述: 转换成es文档的接口, 默认使用jackson序列化
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月18日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
public interface IEsDocMarshall {
	
	default public String toEsDoc() {
		return JsonUtil.toJacksonJson(this);
	}
}
