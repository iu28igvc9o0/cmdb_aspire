package com.aspire.ums.cmdb.cmic;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.common.ResultVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(value = "ip管理用户操作日志类")
@RequestMapping("/cmdb/cmic/ip/operationLog")
public interface ICmdbIPManageOperationLogAPI {

   
	/**
	 * 记录IP管理模块用户操作日志
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
