/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsStatusEnum.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月29日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain;

import lombok.Getter;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsTriggerWayEnum
 * <p/>
 *
 * 类功能描述: ops触发方式枚举
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
public enum OpsTriggerWayEnum {
	TRIGGER_BY_MANUAL(0, "页面执行"), TRIGGER_BY_JOB(1, "定时任务执行"), TRIGGER_BY_API(2, "第三方调用执行");

	private OpsTriggerWayEnum(Integer statusCode, String label) {
		this.statusCode = statusCode;
		this.label = label;
	}
	
	public static OpsTriggerWayEnum fromStatusCode(Integer statusCode) {
		for (OpsTriggerWayEnum triggerWay : OpsTriggerWayEnum.values()) {
			if (triggerWay.getStatusCode().equals(statusCode)) {
				return triggerWay;
			}
		}
		return null;
	}
	
	private Integer	statusCode;
	private String	label;
}
