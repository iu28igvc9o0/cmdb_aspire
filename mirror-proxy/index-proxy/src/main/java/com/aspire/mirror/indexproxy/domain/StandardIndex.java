package com.aspire.mirror.indexproxy.domain;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
* 标准指标对象    <br/>
* Project Name:index-proxy
* File Name:StandardIndex.java
* Package Name:com.aspire.mirror.indexproxy.domain
* ClassName: StandardIndex <br/>
* date: 2018年11月21日 下午4:58:04 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@Data
@JsonInclude(Include.NON_NULL)
public class StandardIndex {

	@JsonProperty("object_type")
	private String objectType;
	
	@JsonProperty("object_id")
    private String objectId;

	@JsonProperty("room_id")
	private String roomId;
	
	@JsonProperty("item_id")
	private String itemId;
	
	@JsonProperty
	private Integer clock;
	
	@JsonProperty
	private String value;
	
	@JsonProperty("pre_value")
	private String preValue;
	
	@JsonProperty("group_key")
	private String groupKey;						// 分组key

	@JsonProperty("group_desc")
	private List<Map<String, Object>> groupDesc;	// 告警分组描述项

	@JsonProperty("remark")
	private String remark;
	
	@JsonProperty("extend_obj")
	private String extendObj;		// 扩展业务对象   比如为  巡检report_id, 或其它自定义的业务对象， 这个属性由业务定制
}
