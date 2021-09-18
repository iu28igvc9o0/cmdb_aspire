package com.aspire.webbas.epm.core.constant;

/**
 * 项目名称: [webbas-component-epm]
 * 包名: [com.aspire.webbas.epm.core.constant]
 * 类名称: [EpmConstant]
 * 类描述: [一句话描述该类的功能]
 * 创建人: [王磊]
 * 创建时间: [2014年8月31日 下午1:42:12]
 */
public class EpmConstant {
	/** 判断值 */
	public static final String YES = "1";
	/** 判断值 */
	public static final String NO = "0";

	/** 流程类型：并发分支，交汇 */
	public static final String PROCESS_TYPE_FORK = "fork";
	/** 交汇 */
	public static final String PROCESS_TYPE_JOIN = "join";

	/** 多人处理类型：并，或 */
	public static final String DEAL_TYPE_AND = "and";
	/** 或 */
	public static final String DEAL_TYPE_OR = "or";

	/** EMP_STATUS_END */
	public static final String EMP_STATUS_END = "end";

	/** EMP_STATUS_PASS */
	public static final String EMP_STATUS_PASS = "pass";
	/** EMP_STATUS_ERROR */
	public static final String EMP_STATUS_ERROR = "error";

	/** 默认选择器值 */
	public static final String CHOICE_DEFAULT_VALUE = "choice_default";

	/** 默认状态 */
	public static final String STATUS_DEFAULT_VALUE = "start";
}
