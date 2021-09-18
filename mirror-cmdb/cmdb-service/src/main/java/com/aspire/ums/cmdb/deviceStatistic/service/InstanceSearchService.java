package com.aspire.ums.cmdb.deviceStatistic.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchResp;


public interface InstanceSearchService {
	
  
	public  PageBean<InstanceSearchResp> selectInstanceByPage(InstanceSearchRequest instanceSearchRequest);


    List<Map<String,String>> selectInstanceByAuth(Map<String, List<String>> queryMap);
    
    
    Map<String, Object> selectDepartBizSystem( String ip , String bizSystem) ;
    
    
}
