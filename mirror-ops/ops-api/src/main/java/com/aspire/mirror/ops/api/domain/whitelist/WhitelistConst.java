package com.aspire.mirror.ops.api.domain.whitelist;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: WhitelistConst
 * <p/>
 *
 * 类功能描述: 自动化白名单常量类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2021年3月6日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2021 卓望公司-版权所有 
 *
 */
public final class WhitelistConst {
	
	/**
	 * 白名单类型枚举 
	 */
	public enum WhitelistTypeEnum {
		// 主机白名单, 巡检白名单, 漏洞白名单
		host, cruisecheck, vulnerability,baseline
	}
	
	/**
	 * 白名单约束对象类型枚举 
	 */
	public enum WhitelistConstraintTypeEnum {
		// 脚本, 作业, 主机IP, 操作系统类型
		script, pipeline, host, os_type,
	}
}
