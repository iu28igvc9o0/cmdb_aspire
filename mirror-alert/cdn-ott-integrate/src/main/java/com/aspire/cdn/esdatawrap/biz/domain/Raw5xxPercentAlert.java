package com.aspire.cdn.esdatawrap.biz.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.aspire.cdn.esdatawrap.biz.metricalert.IMetricAlertParse;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.config.model.Req5xxPecentConfigProps;
import com.aspire.cdn.esdatawrap.util.EncryptUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jayway.jsonpath.DocumentContext;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: Raw5xxPercentAlert
 * <p/>
 *
 * 类功能描述: 5xx百分比raw告警
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月18日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Raw5xxPercentAlert implements IEsDocMarshall, IMetricAlertParse {
	public static final String				THEME_TEMPLATE		= "%sOTT-CDN告警 5XX占比过高";
	public static final String				CONTENT_TEMPLATE	= "%s %s：%s 错误占比为：%s";
	private static final DateTimeFormatter	TIME_FORMAT			= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final String				ITEM_KEY			= "5xx_percent";

	@JsonProperty("province_name")
	private String				provinceName;
	
	@JsonProperty("source_identity")
	private String				sourceIdentity;
	
	@JsonProperty("business_source")
	private String				businessSource;

	@JsonProperty("manufacturer")
	private String				manufacturer;

	@JsonProperty("req_domain")
	private String				reqDomain;
	
	@JsonProperty("cp_name")
	private String				cpName;
	
	@JsonProperty("join_server_ip")
	private String				joinServerIp;

	@JsonProperty("last_log_time")
	private Long				lastLogTime;

	@JsonProperty("sum_5xx_count")
	private Long				sum5xxCount;

	@JsonProperty("total_req_count")
	private Long				totalReqCount;

	@JsonProperty("percent_5xx")
	private Float				percent5xx;

	@JsonProperty("step_count")
	private Integer				stepCount;
	
	/** 
	 * 功能描述: 根据"省份+制造商+域名"生成记录id  
	 * <p>
	 * @return
	 */
	@JsonIgnore
	public String getEsDocId() {
		return EncryptUtil.base64EncryptUrlSafe(provinceName + "|" + manufacturer + "|" + reqDomain);
	}
	
	@JsonIgnore
	public String getAlertTheme() {
		return String.format(THEME_TEMPLATE, manufacturer);
	}
	
	@JsonIgnore
	public String getAlertContent() {
		LocalDateTime localTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastLogTime), ZoneId.systemDefault());
		String logTimeFormat = TIME_FORMAT.format(localTime);
		return String.format(CONTENT_TEMPLATE, logTimeFormat, StringUtils.isBlank(cpName) ? "节点" : cpName, 
				reqDomain, String.format("%.2f", percent5xx * 100) + "%");
	}
	
	@JsonIgnore
	public String getItemId() {
		List<String> itemIdKeys = Arrays.asList(provinceName, manufacturer, reqDomain, ITEM_KEY);
		return EncryptUtil.base64EncryptUrlSafe(StringUtils.join(itemIdKeys, '|'));
	}
	
	public static Raw5xxPercentAlert buildFromBucket(DocumentContext bucketCtx, String businessSource) {
		Raw5xxPercentAlert rawAlert = new Raw5xxPercentAlert();
		rawAlert.setProvinceName(bucketCtx.read("$.key.province_name_group"));
		rawAlert.setBusinessSource(businessSource);
		rawAlert.setSourceIdentity("OTTELK");
        if ("B2B".equalsIgnoreCase(businessSource)) {
			rawAlert.setSourceIdentity("B2BELK");
		}
		rawAlert.setReqDomain(bucketCtx.read("$.key.req_domain_group"));
		rawAlert.setManufacturer(bucketCtx.read("$.key.manufacture_group"));
		Number logTimeStr = bucketCtx.read("$.last_log_time.value");
		Number sum_5xx_count = bucketCtx.read("$.sum_5xx_count.value");
		Number total_req_count = bucketCtx.read("$.total_req_count.value");
		Number percent_5xx = bucketCtx.read("$.percent_5xx.value");
		rawAlert.setLastLogTime(logTimeStr.longValue());
		rawAlert.setSum5xxCount(sum_5xx_count.longValue());
		rawAlert.setTotalReqCount(total_req_count.longValue());
		rawAlert.setPercent5xx(Math.round(percent_5xx.floatValue() * 1000) / 1000f);
		return rawAlert;
	}
	
	/** 
	 * 功能描述: 判断是否满足原始告警的判断条件, 返回键值对： 键为告警状态, 0 不生成告警   	1 生成告警；  2 生成消警；   值为告警级别；
	 * <p>
	 * @return
	 */
	@JsonIgnore
	public Pair<Integer, Integer> resolveRawAlertStatus(Req5xxPecentConfigProps configProps) {
		if (totalReqCount == null || totalReqCount.longValue() == 0
				|| totalReqCount < configProps.getSumTotalCountFloor()) {
			return Pair.of(0, null);
		}
		if (percent5xx.floatValue() >= configProps.getSeriousLevelFloorVal()) {
			return Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_SERIOUS);
		}
		if (percent5xx.floatValue() >= configProps.getHighLevelFloorVal()) {
			return Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_HIGH);
		}
		if (percent5xx.floatValue() >= configProps.getMiddleLevelFloorVal()) {
			return Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_MIDDLE);
		}
		if (percent5xx.floatValue() >= configProps.getLowLevelFloorVal()) {
			return Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_LOW);
		}
		return Pair.of(MetricAlert.MONI_RESULT_REVOKE, MetricAlert.ALERT_LEVEL_LOW);
	}
	
	public MetricAlert parse2MetricAlert(int moniResult, int alertLevel) {
		MetricAlert alert = new MetricAlert();
		alert.setProvince_name(getProvinceName());
		alert.setBusiness_source(getBusinessSource());
        alert.setSource_identity(getSourceIdentity());
		alert.setMoni_target_key(String.join("|", getSourceIdentity(), getBusinessSource(), getManufacturer(), getReqDomain()));
		Map<String, Object> moniTargetReferObj = new HashMap<>();
		moniTargetReferObj.put("sourceIdentity", getSourceIdentity());
        moniTargetReferObj.put("businessSource", getBusinessSource());
		moniTargetReferObj.put("manufacturer", getManufacturer());
		moniTargetReferObj.put("reqDomain", getReqDomain());
//		moniTargetReferObj.put("cpName", getCpName());
		alert.setMoni_target_refer_obj(moniTargetReferObj);
		alert.setItem_key(ITEM_KEY);
		alert.setAlert_level(alertLevel);
		alert.setMoni_result(moniResult);
		alert.setAlert_time(getLastLogTime());
		alert.setLast_alert_time(getLastLogTime());
		alert.setTheme(getAlertTheme());
		alert.setContent(getAlertContent());
		alert.setValue_type(MetricAlert.ItemValueType.VAL_FLOAT);
		alert.setValue_float(getPercent5xx());
		Map<String, Object> referInfo = new HashMap<>();
		referInfo.put("sum_5xx_count", sum5xxCount);
		referInfo.put("total_req_count", totalReqCount);
		referInfo.put("percent_5xx", percent5xx);
//		referInfo.put("join_server_ip", joinServerIp);
		alert.setRefer_info(referInfo);
		return alert;
	}
	
	public static void main(String[] args) {
		Raw5xxPercentAlert alert = new Raw5xxPercentAlert();
		alert.setProvinceName("湖南");
		alert.setManufacturer("华为");
		alert.setReqDomain("www.qq.com");
		alert.setLastLogTime(Instant.now().toEpochMilli());
		alert.setSum5xxCount(100l);
		alert.setTotalReqCount(900l);
		alert.setPercent5xx(0.16f);
		System.out.println(alert.toEsDoc());
	}
}
