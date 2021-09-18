package com.aspire.ums.cmdb.v2.assessment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemPageRequest;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemRequest;
import com.aspire.ums.cmdb.assessment.payload.EquipmentProblemResp;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.assessment.entity.EquipmentProblem;
import com.aspire.ums.cmdb.v2.assessment.mapper.EquipmentProblemMapper;
import com.aspire.ums.cmdb.v2.assessment.service.IEquipmentProblemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

/**
 * 设备问题业务层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.maintenance.service.impl
 * 类名称:    MaintenHardServiceImpl.java
 * 类描述:    设备问题业务层
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
@Slf4j
@Service
@Transactional
public class EquipmentProblemServiceImpl implements IEquipmentProblemService {
	
    @Autowired
    private EquipmentProblemMapper  equipmentProblemMapper;
    
    
    /**
     * 插入设备问题
     *
     * @param equipmentProblem 设备问题
     */
	@Override
	public int insertEquipmentProblem(EquipmentProblem equipmentProblem) {
		
        log.info("equipmentProblem is {} ",equipmentProblem);
        equipmentProblem.setCreateTime(new Date());
        List<EquipmentProblem> problems = new ArrayList<>();
        problems.add(equipmentProblem);
        equipmentProblem.setId(UUIDUtil.getUUID());
		return equipmentProblemMapper.insertEquipmentProblem(problems);
	}
 

    /**
     * 查询单个设备问题根据id
     *
     * @param id 设备问题
     */
	@Override
	public EquipmentProblem selectEquipmentProblemById(String id) {
		
        log.info("id is {} ",id);
    	
		return equipmentProblemMapper.selectEquipmentProblemById(id);
	}
 
  
	@Override
	public int updateEquipmentProblem(EquipmentProblem equipmentProblem) {
		
		log.info("equipmentProblem is {} ",equipmentProblem);
		 equipmentProblem.setUpdateTime(new Date());
    	int count= equipmentProblemMapper.updateEquipmentProblem(equipmentProblem);
    	
		return count;
		 
	}
	
	
	
    
    //删除设备问题
	@Override
	public int deleteEquipmentProblem(String id) {
        
		log.info("id is {} ",id);
    	
		return equipmentProblemMapper.deleteEquipmentProblem(id);
	}

	
	
	
	@Override
	public int approveEquipmentProblem(EquipmentProblem equipmentProblem) {
		
		log.info("equipmentProblem is {} ",equipmentProblem);
		
		Map<String, Object> hashMap=new HashMap<String, Object>();
		 
		
		hashMap.put("province", equipmentProblem.getProvince());
		hashMap.put("quarter", equipmentProblem.getQuarter());

		hashMap.put("assessStatus", equipmentProblem.getAssessStatus()); 
		 
    	int count= equipmentProblemMapper.approveEquipmentProblem(hashMap);
    	
		return count;
		 
	}
	
	
	
	@Override
	public PageBean<EquipmentProblemResp> seleteEquipmentProblemByPage( EquipmentProblemPageRequest equipmentProblemPageRequest) {
		 
       log.info("equipmentProblemPageRequest is {} ",equipmentProblemPageRequest);
		
		PageBean<EquipmentProblemResp> pageBean=new PageBean<EquipmentProblemResp>();
				 
	         
        Map<String, Object> hashMap=new HashMap<String, Object>();
		 
        int pageSize=Integer.valueOf(equipmentProblemPageRequest.getPageSize());
		int pageNo=Integer.valueOf(equipmentProblemPageRequest.getPageNo());
		 
		hashMap.put("pageNo", (pageNo - 1) * pageSize);
		hashMap.put("pageSize", pageSize);
	
		hashMap.put("province", equipmentProblemPageRequest.getProvince());
		hashMap.put("quarter", equipmentProblemPageRequest.getQuarter());

		int count=equipmentProblemMapper.getEquipmentProblemCount(hashMap);
		
        List<EquipmentProblem> equipmentProblemList=equipmentProblemMapper.selectEquipmentProblemByPage(hashMap); 
		
		List<EquipmentProblemResp> equipmentProblemRespList= new ArrayList<EquipmentProblemResp>();
		
        for ( EquipmentProblem equipmentProblem : equipmentProblemList ) {
			
			EquipmentProblemResp equipmentProblemResp=new EquipmentProblemResp();
			
			BeanUtils.copyProperties(equipmentProblem, equipmentProblemResp);
			
			equipmentProblemRespList.add(equipmentProblemResp);
		}
		
 
		pageBean.setCount(count);
		pageBean.setResult(equipmentProblemRespList);
		
		
		return pageBean;
	}


	
	@Override
	public List<EquipmentProblemResp> getEquipmentProblemList(EquipmentProblemRequest equipmentProblemRequest) {
        log.info("equipmentProblemRequest is {} ",equipmentProblemRequest);
		
		Map<String, Object> hashMap=new HashMap<String, Object>();
			 
		hashMap.put("province", equipmentProblemRequest.getProvince());
		hashMap.put("quarter", equipmentProblemRequest.getQuarter());
		hashMap.put("person", equipmentProblemRequest.getPerson()); 
		 	
		
        List<EquipmentProblem> equipmentProblemList=equipmentProblemMapper.getEquipmentProblemList(hashMap); 
		
		List<EquipmentProblemResp> equipmentProblemRespList= new ArrayList<EquipmentProblemResp>();
		
		for ( EquipmentProblem equipmentProblem : equipmentProblemList ) {
			
			EquipmentProblemResp equipmentProblemResp=new EquipmentProblemResp();
			
			BeanUtils.copyProperties(equipmentProblem, equipmentProblemResp);
			
			equipmentProblemRespList.add(equipmentProblemResp);
		}
		
		
		return equipmentProblemRespList;
	}

	
	
	@Override
	@Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
	public int saveEquipmentProblemList(JSONObject data) {
		List<EquipmentProblem> equipmentProblemList = JSON.parseArray(data.getJSONArray("saveData").toJSONString(), EquipmentProblem.class);
		List<String> deleteIds = JSON.parseArray(data.getJSONArray("deleteIds").toJSONString(), String.class);
		log.info("equipmentProblemList is {} ",data);
		for(EquipmentProblem equipmentProblem: equipmentProblemList  ){

			String id=equipmentProblem.getId();

		    if (StringUtils.isNotEmpty(id)) {
		    	equipmentProblem.setUpdateTime(new Date());

			}else{
				equipmentProblem.setId(UUIDUtil.getUUID());
		    	equipmentProblem.setCreateTime(new Date());
			}

		}
		if (deleteIds.size() > 0) {
			equipmentProblemMapper.deleteByBatch(deleteIds);
		}
		if (equipmentProblemList.size() > 0) {
			equipmentProblemMapper.insertEquipmentProblem(equipmentProblemList);
		}
		return  1;
		
	}
	
	
}
