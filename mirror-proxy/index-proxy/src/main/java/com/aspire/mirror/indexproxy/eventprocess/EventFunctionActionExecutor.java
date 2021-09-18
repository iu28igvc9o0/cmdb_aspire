package com.aspire.mirror.indexproxy.eventprocess;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.domain.MonitorActionRecord;

@Component
@Qualifier("functionActionExecutor")
class EventFunctionActionExecutor implements IEventActionExecutor {
	@Override
	public void doAction(MonitorActionRecord actionRecord, Object bizObj) {
		// TODO  
	}
}
