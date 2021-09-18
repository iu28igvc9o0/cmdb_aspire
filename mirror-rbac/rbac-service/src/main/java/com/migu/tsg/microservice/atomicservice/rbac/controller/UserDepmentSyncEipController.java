package com.migu.tsg.microservice.atomicservice.rbac.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.rbac.biz.UserDepmentSyncEipBiz;
import com.migu.tsg.microservice.atomicservice.rbac.service.UserDepartmentSyncEipService;

@RestController
public class UserDepmentSyncEipController implements UserDepartmentSyncEipService {

	@Autowired
	UserDepmentSyncEipBiz userDepmentSyncEipBiz;

	@Override
	public void userDepartmentSyncEip() {
		userDepmentSyncEipBiz.userDeptSyncProcess();
	}
}
