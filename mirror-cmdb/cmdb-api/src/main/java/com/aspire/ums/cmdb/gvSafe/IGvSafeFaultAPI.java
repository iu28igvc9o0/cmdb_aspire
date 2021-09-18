package com.aspire.ums.cmdb.gvSafe;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "政企故障上报")
@RequestMapping("/cmdb/gvSafeFault")
public interface IGvSafeFaultAPI {
	 	@PostMapping("/save")
	    @ApiOperation(value = "保存故障上报数据", notes = "保存故障上报数据", tags = {"CMDB gvSafe API"})
	    @ApiResponses(value = {@ApiResponse(code = 200, message = "新增成功", response = String.class),
	     @ApiResponse(code = 500, message = "内部错误")})
	 	Map<String,Object> saveGvSafeFault(@RequestBody Map<String,Object> request, @RequestParam(value = "userName") String userName);
}
