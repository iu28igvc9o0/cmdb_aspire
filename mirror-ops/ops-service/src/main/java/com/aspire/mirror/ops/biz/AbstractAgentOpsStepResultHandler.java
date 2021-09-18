/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  AbstractAgentOpsStepResultHandler.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月8日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.biz;

import com.aspire.mirror.ops.biz.model.OpsActionAgentResult;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: AbstractAgentOpsStepResultHandler
 * <p/>
 *
 * 类功能描述: ops操作step结果处理抽象类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月8日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
abstract class AbstractAgentOpsStepResultHandler {

	/** 
	 * 功能描述: 是否需要处理当前result  
	 * <p>
	 * @param agentStepResult
	 * @return
	 */
	public abstract boolean aware(OpsActionAgentResult agentStepResult);
	
	/** 
	 * 功能描述: 处理当前result    
	 * <p>
	 * @param agentStepResult
	 */
	public abstract void handle(OpsActionAgentResult agentStepResult);
}
