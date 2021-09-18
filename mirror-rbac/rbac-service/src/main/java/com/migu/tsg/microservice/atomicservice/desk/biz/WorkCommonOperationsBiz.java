package com.migu.tsg.microservice.atomicservice.desk.biz;

import java.util.List;

import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkCommonOperationsDTO;

public interface WorkCommonOperationsBiz {

	List<WorkCommonOperationsDTO> getUserOperationsList(String account, int classifyId);

}
