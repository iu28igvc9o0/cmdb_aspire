package com.migu.tsg.microservice.atomicservice.desk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.desk.biz.WorkOrderDraftBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkOrderDraftDTO;
import com.migu.tsg.microservice.atomicservice.rbac.service.WorkOrderDraftService;

@RestController
public class WorkOrderDraftController implements WorkOrderDraftService{
	
	@Autowired
	WorkOrderDraftBiz workOrderDraftBiz;
	
	@Override
	public List<WorkOrderDraftDTO> getUserDraftList(String account, int classifyId) {
		return workOrderDraftBiz.getUserDraftList(account,classifyId);
	}

	@Override
	public void syncBpmDefIdByKey(String bpmDefKey, String bpmDefId) {
		workOrderDraftBiz.syncBpmDefIdByKey(bpmDefKey,bpmDefId);
	}

}
