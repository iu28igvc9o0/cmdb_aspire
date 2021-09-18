package com.migu.tsg.microservice.atomicservice.desk.biz.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.migu.tsg.microservice.atomicservice.desk.biz.WorkOrderDraftBiz;
import com.migu.tsg.microservice.atomicservice.desk.dao.WorkOrderDraftDao;
import com.migu.tsg.microservice.atomicservice.desk.dao.po.WorkOrderDraft;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkOrderDraftDTO;

@Service
public class WorkOrderDraftBizImpl implements WorkOrderDraftBiz{
	
	@Autowired
	WorkOrderDraftDao workOrderDraftDao;
	
	@Value("${bpm.front.formStarturl}")
	private String bpmFrontFromStartUrl;
	
	@Override
	public List<WorkOrderDraftDTO> getUserDraftList(String account, int classifyId) {
		//获取用户自定义的列表
		List<WorkOrderDraft> userDraftList = workOrderDraftDao.getUserDraftList(account);
		//根据用户类别获取列表，这里可以优化。加入缓存
		List<WorkOrderDraft> classifyDraftList = workOrderDraftDao.getDraftListByClassfyId(classifyId);
		
		List<WorkOrderDraftDTO> retList = new ArrayList<WorkOrderDraftDTO>();
		if(CollectionUtils.isNotEmpty(userDraftList)){
			for(WorkOrderDraft dto : userDraftList){
				WorkOrderDraftDTO rt = new WorkOrderDraftDTO();
				BeanUtils.copyProperties(dto, rt);
				rt.setFullUrl(bpmFrontFromStartUrl + dto.getFormDefId());
				retList.add(rt);
			}
			return retList;
		}
		
		for(WorkOrderDraft dto : classifyDraftList){
			WorkOrderDraftDTO rt = new WorkOrderDraftDTO();
			BeanUtils.copyProperties(dto, rt);
			rt.setFullUrl(bpmFrontFromStartUrl + dto.getFormDefId());
			retList.add(rt);
		}
		return retList;
	}

	@Override
	public void syncBpmDefIdByKey(String bpmDefKey, String bpmDefId) {
		workOrderDraftDao.syncBpmDefIdByKey(bpmDefKey,bpmDefId);
	}
}
