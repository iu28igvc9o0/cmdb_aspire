package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.Hardware;
import com.aspire.ums.cmdb.maintenance.payload.HardwareRequest;

/**
 * 类名称: MaintenHardWareClient
 * 类描述: 硬件维保管理
 * 创建人: PJX
 * 创建时间: 2019/7/31 11:18
 * 版本: v1.0
 */
@FeignClient(value = "CMDB")
public interface MaintenHardWareClient {

    @PostMapping("/cmdb/mainten/hardware/updateHardware")
    Map<String, Object> updateHardware(@RequestBody Hardware hardware);

    @PostMapping("/cmdb/mainten/hardware/batchUpdateHardware")
    Map<String, Object> batchUpdateHardware(@RequestBody Hardware hardwareList);

    @PostMapping("/cmdb/mainten/hardware/selectHardwareByPage")
    Result<Hardware> selectHardwareByPage(@RequestBody HardwareRequest request);

    @RequestMapping(value = "/cmdb/mainten/hardware/getHardwareList", method = RequestMethod.POST)
    List<Hardware> getHardwareList(@RequestBody HardwareRequest request);

    @RequestMapping(value = "/cmdb/mainten/hardware/export", method = RequestMethod.POST)
    List<Map<String, Object>> export(@RequestBody HardwareRequest sendRequest);

    @RequestMapping(value = "/cmdb/mainten/hardware/queryIsExist", method = RequestMethod.GET)
    Map<String, Object> queryIsExist(@RequestParam("deviceSerialNumber") String deviceSerialNumber,@RequestParam("warrantyDate") String warrantyDate);

    @RequestMapping(value = "/cmdb/mainten/hardware/getNeedInfo", method = RequestMethod.GET)
    Map<String,Object> queryInfoByNameAndDeviceSn(@RequestParam("projectName") String projectName,@RequestParam("deviceSn") String deviceSn);
}
