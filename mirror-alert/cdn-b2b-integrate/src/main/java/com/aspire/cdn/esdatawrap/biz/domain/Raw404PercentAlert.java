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
import com.aspire.cdn.esdatawrap.config.model.FocusDoaminReq404PecentConfigProps;
import com.aspire.cdn.esdatawrap.config.model.FocusDomainConfigProps;
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
public class Raw404PercentAlert implements IEsDocMarshall, IMetricAlertParse {
	public static final String				THEME_TEMPLATE		= "%s-%s B2B-CDN告警 404占比过高";
	public static final String				CONTENT_TEMPLATE	= "%s %s：%s：%s 带宽：%s%s, 总请求数：%s, 404数量：%s, 404错误占比为：%s";
	private static final DateTimeFormatter	TIME_FORMAT			= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final String				ITEM_KEY			= "404_percent";
	private static final String[]         BAND_WIDTH_UNIT_ARR	= {"bps", "Kbps", "Mbps", "Gbps"};

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

	@JsonProperty("sum_404_count")
	private Long				sum404Count;

	@JsonProperty("total_req_count")
	private Long				totalReqCount;

	@JsonProperty("percent_404")
	private Float				percent404;
	
	@JsonProperty("bandwidth")
	private Float				bandwidth;
	
	@JsonProperty("bandwidth_unit")
	private String				bandwidthUnit;

	@JsonProperty("step_count")
	private Integer				stepCount;
	
	/** 
	 * 功能描述: 根据"省份+制造商+域名"生成记录id  
	 * <p>
	 * @return
	 */
	@JsonIgnore
	public String getEsDocId() {
		return EncryptUtil.base64EncryptUrlSafe(String.join("|", provinceName, manufacturer, reqDomain, ITEM_KEY));
	}
	
	@JsonIgnore
	public String getAlertTheme() {
		return String.format(THEME_TEMPLATE, provinceName, manufacturer);
	}
	
