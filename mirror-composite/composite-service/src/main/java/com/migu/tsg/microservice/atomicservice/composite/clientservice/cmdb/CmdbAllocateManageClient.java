package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateDomainRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateDomainResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateManagePageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateManagePageResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateNetSegmentResp;
import com.aspire.mirror.composite.service.cmdb.payload.Menu;
 
 

 

@FeignClient( "CMDB")
public interface CmdbAllocateManageClient {

    
    @PostMapping(value = "/v1/cmdb/allocate/insertDomain")
    public String insertDomain(@RequestBody CompAllocateDomainRequest compAllocateDomainRequest);
    
    @PostMapping(value = "/v1/cmdb/allocate/updateDomain")
    public String updateDomain(@RequestBody CompAllocateDomainRequest compAllocateDomainRequest);
    
    
    @GetMapping(value = "/v1/cmdb/allocate/selectDomainById")
    public CompAllocateDomainResp selectAllocateDomainById( @RequestParam("id") String id );
    
    
    @GetMapping(value = "/v1/cmdb/allocate/selectDomainByName")
    public CompAllocateDomainResp selectAllocateDomainByName( @RequestParam("hostnet") String hostnet, @RequestParam("domain") String domain );
    
    
    @GetMapping(value = "/v1/cmdb/allocate/selectNetsegmentByName")
    public CompAllocateNetSegmentResp selectAllocateNetsegmentByName( @RequestParam("hostnet") String hostnet, @RequestParam("netseg") String netseg );
    
    
    
    @DeleteMapping(value = "/v1/cmdb/allocate/deleteDomain")
    public String deleteDomain( @RequestParam("id") String id );
    
    
    @PostMapping(value = "/v1/cmdb/allocate/listDomaineByPage")
    public PageResponse<CompAllocateManagePageResp> selectAllocateManageByPage( @RequestBody  CompAllocateManagePageRequest compAllocateManagePageRequest ) ;
    
    
    @GetMapping(value = "/v1/cmdb/allocate/selectAllocateMenu")
    public  List<Menu> selectMenu();
    
   
    
}
