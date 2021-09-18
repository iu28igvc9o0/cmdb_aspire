package com.aspire.ums.cmdb.v2.index.serivce.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.v2.index.mapper.KGResourceIndexMapper;
import com.aspire.ums.cmdb.v2.index.serivce.KGResourceIndexService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class KGResourceIndexServiceImpl implements KGResourceIndexService {

    @Autowired
    private KGResourceIndexMapper kgResourceIndexMapper;

    @Override
    public List<Map<String, Object>> deviceCountByProduceAll(String deviceClass) {

        return kgResourceIndexMapper.deviceCountByProduceAll(getDeviceParam(deviceClass));
    }

    @Override
    public List<Map<String, Object>> modelCountByProduce(String deviceClass, String produce) {
        Map<String, String> deviceParam = getDeviceParam(deviceClass);
        deviceParam.put("produce",produce);
        return kgResourceIndexMapper.modelCountByProduce(deviceParam);
    }

    private Map<String,String> getDeviceParam(String deviceClass) {
        Map<String,String> param = Maps.newHashMap();
        switch (deviceClass) {
            case "phyServer":
                param.put("deviceType","X86服务器");
                break;
            case "netWork":
                param.put("deviceClass","网络设备");
                break;
            case "storage":
                param.put("deviceClass","存储设备");
                break;
            case "security":
                param.put("deviceClass","安全设备");
                break;
            default:
                param.put("paramType","all");
                param.put("deviceType","X86服务器");
                param.put("deviceClass","网络设备,存储设备,安全设备");
                break;
        }
        return param;
    }

    @Override
    public List<Map<String, Object>> getAllSegmentAddress() {
        return kgResourceIndexMapper.getAllSegmentAddress();
    }

    @Value("${deviceStatus.deviceUse:运行正常}")
    private String deviceUse;
    @Value("${deviceStatus.deviceUnUse:闲置}")
    private String deviceUnUse;

    @Override
    public List<Map<String, Object>> getDeviceUseCount(String segmentAddr, String bizSystem) {

        List<Map<String, Object>> response = Lists.newArrayList();

        String deviceType = phyServer + "," + vmHost;
        // 使用
        Map<String, Object> deviceUseCount = kgResourceIndexMapper.getDeviceStatusCount(segmentAddr, bizSystem, deviceUse,deviceType);
        deviceUseCount.put("deviceStatus","使用");
        response.add(deviceUseCount);
        // 闲置
        Map<String, Object> deviceUnUseCount = kgResourceIndexMapper.getDeviceStatusCount(segmentAddr, bizSystem, deviceUnUse,deviceType);
        deviceUnUseCount.put("deviceStatus","闲置");
        response.add(deviceUnUseCount);
        return response;
    }

    @Value("${cmdb.kg.deviceType.phyServer:X86服务器}")
    private String phyServer;
    @Value("${cmdb.kg.deviceType.vmHost:虚拟机}")
    private String vmHost;

    @Override
    public List<Map<String, Object>> getDeviceUseCountByType(String segmentAddr, String bizSystem) {
        List<Map<String, Object>> res = Lists.newArrayList();
        String deviceType = phyServer + "," + vmHost;
        // 使用
        List<Map<String, Object>> deviceUseCount = kgResourceIndexMapper.getDeviceUseCountByType(segmentAddr, bizSystem, deviceUse,deviceType);
        Map<String, Object> deviceUseMap = listToMap(deviceUseCount);
        // 闲置
        List<Map<String, Object>> deviceUnUseCount = kgResourceIndexMapper.getDeviceUseCountByType(segmentAddr, bizSystem, deviceUnUse,deviceType);
        Map<String, Object> deviceUnUseMap = listToMap(deviceUnUseCount);

        // X86服务器
        Map<String,Object> phy = Maps.newHashMap();
        phy.put("deviceType",phyServer);
        phy.put("use",null != deviceUseMap && null != deviceUseMap.get(phyServer) ? deviceUseMap.get(phyServer) : 0);
        phy.put("unUse",null != deviceUnUseMap && null != deviceUnUseMap.get(phyServer) ? deviceUnUseMap.get(phyServer) : 0);
        res.add(phy);
        Map<String,Object> vm = Maps.newHashMap();
        vm.put("deviceType",vmHost);
        vm.put("use",null != deviceUseMap && null != deviceUseMap.get(vmHost) ? deviceUseMap.get(vmHost) : 0);
        vm.put("unUse", null != deviceUnUseMap && null != deviceUnUseMap.get(vmHost) ? deviceUnUseMap.get(vmHost) : 0);
        res.add(vm);
        return res;
    }

    private Map<String,Object> listToMap(List<Map<String, Object>> list) {
        Map<String,Object> map = Maps.newHashMap();
        list.forEach(item -> {
            map.put(String.valueOf(item.get("device_type")),item.get("d_count"));
        });
        return map;
    }

    @Override
    public List<Map<String, Object>> deviceCountBySegmentUse(String deviceType) {
        if ("all".equals(deviceType)) {
            deviceType = phyServer + "," + vmHost;
        }
        return kgResourceIndexMapper.deviceCountBySegmentUse(deviceType);
    }

    @Override
    public List<Map<String, Object>> deviceCountBySystem(String deviceType, String systemType) {
        if ("all".equals(deviceType)) {
            deviceType = phyServer + "," + vmHost;
        }
        if ("leadSystem".equals(systemType)) {
            systemType = null;
        }
        return kgResourceIndexMapper.deviceCountBySystem(deviceType,systemType,deviceUse);
    }

    @Override
    public String deviceUseByClassAndType(String deviceClass,String deviceType) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> map = kgResourceIndexMapper.deviceUseByClassAndType(deviceClass, deviceType);
            int deviceCount = null != map ? Integer.parseInt(String.valueOf(map.get("device_count"))) : 0;
            int closeCount = null != map ? Integer.parseInt(String.valueOf(map.get("close_count"))) : 0;
            int alertCount = null != map ? Integer.parseInt(String.valueOf(map.get("alert_count"))) : 0;
            response.put("columns", Arrays.asList("状态","台数","排序"));
            List<List<Object>> dataList = Lists.newArrayList();
            dataList.add(Arrays.asList("正常",deviceCount,"A"));
            dataList.add(Arrays.asList("关机",closeCount,"B"));
            dataList.add(Arrays.asList("异常",alertCount,"C"));
            response.put("data",dataList);
            response.put("status",0);
            response.put("msg","");
            response.put("total", CollectionUtils.isEmpty(dataList) ? 0 : dataList.size());
        } catch (Exception e) {
            response.put("status",-1);
            response.put("msg",e.getMessage());
        }
        return JSONObject.toJSONString(response);
    }
}
