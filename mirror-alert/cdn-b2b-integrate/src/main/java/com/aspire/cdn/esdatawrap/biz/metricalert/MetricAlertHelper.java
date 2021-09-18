package com.aspire.cdn.esdatawrap.biz.metricalert;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aspire.cdn.esdatawrap.biz.client.IMetricAlertIntegrateClient;
import com.aspire.cdn.esdatawrap.client.ClientServiceBuilder;
import com.aspire.cdn.esdatawrap.config.model.MetricAlertConfigProps;

import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: MetricAlertHelper
 * <p/>
 *
 * 类功能描述: 指标项告警帮助类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年6月23日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@Component
public final class MetricAlertHelper {
	@Autowired
	private MetricAlertConfigProps	metricAlertProps;
	
	/** 
	 * 功能描述: 处理指标告警数据  
	 * <p>
	 * @param metricAlert
	 */
	public void processMetricAlert(MetricAlert metricAlert) {
		if (log.isDebugEnabled()) {
			log.debug("Begin to handle metric alert: {}", metricAlert);
		}
		Pair<Boolean, String> checkResult = metricAlert.selfDataCheck();
		if (!checkResult.getLeft()) {
			log.error("Check Metric alert error, detail: {}.", checkResult.getRight());
		}
		
		String serviceUrl = metricAlertProps.getServiceUrl();
		IMetricAlertIntegrateClient metricClient
			= ClientServiceBuilder.buildClientService(IMetricAlertIntegrateClient.class, serviceUrl);
		metricClient.pushMetricAlert(metricAlert);
	}
	
	/** 
	 * 功能描述: 批量处理指标告警数据  
	 * <p>
	 * @param metricAlert
	 */
	public void processMetricAlertBatch(List<MetricAlert> metricAlertList) {
		if (log.isDebugEnabled()) {
			log.debug("Begin to handle metric alert in batch with size {}.", metricAlertList.size());
		}
		
		metricAlertList = metricAlertList.stream().filter(alert -> {
			Pair<Boolean, String> checkResult = alert.selfDataCheck();
			if (!checkResult.getLeft()) {
				log.error("Check Metric alert error, detail: {}.", checkResult.getRight());
			}
			return checkResult.getLeft();
		}).collect(Collectors.toList());
		
		if (metricAlertList.size() == 0) {
			return;
		}
		String serviceUrl = metricAlertProps.getServiceUrl();
		IMetricAlertIntegrateClient metricClient
			= ClientServiceBuilder.buildClientService(IMetricAlertIntegrateClient.class, serviceUrl);
		metricClient.pushMetricAlertBatch(metricAlertList);
	}
}
