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
 * 类功能描述: ops故障自愈状态枚举
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
public enum OpsAutoRepairStatusEnum {
	STATUS_100(OpsStatusEnum.STATUS_100.getStatusCode(), "执行中"), 
	STATUS_101(OpsStatusEnum.STATUS_101.getStatusCode(), "执行失败"), 
	STATUS_102(OpsStatusEnum.STATUS_102.getStatusCode(), "执行超时"), 
	STATUS_9(OpsStatusEnum.STATUS_9.getStatusCode(), "执行成功"),
	STATUS_6(OpsStatusEnum.STATUS_6.getStatusCode(), "人工干预"), 
	STATUS_7(OpsStatusEnum.STATUS_7.getStatusCode(), "人工终止");

	private OpsAutoRepairStatusEnum(Integer statusCode, String label) {
		this.statusCode = statusCode;
		this.label = label;
	}

	private Integer	statusCode;
	private String	label;
	
	public static OpsAutoRepairStatusEnum of(int statusCode) {
		for (OpsAutoRepairStatusEnum enumItem : OpsAutoRepairStatusEnum.values()) {
			if (enumItem.getStatusCode() == statusCode) {
				return enumItem;
			}
		}
		return null;
	}
}
