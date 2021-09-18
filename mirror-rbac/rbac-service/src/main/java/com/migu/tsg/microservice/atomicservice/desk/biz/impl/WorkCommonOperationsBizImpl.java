package com.migu.tsg.microservice.atomicservice.desk.biz.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.migu.tsg.microservice.atomicservice.desk.biz.WorkCommonOperationsBiz;
import com.migu.tsg.microservice.atomicservice.desk.dao.WorkCommonOperationsDao;
import com.migu.tsg.microservice.atomicservice.desk.dao.po.WorkCommonOperations;
import com.migu.tsg.microservice.atomicservice.desk.dao.po.WorkOrderDraft;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkCommonOperationsDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkOrderDraftDTO;

@Service
public class WorkCommonOperationsBizImpl implements WorkCommonOperationsBiz{
	
	@Autowired
	WorkCommonOperationsDao workCommonOperationsDao;
	
	@Override
	public List<WorkCommonOperationsDTO> getUserOperationsList(String account, int classifyId) {
		//获取用户自定义的列表
		List<WorkCommonOperations> userOperationList = workCommonOperationsDao.getOperationListByAccount(account);
		//根据用户类别获取列表，这里可以优化。加入缓存
		List<WorkCommonOperations> classifyList = workCommonOperationsDao.getOperationListByClassfyId(classifyId);
		
		List<WorkCommonOperationsDTO> retList = new ArrayList<WorkCommonOperationsDTO>();
		if(CollectionUtils.isNotEmpty(userOperationList)){
			for(WorkCommonOperations dto : userOperationList){
				WorkCommonOperationsDTO rt = new WorkCommonOperationsDTO();
				BeanUtils.copyProperties(dto, rt);
				retList.add(rt);
			}
			return retList;
		}
		
		for(WorkCommonOperations dto : classifyList){
			WorkCommonOperationsDTO rt = new WorkCommonOperationsDTO();
			BeanUtils.copyProperties(dto, rt);
			retList.add(rt);
		}
		return retList;
	}

}
