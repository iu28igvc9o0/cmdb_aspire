package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.assessment.payload.CmdbServiceAssess;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.assessment
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/15 16:15
 * 版本: v1.0
 */
@FeignClient(value = "CMDB")
public interface CmdbServiceAccessClient {
    
//    @GetMapping("/cmdb/device/serviceAssess/insert")
//    Map<String, Object> insert(@RequestParam(value = "quarter") String quarter);
    
    @PostMapping("/cmdb/device/serviceAssess/queryAllData")
    List<CmdbServiceAssess> queryAllData(@RequestParam(value = "device_type",required = false) String deviceType,
                                           @RequestParam(value = "quarter") String quarter);
    
    @PostMapping("/cmdb/device/serviceAssess/saveScore")
    Map<String, Object> saveScore(@RequestBody List<CmdbServiceAssess> list);


    /**
     * 获取厂商评分下设备类型评分状态
     * @param quarter 实例数据
     * @return
     */
    @RequestMapping(value = "/cmdb/device/serviceAssess/getScoreDeviceTypeStatus", method = RequestMethod.GET)
    List<Map<String, Object>> getScoreDeviceTypeStatus(@RequestParam("quarter") String quarter);

}
