package com.aspire.cdn.esdatawrap.biz.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: cdn-b2b-integrate 
 * <p/>
 * 
 * 类名: InitDelay5TimesAlertCache
 * <p/>
 *
 * 类功能描述: 分省、分平面、分域名   首包时延比上次倍增超过5倍告警缓存
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年12月23日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@EqualsAndHashCode(of={"provinceName", "sourceIdentity", "businessSource", "manufacturer", "reqDomain"})
public class InitDelay5TimesAlertCache {
	
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
	
	@JsonProperty("init_delay_value")
	private Float 				initDelayValue;
	
	@JsonProperty("alert_status")
	private Integer 			alertStatus;		// MetricAlert.MONI_RESULT_ACTIVE,  MetricAlert.MONI_RESULT_REVOKE
	
	public String getUniqueKey() {
		return String.join("|", provinceName, sourceIdentity, businessSource, manufacturer, reqDomain);
	}
	
	public static InitDelay5TimesAlertCache from(String provinceName, String sourceIdentity, 
				String businessSource, String manufacturer, String reqDomain, Float initDelayValue) {
		InitDelay5TimesAlertCache item = new InitDelay5TimesAlertCache();
		item.provinceName = provinceName;
		item.sourceIdentity = sourceIdentity;
		item.businessSource = businessSource;
		item.manufacturer = manufacturer;
		item.reqDomain = reqDomain;
		item.initDelayValue = initDelayValue;
		return item;
	}
}
