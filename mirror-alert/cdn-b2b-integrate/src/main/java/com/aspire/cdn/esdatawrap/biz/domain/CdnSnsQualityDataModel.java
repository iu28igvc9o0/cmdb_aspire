package com.aspire.cdn.esdatawrap.biz.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert.ItemValueType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: CdnSnsQualityDataModel
 * <p/>
 *
 * 类功能描述: CDN 点播质量数据model
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年12月3日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public class CdnSnsQualityDataModel {
	
	public static final int			SUCCESS_CODE	= 52003;
//	public static final String[]	GROUP_KEY_LIST	= {
//			"apquality", "pcquality", "cquality", "pquality", "ipv6_apquality", "ipv6_pcquality", "ipv6_cquality", "ipv6_pquality"};
	public static final String[]	GROUP_KEY_LIST	= {"pcquality", "cquality", "ipv6_pcquality", "ipv6_cquality"};
	
	public static final int			ALERT_GENERAL_SERVICE_PECENT	= 1;	// 综合质量占比过大
	public static final int			ALERT_FIRST_FRAME_TIME_EXCEED	= 2;	// 首帧时间超标
	
	/*
	 * 以下字段为业务扩展补充
	 * 
	 */
	private String appName;
	private String groupKey;
	
	/*
	 * 以下字段为接口返回 
	 * 
	 */
	private String	qtime;					// log时间	格式:	 2020-12-02T07:45:00Z
	private String	cdn;
	private String	carrier;				// 运营商
	private String	province;				// 省份
	
	@JsonProperty("error")
	private Double	error;					// 播放失败(占比)

	@JsonProperty("broken_count")
	private Double	brokenCount;			// 中断(占比)

	@JsonProperty("buffer_count")
	private Double	bufferCount;			// 卡顿(占比)
	
	@JsonProperty("leave_without_play")
	private Double	leaveWithoutPlay;		// 未播放离开(占比)

	@JsonProperty("first_frame_all_time")
	private Double	firstFrameAllTime;		// 首屏耗时

	@JsonProperty("dns_cost")
	private Double	dnsCost;				// DNS解析耗时

	@JsonProperty("tran_tcp_cost")
	private Double	tranTcpCost;			// TCP耗时

	@JsonProperty("tran_first_receive_cost")
	private Double	tranFirstReceiveCost;	// 首包耗时
	
	public void poupuMetaInfo(String appName, String groupKey) {
		this.appName = appName;
		this.groupKey = groupKey;
	}
	
	public String getProvince() {
		if ("apquality".equalsIgnoreCase(groupKey) || "cquality".equalsIgnoreCase(groupKey)) {
			return "全网";
		}
		return province;
	}
	
	public Pair<Boolean, String> checkDataValid() {
		if (StringUtils.isBlank(appName)) {
			return Pair.of(false, "The 'appName' property is absent.");
		}
		if (StringUtils.isBlank(groupKey)) {
			return Pair.of(false, "The 'groupKey' property is absent.");
		}
		if (StringUtils.isBlank(qtime)) {
			return Pair.of(false, "The 'qtime' property is absent.");
		}
		if (("pcquality".equalsIgnoreCase(groupKey) || "cquality".equalsIgnoreCase(groupKey)
				|| "ipv6_pcquality".equalsIgnoreCase(groupKey) || "ipv6_cquality".equalsIgnoreCase(groupKey)) 
				&& StringUtils.isBlank(carrier)) {
			return Pair.of(false, "The 'carrier' property is absent.");
		}
		if (("pcquality".equalsIgnoreCase(groupKey) || "pquality".equalsIgnoreCase(groupKey)
				|| "ipv6_pcquality".equalsIgnoreCase(groupKey) || "ipv6_pquality".equalsIgnoreCase(groupKey)) 
				&& (StringUtils.isBlank(province) || "unknown".equals(province))) {
			return Pair.of(false, "The 'province' property is blank or innormal.");
		}
		return Pair.of(true, null);
	}
	
	public MetricAlert parse2MetricAlert(boolean isActive, Integer alertKind) {
		// 综合质量告警： 网络失败率 + 卡断率 + 中断率  超过4%
		// 首帧时间告警: 首帧时间 大于 400ms
		MetricAlert metricAlert = new MetricAlert();
		metricAlert.setProvince_name(getProvince());
		metricAlert.setBusiness_source("B2B");
		metricAlert.setSource_identity("snssdk");
		metricAlert.setAlert_level(MetricAlert.ALERT_LEVEL_SERIOUS);
		Instant time = Instant.parse(qtime);
		metricAlert.setAlert_time(time.toEpochMilli());
		metricAlert.setLast_alert_time(time.toEpochMilli());
		metricAlert.setContent(generateAlertContent(alertKind));
		metricAlert.setItem_key("snssdk_" + groupKey + "_t_" + alertKind);
		metricAlert.setMoni_result(isActive ? MetricAlert.MONI_RESULT_ACTIVE : MetricAlert.MONI_RESULT_REVOKE);
		//Map<String, Object> moniTargetObj = 
		Pair<List<String>, Map<String, Object>> pair = constructMoniTargetObject();
		metricAlert.setMoni_target_key(String.join("|", pair.getLeft()));
		metricAlert.setMoni_target_refer_obj(pair.getRight());
		metricAlert.setRecord_time(System.currentTimeMillis());
		if (alertKind.equals(ALERT_GENERAL_SERVICE_PECENT)) {
			metricAlert.setTheme("SDK告警：网络失败率 + 卡断率 + 中断率  超标;");
			metricAlert.setValue_type(ItemValueType.VAL_FLOAT);
			Double percent = (error == null ? 0 : error) + (bufferCount == null ? 0 : bufferCount) + (brokenCount == null ? 0 : brokenCount);
			metricAlert.setValue_float(percent.floatValue());
		} else if (alertKind.equals(ALERT_FIRST_FRAME_TIME_EXCEED)) {
			metricAlert.setTheme("SDK告警：首帧时间超标;");
			metricAlert.setValue_type(ItemValueType.VAL_FLOAT);
			metricAlert.setValue_float(firstFrameAllTime.floatValue());
		}
		metricAlert.setReverse_sync(false);
		metricAlert.setSkip_step(true);
		return metricAlert;
	}
	
	private String generateAlertContent(Integer alertKind) {
		StringBuilder content = new StringBuilder();
		// "apquality", "pcquality", "cquality", "pquality", "ipv6_apquality", "ipv6_pcquality", "ipv6_cquality", "ipv6_pquality"
		if ("apquality".equalsIgnoreCase(groupKey)) {
			content.append("全运营商、全省份：");
		} 
		else if ("ipv6_apquality".equalsIgnoreCase(groupKey)) {
			content.append("全运营商、全省份、ipv6数据：");
		} 
		else if ("pcquality".equalsIgnoreCase(groupKey)) {
			content.append("运营商=").append(carrier).append("、省份=").append(getProvince()).append("：");
		}
		else if ("ipv6_pcquality".equalsIgnoreCase(groupKey)) {
			content.append("运营商=").append(carrier).append("、省份=").append(getProvince()).append("、ipv6数据：");
		}
		else if ("pquality".equalsIgnoreCase(groupKey)) {
			content.append("全运营商、省份=").append(getProvince()).append("：");
		} 
		else if ("ipv6_pquality".equalsIgnoreCase(groupKey)) {
			content.append("全运营商、省份=").append(getProvince()).append("、ipv6数据：");
		} 
		else if ("cquality".equalsIgnoreCase(groupKey)) {
			content.append("运营商=").append(carrier).append("、全省份").append("：");
		} 
		else if ("ipv6_cquality".equalsIgnoreCase(groupKey)) {
			content.append("运营商=").append(carrier).append("、全省份").append("、ipv6数据：");
		}
		if (alertKind.equals(ALERT_GENERAL_SERVICE_PECENT)) {
			content.append("SDK告警：网络失败率 + 卡断率 + 中断率  超标;");
		} else if (alertKind.equals(ALERT_FIRST_FRAME_TIME_EXCEED)) {
			content.append("SDK告警：首帧时间超标;");
		}
		content.append(" 当前检测值：");
		if (alertKind.equals(ALERT_GENERAL_SERVICE_PECENT)) {
			content.append("网络失败率=").append(error).append("%");
			content.append(",卡断率=").append(bufferCount).append("%");
			content.append(",中断率=").append(brokenCount).append("%");
		} else if (alertKind.equals(ALERT_FIRST_FRAME_TIME_EXCEED)) {
			content.append("首帧时间=").append(firstFrameAllTime).append("ms");
		}
		return content.toString();
	}
	
	private Pair<List<String>, Map<String, Object>> constructMoniTargetObject() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put("appName", appName);
		attrMap.put("groupKey", groupKey);
		
		List<String> attrList = new ArrayList<>();
		attrList.add("appName=" + appName);
		attrList.add("groupKey=" + groupKey);
		
		// "apquality", "pcquality", "cquality", "pquality", "ipv6_apquality", "ipv6_pcquality", "ipv6_cquality", "ipv6_pquality"
		if ("pquality".equalsIgnoreCase(groupKey) || "ipv6_pquality".equalsIgnoreCase(groupKey)) {
			attrList.add("province=" + province);
			attrMap.put("groupKey", groupKey);
		} 
		if ("cquality".equalsIgnoreCase(groupKey) || "ipv6_cquality".equalsIgnoreCase(groupKey)) {
			attrList.add("carrier=" + carrier);
			attrMap.put("carrier", carrier);
		} 
		else if ("pcquality".equalsIgnoreCase(groupKey) || "ipv6_pcquality".equalsIgnoreCase(groupKey)) {
			attrList.add("province=" + province);
			attrList.add("carrier=" + carrier);
			attrMap.put("province", province);
			attrMap.put("carrier", carrier);
		}
		return Pair.of(attrList, attrMap);
	}
}
