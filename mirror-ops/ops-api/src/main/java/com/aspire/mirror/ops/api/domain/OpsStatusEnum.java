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
public enum OpsStatusEnum {
	STATUS_100(100, "执行中"), STATUS_101(101, "执行失败"), STATUS_102(102, "执行超时"), STATUS_9(9, "执行成功"),
	STATUS_5(5, "等待执行"), STATUS_6(6, "等待用户"), STATUS_7(7, "执行阻断"), STATUS_8(8, "执行待审核"), STATUS_10(10, "审核拒绝"), STATUS_11(11, "审核通过");

	private OpsStatusEnum(Integer statusCode, String label) {
		this.statusCode = statusCode;
		this.label = label;
	}

	private Integer	statusCode;
	private String	label;
	
	public static OpsStatusEnum of(int statusCode) {
		for (OpsStatusEnum enumItem : OpsStatusEnum.values()) {
			if (enumItem.getStatusCode() == statusCode) {
				return enumItem;
			}
		}
		return null;
	}
}
