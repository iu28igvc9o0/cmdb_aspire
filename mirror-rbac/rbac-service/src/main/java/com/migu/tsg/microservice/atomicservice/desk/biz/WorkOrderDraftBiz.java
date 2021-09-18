package com.migu.tsg.microservice.atomicservice.desk.biz;

import java.util.List;

import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkOrderDraftDTO;

public interface WorkOrderDraftBiz {

	List<WorkOrderDraftDTO> getUserDraftList(String account, int classifyId);

	void syncBpmDefIdByKey(String bpmDefKey, String bpmDefId);

}
