package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateManagePageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompAllocateManagePageResp;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardwareRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardwareResp;
import com.aspire.ums.cmdb.maintenance.payload.MaintenHardwareRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardPageRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompMaintenHardPageResp;
 
 


@FeignClient( "CMDB")
public interface CmdbMaintenHardClient {

    
    @PostMapping(value = "/v1/cmdb/maintenhard/insertMaintenHardware")
    public String insertMaintenHardware(@RequestBody CompMaintenHardwareRequest compMaintenHardwareRequest);
   
    
    @PostMapping(value = "/v1/cmdb/maintenhard/updateMaintenHardware")
    public String updateMaintenHardware(@RequestBody CompMaintenHardwareRequest compMaintenHardwareRequest);
    
    
    @PostMapping(value = "/v1/cmdb/maintenhard/batchUpdateMaintenHardware")
    public String batchUpdateMaintenHardware(@RequestBody CompMaintenHardwareRequest compMaintenHardwareRequest);
    
    
    
    @GetMapping(value = "/v1/cmdb/maintenhard/selectMaintenHardwareById")
    public CompMaintenHardwareResp selectMaintenHardwareById( @RequestParam("id") String id );
    
    
    @GetMapping(value = "/v1/cmdb/maintenhard/selectMaintenHardwareByDeviceModel")
    public CompMaintenHardwareResp selectMaintenHardwareByDeviceModel( @RequestParam("mainten_factory") String maintenFactory,
																	   @RequestParam("device_model") String deviceModel,
																	   @RequestParam("warranty_date") String warrantyDate);
    
    
    @DeleteMapping(value = "/v1/cmdb/maintenhard/deleteMaintenHardware")
    public String deleteMaintenHardware( @RequestParam("ids") String ids );
    
    
    @PostMapping(value = "/v1/cmdb/maintenhard/listMaintenHardwareByPage")
    public PageResponse<CompMaintenHardPageResp> selectMaintenHardByPage( @RequestBody  CompMaintenHardPageRequest  compMaintenHardPageRequest ) ;
    
    
    @PostMapping(value = "/v1/cmdb/maintenhard/getMaintenHardwareList")
    public List<CompMaintenHardPageResp> getMaintenHardwareList( @RequestBody CompMaintenHardPageRequest  compMaintenHardPageRequest );
    
    
    @PostMapping(value = "/v1/cmdb/maintenhard/insertMaintenHardwareList")
    public String insertMaintenHardwareList( @RequestBody List<CompMaintenHardwareRequest> compMaintenHardwareRequestList ); 
    
   
    
}
