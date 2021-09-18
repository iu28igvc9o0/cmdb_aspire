package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.composite.service.cmdb.ICompInstanceSearchService;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.InstanceSearchResp;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbInstanceSearchClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;

import lombok.extern.slf4j.Slf4j;


 
@Slf4j
@RestController
public class CompInstanceSearchController extends CommonResourceController implements ICompInstanceSearchService {

	
	@Autowired
    private CmdbInstanceSearchClient cmdbInstanceSearchClient;

	@Override
	public PageBean<InstanceSearchResp> selectInstanceByPage(@RequestBody  InstanceSearchRequest instanceSearchRequest) {
		 
		log.info("selectInstanceByPage is {} ",instanceSearchRequest);
		
		return cmdbInstanceSearchClient.selectInstanceByPage(instanceSearchRequest);
		
	}

	  

}
