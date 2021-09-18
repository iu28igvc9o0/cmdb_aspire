package com.aspire.mirror.indexadapt.adapt.mirrorbizmonitordb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 业务监控标准指标扩展对象    <br/>
* Project Name:index-proxy
* File Name:MirrorBizMonitorExtendObject.java
* Package Name:com.aspire.mirror.indexadapt.adapt.migubizmonitordb.model
* ClassName: MirrorBizMonitorExtendObject <br/>
* date: 2018年10月12日 下午6:30:10 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Data
@JsonInclude(Include.NON_NULL)
public class MirrorBizMonitorExtendObject {
	
//	@JsonProperty("trigger_id")
//	private String triggerId;
	
	@JsonProperty("trigger_name")
	private String triggerName;
	
	@JsonProperty("item_id")
	private String itemId;
	
	@JsonProperty("item_name")
	private String itemName;
	
	@JsonProperty("biz_sys")
	private String bizSys;
	
	@JsonProperty("biz_sys_name")
	private String bizSysName;
}
