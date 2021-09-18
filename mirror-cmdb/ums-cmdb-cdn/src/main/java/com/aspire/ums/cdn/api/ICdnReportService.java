package com.aspire.ums.cdn.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cdn.model.LinkPercentReportData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "cdn报表管理")
@RequestMapping("/v1/cmdbCdn/report/")
public interface ICdnReportService {

	/**
	 * 查询连通率报表
	 *
	 */
	@RequestMapping(method=RequestMethod.GET, path="/getLinkPercentReportData")
	@ApiOperation(value = "查询连通率报表", notes = "查询连通率报表", tags = { "CDN Report API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = LinkPercentReportData.class),
			@ApiResponse(code = 500, message = "内部错误") })
	List<LinkPercentReportData> getLinkPercentReportData();
}
