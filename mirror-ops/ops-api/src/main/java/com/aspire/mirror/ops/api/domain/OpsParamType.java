package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsParamType
 * <p/>
 *
 * 类功能描述: 参数类型定义
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年12月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public class OpsParamType {
	@JsonProperty("param_type")
	private String	paramType;
	@JsonProperty("param_type_label")
	private String	paramTypeLabel;
	@JsonProperty("auto_popup_flag")
	private String	autoPopupFlag	= "N";	// 参数值是否为自动填充 Y:系统自动填充 N:手工输入
	@JsonProperty("param_type_desc")
	private String	paramTypeDesc;
}
