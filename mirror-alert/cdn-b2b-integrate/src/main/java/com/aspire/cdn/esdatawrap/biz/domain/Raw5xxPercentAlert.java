package com.aspire.cdn.esdatawrap.biz.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.aspire.cdn.esdatawrap.biz.metricalert.IMetricAlertParse;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.config.model.FocusDomainConfigProps;
import com.aspire.cdn.esdatawrap.config.model.Req5xxPecentConfigProps;
import com.aspire.cdn.esdatawrap.util.EncryptUtil;
import com.aspire.cdn.esdatawrap.util.JsonUtil;
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
	public static final String				ITEM_KEY					= "5xx_percent";
	public static final String				THEME_TEMPLATE				= "%s-%sB2B-CDN告警 5XX占比过高";
	public static final String				CONTENT_TEMPLATE			= "%s %s：%s：%s 带宽：%s%s, 总请求数：%s, 5xx数量：%s, 5XX错误占比为：%s";
	private static final DateTimeFormatter	TIME_FORMAT					= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public static final String				ITEM_KEY_PERCENT_606		= "606_percent";
	public static final String				THEME_TEMPLATE_PERCENT_606  = "%s-%sB2B-CDN告警 606占比过高";
	public static final String			CONTENT_TEMPLATE_PERCENT_606    = "%s %s：%s：%s 带宽：%s%s, 总请求数：%s, 606数量：%s, 606错误占比为：%s";

	public static final String				ITEM_KEY_INIT_DEALY			= "init_delay_5_times";
	public static final String				THEME_TEMPLATE_INIT_DEALY	= "%s-%sB2B-CDN告警  首包时延突增达到5倍";
	public static final String				CONTENT_TEMPLATE_INIT_DEALY	= "%s %s：%s：%s 带宽：%s%s, 总请求数：%s, 首包时延倍增达到5倍：上次时延：%sms, 本次时延：%sms";

	private static final String[]           BAND_WIDTH_UNIT_ARR			= {"bps", "Kbps", "Mbps", "Gbps"};
	
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
	
	@JsonProperty("sum_606_count")
	private Long				sum606Count;

	@JsonProperty("total_req_count")
	private Long				totalReqCount;

	@JsonProperty("percent_5xx")
	private Float				percent5xx;
	
	@JsonProperty("percent_606")
	private Float				percent606;
	
	@JsonProperty("bandwidth")
	private Float				bandwidth;
	
	@JsonProperty("bandwidth_unit")
	private String				bandwidthUnit;
	
	@JsonProperty("init_delay_value")
	private Float 				initDelayValue;

	@JsonProperty("step_count")
	private Integer				stepCount;
	
	private InitDelay5TimesAlertCache previousItem;
	
