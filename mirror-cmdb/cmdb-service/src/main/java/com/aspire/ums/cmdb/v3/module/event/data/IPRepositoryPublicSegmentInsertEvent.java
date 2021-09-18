package com.aspire.ums.cmdb.v3.module.event.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.util.IpUtils;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司 FileName: IPRepositoryIPV4SegmentInsertEvent Author: zhu.juwang Date: 2020/5/12 15:31
 * Description: IPV4网段录入后, 新增IP管理模型数据. History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Slf4j
public class IPRepositoryPublicSegmentInsertEvent extends AbstractModuleEvent {

    private final String CONFIG_CODE = "ip_repository_public_ip";

    @Autowired
    private ICmdbInstanceService iCmdbInstanceService;

    @Autowired
    private ICmdbConfigService iCmdbConfigService;

    private ConfigDictMapper configDictMapper;

    @Override
    public void initSpringBeans() {
        // 初始化 service类
        if (this.iCmdbInstanceService == null) {
            this.iCmdbInstanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
        if (this.iCmdbConfigService == null) {
            this.iCmdbConfigService = SpringUtils.getBean(ICmdbConfigService.class);
        }
        if (this.configDictMapper == null) {
            this.configDictMapper = SpringUtils.getBean(ConfigDictMapper.class);
        }
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String id1 = configDictMapper.getIdByNoteAndCol("未存活", "survival_status");
        String id2 = configDictMapper.getIdByNoteAndCol("已存活", "survival_status");
        String id3 = configDictMapper.getIdByNoteAndCol("未分配", "ipAllocationStatusType");
        String id4 = configDictMapper.getIdByNoteAndCol("已分配", "ipAllocationStatusType");
        String username = handleData.get("username").toString();
        // 处理数据 插入到内网ip地址的模型中. 且无需入库审核.
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> dataMap = iCmdbInstanceService.getInstanceDetail(moduleId, instanceId);
        // 获取ip段
        String segmentStr = String.valueOf(dataMap.get("public_segment_address"));
        if (StringUtils.isEmpty(segmentStr) && !segmentStr.equals("null")) {
            returnMap.put("flag", false);
            returnMap.put("msg", "网段地址为null");
            return returnMap;
        }
        // 解析ip段,生成ipv4地址
        List<String> ipList = new ArrayList<>();
        try {
            ipList = IpUtils.generateIp(segmentStr);
        } catch (Exception e) {
            throw new RuntimeException("网段地址解析错误,请检查填写网段[" + segmentStr + "],格式xx.xx.xx.xx/xx");
        }
        // 过滤网络地址和广播地址
        List<String> filterIps = Lists.newArrayList();
        filterIps.add(IpUtils.getBcast(segmentStr));
        filterIps.add(IpUtils.getNetwork(segmentStr));
        ipList = ipList.stream().filter(e -> !filterIps.contains(e)).collect(Collectors.toList());

        // 查询ipv4的moduleId
        CmdbConfig cmdbConfig = iCmdbConfigService.getConfigByCode(CONFIG_CODE);
        String moduleIdPublic = cmdbConfig.getConfigValue();
        if (StringUtils.isNotEmpty(moduleIdPublic)) {
            CountDownLatch downLatch = new CountDownLatch(ipList.size());
            ipList.stream().forEach(ip -> {
                Map<String, Object> insertData = Maps.newHashMap();
                String networkGataway = String.valueOf(dataMap.get("network_gataway"));
                insertData.put("module_id", moduleIdPublic);
                insertData.put("ip", ip);
                insertData.put("network_gataway", networkGataway);// 网关地址
                insertData.put("network_segment_name", String.valueOf(dataMap.get("network_segment_name")));// 网段名称
                insertData.put("idcType", String.valueOf(dataMap.get("idcType")));// 资源池
                insertData.put("is_pool", String.valueOf(dataMap.get("is_pool")));// 是否资源池管理
                insertData.put("public_segment_address", segmentStr);// 网段地址
                insertData.put("public_segment_owner", segmentStr);// 归属网段地址
                insertData.put("public_ip_type", String.valueOf(dataMap.get("public_ip_type")));// 网段类型
                insertData.put("public_segment_type", String.valueOf(dataMap.get("public_segment_type")));// 线路类型
                insertData.put("public_segment_use", String.valueOf(dataMap.get("public_segment_use")));// 用途分类
                insertData.put("public_segment_sub_use", String.valueOf(dataMap.get("public_segment_sub_use")));// 用途子类
                insertData.put("first_business_system", String.valueOf(dataMap.get("first_business_system")));// 分配一级业务
                insertData.put("alone_business_system", String.valueOf(dataMap.get("alone_business_system")));// 分配独立业务
                insertData.put("survival_status", id1);// 存活状态

                // 分配状态
                String[] gatewayArr = networkGataway.split(",");
                if (Arrays.stream(gatewayArr).anyMatch(e -> e.equals(ip))) {
                    insertData.put("assign_status", id4);
                } else {
                    insertData.put("assign_status", id3);
                }

                EventThreadUtils.FIXED_POOL.execute(() -> {
                    iCmdbInstanceService.addInstance(username, insertData, "自动生成公网IP");
                    downLatch.countDown();
                });
            });
            try {
                downLatch.await();
            } catch (InterruptedException e) {
                log.info("{}", e);
            }
        }
        stopWatch.stop();
        log.info("<<<<<< 自动生成公共IP 完成,耗时:[{}]ms <<<<<<", stopWatch.getTotalTimeMillis());
        returnMap.put("flag", true);
        returnMap.put("msg", "保存成功");
        return returnMap;
    }
}
