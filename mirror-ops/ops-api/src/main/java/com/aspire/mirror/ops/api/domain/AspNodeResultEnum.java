package com.aspire.mirror.ops.api.domain;

import lombok.Getter;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsStatusEnum
 * <p/>
 *
 * 类功能描述: ops状态枚举
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月29日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Getter
public enum AspNodeResultEnum {
	STATUS_0(0, "正常"), STATUS_1(1, "异常"), STATUS_2(2, "无状态");

	private AspNodeResultEnum(Integer statusCode, String label) {
		this.statusCode = statusCode;
		this.label = label;
	}

	private Integer	statusCode;
	private String	label;
	
	public static AspNodeResultEnum of(int statusCode) {
		for (AspNodeResultEnum enumItem : AspNodeResultEnum.values()) {
			if (enumItem.getStatusCode() == statusCode) {
				return enumItem;
			}
		}
		return null;
	}
}
