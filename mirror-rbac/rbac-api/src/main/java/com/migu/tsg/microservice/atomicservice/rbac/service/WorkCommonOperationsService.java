package com.migu.tsg.microservice.atomicservice.rbac.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkCommonOperationsDTO;

import io.swagger.annotations.Api;

/**
 * 服务台 -- 常用操作
 * @author tongzhihong
 */
@Api(tags = "workCommonOperations", description = "服务台 --常用操作 ")
@RequestMapping("/v1/workCommonOperations")
public interface WorkCommonOperationsService {
	/**
	 * 
	 * 获取用户常用操作列表
	 * @param account  用户ID
	 * @param classifyId  用户类别
	 *   
	 */
	@GetMapping(value = "/getUserOperations", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<WorkCommonOperationsDTO> getUserOperations(@RequestParam("account")String account, @RequestParam("classifyId")int classifyId);
}
