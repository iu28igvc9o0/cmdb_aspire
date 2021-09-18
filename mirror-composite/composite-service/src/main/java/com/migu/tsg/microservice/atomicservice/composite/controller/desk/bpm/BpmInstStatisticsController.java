package com.migu.tsg.microservice.atomicservice.composite.controller.desk.bpm;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.desk.bpm.BpmInstStatisticsAPI;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.config.OrderConfig;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.helper.BpmCallHelper;

/**
 * @projectName: BpmInstStatisticsController
 * @description: 类
 * @author: tongzhihong
 * @create: 2020-09-14 10:26
 **/
@RestController
public class BpmInstStatisticsController implements BpmInstStatisticsAPI {

	@Autowired
	BpmCallHelper bpmCallHelper;

	@Autowired
	OrderConfig orderConfig;

	@Override
	public Object instDistributionInBpm(Integer type, Integer isWhole, String startDate,
			String endDate) {
		RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
		String account = authCtx.getUser().getUsername();
		Map<String, Object> req = Maps.newHashMap();
		req.put("startDate", startDate);
		req.put("endDate", endDate);
		req.put("account", account);
		req.put("isWhole", isWhole);
		return bpmCallHelper.getCallUrl(orderConfig.getAllFlowDefListUrl(), req);
	}

	@Override
	public Object orderTimelinessGroupByTenant(@RequestBody Map<String, Object> req) {
		return bpmCallHelper.postCallUrl(orderConfig.getOrderTimelinessGroupByTenantUrl(), req);
	}

	@Override
	public Object orderTimelinessStatistics(@RequestBody Map<String, Object> req) {
		return bpmCallHelper.postCallUrl(orderConfig.getOrderTimelinessStatisticsUrl(), req);
	}

	@Override
	public Object monthInTimeRate(@RequestBody Map<String, Object> req) {
		return bpmCallHelper.postCallUrl(orderConfig.getMonthInTimeRateUrl(), req);
	}

	@Override
	public Object allTypeOrder(@RequestBody Map<String, Object> req) {
		return bpmCallHelper.postCallUrl(orderConfig.getAllTypeOrderUrl(), req);
	}
}
