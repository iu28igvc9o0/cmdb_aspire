package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.index;

import com.aspire.mirror.composite.service.cmdb.index.IKGResourceIndexAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index.IKGResourceServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class IKGResourceIndexController implements IKGResourceIndexAPI {

    @Autowired
    private IKGResourceServiceClient client;

    @Override
    public List<Map<String, Object>> deviceCountByProduceAll(@RequestParam(value = "deviceClass",required = false) String  deviceClass) {
        return client.deviceCountByProduceAll(deviceClass);
    }

    @Override
    public List<Map<String, Object>> modelCountByProduce(@RequestParam(value = "deviceClass",required = false) String deviceClass,
                                                         @RequestParam(value = "produce") String produce) {
        return client.modelCountByProduce(deviceClass,produce);
    }

    @Override
    public List<Map<String, Object>> getAllSegmentAddress() {
        return client.getAllSegmentAddress();
    }

    @Override
    public List<Map<String, Object>> getDeviceUseCount(@RequestParam(value = "segmentAddr",required = false) String segmentAddr,
                                                       @RequestParam(value = "bizSystem",required = false) String bizSystem) {
        return client.getDeviceUseCount(segmentAddr,bizSystem);
    }

    @Override
    public List<Map<String, Object>> getDeviceUseCountByType(@RequestParam(value = "segmentAddr",required = false) String segmentAddr,
                                                             @RequestParam(value = "bizSystem",required = false) String bizSystem) {
        return client.getDeviceUseCountByType(segmentAddr, bizSystem);
    }

    @Override
    public List<Map<String, Object>> deviceCountBySegmentUse(@RequestParam(value = "deviceType",required = false) String deviceType) {
        return client.deviceCountBySegmentUse(deviceType);
    }

    @Override
    public List<Map<String, Object>> deviceCountBySystem(@RequestParam(value = "deviceType",required = false) String deviceType,
                                                         @RequestParam(value = "systemType") String systemType) {
        return client.deviceCountBySystem(deviceType, systemType);
    }

    @Override
    @Authentication(anonymous = true)
    public String deviceUseByPhy() {
        return client.deviceUseByClassAndType("服务器","X86服务器");
    }

    @Override
    @Authentication(anonymous = true)
    public String deviceUseByVm() {
        return client.deviceUseByClassAndType("服务器","虚拟机");
    }

    @Override
    @Authentication(anonymous = true)
    public String deviceUseBySu() {
        return client.deviceUseByClassAndType("服务器","宿主机");
    }
}
