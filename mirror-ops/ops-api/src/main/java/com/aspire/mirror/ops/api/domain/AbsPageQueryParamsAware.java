package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: AbsPageQueryParamsAware
 * <p/>
 *
 * 类功能描述: 分页查询抽象父类s
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月5日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Setter
@Getter
public abstract class AbsPageQueryParamsAware {
	
	@JsonProperty("page_size")
	protected Integer	pageSize;	// 如果为null， 则查询全部数据
	
	@JsonProperty("page_no")
	protected Integer	pageNo;		
	
	
	public Integer getStartIdx() {
		if (pageSize == null) {
			return null;
		}
		return (pageNo == null || pageNo <= 0 ? 0 : pageNo - 1) * pageSize;
	}
}
