package com.migu.tsg.microservice.atomicservice.composite.controller.desk;

import java.util.List;
import java.util.Optional;

import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.DeskLogsAnnotation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.desk.WorkOrderDraftAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.UserClassifyServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.WorkOrderDraftClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserClassifyReq;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkOrderDraftDTO;

/**
 * @projectName: BpmTaskController
 * @description: 服务台 工单起草
 * @author: tongzhihong
 * @create: 2020-09-14 10:26
 **/
@RestController
public class WorkOrderDraftController implements WorkOrderDraftAPI{
	
	@Autowired
	WorkOrderDraftClient workOrderDraftClient;
	
	@Autowired
	UserClassifyServiceClient userClassifyServiceClient;
	
	//@Value("${user.classify.duty.id}")
	private String user_classify_duty;
	
	@Override
	@DeskLogsAnnotation(value = "获取用户工单起草列表")
	public List<WorkOrderDraftDTO> getCurrentUserDraftList() {
		 RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
	     String account = authCtx.getUser().getUsername();
	     return workOrderDraftClient.getUserDraftList(account, 1);
	    /* // 获取用户账号类别
	     List<UserClassifyReq> classifyReqList = userClassifyServiceClient.findListByLdapId(account);
	     if(CollectionUtils.isNotEmpty(classifyReqList)){
	    	 Optional<UserClassifyReq> optional = classifyReqList.stream().filter(it -> user_classify_duty.equals(it.getUuid())).findAny();
	    	 if(optional.isPresent()){
	    		 // 用户是值班人员
	    		 return workOrderDraftClient.getUserDraftList(account, 2);
	    	 }else{
	    		 return workOrderDraftClient.getUserDraftList(account, 1);
	    	 }
	     }else {
	    	 return workOrderDraftClient.getUserDraftList(account, 1);
	     }*/
	     //return Collections.emptyList();
	}

	@Override
	public void syncBpmDefIdByKey(String bpmDefKey, String bpmDefId) {
		workOrderDraftClient.syncBpmDefIdByKey(bpmDefKey,bpmDefId);
	}

}
