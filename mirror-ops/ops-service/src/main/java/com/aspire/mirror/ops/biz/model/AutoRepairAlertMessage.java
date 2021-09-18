/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  AlertMessage4AutoRepair.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月25日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.biz.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-service 
 * <p/>
 * 
 * 类名: AutoRepairAlertMessage
 * <p/>
 *
 * 类功能描述: 自愈指标告警消息
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年2月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(of={"assetPool", "deviceIp", "sourceMark", "apItemType", "apItem"})
public class AutoRepairAlertMessage {

	@JsonProperty("idc_type")
	private String	assetPool;			// 资源池

	@JsonProperty("device_ip")
	private String	deviceIp;			// 设备IP

	@JsonProperty("source_mark")
	private String	sourceMark;			// 指标来源 xj: 巡检 gj: 告警

	@JsonProperty("moni_object")
	private String	apItemType;			// 指标类型

	@JsonProperty("item_key")
	private String	apItem;				// 指标
	
	@JsonProperty("cur_moni_value")
	private String	alertItemValue;		// 指标值
	
	@JsonProperty("cache_time_mark")
	private Long 	cacheTimeMark;		// 缓存时间戳   单位 ms

//	@JsonProperty("alert_id")
//	private String	alertId;			
//
//	@JsonProperty("moni_index")
//	private String	alertDesc;
	
	@JsonIgnore
	protected final Map<String, Object>	extendDataMap	= new HashMap<String, Object>(); 	// 扩展数据
	
	@JsonAnyGetter
	public Map<String, Object> getExtendDataMap() {
		return extendDataMap;
	}
	
	@JsonAnySetter
	public void setExtendData(String key, Object value) {
		this.extendDataMap.put(key, value);
	}
}
