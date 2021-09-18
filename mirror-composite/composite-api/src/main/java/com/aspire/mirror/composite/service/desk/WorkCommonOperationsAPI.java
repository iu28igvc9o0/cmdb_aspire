package com.aspire.mirror.composite.service.desk;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkCommonOperationsDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @projectName: WorkCommonOperationsAPI
 * @description: 接口
 * @author: tongzhihong
 * @create: 2020-09-14 10:37
 **/
@Api("服务台常用操作")
@RequestMapping("${version}/desk/commonOperations")
public interface WorkCommonOperationsAPI {
	
	@RequestMapping(value = "/getUserOperationsList", method = RequestMethod.GET)
	@ApiOperation(value = "获取用户常用操作", notes = "获取用户常用操作",tags = {"desk API "})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
			@ApiResponse(code = 500, message = "内部错误") })
	public List<WorkCommonOperationsDTO> getUserOperationsList();
}
