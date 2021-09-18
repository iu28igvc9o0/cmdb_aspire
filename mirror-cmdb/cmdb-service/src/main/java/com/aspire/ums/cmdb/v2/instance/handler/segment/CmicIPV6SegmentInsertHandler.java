package com.aspire.ums.cmdb.v2.instance.handler.segment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.IpUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司 FileName: CmicInnerSegmentInsertHandler Author: zhu.juwang Date: 2020/7/6 10:59 Description:
 * ${DESCRIPTION} History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Slf4j
public class CmicIPV6SegmentInsertHandler extends AbstractSegmentInsertFactory {

    @Override
    public void createIp(String userName, Map<String, Object> segmentData, String operateType) {
        String segmentStr = String.valueOf(segmentData.get("ipv6_segment_address"));
        if (StringUtils.isEmpty(segmentStr) && !segmentStr.equals("null")) {
            throw new RuntimeException("网段地址为NULL");
        }
        // 解析ip段,生成ipv6地址
        List<String> ipList = new ArrayList<>();
        try {
            ipList = IpUtils.generateIpv6(segmentStr, 254);
        } catch (Exception e) {
            throw new RuntimeException("网段地址解析错误,请检查填写网段[" + segmentStr + "],格式X:X:X:X:X:X:X:1/X");
        }
        // 查询公网IP的moduleId
        CmdbConfig cmdbConfig = cmdbConfigService.getConfigByCode("ip_repository_ipv6_ip");
        String moduleIdIpv6 = cmdbConfig.getConfigValue();

        long ss = Calendar.getInstance().getTimeInMillis();
        int ix = 1;
        List<Map<String, Object>> ipRepList = new ArrayList<>();
        List<Map<String, Object>> ipv6RepList = new ArrayList<>();
        List<Map<String, Object>> segmList = new ArrayList<>();
        List<Map<String, Object>> ipv6List = new ArrayList<>();
        List<Map<String, Object>> logList = new ArrayList<>();
        String id1 = configDictService.getIdByNoteAndCol("未存活", "survival_status");
        String id2 = configDictService.getIdByNoteAndCol("未分配", "ipAllocationStatusType");
        for (String ipString : ipList) {
            long ss1 = Calendar.getInstance().getTimeInMillis();
            String changeContent = "";
            String ipId = UUIDUtil.getUUID();

            Map<String, Object> ipRep = new HashMap<>();
            ipRep.put("id", ipId);
            ipRep.put("module_id", moduleIdIpv6);
            ipRep.put("insert_person", userName);
            ipRep.put("insert_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ipRep.put("update_person", userName);
            ipRep.put("update_time", DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ipRepList.add(ipRep);
            changeContent += "实例ID->" + ipId;
            changeContent += ",模型ID->" + moduleIdIpv6;

            Map<String, Object> ipv6Rep = new HashMap<>();
            ipv6Rep.put("id", ipId);
            ipv6RepList.add(ipv6Rep);

            Map<String, Object> segmVo = new HashMap<>();
            segmVo.put("id", ipId);
            segmVo.put("first_business_system", segmentData.get("first_business_system"));
            segmVo.put("network_segment_name", String.valueOf(segmentData.get("network_segment_name")));
            segmVo.put("idcType", String.valueOf(segmentData.get("idcType")));
            segmVo.put("alone_business_system", String.valueOf(segmentData.get("alone_business_system")));
            segmVo.put("ipv6_segment_type", segmentData.get("ipv6_segment_type"));
            segmVo.put("ipv6_segment_sub_type", String.valueOf(segmentData.get("ipv6_segment_sub_type")));
            segmVo.put("ipv6_region", String.valueOf(segmentData.get("ipv6_region")));
            segmVo.put("ipv6_ip_type", String.valueOf(segmentData.get("ipv6_ip_type")));
            segmVo.put("ipv6_segment_address", String.valueOf(segmentData.get("ipv6_segment_address")));
            segmVo.put("ipv6_network_gataway", String.valueOf(segmentData.get("ipv6_network_gataway")));
            segmList.add(segmVo);

            changeContent += ",网关地址->" + segmentData.get("ipv6_network_gataway");
            changeContent += ",网段名称->" + segmentData.get("network_segment_name");
            changeContent += ",资源池->" + segmentData.get("idcType");
            changeContent += ",网段地址->" + segmentData.get("ipv6_segment_address");
            changeContent += ",网段类型->" + segmentData.get("ipv6_ip_type");
            changeContent += ",安全域->" + segmentData.get("ipv6_region");
            changeContent += ",用途分类->" + segmentData.get("ipv6_segment_type");
            changeContent += ",用途子类->" + segmentData.get("ipv6_segment_sub_type");
            changeContent += ",分配一级业务->" + segmentData.get("first_business_system");
            changeContent += ",分配独立业务->" + segmentData.get("alone_business_system");

            Map<String, Object> ipVo = new HashMap<>();
            ipVo.put("id", ipId);
            ipVo.put("survival_status", id1);
            ipVo.put("assign_status", id2);
            ipVo.put("ipv6_segment_owner", segmentStr);// 归属网段地址
            ipVo.put("ipv6", ipString);
            ipv6List.add(ipVo);

            changeContent += ",存活状态->未存活";
            changeContent += ",分配状态->未分配";
            changeContent += ",IPV6->" + ipString;

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("id", ipId);
            logMap.put("operate_content", changeContent);
            logMap.put("network_segment_type", "IPV6");
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
        cmicInstanceService.batchInsertIPV6Repository(ipv6RepList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增内网库共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertIPV6Segment(segmList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增网段共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertIPV6Ip(ipv6List);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增IP共耗时{} ms", (end - ss));
        ss = Calendar.getInstance().getTimeInMillis();
        cmicInstanceService.batchInsertOperateLog(logList);
        end = Calendar.getInstance().getTimeInMillis();
        log.info("新增日志共耗时{} ms", (end - ss));
    }
}
