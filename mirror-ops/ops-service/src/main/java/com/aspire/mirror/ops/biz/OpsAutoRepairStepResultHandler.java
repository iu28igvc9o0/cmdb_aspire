package com.aspire.mirror.ops.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.mirror.ops.api.domain.OpsAgentStepInstanceResult;
import com.aspire.mirror.ops.api.domain.OpsMessageExtendMeta;
import com.aspire.mirror.ops.api.domain.OpsPipelineInstanceDTO;
import com.aspire.mirror.ops.biz.model.OpsActionAgentResult;
import com.aspire.mirror.ops.biz.model.OpsMessageMeta;
import com.aspire.mirror.ops.domain.OpsPipelineInstance;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsAutoRepairStepResultHandler
 * <p/>
 *
 * 类功能描述: 故障自愈步骤结果处理类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月20日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Service
@Order(Ordered.LOWEST_PRECEDENCE)
class OpsAutoRepairStepResultHandler extends AbstractAgentOpsStepResultHandler {
	@Autowired
	private OpsBaseDataBiz		opsBaseDataBiz;
	@Autowired
	private OpsAutoRepairBiz	autoRepairBiz;
	

	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public boolean aware(OpsActionAgentResult agentStepResult) {
		// 反馈还未完成, 直接跳过
		if (!agentStepResult.getOpsResultDetail().isOver()) {
			return false;
		}

		OpsMessageExtendMeta extendMeta = agentStepResult.getMeta().getExtendMeta();
		if (extendMeta != null && OpsPipelineInstance.BIZ_CLASSIFY_AUTO_REPAIR.equals(extendMeta.getBizClassify())) {
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional(isolation=Isolation.REPEATABLE_READ)
	public void handle(OpsActionAgentResult agentStepResult) {
		OpsMessageMeta meta = agentStepResult.getMeta();
		OpsPipelineInstanceDTO currpipeInst = opsBaseDataBiz.queryOpsPipelineInstanceById(meta.getPipelineInstanceId());
		if (!currpipeInst.isExecuteOver()) {
			log.info("The auto repair pipeline instance with id {} has not finished.", currpipeInst.getPipelineInstanceId());
			return;
		}
		OpsAgentStepInstanceResult stepInstHostResult = opsBaseDataBiz.queryAgentStepInstanceResultByKeys(
				meta.getStepInstanceId(), agentStepResult.getOpsResultDetail().getConcatHost());
		
		autoRepairBiz.handleApSchemePipelineFinish(currpipeInst, stepInstHostResult);
	}
}
