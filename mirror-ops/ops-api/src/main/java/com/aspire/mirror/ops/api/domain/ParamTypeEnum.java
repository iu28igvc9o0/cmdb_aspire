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
public enum ParamTypeEnum {
	TYPE_2("2", "随机密码"), TYPE_3("3", "统一随机密码"),TYPE_4("4", "自定义密码"),
	TYPE_FUN("FUN", "函数"),
	TYPE_IP("IP", "目标IP"),
	TYPE_BIZ_SYS("BIZ_SYS", "目标IP所属业务系统"),
	TYPE_POOL("POOL", "目标IP所属资源池"),
	TYPE_TASK_ID("TASK_ID", "执行任务ID");

	private ParamTypeEnum(String statusCode, String label) {
		this.statusCode = statusCode;
		this.label = label;
	}

	private String	statusCode;
	private String	label;
	
	public static ParamTypeEnum of(String statusCode) {
		for (ParamTypeEnum enumItem : ParamTypeEnum.values()) {
			if (enumItem.getStatusCode().equals(statusCode)) {
				return enumItem;
			}
		}
		return null;
	}
}
