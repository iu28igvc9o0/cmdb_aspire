package com.aspire.mirror.collect.biz.itemvaluefetch;

import java.util.List;

import com.aspire.mirror.collect.api.payload.ObjectItemValueWrap;
import com.aspire.mirror.collect.api.payload.FetchObjectItemsValueRequest;

/**
* 监控项值抓取接口    <br/>
* Project Name:collect-service
* File Name:IMonitorItemValueFetch.java
* Package Name:com.aspire.mirror.collect
* ClassName: IMonitorItemValueFetch <br/>
* date: 2018年9月6日 上午9:54:12 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
public interface IMonitorItemValueFetch {
	
	/**
	* 处理设备的监控项值抓取. <br/>
	*
	* 作者： pengguihua
	* @param request
	* @param resultHolder
	*/  
	public void fetchMonitorItemValues(
			FetchObjectItemsValueRequest request, final List<ObjectItemValueWrap> resultHolder);
}
