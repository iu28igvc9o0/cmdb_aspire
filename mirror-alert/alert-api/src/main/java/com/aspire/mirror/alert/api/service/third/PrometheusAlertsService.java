package com.aspire.mirror.alert.api.service.third;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Prometheus告警服务
 * <p>
 * 项目名称：mirror平台
 * 包：com.aspire.mirror.alert.api.metrics
 * 类名称：PrometheusAlertsService.java
 * 类描述：prometheus告警服务
 * 创建人：zhujiahao
 * 创建时间：2019/1/11 14:00
 * 版本：v1.0
 *
 */
public interface PrometheusAlertsService {

	/**Prometheus服务器告警上传接口
	 * 
	 * @return
	 * @param
	 */
	@PostMapping("/v1/prometheusalerts/create")
	@ApiOperation(value = "接收prometheus服务器告警信息",notes = "接收prometheus服务器告警信息",tags = {"Third API"})
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "成功"),
			@ApiResponse(code = 500,message = "内部错误")
	})
	Object createdAlerts(@RequestBody String message);
}
