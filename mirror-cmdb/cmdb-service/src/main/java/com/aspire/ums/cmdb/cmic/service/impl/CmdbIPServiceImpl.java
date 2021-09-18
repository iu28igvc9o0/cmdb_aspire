package com.aspire.ums.cmdb.cmic.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import com.aspire.ums.cmdb.cmic.service.ICmdbIPService;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司 FileName: CmdbIPServiceImpl Author: zhu.juwang Date: 2020/5/27 10:45 Description:
 * ${DESCRIPTION} History: <author> <time> <version> <desc> 作者姓名 修改时间 版本号 描述
 */
@Service
@Slf4j
public class CmdbIPServiceImpl implements ICmdbIPService {

    @Autowired
    private ICmdbConfigService configService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private JDBCHelper jdbcHelper;

    private static final String UNUSED_FIELDS = "condicationCode,currentPage,moduleType,module_id,namespace,pageSize,sort";

    private static final String INPUT_FIELDS = "ip,network_segment_address,vlan_number,network_gataway,"
            + "ipv6_segment_address,network_segment_name,ipv6";

    private static final String TIME_FIELDS = "assign_time,latest_survival_time";

    @Override
    public Map<String, Object> statisticsIpUseInfo(Map<String, Object> queryConditions) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String ipType = queryConditions.get("ip_type") == null ? "" : queryConditions.get("ip_type").toString();
        Map<String, Object> returnMap = new HashMap<>();
        if (StringUtils.isEmpty(ipType)) {
            returnMap.put("flag", "error");
            returnMap.put("msg", "Ip type [" + ipType + "] mustn't empty.");
            return returnMap;
        }
        try {
            String moduleConfigCode = "";
            if (ipType.equals("inner_ip")) {
                moduleConfigCode = "inner_ip_module_id";
            } else if (ipType.equals("public_ip")) {
                moduleConfigCode = "public_ip_module_id";
            } else if (ipType.equals("ipv6_ip")) {
                moduleConfigCode = "ipv6_ip_module_id";
            } else {
                returnMap.put("flag", "error");
                returnMap.put("msg", "Ip type [" + ipType + "] error. Don't support ip type [" + ipType + "]");
                return returnMap;
            }
            String moduleId = configService
                    .getConfigByCode(moduleConfigCode,
                            new RuntimeException("Missing cmdb_config [config_code=" + moduleConfigCode + "] record."))
                    .getConfigValue();
            String assignStatusFiled = configService.getConfigByCode("assign_status",
                    new RuntimeException("Missing cmdb_config [config_code=assign_status] record.")).getConfigValue();
            String activeStatusFiled = configService.getConfigByCode("survival_status",
                    new RuntimeException("Missing cmdb_config [config_code=survival_status] record.")).getConfigValue();
            String cmdbExistsStatusFiled = configService.getConfigByCode("is_cmdb_manager",
                    new RuntimeException("Missing cmdb_config [config_code=is_cmdb_manager] record.")).getConfigValue();
            if (StringUtils.isEmpty(moduleId)) {
                throw new RuntimeException("Cmdb_config [config_code=" + moduleConfigCode + "] config value is empty.");
            }
            String moduleQuerySQL = null;
            // IP内网地址库统计sql修改，临时方案，后期需根据模型改进
            // TODO:根据模型拼接
            if (ipType.equals("inner_ip")) {
                moduleQuerySQL = configService
                        .getConfigByCode("inner_ipuse_stat_sql",
                                new RuntimeException("Missing cmdb_config [config_code=inner_ipuse_stat_sql] record."))
                        .getConfigValue();
                moduleQuerySQL = moduleQuerySQL.replace("?", moduleId);
            } else if ("ipv6_ip".equals(ipType)) {
                moduleQuerySQL = configService
                        .getConfigByCode("ipv6_ipuse_stat_sql",
                                new RuntimeException("Missing cmdb_config [config_code=ipv6_ipuse_stat_sql] record."))
                        .getConfigValue();
                moduleQuerySQL = moduleQuerySQL.replace("?", moduleId);
            } else {
                moduleQuerySQL = moduleService.getModuleQuerySQL(moduleId);
            }
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("select ").append("sum(1) as totalCount,").append("sum(case when ip_table.").append(assignStatusFiled)
                    .append(" = (select id from t_cfg_dict where col_name='ipAllocationStatusType' and dict_note='已分配' and is_delete='0' limit 1) then 1 else 0 end) as assignCount,")
                    .append("sum(case when ip_table.").append(activeStatusFiled)
                    .append(" = (select id from t_cfg_dict where col_name='survival_status' and dict_note='已存活' and is_delete='0' limit 1) ");
            // 如果是内网或者IPV6, 怎需要增加是否cmdb录入判断
            if (ipType.equals("inner_ip") || ipType.equals("")) {
                sqlBuilder.append("or ip_table.").append(cmdbExistsStatusFiled).append(
                        " = (select id from t_cfg_dict where col_name='whether' and dict_note='是' and is_delete='0' limit 1) ");
            }
            sqlBuilder.append(" then 1 else 0 end) as useCount ");
            sqlBuilder.append("from (").append(moduleQuerySQL).append(") ip_table where 1=1 ");
            // 查询条件
            StringBuilder conditionBuilder = new StringBuilder();
            Map<String, Object> conditions = null;
            try {
                conditions = (Map) queryConditions.get("conditions");
            } catch (Exception e) {
                returnMap.put("flag", "error");
                returnMap.put("msg", "请求参数不符合要求");
                return returnMap;
            }
            if (!CollectionUtils.isEmpty(conditions)) {
                for (String key : conditions.keySet()) {
                    if (!UNUSED_FIELDS.contains(key) && StringUtils.isNotEmpty(conditions.get(key))) {
                        String realKey = key;
                        if (key.equalsIgnoreCase("network_segment_address") && ipType.equalsIgnoreCase("public_ip")) {
                            realKey = "public_segment_address";
                        }
                        if (INPUT_FIELDS.contains(key)) { // 模糊查询
                            conditionBuilder.append(" and ip_table.").append(realKey).append(" like concat('%', '")
                                    .append(conditions.get(key).toString()).append("', '%')");
                        } else if (TIME_FIELDS.contains(key)) { // 时间范围
                            String[] timeRange = conditions.get(key).toString().split(",");
                            if (timeRange.length != 2) {
                                returnMap.put("flag", "error");
                                returnMap.put("msg", key + "的值不规范");
                            }
                            conditionBuilder.append(" and ip_table.").append(realKey).append(" >= '").append(timeRange[0])
                                    .append("'");
                            ;
                            conditionBuilder.append(" and ip_table.").append(realKey).append(" <= '").append(timeRange[1])
                                    .append("'");
                            ;
                        } else {
                            conditionBuilder.append(" and ip_table.").append(realKey).append(" = '")
                                    .append(conditions.get(key).toString()).append("'");
                        }
                    }
                }
            }
            sqlBuilder.append(conditionBuilder);
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("totalCount", 0);
            dataMap.put("assignCount", 0);
            dataMap.put("assignPercent", 0);
            dataMap.put("useCount", 0);
            dataMap.put("usePercent", 0);
            log.info("sql: {}", sqlBuilder.toString());
            CmdbSqlManage cmdbSqlManage = new CmdbSqlManage(sqlBuilder.toString(), null, Constants.NEED_AUTH);
            List<Map<String, Object>> resultList = jdbcHelper.getQueryList(cmdbSqlManage, null, null, null, null);
            if (resultList != null && resultList.size() > 0) {
                if (resultList.get(0).get("totalCount") != null) {
                    dataMap.put("totalCount", resultList.get(0).get("totalCount"));
                }
                if (resultList.get(0).get("assignCount") != null) {
                    dataMap.put("assignCount", resultList.get(0).get("assignCount"));
                }
                if (resultList.get(0).get("useCount") != null) {
                    dataMap.put("useCount", resultList.get(0).get("useCount"));
                }
                if (Double.valueOf(dataMap.get("totalCount").toString()) != 0) {
                    if (resultList.get(0).get("assignCount") != null) {
                        double assignPercent = Double.valueOf(resultList.get(0).get("assignCount").toString())
                                / Double.valueOf(dataMap.get("totalCount").toString()) * 100;
                        dataMap.put("assignPercent", String.format("%.2f", assignPercent) + "%");
                    }
                    if (resultList.get(0).get("useCount") != null) {
                        double usePercent = Double.valueOf(resultList.get(0).get("useCount").toString())
                                / Double.valueOf(resultList.get(0).get("totalCount").toString()) * 100;
                        dataMap.put("usePercent", String.format("%.2f", usePercent) + "%");
                    }
                }
            }
            stopWatch.stop();
            log.info("IP地址库统计耗时：{}ms", stopWatch.getTotalTimeMillis());
            returnMap.put("flag", "sucess");
            returnMap.put("data", dataMap);
        } catch (Exception e) {
            returnMap.put("flag", "error");
            returnMap.put("msg", e.getMessage());
        }
        return returnMap;
    }
}
