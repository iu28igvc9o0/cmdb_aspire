package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftPageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftPageResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftwareRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenSoftwareResp;
 
 


@FeignClient( "CMDB")
public interface CmdbMaintenSoftClient {

    
    @PostMapping(value = "/v1/cmdb/maintensoft/insertMaintenSoftware")
    Map<String, Object> insertMaintenSoftware(@RequestBody CompMaintenSoftwareRequest compMaintenSoftwareRequest);


//    @PostMapping(value = "/v1/cmdb/maintensoft/updateMaintenSoftware")
//    Map<String, Object> updateMaintenSoftware(@RequestBody CompMaintenSoftwareRequest compMaintenSoftwareRequest);


    @PostMapping(value = "/v1/cmdb/maintensoft/batchUpdateMaintenSoftware")
    Map<String, Object> batchUpdateMaintenSoftware(@RequestBody CompMaintenSoftwareRequest compMaintenSoftwareRequest);


    @GetMapping(value = "/v1/cmdb/maintensoft/selectMaintenSoftwareById")
     CompMaintenSoftwareResp selectMaintenSoftwareById( @RequestParam("id") String id );


    @GetMapping(value = "/v1/cmdb/maintensoft/selectMaintenSoftwareBySoftNmae")
     CompMaintenSoftwareResp selectMaintenSoftwareBySoftNmae(@RequestParam("project") String project , @RequestParam("softwareName") String softwareName );


    @DeleteMapping(value = "/v1/cmdb/maintensoft/deleteMaintenSoftware")
    Map<String, Object> deleteMaintenSoftware( @RequestParam("ids") String ids );


    @PostMapping(value = "/v1/cmdb/maintensoft/listMaintenSoftwareByPage")
     PageResponse<CompMaintenSoftPageResp> selectMaintenSoftByPage( @RequestBody  CompMaintenSoftPageRequest  compMaintenSoftPageRequest ) ;


//    @PostMapping(value = "/v1/cmdb/maintensoft/getMaintenSoftwareList")
//     List<CompMaintenSoftPageResp> getMaintenSoftwareList( @RequestBody CompMaintenSoftPageRequest  compMaintenSoftPageRequest );


    @PostMapping(value = "/v1/cmdb/maintensoft/insertMaintenSoftwareList")
    Map<String, Object> insertMaintenSoftwareList( @RequestBody List<CompMaintenSoftwareRequest> compMaintenSoftwareRequestList );

   
    
}