//	/** 
//	 * 功能描述: 根据"省份+制造商+域名"生成记录id  
//	 * <p>
//	 * @return
//	 */
//	@JsonIgnore
//	public String getEsDocId() {
//		return EncryptUtil.base64EncryptUrlSafe(provinceName + "|" + manufacturer + "|" + reqDomain);
//	}
	
	@JsonIgnore
	public String getAlertTheme() {
		return String.format(THEME_TEMPLATE, provinceName, manufacturer);
	}
	
	@JsonIgnore
	public String getAlertThemepPercent606() {
		return String.format(THEME_TEMPLATE_PERCENT_606, provinceName, manufacturer);
	}
	
	@JsonIgnore
	public String getInitDelayAlertTheme() {
		return String.format(THEME_TEMPLATE_INIT_DEALY, provinceName, manufacturer);
	}
	
	@JsonIgnore
	public String getAlertContent() {
		LocalDateTime localTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastLogTime), ZoneId.systemDefault());
		String logTimeFormat = TIME_FORMAT.format(localTime);
		String provManu = provinceName + (StringUtils.isBlank(manufacturer) ? "" : "-" + manufacturer); 
		return String.format(CONTENT_TEMPLATE, logTimeFormat, provManu, StringUtils.isBlank(cpName) ? "域名" : cpName, 
				reqDomain, bandwidth, bandwidthUnit, totalReqCount, sum5xxCount, String.format("%.2f", percent5xx * 100) + "%");
	}
	
	@JsonIgnore
	public String getAlertContentPercent606() {
		LocalDateTime localTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastLogTime), ZoneId.systemDefault());
		String logTimeFormat = TIME_FORMAT.format(localTime);
		String provManu = provinceName + (StringUtils.isBlank(manufacturer) ? "" : "-" + manufacturer); 
		return String.format(CONTENT_TEMPLATE_PERCENT_606, logTimeFormat, provManu, StringUtils.isBlank(cpName) ? "域名" : cpName, 
				reqDomain, bandwidth, bandwidthUnit, totalReqCount, sum606Count, String.format("%.2f", percent606 * 100) + "%");
	}
	
	@JsonIgnore
	public String getInitDelayAlertContent() {
		LocalDateTime localTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastLogTime), ZoneId.systemDefault());
		String logTimeFormat = TIME_FORMAT.format(localTime);
		String provManu = provinceName + (StringUtils.isBlank(manufacturer) ? "" : "-" + manufacturer);
		return String.format(CONTENT_TEMPLATE_INIT_DEALY, logTimeFormat, provManu, StringUtils.isBlank(cpName) ? "域名" : cpName, 
				reqDomain, bandwidth, bandwidthUnit, totalReqCount, this.previousItem.getInitDelayValue(), initDelayValue);
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
		rawAlert.setSourceIdentity("B2BELK");
		if ("OTT".equalsIgnoreCase(businessSource)) {
			rawAlert.setSourceIdentity("OTTELK");
		}
		rawAlert.setReqDomain(bucketCtx.read("$.key.req_domain_group"));
		rawAlert.setCpName(bucketCtx.read("$.key.cp_name_group"));
		rawAlert.setManufacturer(JsonUtil.readWithDefault(bucketCtx, "$.key.manufacture_group", "-"));
		
		List<String> serverIpList = new ArrayList<>();
		Integer serverIpCount = JsonUtil.readWithDefault(bucketCtx, "$.server_ip_list.buckets.length()", new Integer(0));
		for (int i = 0; i < serverIpCount; i++) {
			serverIpList.add(bucketCtx.read("$.server_ip_list.buckets[" + i  + "]['key']"));
		}
		rawAlert.setJoinServerIp(String.join(",", serverIpList));
		
		Number logTimeStr = bucketCtx.read("$.last_log_time.value");
		Number sum_5xx_count = bucketCtx.read("$.sum_5xx_count.value");
		Number sum_606_count = bucketCtx.read("$.sum_606_count.value");
		Number total_req_count = bucketCtx.read("$.total_req_count.value");
		Number percent_5xx = bucketCtx.read("$.percent_5xx.value");
		Number percent_606 = bucketCtx.read("$.percent_606.value");
		Number bandwidth = bucketCtx.read("$.bandwidth.value");
		Pair<Double, String> resolveResult = resolveBandwidth(bandwidth == null ? 0d : bandwidth.doubleValue());
		Number initDelayValue = bucketCtx.read("$.initDelayValue.value");
		initDelayValue = initDelayValue == null ? 0 : initDelayValue; 
		
		rawAlert.setLastLogTime(logTimeStr.longValue());
		rawAlert.setSum5xxCount(sum_5xx_count == null ? 0L : sum_5xx_count.longValue());
		rawAlert.setSum606Count(sum_606_count == null ? 0L : sum_606_count.longValue());
		rawAlert.setTotalReqCount(total_req_count.longValue());
		rawAlert.setPercent5xx(percent_5xx == null ? 0f : Math.round(percent_5xx.floatValue() * 1000) / 1000f);
		rawAlert.setPercent606(percent_606 == null ? 0f : Math.round(percent_606.floatValue() * 1000) / 1000f);
		rawAlert.setBandwidth(resolveResult.getKey().floatValue());
		rawAlert.setBandwidthUnit(resolveResult.getValue());
		rawAlert.setInitDelayValue(initDelayValue.floatValue());
		return rawAlert;
	}
	
	private static Pair<Double, String> resolveBandwidth(Double bandwidth) {
		int i = 0;
		int base = 1024;
		for (; i < BAND_WIDTH_UNIT_ARR.length; i++) {
			double stepVal = bandwidth / base;
			if (stepVal < 1) {
				break;
			}
			bandwidth = stepVal;
		}
		return Pair.of(trimSubnum(bandwidth, 2), BAND_WIDTH_UNIT_ARR[i]);
	}
	
	/** 
	 * 功能描述: 精确到小数位  
	 * <p>
	 * @param source
	 * @return
	 */
	private static Double trimSubnum(Double source, int subNum) {
		if (source == null) {
			return 0d;
		}
		Double result = source.doubleValue() * Math.pow(10, subNum);
		Long powResult = result.longValue();
		return powResult / Math.pow(10, subNum);
	}
	
	/** 
	 * 功能描述: 判断是否满足原始告警的判断条件, 返回键值对： 键为告警状态, 0 不生成告警   	1 生成告警；  2 生成消警；   值为告警级别；
	 * <p>
	 * @return
	 */
	@JsonIgnore
	public Pair<Integer, Integer> resolveRawAlertStatus(
				Req5xxPecentConfigProps configProps, FocusDomainConfigProps focusDomainConfigProps) {
		if (totalReqCount == null || totalReqCount.longValue() == 0
				|| totalReqCount < configProps.getSumTotalCountFloor()) {
			return Pair.of(0, null);
		}
		// 是否为指定的商用域名
		boolean isFocusDomain = focusDomainConfigProps.getFocusPriorityDomainList().stream().filter(
				foucusDomain -> foucusDomain.equalsIgnoreCase(reqDomain)).findAny().isPresent();
		if (isFocusDomain) {
			// 商业域名只有一个阈值 
			if (percent5xx.floatValue() >= configProps.getFocusDomainFloorVal()) {
				return Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_SERIOUS);
			}
			return Pair.of(MetricAlert.MONI_RESULT_REVOKE, MetricAlert.ALERT_LEVEL_SERIOUS);
		}
		
		Pair<Integer, Integer> result = MutablePair.of(MetricAlert.MONI_RESULT_REVOKE, MetricAlert.ALERT_LEVEL_LOW);
