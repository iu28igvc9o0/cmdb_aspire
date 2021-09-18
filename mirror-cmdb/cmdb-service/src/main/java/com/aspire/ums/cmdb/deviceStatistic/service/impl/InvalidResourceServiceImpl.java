package com.aspire.ums.cmdb.deviceStatistic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.deviceStatistic.mapper.InvalidResourceMapper;
import com.aspire.ums.cmdb.deviceStatistic.payload.InvalidResourceRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InvalidResourceResp;
import com.aspire.ums.cmdb.deviceStatistic.service.InvalidResourceService;

import lombok.extern.slf4j.Slf4j;

/**
 * 设备统计业务层
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.deviceStatistic.service.impl
 * 类名称:    DeviceStatisticServiceImpl.java
 * 类描述:    设备统计业务层
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
@Slf4j
@Service
@Transactional
public class InvalidResourceServiceImpl implements InvalidResourceService {
	
    
    @Autowired
    private  InvalidResourceMapper invalidResourceMapper;
    

	@Override
	public List<InvalidResourceResp> selectInvalidResource(InvalidResourceRequest invalidResourceRequest) {
        
		log.info("invalidResourceRequest is {} ",invalidResourceRequest);
		
		Map<String, Object> hashMap=new HashMap<String, Object>();
		 
		hashMap.put("idcType", invalidResourceRequest.getIdcType());
		hashMap.put("department1", invalidResourceRequest.getDepartment1());
		hashMap.put("department2", invalidResourceRequest.getDepartment2());
		hashMap.put("bizSystem", invalidResourceRequest.getBizSystem());
		hashMap.put("podName", invalidResourceRequest.getPodName()); 
		
		 
        List<Map<String, Object>> deviceStatisticList=invalidResourceMapper.selectInvalidResource(hashMap);
		
		List<InvalidResourceResp> invalidResourceRespList= new ArrayList<InvalidResourceResp>();
		
        for ( Map<String, Object> map : deviceStatisticList ) {
			
        	InvalidResourceResp  invalidResourceResp=new InvalidResourceResp();
			
        	invalidResourceResp.setIdcType((String) map.get("idcType"));
        	invalidResourceResp.setDepartment1((String) map.get("department1"));
        	invalidResourceResp.setDepartment2((String) map.get("department2"));
        	invalidResourceResp.setBizSystem((String) map.get("bizSystem"));
        	invalidResourceResp.setPodName((String) map.get("podName"));
        	invalidResourceResp.setPhysicalNumber((String) map.get("physicalNumber") );
        	invalidResourceResp.setVirtualNumber ( (String)map.get("virtualNumber") );
        	invalidResourceResp.setPlanTime ( (String) map.get("planTime") );
        	invalidResourceResp.setRealityTime ( (String) map.get("realityTime"));
        		
        	invalidResourceRespList.add(invalidResourceResp);
		}
		
		
		return invalidResourceRespList;
	}

	@Override
	public int insertInvalidResource(List<InvalidResourceResp> invalidResourceList) {
		
		int count=invalidResourceMapper.insertInvalidResource(invalidResourceList);
		
		
		
		return count;
	}

    
	
}
