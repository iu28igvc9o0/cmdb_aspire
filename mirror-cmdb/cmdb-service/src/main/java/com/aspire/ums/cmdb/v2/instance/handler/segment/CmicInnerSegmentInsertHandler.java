package com.aspire.ums.cmdb.v2.instance.handler.segment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.springframework.util.StopWatch;

import com.alibaba.fastjson.JSONArray;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.IpUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司 FileName: CmicInnerSegmentInsertHandler Author: zhu.juwang Date: 2020/7/6 10:59 Description:
 * ${DESCRIPTION} History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Slf4j
public class CmicInnerSegmentInsertHandler extends AbstractSegmentInsertFactory {

    @Override
    public void createIp(String userName, Map<String, Object> segmentData, String operateType) {
        String segmentStr = segmentData.get("network_segment_address").toString();
        List<String> filterIps = Lists.newArrayList();
        filterIps.add(IpUtils.getBcast(segmentStr));
        filterIps.add(IpUtils.getNetwork(segmentStr));
        List<String> ipList;
        try {
            ipList = IpUtils.generateIp(segmentStr);
            ipList = ipList.stream().filter(e -> !filterIps.contains(e)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(" 解析ip段:[" + segmentStr + "]生成ipv4地址失败,请检查网段格式xx.xx.xx.xx/xx" + e.getMessage());
        }
        long ss = Calendar.getInstance().getTimeInMillis();
        int ix = 1;
        List<Map<String, Object>> ipRepList = new ArrayList<>();
        List<Map<String, Object>> innerRepList = new ArrayList<>();
        List<Map<String, Object>> segmList = new ArrayList<>();
        List<Map<String, Object>> innerIpList = new ArrayList<>();
        List<Map<String, Object>> logList = new ArrayList<>();
        String id1 = configDictService.getIdByNoteAndCol("未存活", "survival_status");
        String id2 = configDictService.getIdByNoteAndCol("未分配", "ipAllocationStatusType");
        String id3 = configDictService.getIdByNoteAndCol("否", "whether");
        for (String ipString : ipList) {
            long ss1 = Calendar.getInstance().getTimeInMillis();
            String changeContent = "";
            String ipId = UUIDUtil.getUUID();
            Map<String, Object> ipRep = new HashMap<>();
            ipRep.put("id", ipId);
            ipRep.put("module_id", "2893541da55d415c8f99220137cdc599");
            ipRep.put("insert_person", userName);
            ipRep.put("insert_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ipRep.put("update_person", userName);
            ipRep.put("update_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ipRepList.add(ipRep);

            changeContent += "实例ID->" + ipId;
            changeContent += ",模型ID->2893541da55d415c8f99220137cdc599";

            Map<String, Object> innerRep = new HashMap<>();
            innerRep.put("id", ipId);
            innerRepList.add(innerRep);

            Map<String, Object> segmVo = new HashMap<>();
            if (MapUtils.isNotEmpty(segmentData)) {
                segmVo.putAll(segmentData);
            }
            segmVo.put("id", ipId);
            // segmVo.put("network_gataway", segmentData.get("network_gataway"));
            // segmVo.put("network_segment_address", segmentData.get("network_segment_address"));
            // segmVo.put("vlan_number", segmentData.get("vlan_number"));
            // segmVo.put("safe_region", segmentData.get("safe_region"));
            // segmVo.put("first_business_system", segmentData.get("first_business_system"));
            // segmVo.put("idcType", segmentData.get("idcType"));
            // segmVo.put("alone_business_system", segmentData.get("alone_business_system"));
            // segmVo.put("inner_segment_type", segmentData.get("inner_segment_type"));
            // segmVo.put("inner_segment_ip_type", segmentData.get("inner_segment_ip_type"));
            // segmVo.put("is_pool", segmentData.get("is_pool"));
            segmList.add(segmVo);

            changeContent += ",网关地址->" + segmentData.get("network_gataway");
            changeContent += ",网段地址->" + segmentData.get("network_segment_address");
            changeContent += ",VLAN号->" + segmentData.get("vlan_number");
            changeContent += ",安全域->" + segmentData.get("safe_region");
            changeContent += ",分配一级业务->" + segmentData.get("first_business_system");
            changeContent += ",资源池->" + segmentData.get("idcType");
            changeContent += ",分配独立业务->" + segmentData.get("alone_business_system");
            changeContent += ",内网网段类型->IPV4";

            Map<String, Object> ipVo = new HashMap<>();
            ipVo.put("id", ipId);
            ipVo.put("survival_status", id1);
            ipVo.put("assign_status", id2);
            ipVo.put("is_cmdb_manager", id3);
            ipVo.put("public_segment_owner", segmentStr);
            ipVo.put("ip", ipString);
            innerIpList.add(ipVo);

            changeContent += ",存活状态->未存活";
            changeContent += ",分配状态->未分配";
            changeContent += ",是否CMDB管理->否";
            changeContent += ",归属网段地址->" + segmentStr;

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("id", ipId);
            logMap.put("operate_content", changeContent);
            logMap.put("network_segment_type", "内网IP");
            logMap.put("operate_type", operateType);
            logMap.put("operate_result", "审批通过");
            logMap.put("operate_obj", ipString);
            logMap.put("operate_from", "自动生成操作日志");
            logMap.put("operate_obj_type", "IP管理");
            logMap.put("idcType", segmentData.get("idcType"));
            logMap.put("operate_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            logMap.put("network_segment_sub_type", segmentData.get("network_segment_sub_type"));
            logMap.put("operate_account", userName);
            logList.add(logMap);
            long end = Calendar.getInstance().getTimeInMillis();
            log.info("当前处理第{}条, 耗时{} ms", ix, (end - ss1));
            ix++;
        }
        long end = Calendar.getInstance().getTimeInMillis();
        log.info("组装共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertIpRepository(ipRepList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增内网共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertInnerRepository(innerRepList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增内网库共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertInnerSegment(segmList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增网段共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertInnerIp(innerIpList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增IP共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertOperateLog(logList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增日志共耗时{} ms", (end - ss));
        // 更新存活IP空闲IP数量
        taskExecutor.execute(() -> updateCountSegmentIp(segmentStr));
        taskExecutor.execute(() -> updateAssignStatus(segmentStr));
    }

    private void updateAssignStatus(String networkSegmentAddress) {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        Map<String, String> params = Maps.newHashMap();
        params.put("networkSegmentAddress", networkSegmentAddress);
        cmicInstanceService.updateAssignStatus4InnerIp(params);
        stopwatch.stop();
        log.info("更新网段:[{}],200号段之后分配状态耗时:[{}]ms.", networkSegmentAddress, stopwatch.getTotalTimeMillis());
    }

    private void updateCountSegmentIp(String segmentValue) {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("need_count_ip_module");
        List<Map> needCounts = JSONArray.parseArray(cmdbConfig.getConfigValue(), Map.class);
        for (Map needCount : needCounts) {
            try {
                count(needCount.get("ip").toString(), needCount.get("ipSegment").toString(), needCount.get("segment").toString(),
                        needCount.get("segmentAddress").toString(), segmentValue);
                log.info("统计模型[" + needCount.get("segment").toString() + "]各状态ip数据结束");
            } catch (Exception e) {
                throw new RuntimeException("统计模型[" + needCount.get("segment").toString() + "]各状态ip数据失败. error", e);
            }
        }
        stopwatch.stop();
        log.info("更新网段:[{}],IP空闲数/IP存活数耗时:[{}]ms.", segmentValue, stopwatch.getTotalTimeMillis());
    }

    private void count(String ip, String ipSegment, String segment, String segmentAddress, String segmentValue) {
        CmdbConfig ipConfig = cmdbConfigService.getConfigByCode(ip);
        CmdbV3ModuleCatalog ipCatalog = catalogService.getByModuleId(ipConfig.getConfigValue());
        CmdbConfig segmentConfig = cmdbConfigService.getConfigByCode(segment);
        CmdbV3ModuleCatalog segmentCatalog = catalogService.getByModuleId(segmentConfig.getConfigValue());
        // 获取模型下所有网段信息
        instanceService.countSegmentIp4Segment(segmentCatalog.getCatalogCode(), segmentAddress, ipCatalog.getCatalogCode(),
                ipSegment, segmentValue);
    }
}
