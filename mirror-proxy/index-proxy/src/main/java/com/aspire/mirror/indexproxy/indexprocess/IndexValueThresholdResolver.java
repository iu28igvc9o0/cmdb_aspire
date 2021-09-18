package com.aspire.mirror.indexproxy.indexprocess;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;
import com.aspire.mirror.indexproxy.indexprocess.model.MonitorItemValue;

/**
* 指标值阈值处理接口, 返回每个触发器的匹配结果  <br/>
* Project Name:index-proxy
* File Name:IndexValueThresholdResolver.java
* Package Name:com.aspire.mirror.indexproxy.indexprocess
* ClassName: IndexValueThresholdResolver <br/>
* date: 2018年8月16日 下午3:56:14 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
interface IndexValueThresholdResolver {
	/**
	* 指标值阈值匹配. <br/>
	*
	* 作者： pengguihua
	* @param item
	* @param triggerList
	* @return
	*/  
	public List<Pair<MonitorTriggerRecord, Boolean>> resolveThresholds(
			MonitorItemValue itemVal, List<MonitorTriggerRecord> triggerList);
}	
