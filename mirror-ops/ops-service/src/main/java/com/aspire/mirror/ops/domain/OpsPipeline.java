/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsPipeline.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月22日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.domain;

import java.util.Date;

import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.ops.api.domain.AspNodeResultEnum;
import com.aspire.mirror.ops.api.domain.OpsPipelineDTO;
import com.aspire.mirror.ops.api.domain.OpsStatusEnum;
import com.aspire.mirror.ops.api.domain.OpsTriggerWayEnum;
import com.aspire.mirror.ops.controller.authcontext.RequestAuthContext;

import lombok.Getter;
import lombok.Setter;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: OpsPipeline
 * <p/>
 *
 * 类功能描述: 作业流水
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年10月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Setter
@Getter
public class OpsPipeline extends OpsPipelineDTO {
	
	public OpsPipelineInstance newInstance(OpsTriggerWayEnum triggerWay) {
		return newInstance(triggerWay, null);
	}
	
	public OpsPipelineInstance newInstance(OpsTriggerWayEnum triggerWay, Integer bizClassify) {
		if (bizClassify == null) {
			bizClassify = OpsPipelineInstance.BIZ_CLASSIFY_OPS;
		}
		OpsPipelineInstance instance = new OpsPipelineInstance();
		instance.setInstanceClassify(OpsPipelineInstance.CLASSIFY_PIPELINE);
		instance.setLabelId(labelId);
		instance.setOperator(RequestAuthContext.getRequestHeadUserName());
		instance.setTriggerWay(triggerWay.getStatusCode());
		instance.setBizClassify(bizClassify);
		instance.setPipelineId(pipelineId);
		instance.setPipelineInstanceName(pipelineName);
		instance.setCreateTime(new Date());
		instance.setStartTime(new Date());
		instance.setStatus(OpsStatusEnum.STATUS_5.getStatusCode());
		instance.setAspNodeResult(AspNodeResultEnum.STATUS_2.getStatusCode());
		return instance;
	}
	public String newVersion() {
		Date now = new Date();
		return "V" + DateUtil.format(now, DateUtil.DATE_TIME_FORMAT);
	}
}