	@JsonIgnore
	public String getAlertContent() {
		LocalDateTime localTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lastLogTime), ZoneId.systemDefault());
		String logTimeFormat = TIME_FORMAT.format(localTime);
		String provManu = provinceName + (StringUtils.isBlank(manufacturer) ? "" : "-" + manufacturer); 
		return String.format(CONTENT_TEMPLATE, logTimeFormat, provManu, StringUtils.isBlank(cpName) ? "域名" : cpName, 
				reqDomain, bandwidth, bandwidthUnit, totalReqCount, sum404Count, String.format("%.2f", percent404 * 100) + "%");
	}
	
	@JsonIgnore
	public String getItemId() {
		List<String> itemIdKeys = Arrays.asList(provinceName, manufacturer, reqDomain, ITEM_KEY);
		return EncryptUtil.base64EncryptUrlSafe(StringUtils.join(itemIdKeys, '|'));
	}
	
	public static Raw404PercentAlert buildFromBucket(DocumentContext bucketCtx, String businessSource) {
		Raw404PercentAlert rawAlert = new Raw404PercentAlert();
		rawAlert.setBusinessSource(businessSource);
		rawAlert.setSourceIdentity("B2BELK");
		if ("OTT".equalsIgnoreCase(businessSource)) {
			rawAlert.setSourceIdentity("OTTELK");
		}
		rawAlert.setProvinceName(bucketCtx.read("$.key.province_name_group"));
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
		Number sum_404_count = bucketCtx.read("$.sum_404_count.value");
		Number total_req_count = bucketCtx.read("$.total_req_count.value");
		Number percent_404 = bucketCtx.read("$.percent_404.value");
		Number bandwidth = bucketCtx.read("$.bandwidth.value");
		Pair<Double, String> resolveResult = resolveBandwidth(bandwidth == null ? 0d : bandwidth.doubleValue());
		rawAlert.setLastLogTime(logTimeStr.longValue());
		rawAlert.setSum404Count(sum_404_count.longValue());
		rawAlert.setTotalReqCount(total_req_count.longValue());
		rawAlert.setPercent404(Math.round(percent_404.floatValue() * 1000) / 1000f);
		rawAlert.setBandwidth(resolveResult.getLeft().floatValue());
		rawAlert.setBandwidthUnit(resolveResult.getRight());
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
			FocusDoaminReq404PecentConfigProps configProps, FocusDomainConfigProps focusDomainConfigProps) {
		if (totalReqCount == null || totalReqCount.longValue() == 0
				|| totalReqCount < configProps.getSumTotalCountFloor()) {
			return Pair.of(0, null);
		}
		// 是否为指定的商用域名
		boolean isFocusDomain = focusDomainConfigProps.getFocusPriorityDomainList().stream().filter(
				foucusDomain -> foucusDomain.equalsIgnoreCase(reqDomain)).findAny().isPresent();
		if (isFocusDomain) {
			boolean isHighLevel = configProps.getFocusDomainAlertLevelHigh().stream().filter(
					highLevelDomain -> highLevelDomain.equalsIgnoreCase(reqDomain)).findAny().isPresent();
			Integer alertLevel = isHighLevel ? MetricAlert.ALERT_LEVEL_HIGH : MetricAlert.ALERT_LEVEL_SERIOUS;
			// 商业域名只有一个阈值 
			if (percent404.floatValue() >= configProps.getFocusDomainFloorVal()) {
				return Pair.of(MetricAlert.MONI_RESULT_ACTIVE, alertLevel);
			}
			return Pair.of(MetricAlert.MONI_RESULT_REVOKE, alertLevel);
		}
		return Pair.of(0, null);
	}
	
	@JsonIgnore
	public Pair<Integer, Integer> resolveWholeCountryBizDomainRawAlertStatus(
			FocusDoaminReq404PecentConfigProps configProps, FocusDomainConfigProps focusDomainConfigProps) {
		// 是否为指定的商用域名
		boolean isFocusDomain = focusDomainConfigProps.getFocusPriorityDomainList().stream().filter(
				foucusDomain -> foucusDomain.equalsIgnoreCase(reqDomain)).findAny().isPresent();
		if (!isFocusDomain) {
			throw new RuntimeException("The current reqdomain" + reqDomain  + " is not one of the focus business domain.") ;
		}
		boolean isHighLevel = configProps.getFocusDomainAlertLevelHigh().stream().filter(
				highLevelDomain -> highLevelDomain.equalsIgnoreCase(reqDomain)).findAny().isPresent();
		Integer alertLevel = isHighLevel ? MetricAlert.ALERT_LEVEL_HIGH : MetricAlert.ALERT_LEVEL_SERIOUS;
		Pair<Integer, Integer> result = MutablePair.of(MetricAlert.MONI_RESULT_REVOKE, alertLevel);
		if (percent404.floatValue() >= configProps.getFocusDomainWholeCountryFloorVal()) {
			result = Pair.of(MetricAlert.MONI_RESULT_ACTIVE, alertLevel);
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
		alert.setValue_float(getPercent404());
		Map<String, Object> referInfo = new HashMap<>();
		referInfo.put("sum_404_count", sum404Count);
		referInfo.put("total_req_count", totalReqCount);
		referInfo.put("percent_404", getPercent404());
		referInfo.put("join_server_ip", joinServerIp);
		alert.setRefer_info(referInfo);
		return alert;
	}
	
	public static void main(String[] args) {
		Raw404PercentAlert alert = new Raw404PercentAlert();
		alert.setProvinceName("湖南");
		alert.setManufacturer("华为");
		alert.setReqDomain("www.qq.com");
		alert.setLastLogTime(Instant.now().toEpochMilli());
		alert.setSum404Count(1030l);
		alert.setTotalReqCount(1900l);
		alert.setPercent404(0.16f);
		System.out.println(alert.toEsDoc());
	}
}
