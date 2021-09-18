package com.aspire.cdn.esdatawrap.biz.umsalert;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: cdn-integrate 
 * <p/>
 * 
 * 类名: UmsAlertMessage
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月22日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public final class UmsAlertMessage {
	public static final String	OBJECT_TYPE_DEVICE	= "1";
	public static final String	OBJECT_TYPE_BIZ		= "2";

	@JsonProperty("moni_result")
	private String				moniResult;					// 1: 新增告警 2: 解除告警

	@JsonProperty("alert_id")
	private String				alertId;

	@JsonProperty("source")
	private String				source;						// 告警来源:  B2BELK,OTTELK,ZTE,HangYan,PuTian

	@JsonProperty("device_ip")
	private String				deviceIp;

	@JsonProperty("serv_system")
	private String				servSystem;					//  当source字段值为OTTELK时, 此字段值为OTT，source取其它值时，此字段值为B2B

	@JsonProperty("monitor_object")
	private String				monitorObject;				// 监控对象

	@JsonProperty("monitor_index")
	private String				monitorIndex;				// 告警内容

	@JsonProperty("alert_level")
	private String				alertLevel;					// 2-低，3-中，4-高，5-重大

	@JsonProperty("alert_desc")
	private String				alertDesc;

	@JsonProperty("cur_moni_time")
	private String				curMoniTime;

	@JsonProperty("cur_moni_value")
	private String				curMoniValue;

	@JsonProperty("business_system")
	private String				businessSystem;				// 如果为全网的告警，则填写"全网"，否则填写对应的省份名

	@JsonProperty("item_id")
	private String				itemId;
	
	@JsonProperty("item_key")
	private String				itemKey;

	@JsonProperty("alert_start_time")
	private String				alertStartTime;

	@JsonProperty("object_type")
	private String				objectType;					// 1-设备（默认），2-业务

	@JsonProperty("key_comment")
	private String				keyComment;					// 告警标题
	
	private Map<String, Object> ext;
	
	public void addExtAttr(String attrKey, Object attrVal) {
		if (attrVal == null) {
			return;
		}
		if (ext == null) {
			ext = new HashMap<>();
		}
		ext.put(attrKey, attrVal);
	}
}
