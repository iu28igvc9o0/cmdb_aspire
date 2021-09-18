package com.migu.tsg.microservice.atomicservice.composite.controller.desk;

import java.util.List;
import java.util.Optional;

import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.DeskLogsAnnotation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.desk.WorkCommonOperationsAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.UserClassifyServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.WorkCommonOperationsClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserClassifyReq;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.WorkCommonOperationsDTO;

/**
 * @projectName: BpmTaskController
 * @description: 服务台 常见操作
 * @author: tongzhihong
 * @create: 2020-09-14 10:26
 **/
@RestController
public class WorkCommonOperationsController implements WorkCommonOperationsAPI{
	@Autowired
	WorkCommonOperationsClient workCommonOperationsClient;
	
	@Autowired
	UserClassifyServiceClient userClassifyServiceClient;
	
	//@Value("${user.classify.duty.id}")
	private String user_classify_duty;
	
	@Override
	@DeskLogsAnnotation(value = "获取用户常用操作")
	public List<WorkCommonOperationsDTO> getUserOperationsList() {
		 RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
	     String account = authCtx.getUser().getUsername();
	     return workCommonOperationsClient.getUserOperations(account, 1);
	     
	     /*// 获取用户账号类别
	     List<UserClassifyReq> classifyReqList = userClassifyServiceClient.findListByLdapId(account);
	     if(CollectionUtils.isNotEmpty(classifyReqList)){
	    	 Optional<UserClassifyReq> optional = classifyReqList.stream().filter(it -> user_classify_duty.equals(it.getUuid())).findAny();
	    	 if(optional.isPresent()){
	    		 // 用户是值班人员
	    		 return workCommonOperationsClient.getUserOperations(account, 2);
	    	 }else{
	    		 return workCommonOperationsClient.getUserOperations(account, 1);
	    	 }
	     } else {
	    	 return workCommonOperationsClient.getUserOperations(account, 1);
	     }*/
	     //return Collections.emptyList();
	}

}
