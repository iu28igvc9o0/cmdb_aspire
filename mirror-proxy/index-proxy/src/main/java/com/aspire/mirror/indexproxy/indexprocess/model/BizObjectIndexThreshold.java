package com.aspire.mirror.indexproxy.indexprocess.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 指标值阈值事件业务对象    <br/>
* Project Name:index-proxy
* File Name:BizObjectIndexThreshold.java
* Package Name:com.aspire.mirror.indexproxy.indexprocess.model
* ClassName: BizObjectIndexThreshold <br/>
* date: 2018年8月20日 下午5:25:37 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
public class BizObjectIndexThreshold {
	@JsonProperty("extend_obj")
	private String extendObj;		// 扩展业务对象   比如为  巡检report_id, 或业务告警的其它个性json串， 这个属性由业务定制
	
	@JsonProperty("object_type")
	private String objectType;
	
	@JsonProperty("object_id")
    private String objectId;

	@JsonProperty("room_id")
	private String roomId;
	
	@JsonProperty
	private String itemId;		// 指标id
	@JsonProperty
	private String itemName;	// 指标name
	@JsonProperty
	private Integer clock;		// 监控值时间戳(秒)
	@JsonProperty
	private String itemValue;	// 指标值字面值
	@JsonProperty
	private String triggerId;	// 触发器id
	@JsonProperty
	private String triggerName;	// 触发器name
	@JsonProperty
	private String priority;	// 指标异常级别	
	@JsonProperty
	private String status;		// 0:指标正常      1:指标异常
	
	@JsonProperty("group_key")
	private String groupKey;	// 告警分组
	@JsonProperty("group_desc")
	private List<Map<String, Object>> groupDesc;	// 告警分组描述项
	@JsonProperty("remark")
	private String remark;		// remark
}
