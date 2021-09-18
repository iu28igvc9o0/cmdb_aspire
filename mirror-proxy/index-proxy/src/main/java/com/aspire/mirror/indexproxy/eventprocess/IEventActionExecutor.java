package com.aspire.mirror.indexproxy.eventprocess;

import com.aspire.mirror.indexproxy.domain.MonitorActionRecord;

/**
* action执行接口    <br/>
* Project Name:index-proxy
* File Name:IEventActionExecutor.java
* Package Name:com.aspire.mirror.indexproxy.eventprocess
* ClassName: IEventActionExecutor <br/>
* date: 2018年8月21日 上午9:59:22 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
interface IEventActionExecutor {
	/**
	* doAction:执行动作. <br/>
	*
	* 作者： pengguihua
	* @param bizObj
	* @param bizObj
	*/  
	public void doAction(MonitorActionRecord actionRecord, Object bizObj);
}
