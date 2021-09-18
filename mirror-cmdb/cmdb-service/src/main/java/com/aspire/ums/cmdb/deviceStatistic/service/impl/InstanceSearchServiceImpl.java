package com.aspire.ums.cmdb.deviceStatistic.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.deviceStatistic.mapper.InstanceSearchMapper;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchResp;
import com.aspire.ums.cmdb.deviceStatistic.service.InstanceSearchService;

import lombok.extern.slf4j.Slf4j;

 
@Slf4j
@Service
@Transactional
public class InstanceSearchServiceImpl implements InstanceSearchService {
	
    
    @Autowired
    private  InstanceSearchMapper instanceSearchMapper;
    
   
	@Override
	public PageBean<InstanceSearchResp> selectInstanceByPage(InstanceSearchRequest instanceSearchRequest) {
        
		PageBean<InstanceSearchResp> pageBean=new PageBean<InstanceSearchResp>();
		 
        Map<String, Object> hashMap=new HashMap<String, Object>();
		 
 	
		if (instanceSearchRequest.getPageNo()!=null && instanceSearchRequest.getPageSize()!=null ) {
			int pageSize=Integer.valueOf(instanceSearchRequest.getPageSize());
			int pageNo=Integer.valueOf(instanceSearchRequest.getPageNo());
			
			hashMap.put("pageSize", pageSize);
			hashMap.put("pageNo", (pageNo - 1) * pageNo);
		}
		if(StringUtils.isNotBlank(instanceSearchRequest.getBizSystem())) {
			hashMap.put("bizSystemList",instanceSearchRequest.getBizSystem().split(","));
		}
		//hashMap.put("bizSystem", instanceSearchRequest.getBizSystem());
		hashMap.put("idcType", instanceSearchRequest.getIdcType());
		hashMap.put("roomId", instanceSearchRequest.getRoomId());
		hashMap.put("deviceClass", instanceSearchRequest.getDeviceClass());
		hashMap.put("deviceType", instanceSearchRequest.getDeviceType());
		hashMap.put("ip", instanceSearchRequest.getIp());
		 
		
        int count=instanceSearchMapper.getInstanceSearchCount(hashMap);
		
		List<Map<String, Object>> instanceSearchList=instanceSearchMapper.getInstanceSearchByPage (hashMap); 
		
		List<InstanceSearchResp> instanceSearchPageList= new ArrayList<InstanceSearchResp>();
		
		for ( Map<String, Object> map : instanceSearchList ) {
			
			InstanceSearchResp instanceSearchResp=new InstanceSearchResp();		
	 
			instanceSearchResp.setBizSystem((String)map.get("bizSystem"));
			instanceSearchResp.setIdcType((String)map.get("idcType"));
			instanceSearchResp.setRoomId((String)map.get("roomId"));
			instanceSearchResp.setDeviceClass((String)map.get("device_class"));
			instanceSearchResp.setDeviceType((String)map.get("device_type"));
			instanceSearchResp.setIp((String)map.get("ip"));
				 		
			instanceSearchPageList.add(instanceSearchResp);
		}
		
		pageBean.setCount(count);
		pageBean.setResult(instanceSearchPageList);
		
		
		return pageBean;
	}

	@Override
	public List<Map<String, String>> selectInstanceByAuth(Map<String, List<String>> queryMap) {
		return instanceSearchMapper.selectInstanceByAuth(queryMap);
	}

	@Override
	public Map<String, Object> selectDepartBizSystem(String ip, String bizSystem) {
		
		
		Map<String, Object>  departmentMap= instanceSearchMapper.selectDepartment(ip,bizSystem);
	    
	    List<Map<String,Object>>  bizSystemList= instanceSearchMapper.selectbizSystemConcatList( bizSystem );
		
	    Map<String, Object> resultMap=new HashMap<String, Object>();
	    if (departmentMap != null) {
			resultMap.put("department1", departmentMap.containsKey("department1") ? departmentMap.get("department1") : "");
			resultMap.put("department2", departmentMap.containsKey("department2") ? departmentMap.get("department2") : "");
		}
	    resultMap.put("contactList", bizSystemList);
		
		return resultMap;
		
		
	}


}
