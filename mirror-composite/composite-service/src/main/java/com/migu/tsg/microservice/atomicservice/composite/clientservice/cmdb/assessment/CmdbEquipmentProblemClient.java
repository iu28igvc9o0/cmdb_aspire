package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.payload.CompEquipmentProblemPageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompEquipmentProblemRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompEquipmentProblemResp;
import com.aspire.ums.cmdb.common.PageBean;

@FeignClient(value = "CMDB")
public interface CmdbEquipmentProblemClient   {

	
	@PostMapping(value = "/v1/cmdb/assessment/insertEquipmentProblem")
    public String insertEquipmentProblem(@RequestBody CompEquipmentProblemRequest compEquipmentProblemRequest);
   
	
	
	@PostMapping(value = "/v1/cmdb/assessment/selectEquipmentProblemById")
    public CompEquipmentProblemResp selectEquipmentProblemById(@RequestParam("id") String id);
   
	

	@PostMapping(value = "/v1/cmdb/assessment/updateEquipmentProblem")
    public String updateEquipmentProblem(@RequestBody CompEquipmentProblemRequest compEquipmentProblemRequest);
   
     
	@DeleteMapping(value = "/v1/cmdb/assessment/deleteEquipmentProblem")
    public String deleteEquipmentProblem(@RequestParam("id") String id);
   
   
	@PostMapping(value = "/v1/cmdb/assessment/seleteEquipmentProblemByPage")
	public PageBean<CompEquipmentProblemResp> seleteEquipmentProblemByPage( @RequestBody CompEquipmentProblemPageRequest compEquipmentProblemPageRequest ); 
	
	
	@PostMapping(value = "/v1/cmdb/assessment/approveEquipmentProblem")
    public String approveEquipmentProblem(@RequestBody CompEquipmentProblemRequest compEquipmentProblemRequest);
   
	
	@PostMapping(value = "/v1/cmdb/assessment/saveEquipmentProblemList")
	public String saveEquipmentProblemList( @RequestBody JSONObject data);
		 
     
	@PostMapping(value = "/v1/cmdb/assessment/getEquipmentProblemList")
	public List<CompEquipmentProblemResp> getEquipmentProblemList( @RequestBody CompEquipmentProblemRequest compEquipmentProblemRequest );  
	  
	

}
