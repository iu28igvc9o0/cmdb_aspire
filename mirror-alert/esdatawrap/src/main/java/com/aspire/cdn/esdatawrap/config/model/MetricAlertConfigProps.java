package com.aspire.cdn.esdatawrap.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="matric-alert")
public class MetricAlertConfigProps {
	private Integer stepcountCutdown;
	
	/** 
	 * 功能描述: 是否配置了告警收敛  
	 * <p>
	 * @return
	 */
	public boolean isMetricAlertStepCutDown() {
		return stepcountCutdown.intValue() > 1;
	}
}
