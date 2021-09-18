package com.migu.tsg.microservice.atomicservice.composite.controller.desk.bpm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.desk.bpm.UmsFlowHomeAPI;
import com.aspire.mirror.composite.service.desk.bpm.payload.UmsFlowHomeInstParam;
import com.migu.tsg.microservice.atomicservice.composite.config.OrderConfig;
import com.migu.tsg.microservice.atomicservice.composite.helper.BpmCallHelper;


/**
 * UMS 流程首页接口
 * @projectName: BpmFlowHomeController
 * @description: 类
 * @author: tongzhihong
 * @create: 2020-11-24 10:26
 **/
@RestController
public class BpmFlowHomeController implements UmsFlowHomeAPI{
	
	@Autowired
	BpmCallHelper bpmCallHelper;

	@Autowired
	OrderConfig orderConfig;
	
	private static final Logger LOG = LoggerFactory.getLogger(BpmFlowHomeController.class);
	
	@Override
	public Object getBpmInsData(@RequestBody UmsFlowHomeInstParam req) {
		LOG.info("param:{}",JSONObject.toJSONString(req));
		return bpmCallHelper.postCallUrl(orderConfig.getBpmInsData(), req);
	}

	@Override
	public Object getOfficialDocumentList(@RequestBody UmsFlowHomeInstParam req) {
		LOG.info("param:{}",JSONObject.toJSONString(req));
		return bpmCallHelper.postCallUrl(orderConfig.getOfficialDocumentList(), req);
	}

	@Override
	public Object getInstEfficiencyShow(@RequestBody UmsFlowHomeInstParam req) {
		LOG.info("param:{}",JSONObject.toJSONString(req));
		return bpmCallHelper.postCallUrl(orderConfig.getInstEfficiencyShow(), req);
	}

	@Override
	public Object getInstEfficiencyReport(@RequestBody UmsFlowHomeInstParam req) {
		LOG.info("param:{}",JSONObject.toJSONString(req));
		return bpmCallHelper.postCallUrl(orderConfig.getInstEfficiencyReport(), req);
	}

	@Override
	public Object getWorkTop(@RequestBody UmsFlowHomeInstParam req) {
		LOG.info("param:{}",JSONObject.toJSONString(req));
		return bpmCallHelper.postCallUrl(orderConfig.getWorkTop(), req);
	}

	@Override
	public Object getWorkAssessmentReport(@RequestBody UmsFlowHomeInstParam req) {
		LOG.info("param:{}",JSONObject.toJSONString(req));
		return bpmCallHelper.postCallUrl(orderConfig.getWorkAssessmentReport(), req);
	}

	@Override
	public Object getDepartmentInstCloseInfo(@RequestBody UmsFlowHomeInstParam req) {
		LOG.info("param:{}",JSONObject.toJSONString(req));
		return bpmCallHelper.postCallUrl(orderConfig.getDepartmentInstCloseInfo(), req);
	}

	@Override
	public Object instSearch(@RequestBody UmsFlowHomeInstParam req) {
		LOG.info("param:{}",JSONObject.toJSONString(req));
		return bpmCallHelper.postCallUrl(orderConfig.getInstSearch(), req);
	}

	@Override
	public Object alarmStatistics(@RequestBody UmsFlowHomeInstParam req) {
		LOG.info("param:{}",JSONObject.toJSONString(req));
		return bpmCallHelper.postCallUrl(orderConfig.getAlarmStatistics(), req);
	}

}
