package com.migu.tsg.microservice.atomicservice.composite.controller.desk.bpm;

import com.aspire.mirror.composite.service.desk.bpm.payload.BpmRemindRecordVo;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.DeskLogsAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.desk.bpm.BpmInstAPI;
import com.aspire.mirror.composite.service.desk.bpm.payload.DeskOrderReq;
import com.migu.tsg.microservice.atomicservice.composite.config.OrderConfig;
import com.migu.tsg.microservice.atomicservice.composite.helper.BpmCallHelper;

/**
 * @projectName: BpmTaskController
 * @description: 类
 * @author: tongzhihong
 * @create: 2020-09-14 10:26
 **/
@RestController
public class BpmInstController implements BpmInstAPI{
	
	@Autowired
	OrderConfig orderConfig;
	
	@Autowired
	BpmCallHelper bpmCallHelper;
	
	@Override
	@DeskLogsAnnotation(value = "获取工单数据",httpMethod = "POST")
	public Object getOrderList(@RequestBody DeskOrderReq req) throws Exception {
		return bpmCallHelper.postCallUrl(orderConfig.getGetBpmInsDataUrl(), req);
	}

	@Override
	@DeskLogsAnnotation(value = "获取工单数据(包含工单统计数量)",httpMethod = "POST")
	public Object getOrderListNew(@RequestBody DeskOrderReq req) throws Exception {
		return bpmCallHelper.postCallUrl(orderConfig.getGetBpmInsDataNewUrl(), req);
	}

	@Override
	@DeskLogsAnnotation(value = "催办",OpType = "Save",httpMethod = "POST")
	public Object reminder(@RequestBody BpmRemindRecordVo vo) throws Exception {
		return bpmCallHelper.postCallUrl(orderConfig.getReminderUrl(), vo);
	}


}
