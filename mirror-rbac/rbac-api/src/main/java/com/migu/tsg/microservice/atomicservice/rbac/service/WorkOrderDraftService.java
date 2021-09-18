package com.migu.tsg.microservice.atomicservice.rbac.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkOrderDraftDTO;

import io.swagger.annotations.Api;

/**
 * 工单起草
 * @author tongzhihong
 *
 */
@Api(tags = "WorkOrderDraft", description = "工单起草")
@RequestMapping("/v1/workOrderDraft")
public interface WorkOrderDraftService {
	
	/**
	 * 
	 * 获取用户工单起草列表
	 * @param account  用户ID
	 * @param classifyId  用户类别
	 *   
	 */
	@GetMapping(value = "/getUserDraftList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<WorkOrderDraftDTO> getUserDraftList(@RequestParam("account")String account, @RequestParam("classifyId")int classifyId);
	
	/**
	 * 
	 * 根据流程Key，更新流程ID
	 * @param account  用户ID
	 * @param classifyId  用户类别
	 *   
	 */
	@GetMapping(value = "/syncBpmDefIdByKey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	void syncBpmDefIdByKey(@RequestParam("bpmDefKey")String bpmDefKey, @RequestParam("bpmDefId")String bpmDefId);
}
