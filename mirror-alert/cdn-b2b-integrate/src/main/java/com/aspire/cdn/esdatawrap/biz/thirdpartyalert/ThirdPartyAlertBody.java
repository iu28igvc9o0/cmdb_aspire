package com.aspire.cdn.esdatawrap.biz.thirdpartyalert;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.annotation.AnnotationUtils;

import com.aspire.cdn.esdatawrap.anno.EmptyCheckWithConds;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert.ItemValueType;
import com.aspire.cdn.esdatawrap.biz.umsalert.UmsAlertMessage;
import com.aspire.cdn.esdatawrap.util.Md5Util;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: ThirdPartyAlertBody
 * <p/>
 *
 * 类功能描述: 第三方告警接入消息体
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年8月13日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@Slf4j
public class ThirdPartyAlertBody {
	public static final String	ALERT_TYPE_DEVICE	= "1";
	public static final String	ALERT_TYPE_BIZ		= "2";

	@JsonProperty("source")
	@EmptyCheckWithConds(tipKey="source")
	private String				sourceIdentify;			// 告警来源标识      B2BELK|OTTELK|ZTE|HangYan|PuTian|InternetTV

	@JsonProperty("business_system")
	@EmptyCheckWithConds(tipKey="business_system")
	private String				businessIdentify;		// 全网|各省名

	@JsonProperty("serv_system")
	@EmptyCheckWithConds(conds=ALERT_TYPE_BIZ, tipKey="serv_system")
	private String				servSystem;				// sourceIdentify=OTTELK时取值 OTT， sourceIdentify取其它值时，此字段均取值B2B

	@JsonProperty("manufacturer")
	private String				manufacturer;			// 厂商,平面

	@JsonProperty("monitor_object")
	@EmptyCheckWithConds(tipKey="monitor_object")
	private Map<String, Object>	monitorObject;			// 监控对象JSON定义, 对象的所有键，作为监控对象的属性; 所有键值共同定义唯一监控目标对象;

	@JsonProperty("object_type")
	@EmptyCheckWithConds(tipKey="object_type")
	private String				objectType;				// 告警类型     1-设备    2-业务

	@JsonProperty("device_ip")
	@EmptyCheckWithConds(conds=ALERT_TYPE_DEVICE, tipKey="device_ip")
	private String				deviceIp;				// 设备IP, 当为设备告警时, 此项为必填

	@JsonProperty("item_key")
	@EmptyCheckWithConds(tipKey="item_key")
	private String				itemKey;				// 监控项key

	@JsonProperty("alert_level")
	@EmptyCheckWithConds(tipKey="alert_level")
	private String				alertLevel;				// 告警级别

	@JsonProperty("moni_result")
	@EmptyCheckWithConds(tipKey="moni_result")
	private String				moniResult;				// 1: 新增告警 2: 解除告警

	@JsonProperty("key_comment")
	@EmptyCheckWithConds(tipKey="key_comment")
	private String				alertTitle;				// 告警标题

	@JsonProperty("monitor_index")
	@EmptyCheckWithConds(tipKey="monitor_index")
	private String				alertContent;			// 告警详情

	@JsonProperty("cur_moni_time")
//	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@EmptyCheckWithConds(tipKey="cur_moni_time")
	private String				curMoniTime;			// 当前监测时间

	@JsonProperty("cur_moni_value")
	private String				curMoniValue;			// 当前监测值
	
	
	/** 
	 * 功能描述: 获取需要验证非空字段  
	 * <p>
	 * @return
	 */
	public static List<Pair<Field, EmptyCheckWithConds>> fetchNotEmptyCheckFiledList() {
		List<Pair<Field, EmptyCheckWithConds>> resultList = new ArrayList<>();
		for (Field field : ThirdPartyAlertBody.class.getDeclaredFields()) {
			EmptyCheckWithConds anno = AnnotationUtils.getAnnotation(field, EmptyCheckWithConds.class);
			if (anno != null) {
				field.setAccessible(true);
				resultList.add(Pair.of(field, anno));
			}
		}
		return resultList;
	}
	
	public UmsAlertMessage parseUmsAlert() {
		UmsAlertMessage umsAlert = new UmsAlertMessage();
		umsAlert.setAlertId(generateRawAlertId());
		umsAlert.setSource(sourceIdentify);
		umsAlert.setBusinessSystem(businessIdentify);
		umsAlert.setServSystem(servSystem);
		umsAlert.setDeviceIp(deviceIp);
		umsAlert.setObjectType(objectType);
		umsAlert.setAlertLevel(alertLevel);
		// 对于电视预警，告警级别根据告警内容里面的红橙黄关键字，设置成相应的级别
		if ("tv_pre_warn".equals(itemKey) && StringUtils.isNotBlank(alertContent)) {
			if (alertContent.contains("红色")) {
				umsAlert.setAlertLevel(String.valueOf(MetricAlert.ALERT_LEVEL_SERIOUS));
			} 
			else if (alertContent.contains("橙色")) {
				umsAlert.setAlertLevel(String.valueOf(MetricAlert.ALERT_LEVEL_HIGH));
			}
			else if (alertContent.contains("黄色")) {
				umsAlert.setAlertLevel(String.valueOf(MetricAlert.ALERT_LEVEL_MIDDLE));
			}
		}
		umsAlert.setMoniResult(moniResult);
		umsAlert.setKeyComment(alertTitle);
		umsAlert.setMonitorIndex(alertContent);
		String joinMoniObj = getMonitorObjectJoinStr();
		umsAlert.setMonitorObject(joinMoniObj);
		umsAlert.setAlertDesc(alertContent);
		umsAlert.setAlertStartTime(curMoniTime);
		umsAlert.setCurMoniTime(curMoniTime);
		umsAlert.setCurMoniValue(curMoniValue);
		umsAlert.setItemId(generateItemId());
		umsAlert.setItemKey(itemKey);
		umsAlert.addExtAttr("device_mfrs", manufacturer);
		return umsAlert;
	}
	
