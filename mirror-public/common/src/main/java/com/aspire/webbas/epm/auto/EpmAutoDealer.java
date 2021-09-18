package com.aspire.webbas.epm.auto;

import com.aspire.webbas.epm.core.entity.EpmProcess;
import com.aspire.webbas.epm.core.entity.EpmTask;

/**
 * 自动执行任务处理类接口
 * @author wanglei
 *
 */
public interface EpmAutoDealer {

	/**
	 * 执行方法
	 * @param epmTask 任务参数
	 * @return 流程对象
	 */
	public EpmProcess execute(EpmTask epmTask);
}
