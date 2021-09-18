package com.migu.tsg.microservice.atomicservice.composite.controller.desk.bpm;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.aspire.mirror.composite.service.desk.bpm.BpmAnnouncementAPI;
import com.migu.tsg.microservice.atomicservice.composite.config.OrderConfig;
import com.migu.tsg.microservice.atomicservice.composite.helper.BpmCallHelper;

public class BpmAnnouncementController implements BpmAnnouncementAPI{
	
	@Autowired
	OrderConfig orderConfig;
	
	@Autowired
	BpmCallHelper bpmCallHelper;
	
	@SuppressWarnings("unchecked")
	@Override
	public Object list() throws Exception {
		return bpmCallHelper.getCallUrl(orderConfig.getRecentAnnouncementUrl(), MapUtils.EMPTY_MAP);
	}

}
