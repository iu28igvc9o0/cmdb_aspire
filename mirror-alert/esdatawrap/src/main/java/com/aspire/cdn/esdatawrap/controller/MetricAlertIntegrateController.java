package com.aspire.cdn.esdatawrap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlert;
import com.aspire.cdn.esdatawrap.biz.metricalert.MetricAlertHelper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/** 
 *
 * 项目名称: esdatawrap 
 * <p/>
 * 
 * 类名: MetricAlertIntegrateController
 * <p/>
 *
 * 类功能描述: 指标告警集成Controller
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年7月11日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Slf4j
@RestController
@Api(value = "指标告警数据集成")
@RequestMapping("/esdatawrap/metricAlert")
@ConditionalOnExpression("!'none'.equals('${spring.main.web-application-type}')")
public class MetricAlertIntegrateController {
	@Autowired
	private MetricAlertHelper metricAlertHelper; 
	
	@PostMapping(value = "/pushMetricAlert", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "推入指标告警|消警数据", notes = "推入指标告警|消警数据", tags = {"Esdatawrap metric alert integrate service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回"), @ApiResponse(code = 500, message = "Unexpected error")})
	public void pushMetricAlert(@RequestBody MetricAlert metricAlert) {
		if (log.isDebugEnabled()) {
			log.debug("Received metricAlert message from business_source {}.", metricAlert.getBusiness_source());
		}
		metricAlertHelper.processMetricAlert(metricAlert);
	}
	
	@PostMapping(value = "/pushMetricAlertBatch", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "批量推入指标告警|消警数据", notes = "批量推入指标告警|消警数据", tags = {"Esdatawrap metric alert integrate service API"})
    @ApiResponses(value = {@ApiResponse(code = 204, message = "返回"), @ApiResponse(code = 500, message = "Unexpected error")})
	public void pushMetricAlertBatch(@RequestBody List<MetricAlert> metricAlertList) {
		if (log.isDebugEnabled()) {
			log.debug("Received metricAlert message batchList at {}.", System.currentTimeMillis());
		}
		for (MetricAlert metricAlert : metricAlertList) {
			try {
				metricAlertHelper.processMetricAlert(metricAlert);
			} catch (Exception e) {
				log.error(null, e);
			}
		}
	}
}
