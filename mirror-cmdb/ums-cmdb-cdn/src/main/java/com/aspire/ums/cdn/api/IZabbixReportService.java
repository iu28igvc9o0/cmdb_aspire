/**
 *
 * 项目名： ums-cmdb-cdn 
 * <p/> 
 *
 * 文件名:  IZabbixReportService.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2019 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.ums.cdn.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cdn.model.LinkPercentReportData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/** 
 *
 * 项目名称: ums-cmdb-cdn 
 * <p/>
 * 
 * 类名: IZabbixReportService
 * <p/>
 *
 * 类功能描述: TODO
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年9月3日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Api(value = "zabbix报表管理")
@RequestMapping("/v1/cdn/zabbix/report/")
public interface IZabbixReportService {
	/**
	 * 查询监控项历史记录
	 *
	 */
	@RequestMapping(method=RequestMethod.GET, path="/fetchItemHistoryVals")
	@ApiOperation(value = "查询监控项历史记录", notes = "查询监控项历史记录", tags = { "CDN Zabbix Report API" })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "成功", response = LinkPercentReportData.class),
			@ApiResponse(code = 500, message = "内部错误") })
	List<String> fetchItemHistoryVals();
}
