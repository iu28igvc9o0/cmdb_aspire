package com.aspire.cdn.esdatawrap.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="local.config.es-run-action.req5xx-percent")
public class Req5xxPecentConfigProps {
	private Integer				sumTotalCountFloor;								// 总请求数下限, 少于该值则不参与计算
//	private Float				triggerPercentFloor;							// 判断百分比, 大于等于该值判断为一次原始告警
	private Float 				seriousLevelFloorVal;							// 严重级别阈值
	private Float 				highLevelFloorVal;								// 高级别阈值
	private Float 				middleLevelFloorVal;							// 中级别阈值
	private Float 				lowLevelFloorVal;								// 低级别阈值
	private Float 				focusDomainFloorVal;							// 低级别阈值
	private Float 				focusDomainWholeCountryFloorVal;				// 商业域名全网匹配阈值
	private Integer 			intervalMinutes 		= 2;					// 扫描间隔
	private Integer 			initDelayFloor 			= 200;					// 首包时延统计最低值
}
