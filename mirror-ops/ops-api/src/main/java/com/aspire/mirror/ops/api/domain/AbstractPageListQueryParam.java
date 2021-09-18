/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  AbstractPageQueryParam.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月2日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: AbstractPageQueryParam
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月2日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Setter
@Getter
public abstract class AbstractPageListQueryParam {
	@JsonProperty("page_size")	
	protected Integer			pageSize;					// 如果为null， 则查询全部数据
	@JsonProperty("page_no")
	protected Integer			pageNo;						// 从0开始
	
	@JsonProperty("order_type")	
	protected String			orderType;					// ASC | DESC
	
	@JsonProperty("order_columns")	
	protected String			orderColumn;				// 排序字段名, 只支持单个字段
	
	public Integer getStartIdx() {
		if (pageSize == null) {
			return null;
		}
		return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
	}
}
