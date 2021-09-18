package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import com.aspire.mirror.composite.service.cmdb.payload.CompDeviceStatisticRequest;
import com.aspire.mirror.composite.service.cmdb.payload.CompDeviceStatisticResp;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@FeignClient( "CMDB")
public interface CmdbDeviceStatisticClient {

    @PostMapping(value = "/v1/cmdb/deviceStatistic/selectDeviceBydeviceType")
    List<CompDeviceStatisticResp> selectDeviceBydeviceType( @RequestBody CompDeviceStatisticRequest  compDeviceStatisticRequest );

    @PostMapping(value = "/v1/cmdb/deviceStatistic/selectDeviceBydeviceTypeDeviceMfrs")
    List<CompDeviceStatisticResp> selectDeviceBydeviceTypeDeviceMfrs( @RequestBody CompDeviceStatisticRequest  compDeviceStatisticRequest );

    @PostMapping(value = "/v1/cmdb/deviceStatistic/selectDeviceByidcTypeDeviceType")
    List<CompDeviceStatisticResp> selectDeviceByidcTypeDeviceType( @RequestBody CompDeviceStatisticRequest  compDeviceStatisticRequest );

    @PostMapping(value = "/v1/cmdb/deviceStatistic/selectDeviceBybizSystem")
    List<CompDeviceStatisticResp> selectDeviceBybizSystem( @RequestBody CompDeviceStatisticRequest  compDeviceStatisticRequest );
    
    @PostMapping(value = "/v1/cmdb/deviceStatistic/selectDeviceByDepartment")
    List<CompDeviceStatisticResp> selectDeviceByDepartment( @RequestBody CompDeviceStatisticRequest  compDeviceStatisticRequest );

    @PostMapping(value = "/v1/cmdb/deviceStatistic/selectDeviceByMultiIdcType")
    List<Map<String, Object>> selectDeviceByMultiIdcType( @RequestBody List<String> idcTypes ) ;

    /**
     *  各资源池业务统计
     * @return 模型列表
     */
    @GetMapping(value = "/v1/cmdb/deviceStatistic/idcTypeStatistic" )
    List<Map<String, Object>> selectIdcTypeStatistic();

    /**
     *  资源池下工期统计
     * @param idcType 所属资源池
     * @return 模型列表
     */
    @GetMapping(value = "/v1/cmdb/deviceStatistic/projectStatistic" )
    List<Map<String, Object>> selectProjectStatistic(@RequestParam("idcType") String idcType);

    /**
     *  指定资源池、工期下的POD池统计
     * @return 模型列表
     */
    @GetMapping(value = "/v1/cmdb/deviceStatistic/podStatistic" )
    List<Map<String, Object>> selectPodStatistic(@RequestParam("idcType") String idcType,
                                                 @RequestParam("projectName") String projectName);
    
}
