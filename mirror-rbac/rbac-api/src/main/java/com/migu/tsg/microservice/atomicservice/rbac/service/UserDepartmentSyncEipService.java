package com.migu.tsg.microservice.atomicservice.rbac.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 卓望用户部门同步
 * @author tongzhihong
 *
 */

@Api(tags = "user", description = "人员组织信息同步")
@RequestMapping("/v1/userDepartmentSyncEip")
public interface UserDepartmentSyncEipService {
	
	@GetMapping(value = "/userDepartmentSyncEip", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ApiOperation(value = "用户组织同步", notes = "用户组织同步")
	public void userDepartmentSyncEip();
}
