package com.aspire.mirror.composite.service.alert;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbQueryInstance;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "性能分析")
@RequestMapping(value = "/${version}/alerts/netPerformanceAnalysis")
public interface IComNetPerformanceAnalysisService {

	@RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "网络性能分析", notes = "获取CI表头", tags = {"netPerformanceAnalysis API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
	Result<Map<String, Object>> getNetPerformanceAnalysiseList(@RequestBody CmdbQueryInstance queryInstance
    		,@RequestParam(value = "granularity",required=false)String granularity,
    		@RequestParam(value = "sendTimeStart",required=false)String sendTimeStart,
    		@RequestParam(value = "sendTimeEnd",required=false)String sendTimeEnd)throws ParseException;

	@RequestMapping(value = "/trends", method = RequestMethod.GET)
    @ApiOperation(value = "网络性能分析趋势", notes = "获取CI表头", tags = {"netPerformanceAnalysis API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
	Map<String, Object> getNetPerformanceAnalysiseTrends(@RequestParam(value = "ip") String ip,
			@RequestParam(value = "granularity", required = false) String granularity,
			@RequestParam(value = "sendTimeStart", required = false) String sendTimeStart,
			@RequestParam(value = "sendTimeEnd", required = false) String sendTimeEnd,
			@RequestParam(value = "idcType", required = false) String idcType
			,@RequestParam(value = "monitorFlag", required = false) String monitorFlag) throws ParseException;

	@ApiOperation(value = "导出利用率数据", notes = "导出数据", tags = {"netPerformanceAnalysis API"})
	@RequestMapping(value = "/exportTrends", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	void exportNetPerformanceAnalysiseTrends(@RequestParam(value = "ip") String ip,
			@RequestParam(value = "granularity", required = false) String granularity,
			@RequestParam(value = "sendTimeStart", required = false) String sendTimeStart,
			@RequestParam(value = "sendTimeEnd", required = false) String sendTimeEnd,
			@RequestParam(value = "idcType", required = false) String idcType
			,@RequestParam(value = "monitorFlag", required = false) String monitorFlag, HttpServletResponse response) throws Exception;

	@ApiOperation(value = "导出网络设备性能数据", notes = "导出数据", tags = {"netPerformanceAnalysis API"})
	@RequestMapping(value = "/exportNetList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	void exportNetPerformanceAnalysiseList(@RequestBody CmdbQueryInstance queryInstance,
			@RequestParam(value = "granularity", required = false) String granularity,
			@RequestParam(value = "sendTimeStart", required = false) String sendTimeStart,
			@RequestParam(value = "sendTimeEnd", required = false) String sendTimeEnd, HttpServletResponse response) throws Exception;
   
}
