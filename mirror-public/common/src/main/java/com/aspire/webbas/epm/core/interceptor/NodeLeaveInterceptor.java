package com.aspire.webbas.epm.core.interceptor;

import java.util.Map;

import com.aspire.webbas.epm.core.entity.EpmProcess;

/**
 * 节点拦截器（离开节点）
 * @author wanglei
 *
 */
public interface NodeLeaveInterceptor {
	/**
	 * 拦截处理
	 * @param process
	 *            下一环节流程
	 * @param paramMap
	 *            xml中配置的拦截器对应的param参数和流程流转及EpmProcessService.next(String
	 *            taskId, EpmProcess process)传入的paramMap中的参数
	 */
	void execute(EpmProcess process, Map<String, Object> paramMap);
}
