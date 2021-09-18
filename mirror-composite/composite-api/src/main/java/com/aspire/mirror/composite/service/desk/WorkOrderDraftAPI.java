package com.aspire.mirror.composite.service.desk;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkOrderDraftDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @projectName: WorkOrderDraftAPI
 * @description: 接口
 * @author: tongzhihong
 * @create: 2020-09-14 10:37
 **/
@Api("服务台工单起草")
@RequestMapping("${version}/desk/workDraft")
public interface WorkOrderDraftAPI {

	@RequestMapping(value = "/getUserDraftList", method = RequestMethod.GET)
	@ApiOperation(value = "获取用户工单起草列表", notes = "获取用户工单起草列表",tags = {"desk API "})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
			@ApiResponse(code = 500, message = "内部错误") })
	public List<WorkOrderDraftDTO> getCurrentUserDraftList();
	
	@RequestMapping(value = "/syncBpmDefIdByKey", method = RequestMethod.GET)
	@ApiOperation(value = "根据流程Key同步表单ID", notes = "根据流程Key同步表单ID",tags = {"desk API "})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "查询成功", response = List.class),
			@ApiResponse(code = 500, message = "内部错误") })
	public void syncBpmDefIdByKey(@RequestParam("bpmDefKey")String bpmDefKey , @RequestParam("bpmDefId")String bpmDefId);
}