//		if (percent5xx.floatValue() >= configProps.getSeriousLevelFloorVal()) {
//			result = Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_SERIOUS);
//		}
//		else 
		if (percent5xx.floatValue() >= configProps.getHighLevelFloorVal()) {
			result = Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_HIGH);
		}
		else if (percent5xx.floatValue() >= configProps.getMiddleLevelFloorVal()) {
			result = Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_MIDDLE);
		}
		else if (percent5xx.floatValue() >= configProps.getLowLevelFloorVal()) {
			result = Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_LOW);
		}
		return result;
	}
	
	@JsonIgnore
	public Pair<Integer, Integer> resolveWholeCountryBizDomainRawAlertStatus(
				Req5xxPecentConfigProps configProps, FocusDomainConfigProps focusDomainConfigProps) {
		// 是否为指定的商用域名
		boolean isFocusDomain = focusDomainConfigProps.getFocusPriorityDomainList().stream().filter(
				foucusDomain -> foucusDomain.equalsIgnoreCase(reqDomain)).findAny().isPresent();
		if (!isFocusDomain) {
			throw new RuntimeException("The current reqdomain" + reqDomain  + " is not one of the focus business domain.") ;
		}
		
		Pair<Integer, Integer> result = MutablePair.of(MetricAlert.MONI_RESULT_REVOKE, MetricAlert.ALERT_LEVEL_SERIOUS);
		if (percent5xx.floatValue() >= configProps.getFocusDomainWholeCountryFloorVal()) {
			result = Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_SERIOUS);
		}
		return result;
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
		moniTargetReferObj.put("cpName", getCpName());
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
		referInfo.put("percent_5xx", getPercent5xx());
		referInfo.put("join_server_ip", joinServerIp);
		alert.setRefer_info(referInfo);
		alert.setReverse_sync(false);
		alert.setSkip_step(false);	
		return alert;
	}
	
	@JsonIgnore
	public Pair<Integer, Integer> resolveRawInitDelayAlertStatus(
						final Req5xxPecentConfigProps configProps, final InitDelay5TimesAlertCache previousItem) {
		this.previousItem = previousItem;
		Float previousVal = previousItem.getInitDelayValue();
		if (previousVal == null || previousVal.floatValue() <= 0) {
			return Pair.of(0, null);
//			return Pair.of(MetricAlert.MONI_RESULT_REVOKE, MetricAlert.ALERT_LEVEL_LOW);
		}
		
		if (this.totalReqCount == null || this.initDelayValue == null || this.totalReqCount < configProps.getSumTotalCountFloor()) {
			if (MetricAlert.MONI_RESULT_ACTIVE.equals(previousItem.getAlertStatus())) {
				return Pair.of(MetricAlert.MONI_RESULT_REVOKE, MetricAlert.ALERT_LEVEL_LOW);
			}
			return Pair.of(0, null);
		}
		
		previousVal = previousVal < configProps.getInitDelayFloor() ? configProps.getInitDelayFloor() : previousVal;
		if (this.initDelayValue / previousVal >= 5) {
			return Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_LOW);
		} 
		else if (MetricAlert.MONI_RESULT_ACTIVE.equals(previousItem.getAlertStatus())) {
			return Pair.of(MetricAlert.MONI_RESULT_REVOKE, MetricAlert.ALERT_LEVEL_LOW);
		}
		return Pair.of(0, null);
	}
	
	public MetricAlert parse2InitDelayMetricAlert(int moniResult, int alertLevel) {
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
		moniTargetReferObj.put("cpName", getCpName());
		alert.setMoni_target_refer_obj(moniTargetReferObj);
		alert.setItem_key(ITEM_KEY_INIT_DEALY);
		alert.setAlert_level(alertLevel);
		alert.setMoni_result(moniResult);
		alert.setAlert_time(getLastLogTime());
		alert.setLast_alert_time(getLastLogTime());
		alert.setTheme(getInitDelayAlertTheme());
		alert.setContent(getInitDelayAlertContent());
		alert.setValue_type(MetricAlert.ItemValueType.VAL_FLOAT);
		alert.setValue_float(initDelayValue);
		Map<String, Object> referInfo = new HashMap<>();
		referInfo.put("init_delay_value", initDelayValue);
		referInfo.put("total_req_count", totalReqCount);
		referInfo.put("join_server_ip", joinServerIp);
		alert.setRefer_info(referInfo);
		alert.setReverse_sync(false);
		alert.setSkip_step(false);	
		return alert;
	}
	
	@JsonIgnore
	public Pair<Integer, Integer> resolveRaw606PercentAlertStatus(final Req5xxPecentConfigProps configProps) {
		if (sum606Count == null || totalReqCount == null || totalReqCount.longValue() == 0
				|| totalReqCount < configProps.getSumTotalCountFloor()) {
			return Pair.of(0, null);
		}
		if (StringUtils.isBlank(this.cpName) || this.cpName.indexOf("芒果") < 0) {
			return Pair.of(0, null);
		}
		if (percent606.floatValue() >= configProps.getHighLevelFloorVal()) {
			return Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_HIGH);
		}
		else if (percent606.floatValue() >= configProps.getMiddleLevelFloorVal()) {
			return Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_MIDDLE);
		}
		else if (percent606.floatValue() >= configProps.getLowLevelFloorVal()) {
			return Pair.of(MetricAlert.MONI_RESULT_ACTIVE, MetricAlert.ALERT_LEVEL_LOW);
		}
		return Pair.of(MetricAlert.MONI_RESULT_REVOKE, MetricAlert.ALERT_LEVEL_LOW);
	}
	
	public MetricAlert parse2Percent606MetricAlert(int moniResult, int alertLevel) {
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
		moniTargetReferObj.put("cpName", getCpName());
		alert.setMoni_target_refer_obj(moniTargetReferObj);
		alert.setItem_key(ITEM_KEY_PERCENT_606);
		alert.setAlert_level(alertLevel);
		alert.setMoni_result(moniResult);
		alert.setAlert_time(getLastLogTime());
		alert.setLast_alert_time(getLastLogTime());
		alert.setTheme(getAlertThemepPercent606());
		alert.setContent(getAlertContentPercent606());
		alert.setValue_type(MetricAlert.ItemValueType.VAL_FLOAT);
		alert.setValue_float(getPercent606());
		Map<String, Object> referInfo = new HashMap<>();
		referInfo.put("sum_606_count", sum606Count);
		referInfo.put("total_req_count", totalReqCount);
		referInfo.put("percent_606", getPercent606());
		referInfo.put("join_server_ip", joinServerIp);
		alert.setRefer_info(referInfo);
		alert.setReverse_sync(false);
		alert.setSkip_step(false);	
		return alert;
	}
	
	public static void main(String[] args) {
//		Raw5xxPercentAlert alert = new Raw5xxPercentAlert();
//		alert.setProvinceName("湖南");
//		alert.setManufacturer("华为");
//		aler
//		alert.setReqDomain("pcdowncmnetzte.titan.mgtv.com");
//		alert.setLastLogTime(Instant.now().toEpochMilli());
//		alert.setSum5xxCount(100l);
//		alert.setTotalReqCount(900l);
//		alert.setPercent5xx(0.16f);
//		System.out.println(alert.toEsDoc());
		System.out.println("新芒果".indexOf("芒果"));
	}
}
