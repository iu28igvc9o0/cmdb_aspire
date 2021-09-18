package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.ICompAllocateManageService;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateDomainRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateDomainResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateManagePageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateManagePageResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateNetSegmentResp;
import com.aspire.mirror.composite.service.cmdb.payload.Menu;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbAllocateManageClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;

import lombok.extern.slf4j.Slf4j;


/**
 * ip分配
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:    CompAllocateManageController.java
 * 类描述:    ip分配
 * 创建人:    JinSu
 * 创建时间:  2018/9/19 15:40
 * 版本:      v1.0
 */
@Slf4j
@RestController
public class CompAllocateManageController extends CommonResourceController implements ICompAllocateManageService {

	
	@Autowired
    private CmdbAllocateManageClient cmdbAllocateManageClient;
	
	
	
	@Override
	public String insertDomain(@RequestBody  CompAllocateDomainRequest compAllocateDomainRequest) {
		
		log.info("compAllocateDomainRequest is {} ",compAllocateDomainRequest);
		
		cmdbAllocateManageClient.insertDomain(compAllocateDomainRequest);
		 
		return "success";
	}
	
	@Override
	public String updateDomain(@RequestBody  CompAllocateDomainRequest compAllocateDomainRequest) {
		 
		log.info("compAllocateDomainRequest is {} ",compAllocateDomainRequest);
		
		cmdbAllocateManageClient.updateDomain(compAllocateDomainRequest);
		 
		return "success";
	}
	
	@Override
	public CompAllocateDomainResp selectAllocateDomainById(@RequestParam("id")  String id) {
		
		log.info("id is {} ",id); 
		
		return cmdbAllocateManageClient.selectAllocateDomainById(id);
		 
	}
	

	@Override
	public CompAllocateDomainResp selectAllocateDomainByName(@RequestParam("hostnet") String hostnet, @RequestParam("domain")  String domain) {
		
		log.info("hostnet is {} ",hostnet);
		
		log.info("domain is {} ",domain); 
		
		return cmdbAllocateManageClient.selectAllocateDomainByName(hostnet,domain);
		
	   
	}
	

	 @Override
		public CompAllocateNetSegmentResp selectAllocateNetsegmentByName(@RequestParam("hostnet") String hostnet, @RequestParam("netseg") String netseg) {
		
		    log.info("hostnet is {} ",hostnet);
			
			log.info("netseg is {} ",netseg); 
		 
		 return cmdbAllocateManageClient.selectAllocateNetsegmentByName(hostnet,netseg);
		}

	

	@Override
	public String deleteDomain( @RequestParam("id")  String id) {
		 
		log.info("id is {} ",id);
		
		cmdbAllocateManageClient.deleteDomain(id);
		
		return "success";
	}

	 

	@Override
	public PageResponse<CompAllocateManagePageResp> selectAllocateManageByPage( @RequestBody CompAllocateManagePageRequest compAllocateManagePageRequest) {
		 
		log.info("compAllocateManagePageRequest is {} ",compAllocateManagePageRequest);
		
		return cmdbAllocateManageClient.selectAllocateManageByPage(compAllocateManagePageRequest);
		
 
	}

	@Override
	public List<Menu> selectMenu() {
	 
		return cmdbAllocateManageClient.selectMenu();
	}

	
	

	

}
