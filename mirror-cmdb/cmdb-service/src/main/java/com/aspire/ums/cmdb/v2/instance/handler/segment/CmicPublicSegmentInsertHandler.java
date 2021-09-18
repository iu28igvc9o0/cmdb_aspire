package com.aspire.ums.cmdb.v2.instance.handler.segment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.IpUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司 FileName: CmicInnerSegmentInsertHandler Author: zhu.juwang Date: 2020/7/6 10:59 Description:
 * ${DESCRIPTION} History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Slf4j
public class CmicPublicSegmentInsertHandler extends AbstractSegmentInsertFactory {

    @Override
    public void createIp(String userName, Map<String, Object> segmentData, String operateType) {
        String segmentStr = String.valueOf(segmentData.get("public_segment_address"));
        if (StringUtils.isEmpty(segmentStr) && !segmentStr.equals("null")) {
            throw new RuntimeException("网段地址为null");
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

        long ss = Calendar.getInstance().getTimeInMillis();
        int ix = 1;
        List<Map<String, Object>> ipRepList = new ArrayList<>();
        List<Map<String, Object>> publicRepList = new ArrayList<>();
        List<Map<String, Object>> segmList = new ArrayList<>();
        List<Map<String, Object>> publicIpList = new ArrayList<>();
        List<Map<String, Object>> logList = new ArrayList<>();
        String id1 = configDictService.getIdByNoteAndCol("未存活", "survival_status");
        String id2 = configDictService.getIdByNoteAndCol("未分配", "ipAllocationStatusType");
        String id3 = configDictService.getIdByNoteAndCol("否", "whether");
        CmdbConfig cmdbConfig = super.cmdbConfigService.getConfigByCode("ip_repository_public_ip");
        String moduleIdPublic = cmdbConfig.getConfigValue();
        for (String ipString : ipList) {
            long ss1 = Calendar.getInstance().getTimeInMillis();
            String changeContent = "";
            String ipId = UUIDUtil.getUUID();
            Map<String, Object> ipRep = new HashMap<>();
            ipRep.put("id", ipId);
            ipRep.put("module_id", moduleIdPublic);
            ipRep.put("insert_person", userName);
            ipRep.put("insert_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ipRep.put("update_person", userName);
            ipRep.put("update_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ipRepList.add(ipRep);
            changeContent += "实例ID->" + ipId;
            changeContent += ",模型ID->2893541da55d415c8f99220137cdc599";

            Map<String, Object> publicRep = new HashMap<>();
            publicRep.put("id", ipId);
            publicRepList.add(publicRep);

            Map<String, Object> segmVo = new HashMap<>();
            segmVo.put("id", ipId);
            segmVo.put("network_gataway", segmentData.get("network_gataway"));// 网关地址
            segmVo.put("network_segment_name", String.valueOf(segmentData.get("network_segment_name")));// 网段名称
            segmVo.put("idcType", String.valueOf(segmentData.get("idcType")));// 资源池
            segmVo.put("is_pool", String.valueOf(segmentData.get("is_pool")));// 是否资源池管理
            segmVo.put("public_segment_address", segmentStr);// 网段地址
            segmVo.put("public_ip_type", String.valueOf(segmentData.get("public_ip_type")));// 网段类型
            segmVo.put("public_segment_type", String.valueOf(segmentData.get("public_segment_type")));// 线路类型
            segmVo.put("public_segment_use", String.valueOf(segmentData.get("public_segment_use")));// 用途分类
            segmVo.put("public_segment_sub_use", String.valueOf(segmentData.get("public_segment_sub_use")));// 用途子类
            segmVo.put("first_business_system", String.valueOf(segmentData.get("first_business_system")));// 分配一级业务
            segmVo.put("alone_business_system", String.valueOf(segmentData.get("alone_business_system")));// 分配独立业务
            segmList.add(segmVo);

            changeContent += ",网关地址->" + segmentData.get("network_gataway");
            changeContent += ",网段名称->" + segmentData.get("network_segment_name");
            changeContent += ",资源池->" + segmentData.get("idcType");
            changeContent += ",是否资源池管理->" + segmentData.get("is_pool");
            changeContent += ",网段地址->" + segmentData.get("public_segment_address");
            changeContent += ",网段类型->" + segmentData.get("public_ip_type");
            changeContent += ",线路类型->" + segmentData.get("public_segment_type");
            changeContent += ",用途分类->" + segmentData.get("public_segment_use");
            changeContent += ",用途子类->" + segmentData.get("public_segment_sub_use");
            changeContent += ",分配一级业务->" + segmentData.get("first_business_system");
            changeContent += ",分配独立业务->" + segmentData.get("alone_business_system");

            Map<String, Object> ipVo = new HashMap<>();
            ipVo.put("id", ipId);
            ipVo.put("survival_status", id1);
            ipVo.put("assign_status", id2);
            ipVo.put("public_segment_owner", segmentStr);// 归属网段地址
            ipVo.put("ip", ipString);
            publicIpList.add(ipVo);

            changeContent += ",存活状态->未存活";
            changeContent += ",分配状态->未分配";
            changeContent += ",是否CMDB管理->否";

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("id", ipId);
            logMap.put("operate_content", changeContent);
            logMap.put("network_segment_type", "公网IP");
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
        cmicInstanceService.batchInsertPublicRepository(publicRepList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增内网库共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertPublicSegment(segmList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增网段共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertPublicIp(publicIpList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增IP共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertOperateLog(logList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增日志共耗时{} ms", (end - ss));
    }
}
