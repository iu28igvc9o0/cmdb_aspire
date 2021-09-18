package com.aspire.ums.cmdb.v2.assessment.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.IEquipmentProblemAPI;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemPageRequest;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemRequest;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemResp;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.v2.assessment.entity.EquipmentProblem;
import com.aspire.ums.cmdb.v2.assessment.service.IEquipmentProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: EquipmentProblemController
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
 
@RestController
@Slf4j
public class EquipmentProblemController implements IEquipmentProblemAPI   {
	
	 
    @Autowired
    private IEquipmentProblemService  equipmentProblemService;
    
    
    /**
     *  新增设备问题
     * @return 模型列表.3
     */
	@Override
	public String insertEquipmentProblem( @RequestBody  EquipmentProblemRequest equipmentProblemRequest) {
		
		log.info("equipmentProblemRequest is {} ",equipmentProblemRequest);
   	 
		EquipmentProblem equipmentProblem=new EquipmentProblem();
    	
    	BeanUtils.copyProperties(equipmentProblemRequest, equipmentProblem);
    	
    	equipmentProblemService.insertEquipmentProblem( equipmentProblem );
    	
    	return "success";
	}
	
	
	 /**
     *  查询设备问题通过id
     * @return  
     */ 
	@Override
	public EquipmentProblemResp selectEquipmentProblemById(@RequestParam("id")  String id) {
        
		log.info("id is {} ",id);
    	
    	EquipmentProblem EquipmentProblem=equipmentProblemService.selectEquipmentProblemById(id);
    	
    	EquipmentProblemResp EquipmentProblemResp=new EquipmentProblemResp();
    	
    	BeanUtils.copyProperties(EquipmentProblem, EquipmentProblemResp);
    	 
        return EquipmentProblemResp;
	}

    
	
	/**
     *  修改设备问题
     * @return 模型列表
     */
	@Override
	public String updateEquipmentProblem( @RequestBody   EquipmentProblemRequest equipmentProblemRequest) {
		 
       log.info("equipmentProblemRequest is {} ",equipmentProblemRequest); 
    	
       
        EquipmentProblem equipmentProblem=new EquipmentProblem();
    	
    	BeanUtils.copyProperties(equipmentProblemRequest, equipmentProblem);
    	
    	equipmentProblemService.updateEquipmentProblem( equipmentProblem );
    	
    	return "success";
		
		 
	}


	 /**
     *  删除设备问题
     * @return 模型列表
     */
	@Override
	public String deleteEquipmentProblem( @RequestParam("id")  String id) {
        
		log.info("id is {} ",id);
    	
		equipmentProblemService.deleteEquipmentProblem(id);
    	
    	return "success";  
	}
   
	
    /**
     *  分页查询设备问题
     * @return 模型列表
     */
	@Override
	public PageBean<EquipmentProblemResp> seleteEquipmentProblemByPage( @RequestBody  EquipmentProblemPageRequest equipmentProblemPageRequest) {
		 
        log.info("equipmentProblemPageRequest is {} ",equipmentProblemPageRequest);
    	
        return equipmentProblemService.seleteEquipmentProblemByPage(equipmentProblemPageRequest);
	}
	


	/**
     *  审批设备问题
     * @return 模型列表
     */
	@Override
	public String approveEquipmentProblem( @RequestBody   EquipmentProblemRequest equipmentProblemRequest) {
		 
       log.info("equipmentProblemRequest is {} ",equipmentProblemRequest); 
    	
       
        EquipmentProblem equipmentProblem=new EquipmentProblem();
    	
    	BeanUtils.copyProperties(equipmentProblemRequest, equipmentProblem);
    	
    	equipmentProblemService.approveEquipmentProblem( equipmentProblem );
    	
    	return "success";
		
    	
	}
	
	
	
	 /**
     *  查询设备问题列表
     * @return 模型列表
     */
	@Override
	public List<EquipmentProblemResp> getEquipmentProblemList(@RequestBody  EquipmentProblemRequest equipmentProblemRequest) {
        
		log.info("equipmentProblemRequest is {} ",equipmentProblemRequest);
    	
    	return equipmentProblemService.getEquipmentProblemList(equipmentProblemRequest);
	}

	

	 /**
     * 新增设备问题列表
     * @return 模型列表
     */
	 @Override
	 public String saveEquipmentProblemList(@RequestBody JSONObject data) {

		 log.info("equipmentProblemRequestList  data is {} ", data);
		 List<EquipmentProblemRequest> equipmentProblemReqList = JSON.parseArray(data.getJSONArray("saveData").toJSONString(), EquipmentProblemRequest.class);

		 List<EquipmentProblem> equipmentProblemList=new ArrayList<EquipmentProblem>();
	    	
	    	for(EquipmentProblemRequest equipmentProblemRequest : equipmentProblemReqList ){
	    		
	    		 EquipmentProblem equipmentProblem=new EquipmentProblem();
	    	    	
	    	     BeanUtils.copyProperties(equipmentProblemRequest, equipmentProblem);
	    		
	    	     equipmentProblemList.add(equipmentProblem);
	    	}
	    	
	    	data.put("saveData", equipmentProblemList);
			equipmentProblemService.saveEquipmentProblemList(data);
				 
			return "success";
			
	  }
    
	  
		
}
