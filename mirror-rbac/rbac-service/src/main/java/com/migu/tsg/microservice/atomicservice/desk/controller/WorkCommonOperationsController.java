package com.migu.tsg.microservice.atomicservice.desk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.desk.biz.WorkCommonOperationsBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkCommonOperationsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.service.WorkCommonOperationsService;

@RestController
public class WorkCommonOperationsController implements WorkCommonOperationsService{
	
	@Autowired
	WorkCommonOperationsBiz workCommonOperationsBiz;
	
	@Override
	public List<WorkCommonOperationsDTO> getUserOperations(String account, int classifyId) {
		return workCommonOperationsBiz.getUserOperationsList(account, classifyId);
	}

}
