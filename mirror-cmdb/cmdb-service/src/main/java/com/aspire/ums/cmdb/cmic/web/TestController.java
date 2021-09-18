package com.aspire.ums.cmdb.cmic.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.cmic.ITestAPI;
import com.aspire.ums.cmdb.cmic.payload.TestEntity;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.util.IpUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: TestController
 * Author:   zhu.juwang
 * Date:     2020/7/6 11:24
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class TestController implements ITestAPI {
    @Override
    public void test(@RequestBody TestEntity entity) {
        // 生成IP库数据
        StringBuilder builder = new StringBuilder();
        for (Object d : entity.getEntity()) {
            Map<String, String> data = (Map) d;
            String id = UUIDUtil.getUUID();
            String temS = "INSERT INTO `cmdb_ip_repository` (`id`, `is_delete`, `update_person`, `module_id`, `update_time`, `insert_time`, `insert_person`) VALUES "
                        + "('%s', '0', 'alauda', '7e6a5f63a0204b92bd783508eed0818d', '2020-07-03 14:44:56', '2020-07-03 11:38:22', 'alauda');\n";
            String sql = String.format(temS, id);
            builder.append(sql);
            String temIn = "INSERT INTO `cmdb_ip_repository_inner` (`id`, `is_delete`) VALUES ('%s', '0');\n";
            String sqlIn = String.format(temIn, id);
            builder.append(sqlIn);
            String temSeg = "INSERT INTO `cmdb_ip_repository_inner_segment` (`id`, `is_delete`, `network_gataway`, `network_segment_address`, `vlan_number`, `safe_region`, `first_business_system`, `active_ip_count`, `assign_ip_count`, `idcType`, `alone_business_system`, `inner_segment_type`, `inner_segment_sub_type`, `inner_segment_ip_type`, `is_pool`) VALUES" +
                    " ('%s', '0', '%s', '%s', '%s', '%s', '%s', '', '', '%s', '%s', '%s', '%s', 'IPV4', '%s');\n";
            String sqlSeg = String.format(temSeg, id, data.get("网关地址[必填]"),
                    data.get("网段地址[必填]"), data.get("VLAN号[必填]"), data.get("安全域"), data.get("分配一级业务[必填]"), data.get("资源池[必填]"),
                    data.get("分配独立业务"), data.get("内网网段类型"), data.get("内网网段子类"), data.get("是否资源池管理[必填]"));
            builder.append(sqlSeg);
            // 处理网段对应的IP信息
            String segmentStr = data.get("网段地址[必填]");
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
            for (String ipString : ipList) {
                StringBuilder ipBuilder = new StringBuilder();
                String ipId = UUIDUtil.getUUID();
                String ipTemS = "INSERT INTO `cmdb_ip_repository` (`id`, `is_delete`, `update_person`, `module_id`, `update_time`, `insert_time`, `insert_person`) VALUES "
                        + "('%s', '0', 'alauda', '2893541da55d415c8f99220137cdc599', '2020-07-03 14:44:56', '2020-07-03 11:38:22', 'alauda');\n";
                String ipSql = String.format(ipTemS, ipId);
                ipBuilder.append(ipSql);
                String ipTemIn = "INSERT INTO `cmdb_ip_repository_inner` (`id`, `is_delete`) VALUES ('%s', '0');\n";
                String ipSqlIn = String.format(ipTemIn, ipId);
                ipBuilder.append(ipSqlIn);
                String ipTemSeg = "INSERT INTO `cmdb_ip_repository_inner_segment` (`id`, `is_delete`, `network_gataway`, `network_segment_address`, `vlan_number`, `safe_region`, `first_business_system`, `active_ip_count`, `assign_ip_count`, `idcType`, `alone_business_system`, `inner_segment_type`, `inner_segment_sub_type`, `inner_segment_ip_type`, `is_pool`) VALUES" +
                        " ('%s', '0', '%s', '%s', '%s', '%s', '%s', '', '', '%s', '%s', '%s', '%s', 'IPV4', '%s');\n";
                String ipSqlSeg = String.format(ipTemSeg, ipId, data.get("网关地址[必填]"),
                        data.get("网段地址[必填]"), data.get("VLAN号[必填]"), data.get("安全域"), data.get("分配一级业务[必填]"), data.get("资源池[必填]"),
                        data.get("分配独立业务"), data.get("内网网段类型"), data.get("内网网段子类"), data.get("是否资源池管理[必填]"));
                ipBuilder.append(ipSqlSeg);
                String ipSqlTem = "INSERT INTO `cmdb_ip_repository_inner_ip` (`id`, `is_delete`, `request_person`, `device_name`, `survival_status`, `request_process_id`, `is_cmdb_manager`, `ip`, `assign_user`, `latest_survival_time`, `assign_status`, `interface_name`, `online_business`, `first_survival_time`, `request_time`, `sub_business_module`, `useful_life`, `network_segment_owner`, `ip_use`, `assign_time`, `inner_ip_sub_use`, `inner_ip_use`, `request_status`) " +
                        "VALUES ('%s', '0', '', NULL, '未存活', '', NULL, '%s', '', NULL, '未分配', NULL, NULL, NULL, '', NULL, '', '%s', NULL, '', '', '', NULL);\n";
                String ipIpSql = String.format(ipSqlTem, ipId, ipString, segmentStr);
                ipBuilder.append(ipIpSql);
                builder.append(ipBuilder);
            }
        }
        // luowenbo 2020.07.17 硬编码文件分隔符
        String pathName = "E:\\t.sql".replace("\\",File.separator);
        File file = new File(pathName);
        //如果文件不存在，创建一个文件
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file);
            String content = builder.toString();
            bw = new BufferedWriter(fw);
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // luowenbo 2020.07.17 null引用漏洞
                if(null != bw) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(null != fw) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] a) {
        String aa = "select * from [<auth>] [<where>] sss";
        System.out.print(aa.replace(Constants.INNER_AUTH_STRING, ""));
    }
}
