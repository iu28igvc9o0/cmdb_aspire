package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.assessment;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.assessment.ICompEquipmentProblemAPI;
import com.aspire.mirror.composite.service.cmdb.payload.CompEquipmentProblemPageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompEquipmentProblemRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompEquipmentProblemResp;
import com.aspire.ums.cmdb.common.PageBean;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbEquipmentProblemClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * ip分配
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:    CompAllocateHardController.java
 * 类描述:    ip分配
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 15:40
 * 版本:      v1.0
 */
@Slf4j
@RestController
public class CompEquipmentProblemController extends CommonResourceController implements ICompEquipmentProblemAPI {

	
	@Autowired
    private CmdbEquipmentProblemClient cmdbEquipmentProblemClient;
	
	 
	
	
	@Override
	public String insertEquipmentProblem(CompEquipmentProblemRequest compEquipmentProblemRequest) {
       
		log.info("compEquipmentProblemRequest is {} ", compEquipmentProblemRequest);
		
		cmdbEquipmentProblemClient.insertEquipmentProblem(compEquipmentProblemRequest);
		 
		return "success";
	}
	
	
	
	@Override
	public CompEquipmentProblemResp selectEquipmentProblemById(String id) {
        
		log.info("id is {} ",id);
		
		return cmdbEquipmentProblemClient.selectEquipmentProblemById(id);
	}
	
	
 

	@Override
	public String updateEquipmentProblem(CompEquipmentProblemRequest compEquipmentProblemRequest) {

		log.info("compEquipmentProblemRequest is {} ",compEquipmentProblemRequest);
		
	    cmdbEquipmentProblemClient.updateEquipmentProblem(compEquipmentProblemRequest);
	    
	    return "success";
	}
	
 
	
	
	@Override
	public String deleteEquipmentProblem(String id) {

		log.info("id is {} ",id);
		
		cmdbEquipmentProblemClient.deleteEquipmentProblem(id);
		
		return "success";
	}

	

	
	@Override
	public PageBean<CompEquipmentProblemResp> seleteEquipmentProblemByPage(@RequestBody CompEquipmentProblemPageRequest compequipmentProblemPageRequest) {
		log.info("compequipmentProblemPageRequest is {} ",compequipmentProblemPageRequest);
		String page = compequipmentProblemPageRequest.getPage();
		PageBean<CompEquipmentProblemResp> result = cmdbEquipmentProblemClient.seleteEquipmentProblemByPage(compequipmentProblemPageRequest);
		if (result.getCount() > 0 && StringUtils.isNotEmpty(page) && "approve".equals(page)){
			// 审核状态 -1待审核 0拒绝  1通过 2保存中
			String status = result.getResult().get(0).getAssessStatus();
			if ("-1".equals(status) || "1".equals(status)) {
				return result;
			} else {
				return new PageBean<>();
			}
		}
		return result;
	}
	

	@Override
	public String approveEquipmentProblem(CompEquipmentProblemRequest compEquipmentProblemRequest) {

		log.info("compEquipmentProblemRequest is {} ",compEquipmentProblemRequest);
		
	    cmdbEquipmentProblemClient.approveEquipmentProblem(compEquipmentProblemRequest);
	    
	    return "success";
	}
	
	

	@Override
	public String saveEquipmentProblemList(@RequestBody JSONObject data) {
		 
		log.info("compEquipmentProblemRequestList is {} ",data);
		 
		return cmdbEquipmentProblemClient.saveEquipmentProblemList(data);
	}

	@Override
	public List<CompEquipmentProblemResp> getEquipmentProblemList(@RequestBody CompEquipmentProblemRequest compEquipmentProblemRequest) {
		 
		log.info("compEquipmentProblemRequest is {} ",compEquipmentProblemRequest);
		 
		return cmdbEquipmentProblemClient.getEquipmentProblemList(compEquipmentProblemRequest);
	}
	
	

 

}
