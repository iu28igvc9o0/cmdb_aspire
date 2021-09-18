package com.aspire.cdn.esdatawrap.biz.report.model;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.aspire.cdn.esdatawrap.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: CustomOpsReportParam
 * <p/>
 *
 * 类功能描述: 自定义报表参数对象
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年9月10日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public class CustomOpsReportParam {
	public static final String				REPORT_KEY_BANDWIDTH					= "bandwidth";
	public static final String				REPORT_KEY_SERVICE_SUCC_PERCENT			= "serviceSuccPercent";
	public static final String				REPORT_KEY_FILE_HIT_SUC_PERCENT			= "fileHitSucPercent";
	public static final String				REPORT_KEY_REQUEST_COUNT				= "requestCount";
	public static final String				REPORT_KEY_RETURN_STATUS_SUC_PERCENT	= "returnStatusSucPercent";
	public static final String				REPORT_KEY_INIT_BIT_DELAY_TIME			= "initBitDelayTime";
	public static final String				REPORT_KEY_DOWNLOAD_RATE				= "downloadRate";
	public static final String				REPORT_KEY_HTTP_STATUS_WEIGTH			= "httpStatusWeight";
	public static final DateTimeFormatter	TIME_FORMAT								= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	private String							reportKey;
	private String							provinceCode;
	private String							city;
	private String							startTime;																					// yyyy-MM-dd
	private String							endTime;																					// yyyy-MM-dd
																																		// HH:mm
	
	@JsonIgnore
	public Long getStartTimeMill() {
		return LocalDateTime.parse(startTime, TIME_FORMAT).toInstant(ZoneOffset.of("+8")).toEpochMilli();
	}
	
	@JsonIgnore
	public Long getEndTimeTimeMill() {
		return LocalDateTime.parse(endTime, TIME_FORMAT).toInstant(ZoneOffset.of("+8")).toEpochMilli();
	}
	
	@JsonIgnore
	public String getProvinceQueryCondStr(Map<String, String> provinceCodeMapName) {
		if (StringUtils.isBlank(provinceCode)) {
			return null;
		}
		Map<String, Map<String, String>> provCond = new HashMap<>();
		provCond.put("match", Collections.singletonMap("province_name", provinceCodeMapName.get(provinceCode)));
		return JsonUtil.toJacksonJson(provCond);
	}
	
	@JsonIgnore
	public String getCityQueryCondStr() {
		if (StringUtils.isBlank(city)) {
			return null;
		}
		Map<String, Map<String, String>> cityCond = new HashMap<>();
		cityCond.put("match", Collections.singletonMap("city_name", city));
		return JsonUtil.toJacksonJson(cityCond);
	}	
}
