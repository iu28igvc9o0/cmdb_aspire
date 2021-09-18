package com.aspire.ums.cmdb.v2.index.web;

import com.aspire.ums.cmdb.index.KGResourceIndexAPI;
import com.aspire.ums.cmdb.v2.index.serivce.KGResourceIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class KGResourceIndexController implements KGResourceIndexAPI {

    @Autowired
    private KGResourceIndexService service;

    @Override
    public List<Map<String, Object>> deviceCountByProduceAll(@RequestParam(value = "deviceClass",required = false) String deviceClass) {
        return service.deviceCountByProduceAll(deviceClass);
    }

    @Override
    public List<Map<String, Object>> modelCountByProduce(@RequestParam(value = "deviceClass",required = false) String deviceClass,
                                                         @RequestParam(value = "produce") String produce) {
        return service.modelCountByProduce(deviceClass,produce);
    }

    @Override
    public List<Map<String, Object>> getAllSegmentAddress() {
        return service.getAllSegmentAddress();
    }

    @Override
    public List<Map<String, Object>> getDeviceUseCount(@RequestParam(value = "segmentAddr",required = false) String segmentAddr,
                                                       @RequestParam(value = "bizSystem",required = false) String bizSystem) {
        return service.getDeviceUseCount(segmentAddr, bizSystem);
    }

    @Override
    public List<Map<String, Object>> getDeviceUseCountByType(@RequestParam(value = "segmentAddr",required = false) String segmentAddr,
                                                             @RequestParam(value = "bizSystem",required = false) String bizSystem) {
        return service.getDeviceUseCountByType(segmentAddr, bizSystem);
    }

    @Override
    public List<Map<String, Object>> deviceCountBySegmentUse(@RequestParam(value = "deviceType",required = false) String deviceType) {
        return service.deviceCountBySegmentUse(deviceType);
    }

    @Override
    public List<Map<String, Object>> deviceCountBySystem(@RequestParam(value = "deviceType",required = false) String deviceType,
                                                         @RequestParam(value = "systemType") String systemType) {
        return service.deviceCountBySystem(deviceType, systemType);
    }

    @Override
    public String deviceUseByClassAndType(@RequestParam(value = "deviceClass") String deviceClass,
                                          @RequestParam(value = "deviceType") String deviceType) {
        return service.deviceUseByClassAndType(deviceClass,deviceType);
    }
}