	public MetricAlert parse2MetricAlert() {
		MetricAlert metricAlert = new MetricAlert();
		metricAlert.setSource_identity(sourceIdentify);
		metricAlert.setProvince_name(businessIdentify);
		metricAlert.setBusiness_source(servSystem);
		metricAlert.setMoni_target_refer_obj(monitorObject);
		metricAlert.setMoni_target_key(getMonitorObjectJoinStr());
		metricAlert.setAlert_level(new Integer(alertLevel));
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			metricAlert.setAlert_time(format.parse(curMoniTime).getTime());
			metricAlert.setLast_alert_time(metricAlert.getAlert_time());
		} catch (Exception e) {
			log.error(null, e);
		}
		metricAlert.setTheme(alertTitle);
		metricAlert.setContent(alertContent);
		metricAlert.setItem_key(itemKey);
		metricAlert.setValue_type(ItemValueType.VAL_TEXT);
		metricAlert.setValue_text(String.valueOf(curMoniValue));
		metricAlert.setMoni_result(new Integer(moniResult));
		metricAlert.setReverse_sync(true);
		metricAlert.setSkip_step(true);
		return metricAlert;
	}
	
	public static void main(String[] args) {
		ThirdPartyAlertBody body = new ThirdPartyAlertBody();
		body.setAlertLevel("5");
		body.setSourceIdentify("InternetTV");
		body.setBusinessIdentify("浙江");
		body.setServSystem("OTT");
		body.setObjectType(ALERT_TYPE_BIZ);
		body.setItemKey("tv_pre_warn");
		body.setAlertLevel("3");
		
		Map<String, Object>	moniObj = new HashMap<>();
		moniObj.put("biz_key", "tv_pre_warn");
		moniObj.put("alam_data_id", "20210127095723800010000007452");
		body.setMonitorObject(moniObj);
		
		System.out.println(body.generateRawAlertId());
		
//		keyList.add(sourceIdentify);
//		keyList.add(businessIdentify);
//		if (ALERT_TYPE_DEVICE.equals(objectType)) {
//			keyList.add(deviceIp);
//		} 
//		else if (ALERT_TYPE_BIZ.equals(objectType)) {
//			keyList.add(servSystem);
//		}
//		keyList.add(getMonitorObjectJoinStr());
//		keyList.add(itemKey);
//		keyList.add(alertLevel);
	}
	
//	private String getAlertKey() {
//		return Md5Util.getMD5String(String.join("|", businessIdentify, getMonitorObjectJoinStr(), itemKey));
//	}
	
	/** 
	 * 功能描述: 根据   businessIdentify + sourceIdentify + monitor_object + item_key 生成item_id <p>
	 * 
	 * 注意：为什么采用以上3个键组成item_id，原因由UMS以下查找告警的逻辑决定： <br/>
	 * 参考com.aspire.mirror.alert.server.v2.biz.AlertsHandleV2Helper.handleAlertActive() <br/>
	 * 
	 * <pre>
	 * AlertsV2 alertQuery = new AlertsV2();
	 *   if (AlertsDTOV2.OBJECT_TYPE_BIZ.equals(alert.getObjectType())) {
     *      alertQuery.setBizSys(alert.getBizSys());
     *   } else {
     *       alertQuery.setDeviceIp(alert.getDeviceIp());
     *   }
     *   alertQuery.setItemId(alert.getItemId());
     *   alertQuery.setAlertLevel(alert.getAlertLevel());
     *   alertQuery.setSource(alert.getSource());
     *   List<AlertsDTOV2> queryList = alertsBiz.select(alertQuery);
	 * </pre>
	 * 
	 * <p>
	 * @return
	 */
	private String generateItemId() {
		return Md5Util.getMD5String(String.join("|", businessIdentify, sourceIdentify, getMonitorObjectJoinStr(), itemKey));
	}
	
	/** 
	 * 功能描述: 生成告警id 	<br/>
	 * 业务告警： 根据 source + business_system + serv_system + monitor_object + item_key + alert_level
	 * 系统告警： 根据 source + business_system + device_ip + monitor_object + item_key + alert_level 
	 * <p>
	 * @return
	 */
	private String generateRawAlertId() {
		List<String> keyList = new ArrayList<>();
		keyList.add(sourceIdentify);
		keyList.add(businessIdentify);
		if (ALERT_TYPE_DEVICE.equals(objectType)) {
			keyList.add(deviceIp);
		} 
		else if (ALERT_TYPE_BIZ.equals(objectType)) {
			keyList.add(servSystem);
		}
		keyList.add(getMonitorObjectJoinStr());
		keyList.add(itemKey);
		keyList.add(alertLevel);
		return Md5Util.getMD5String(StringUtils.join(keyList, "|"));
	}
	
	private String getMonitorObjectJoinStr() {
		List<String> keyList = new ArrayList<>(monitorObject.keySet());
		Collections.sort(keyList);	// 根据key排序,保证顺序
		List<String> valList = new ArrayList<>();
		for (String key : keyList) {
			valList.add(String.valueOf(monitorObject.get(key)));
		}
		return StringUtils.join(valList, "|");
	}
	
}

