/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsScript.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsScript
 * <p/>
 *
 * 类功能描述: 脚本
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Getter
@Setter
public class SensitiveConfigQueryModel extends SensitiveConfig {
	private String	commandLike;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	createTimeStart;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	createTimeEnd;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	updateTimeStart;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date	updateTimeEnd;

	@JsonProperty("page_size")
	private Integer	pageSize;		// 如果为null， 则查询全部数据
	@JsonProperty("page_no")
	private Integer	pageNo;			// 从0开始

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
