package com.aspire.mirror.composite.service.cmdb.cmic;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.ResultVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * ip管理用户操作日志
 * 用于统计用户菜单点击量
 * TODO 暂时记录大菜单点击数
 * @author cuizhijun
 *
 */
@Api(value = "ip管理用户操作日志")
@RequestMapping("${version}/cmdb/cmic/ip/operationLog")
public interface ICmdbIPManageOperationLogAPI {

   
	/**
	 * 记录用户操作日志
	 * @param menuUrl
	 * @return
	 */
    @PostMapping(value = "/log")
    @ApiOperation(value = "记录ip管理用户操作日志", notes = "记录ip管理用户操作日志",
            tags = {"CMDB CMIC IP OPERATIONLOG API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "添加成功", response = ResultVo.class),
            @ApiResponse(code = 500, message = "内部错误")})
    ResultVo<String> addOperationLog(@RequestParam("menuUrl") String menuUrl);
}
