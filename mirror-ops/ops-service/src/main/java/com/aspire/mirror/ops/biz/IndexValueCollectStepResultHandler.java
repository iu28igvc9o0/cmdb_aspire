/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  IndexValueCollectStepResultHandler.java 
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.aspire.mirror.ops.api.domain.OpsMessageExtendMeta;
import com.aspire.mirror.ops.biz.model.OpsActionAgentResult;
import com.aspire.mirror.ops.biz.model.OpsActionAgentResult.AgentOpsResultDetail;
import com.aspire.mirror.ops.domain.OpsPipelineInstance;
import com.aspire.mirror.ops.util.JsonUtil;
import com.jayway.jsonpath.DocumentContext;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: IndexValueCollectStepResultHandler
 * <p/>
 *
 * 类功能描述: 巡检指标脚本采集结果处理
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
class IndexValueCollectStepResultHandler extends AbstractAgentOpsStepResultHandler {
	@Autowired
	private KafkaTemplate<String, String>	kafkaTemplate;
	public static final String				CALLBACK_KAFKA	= "kafka";

	@Override
	public boolean aware(OpsActionAgentResult agentStepResult) {
		// 还未完成, 直接跳过
		if (!agentStepResult.getOpsResultDetail().isOver()) {
			return false;
		}
		OpsMessageExtendMeta extendMeta = agentStepResult.getMeta().getExtendMeta();
		if (extendMeta != null && OpsPipelineInstance.BIZ_CLASSIFY_INDEX_COLLECT.equals(extendMeta.getBizClassify())) {
			return true;
		}
		return false;
	}
	
	@Override
	public void handle(OpsActionAgentResult agentStepResult) {
		try {
			AgentOpsResultDetail opsResultDetail = agentStepResult.getOpsResultDetail();
			if (!opsResultDetail.isOver()) {
				return;
			}
			
			String targetHost = opsResultDetail.getConcatHost();
			OpsMessageExtendMeta extendMeta = agentStepResult.getMeta().getExtendMeta();
			Object callbackObj = extendMeta.getAttrValByKey(targetHost);
			
			Map<String, Object> extendObj = new HashMap<>();
			Integer exitCode = opsResultDetail.getExitCode();
			extendObj.put("flag", exitCode == null ? false : (exitCode == 0 ? true : false));
			String opslog = opsResultDetail.getOpsLog();
			opslog = opslog == null ? null : opslog.trim();
			extendObj.put("log", opslog);
			extendObj.put("aspnode_result", opsResultDetail.getAspNodeResult());
			extendObj.put("aspnode_msg", opsResultDetail.getAspNodeMessage());
			extendObj.put("clock", TimeUnit.MICROSECONDS.toSeconds(System.currentTimeMillis()));
			
			DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(callbackObj);
			jsonCtx.set("$.extend_obj", JsonUtil.toJacksonJson(extendObj));
			if (CALLBACK_KAFKA.equals(extendMeta.getAttrValByKey("callbackType"))) {
				kafkaTemplate.send(String.valueOf(extendMeta.getAttrValByKey("callbackTarget")), 
						JsonUtil.toJacksonJson(callbackObj));
			}
		} catch (Throwable e) {
			log.error(null, e);
		}
	}
}
