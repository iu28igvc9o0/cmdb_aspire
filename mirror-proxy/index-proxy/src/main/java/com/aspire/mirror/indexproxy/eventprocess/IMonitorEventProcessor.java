package com.aspire.mirror.indexproxy.eventprocess;

import com.aspire.mirror.indexproxy.domain.MonitorEventRecord;

/**
* 监控事件处理接口    <br/>
* Project Name:index-proxy
* File Name:IMonitorEventProcessor.java
* Package Name:com.aspire.mirror.indexproxy.eventprocess
* ClassName: IMonitorEventProcessor <br/>
* date: 2018年8月20日 下午5:16:03 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
interface IMonitorEventProcessor {
	
	/**
	* 是否需要对指定的事件做处理. <br/>
	*
	* 作者： pengguihua
	* @param event
	* @return
	*/  
	public boolean isAware(MonitorEventRecord event);
	
	/**
	* 处理事件. <br/>
	*
	* 作者： pengguihua
	* @param event
	*/  
	public void processEvent(MonitorEventRecord event);
}
