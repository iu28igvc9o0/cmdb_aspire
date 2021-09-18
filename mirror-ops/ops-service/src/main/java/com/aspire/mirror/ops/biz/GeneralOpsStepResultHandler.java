/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  NormalOpsStepResultHandler.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月18日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.aspire.mirror.ops.biz.model.OpsActionAgentResult;
import com.aspire.mirror.ops.biz.model.OpsActionAgentResult.AgentOpsResultDetail;
import com.aspire.mirror.ops.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: GeneralOpsStepResultHandler
 * <p/>
 *
 * 类功能描述: 通用ops操作结果处理
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月18日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class GeneralOpsStepResultHandler extends AbstractAgentOpsStepResultHandler {
	@Autowired
	private OpsActionBiz opsBiz;
	
	@Override
	public boolean aware(OpsActionAgentResult agentStepResult) {
		if (log.isDebugEnabled()) {
			AgentOpsResultDetail detail = agentStepResult.getOpsResultDetail();
			long threadId = Thread.currentThread().getId();
			boolean isOver = detail.isOver();
			Integer status = detail.getStatusFlag();
			String aspnodeResult = detail.getAspNodeResult() == null ? "" : String.valueOf(detail.getAspNodeResult());
			String metaJson = JsonUtil.toJacksonJson(agentStepResult.getMeta());

			log.debug("Got OpsActionAgentResult message: thread={} | isOver={} | status={} | aspnodeResult={} | OpsMessageMeta={} ", 
					threadId, isOver, status, aspnodeResult, metaJson);
		}
		return true;
	}
	
	/** 
	 * 方法重写 <br/>
	 * 功能描述： 
	 * <p>
	 * @param agentStepResult 
	 * @see com.aspire.mirror.ops.biz.AbstractAgentOpsStepResultHandler#handle(com.aspire.mirror.ops.biz.model.OpsActionAgentResult) 
	 */
	@Override
	public void handle(OpsActionAgentResult agentStepResult) {
		opsBiz.processOpsAgentStepResult(agentStepResult);
	}
}
