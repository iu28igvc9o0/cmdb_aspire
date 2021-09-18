package com.aspire.cdn.esdatawrap.config.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="local.config.es-run-action.focus-domain-req404-percent")
public class FocusDoaminReq404PecentConfigProps {
	private Integer				sumTotalCountFloor;								// 总请求数下限, 少于该值则不参与计算
	private Float 				focusDomainFloorVal;							// 低级别阈值
	private Float 				focusDomainWholeCountryFloorVal;				// 商业域名全网匹配阈值
	private final List<String>  focusDomainAlertLevelHigh = new ArrayList<>();	// 指定级别为“高”的商业域名列表
	private final List<String>  excludeDomainList 		  = new ArrayList<>();	// 需要排除404告警的域名列表
	private Integer 			intervalMinutes 		  = 2;					// 扫描间隔
}
