package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.aspire.mirror.composite.service.cmdb.payload.CompInvalidResourceRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompInvalidResourceResp;
 
 

@FeignClient( "CMDB")
public interface CmdbInvalidResourceClient {

      
    @PostMapping(value = "/v1/cmdb/invalidResource/selectInvalidResource")
    public List<CompInvalidResourceResp> selectInvalidResource( @RequestBody CompInvalidResourceRequest  compInvalidResourceRequest );
    
    
    @PostMapping(value = "/v1/cmdb/invalidResource/insertInvalidResource")
    public String insertInvalidResource( @RequestBody List<CompInvalidResourceResp>  compInvalidResourceList );
    
      
    
}
