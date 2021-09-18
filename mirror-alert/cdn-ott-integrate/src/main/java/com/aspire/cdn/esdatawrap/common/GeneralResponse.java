package com.aspire.cdn.esdatawrap.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: GeneralResponse
 * <p/>
 *
 * 类功能描述: 通用请求响应
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月30日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse {

	@JsonProperty("flag")
	private boolean	flag	= true;	// 调用是否成功

	@JsonProperty("error_tip")
	private String	errorTip;		// 调用出错时的提示内容

	@JsonProperty("biz_data")
	private Object	bizData;		// 业务对象
	
	
	public GeneralResponse(boolean flag) {
		this.flag = flag;
	}
	
	public GeneralResponse(boolean flag, String errorTip) {
		this.flag = flag;
		this.errorTip = errorTip;
	}
}
