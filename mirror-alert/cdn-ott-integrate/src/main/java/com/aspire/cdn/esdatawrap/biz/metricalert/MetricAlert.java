package com.aspire.cdn.esdatawrap.biz.metricalert;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;

import com.aspire.cdn.esdatawrap.biz.domain.IEsDocMarshall;
import com.aspire.cdn.esdatawrap.biz.umsalert.UmsAlertMessage;
import com.aspire.cdn.esdatawrap.util.Md5Util;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: MetricAlert
 * <p/>
 *
 * 类功能描述: 指标告警记录, 对应到ES的 metric_alert索引 <p/>
 * 注意：<br/>   
 * 1. province_name(资源池) +  moni_target_code  +  item_id 确定同一个监控对象的同一个监控告警  <p>
 * 2. province_name(资源池) +  moni_target_code  +  item_id + alert_level 唯一确定一条告警 
 * 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月23日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public final class MetricAlert implements IEsDocMarshall {
	public static final Integer MONI_RESULT_ACTIVE 			= 1;	// 告警
	public static final Integer MONI_RESULT_REVOKE 			= 2;	// 消警
	public static final Integer HISTORY_FLAG_CURRENT		= 0;	// 当前告警
	public static final Integer HISTORY_FLAG_HISTORY		= 1;	// 历史告警
	public static final DateTimeFormatter	TIME_FORMAT		= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static final Integer ALERT_LEVEL_LOW	 			= 2;
	public static final Integer ALERT_LEVEL_MIDDLE			= 3;
	public static final Integer ALERT_LEVEL_HIGH 			= 4;
	public static final Integer ALERT_LEVEL_SERIOUS	 		= 5;
	public static final Long 	NO_SCAN_MARK_TIME			= -1L;
	
	private String				province_name;								// 省份, 当成资源池
	private String				source_identity;							// 来源标识:  B2BELK, OTTELK, 中兴 , 杭研 , 普天
	private String				business_source;							// 业务源   B2B|OTT
	private String 				moni_target_key;							// 监控目标对象key, 由业务键生成唯一key, 根据业务决定
	private Map<String, Object> moni_target_refer_obj;						// 监控目标对象信息, 例如 主机IP  或者   域名,制造商等信息
	private String				item_key;									// 监控项key
	private Integer				alert_level = 2;							// 告警级别： 2-低，3-中，4-高，5-重大
	private Integer				moni_result;								// 1: 告警	2： 消警
	private Integer				history_flag = HISTORY_FLAG_CURRENT;		// 历史标记(注意不是区分活动告警、历史告警)	0: 当前活动	1：当前历史
	private Long				alert_time;									// 告警/消警时间
	private Long				last_alert_time;							// 最新告警时间
	private String				theme;										// 告警主题
	private String				content;									// 告警内容
	private ItemValueType		value_type;
	private Float				value_float;
	private Float				value_integer;
	private String				value_text;	
	private Integer				step_count	= 1;							// 未消除之前的连续告警次数
	private Map<String, Object>	refer_info;									// 其它属性
	private Long				record_time = System.currentTimeMillis();	// 告警记录入库时间
	private Long				scan_mark_time	= NO_SCAN_MARK_TIME;		// 告警的可扫描时间（第三方扫描告警时，通过指定此值，定时获取最新的告警） 
	
	/** 
	 * 功能描述: 告警id生成规则:   province_name|item_id|alert_level
	 * <p>
	 * @return
	 */
	@JsonProperty("alert_key")
	public String getAlertKey() {
		return Md5Util.getMD5String(String.join("|", province_name, moni_target_key, item_key));
	}
	
	/** 
	 * 功能描述: 计算ES存储文档id  
	 * <p>
	 * @return
	 */
	@JsonIgnore
	public String generateDocId() {
		return Md5Util.getMD5String(String.join("|", province_name, moni_target_key, item_key, 
				String.valueOf(alert_level), String.valueOf(record_time)));
	}
	
	@JsonIgnore
	public Pair<Boolean, String> selfDataCheck() {
		if (StringUtils.isBlank(province_name) || StringUtils.isBlank(moni_target_key) 
				|| StringUtils.isBlank(item_key) || alert_level == null 
				|| moni_result == null || alert_time == null || record_time == null || scan_mark_time == null) {
			return Pair.of(false, "The required fields must be non empty, please check:" + this.toString());
		}
		return Pair.of(true, null);
	}
	
//	@JsonIgnore
//	public Map<String, Object> resolveActiveAlertUpdateParams() {
//		Map<String, Object> params = new HashMap<>();
//		params.put("content", content);
//		params.put("last_alert_time", last_alert_time);
//		params.put("step_count", step_count);
//		params.put("scan_mark_time", scan_mark_time);
//		params.put("value_float", value_float);
//		params.put("value_integer", value_integer);
//		params.put("value_text", value_text);
//		return params;
//	}
//	
//	@JsonIgnore
//	public Map<String, Object> resolveActiveAlertRevokeUpdateParams() {
//		Map<String, Object> params = new HashMap<>();
//		params.put("history_flag", MetricAlert.HISTORY_FLAG_HISTORY);
//		return Collections.singletonMap("doc", params);
//	}
//	
//	@JsonIgnore
//	public void set2NoScanMarkTime() {
//		scan_mark_time = NO_SCAN_MARK_TIME;
//	}
//	
//	@JsonIgnore
//	public void refreshScanMarkTime() {
//		scan_mark_time = System.currentTimeMillis();
//	}
	
	@JsonIgnore
	public String resolveItemId() {
		return Md5Util.getMD5String(String.join("|", province_name, business_source, moni_target_key, item_key));
	}
	
	@JsonIgnore
	public String getAlertValueString() {
		if (ItemValueType.VAL_FLOAT == this.value_type) {
			return String.valueOf(this.value_float);
		} 
		if (ItemValueType.VAL_INTEGER == this.value_type) {
			return String.valueOf(this.value_integer);
		}
		return this.value_text;
	}
	
	public MetricAlert copy() {
		MetricAlert copy = new MetricAlert();
		BeanUtils.copyProperties(this, copy);
		return copy;
	}
	
	public static List<UmsAlertMessage> parse2UmsAlertMsgList(List<MetricAlert> alertList) {
		return alertList.stream().map(alert -> {
			UmsAlertMessage msg = new UmsAlertMessage();
			msg.setSource(alert.getSource_identity());
			msg.setBusinessSystem(alert.getProvince_name());
			msg.setServSystem(alert.getBusiness_source());
			Object joinServerIp = alert.getRefer_info() == null ? null : alert.getRefer_info().get("join_server_ip");
			if (joinServerIp != null) {
				msg.setDeviceIp(String.valueOf(joinServerIp));
			}
			Map<String, Object> moniTargetReferObj = alert.getMoni_target_refer_obj();
			if (moniTargetReferObj != null) {
				msg.addExtAttr("device_mfrs", moniTargetReferObj.get("manufacturer"));
				msg.addExtAttr("cp_name", moniTargetReferObj.get("cpName"));
				msg.addExtAttr("req_domain", moniTargetReferObj.get("reqDomain"));
			}
			
			msg.setAlertId(alert.getAlertKey() + "_" + alert.getAlert_level());
			msg.setAlertLevel(alert.getAlert_level().toString());
			msg.setMoniResult(String.valueOf(alert.getMoni_result()));
			msg.setKeyComment(alert.getTheme());
			msg.setMonitorIndex(alert.getContent());
			msg.setMonitorObject(alert.getMoni_target_key());
			msg.setAlertDesc(alert.getContent());
			LocalDateTime localStartTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(alert.getAlert_time()), ZoneId.systemDefault());
			msg.setAlertStartTime(TIME_FORMAT.format(localStartTime));
			LocalDateTime localCurrTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(alert.getLast_alert_time()), ZoneId.systemDefault());
			msg.setCurMoniTime(TIME_FORMAT.format(localCurrTime));
			msg.setCurMoniValue(alert.getAlertValueString());
			msg.setItemId(alert.resolveItemId());
			msg.setItemKey(alert.getItem_key());
			msg.setObjectType(UmsAlertMessage.OBJECT_TYPE_BIZ);
			return msg;
		}).collect(Collectors.toList());
	}
	
	@Override
	public String toString() {
		return String.join("|", province_name, moni_target_key, item_key, String.valueOf(alert_level), String.valueOf(alert_time));
	}
	
	// 值类型枚举
	public static enum ItemValueType {
		VAL_TEXT, VAL_INTEGER, VAL_FLOAT;
	}
}
