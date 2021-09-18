package com.aspire.mirror.log.api.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.mirror.log.api.dto.BizLogChartListResponse;
import com.aspire.mirror.log.api.dto.BusinessLogRequest;
import com.aspire.mirror.log.api.dto.BusinessLogResponse;
import com.aspire.mirror.log.api.dto.model.BusinessLogDTO;

/**
 * 业务日志接口
 * 项目名称: 微服务运维平台（log-api 模块）
 * 包: com.migu.tsg.microservice.monitor.log.service
 * 类名称: IBusinessLogService.java
 * 类描述: 业务日志接口
 * 创建人: sunke
 * 创建时间: 2017年8月10日 上午10:04:00
 */
@Api(value = "业务日志接口")
public interface IBusinessLogService {

	/**
	 * list:业务日志信息
	 * @param request 请求参数
	 * @return 返回内容
	 */
	@RequestMapping(value = "/ajax/services/list", produces = { "application/json" }, method = RequestMethod.POST)
    @ApiOperation(value = "业务日志信息", notes = "业务日志信息", response = BusinessLogResponse.class, tags = {
            "BusinessLogResponse", })
    @ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "返回",
			response = BusinessLogResponse.class),
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Unexpected error",
					response = BusinessLogResponse.class) })
	@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.POST }, origins = "*")
	BusinessLogResponse list(@RequestBody BusinessLogRequest request);


	/**
	 * 基于时间的日志分布数列查询（页面日志分布构图数据）
	 * @param request 日志分布查询条件
	 * @return 基于时间的日志分布数列
	 */
	@RequestMapping(value = "/ajax/services/aggregations", produces = { "application/json" },
			method = RequestMethod.POST)
	@ApiOperation(value = "基于时间的日志分布数列查询（页面日志分布构图数据）", notes = "基于时间的日志分布数列查询（页面日志分布构图数据）",
			response = BizLogChartListResponse.class, tags = {"BizLogChartListResponse", })
	@ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "返回",
			response = BizLogChartListResponse.class),
			@ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Unexpected error",
					response = BizLogChartListResponse.class) })
	@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.POST }, origins = "*")
	BizLogChartListResponse listChart(@RequestBody BusinessLogRequest request);
	/**
     * 保存业务日志
     * @param request 日志内容
     * @return 基于时间的日志分布数列
     */
    @RequestMapping(value = "/ajax/services/saveBiz", produces = { "application/json" },
            method = RequestMethod.POST)
    @ApiOperation(value = "保存业务日志", notes = "保存业务日志")
    @ApiResponses(value = { @ApiResponse(code = HttpStatus.SC_OK, message = "返回")})
    @CrossOrigin(methods = RequestMethod.POST , origins = "*")
	void saveBiz(@RequestBody List<BusinessLogDTO> bizLogInfos);

}
