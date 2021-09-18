package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.assessment;

import com.aspire.mirror.composite.service.cmdb.assessment.IServiceAccessAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbServiceAssess;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.assessment.CmdbServiceAccessClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.assessment
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/15 16:13
 * 版本: v1.0
 */
@RestController
@Slf4j
public class CmdbServiceAccessController implements IServiceAccessAPI {
    
    @Autowired
    private CmdbServiceAccessClient serviceAccessClient;
    
    @Override
    public List<CmdbServiceAssess> queryAllData(@RequestParam(value = "device_type",required = false) String deviceType,
                                                  @RequestParam(value = "quarter") String quarter) {
        return serviceAccessClient.queryAllData(deviceType, quarter);
    }
    
    @Override
    public Map<String, Object> saveScore(@RequestBody List<CmdbServiceAssess> list) {
        return serviceAccessClient.saveScore(list);
    }

    @Override
    public List<Map<String, Object>> getScoreDeviceTypeStatus(@RequestParam("quarter") String quarter){
        return serviceAccessClient.getScoreDeviceTypeStatus(quarter);
    }


}
