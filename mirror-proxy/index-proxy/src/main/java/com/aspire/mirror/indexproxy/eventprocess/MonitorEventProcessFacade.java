package com.aspire.mirror.indexproxy.eventprocess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.domain.MonitorEventRecord;

import lombok.extern.slf4j.Slf4j;

/**
* 监控事件处理门面业务入口    <br/>
* Project Name:index-proxy
* File Name:MonitorEventProcessFacade.java
* Package Name:com.aspire.mirror.indexproxy.eventprocess
* ClassName: MonitorEventProcessFacade <br/>
* date: 2019年7月1日 下午7:39:35 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Slf4j
@Component
public class MonitorEventProcessFacade {
	@Autowired
	private List<IMonitorEventProcessor> processorList;
	
	public void process(MonitorEventRecord event) {
		for (IMonitorEventProcessor processor : processorList) {
			try {
				if (processor.isAware(event)) {
					processor.processEvent(event);
				}
			} catch (Throwable e) {
				log.error("Error when process the monitor event.", e);
			}
		}
	}
}
