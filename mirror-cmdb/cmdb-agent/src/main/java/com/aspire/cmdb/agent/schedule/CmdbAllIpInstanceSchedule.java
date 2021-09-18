package com.aspire.cmdb.agent.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbAllIpInstanceSchedule
 * Author:   hangfang
 * Date:     2020/7/27
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@EnableScheduling
@Slf4j
public class CmdbAllIpInstanceSchedule {

    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private JDBCHelper jdbcHelper;
    @Autowired
    private ICmdbCodeService cmdbCodeService;
    @Autowired
    private ICmdbV3ModuleCodeSettingService codeSettingService;
    @Autowired
    private ICmdbV3ModuleCatalogService catalogService;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.allIp.topic:SYNC_ALL_INSTANCE_IP_TO_ALERT}")
    private String kafkaTopic;
    @Value("${kafka.allIp.pageSize: 100}")
    private Integer pageSize;

//    @XxlJob("syncAllIpInstanceToAlertJobHandler")
    @Scheduled(cron = "0 0 23 * * ?")
    public void getAllIpInstance() {
        log.info("-----------getAllIpInstance,开始发送CI_IP数据------------");
        int total = 0;
        int currentPage = 1;
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("currentPage", currentPage);
        queryParams.put("pageSize", pageSize);
        Module module = moduleService.getDefaultModule("host");
        String querySql = this.getQuerySql(module);
        total = this.getAllIPInstanceCount();
        log.info("-----------getAllIpInstance,共获取到{}条数据准备发送kafka------------", total);
        long startTime = new Date().getTime();
        int totalPage = total % pageSize == 0 ? total / pageSize : (total / pageSize + 1);
        for (int i = 1; i <= totalPage; i ++) {
            log.info("-----------getAllIpInstance,请求CI_IP数据，当前第{}页------------", i);
            queryParams.put("currentPage", i);
            Result<Map<String, Object>> pagedResult = new Result();
            pagedResult.setData(this.getAllIPInstance(querySql, queryParams));
            pagedResult.setTotalSize(total);
            pagedResult.setColumns(moduleService.getModuleColumns(module.getId()));
            kafkaTemplate.send(kafkaTopic, JSONObject.toJSONString(pagedResult));
            log.info("-----------getAllIpInstance,请求CI_IP数据第{}页结束，已发送数据量：{},耗时{}------------", i, (i * pageSize), new Date().getTime() - startTime );
        }
        log.info("-----------getAllIpInstance,请求CI_IP数据结束,共发送数据{}条，总耗时{}------------", total, new Date().getTime() - startTime );
       // return ReturnT.SUCCESS;
    }

    private Integer getAllIPInstanceCount() {
        // 组织SQL
        String finalCountSql = "select 1 from cmdb_instance_ip_manager m ";
        finalCountSql += "inner join cmdb_instance c on m.instance_id = c.id ";
        finalCountSql += "where IFNULL(m.ip, '') != '' and c.is_delete='0'";
        Long start1 = new Date().getTime();
        CmdbSqlManage countSqlManager = new CmdbSqlManage(finalCountSql, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        int count = jdbcHelper.getInt(countSqlManager, null, null);
        log.info("获取设备总数===>耗时 {} ms", (new Date().getTime() - start1));
        return count;
    }

    private List<Map<String, Object>> getAllIPInstance(String sqlQuery, Map<String, Object> params) {
//        // 组织SQL
//        String finalSql = "select m.ip cmdb_instance_manager_ip, c.* from cmdb_instance_ip_manager m ";
//        finalSql += "inner join (" + querySql + ") c on m.instance_id = c.id ";
//        finalSql += "where IFNULL(m.ip, '') != '' " + Constants.INNER_LIMIT_STRING;
//        String whereString = "";
//        Map<String, Object> queryParams = new HashMap<>();
//        if (params.containsKey("idcType") && StringUtils.isNotEmpty(params.get("idcType"))) {
//            whereString += jdbcHelper.eqSql("idcType");
//            queryParams.put("idcType", params.get("idcType"));
//        }
//        if (params.containsKey("update_time") && StringUtils.isNotEmpty(params.get("update_time"))) {
//            whereString += jdbcHelper.gteSql("update_time");
//            queryParams.put("update_time", params.get("update_time"));
//        }
        Integer pageSize = 100, currentPage = 1;
        try {
            pageSize = Integer.parseInt(params.get("pageSize").toString());
            currentPage = Integer.parseInt(params.get("currentPage").toString());
        } catch (Exception e) {
            log.warn("Get paged error, use default size instanceof.");
        }
        String limitString = " limit " + ((currentPage - 1) * pageSize) + ", " + pageSize;
        long start1 = new Date().getTime();
        CmdbSqlManage sqlManage = new CmdbSqlManage(sqlQuery, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
        List<Map<String, Object>> dataList = jdbcHelper.getQueryList(sqlManage, null,null, limitString, null);
        log.info("获取设备数据列表===>耗时 {} ms", (new Date().getTime() - start1));
        return dataList;
    }

    private String getQuerySql(Module module) {
        CmdbV3ModuleCodeSetting codeSetting = new CmdbV3ModuleCodeSetting();
        codeSetting.setModuleId(module.getId());
        List<CmdbV3ModuleCodeSetting> codeSettingList = codeSettingService.listByEntity(codeSetting);
        String moduleTableName = module.getModuleCatalog().getCatalogCode();
        // 对code_setting进行分组
        Map<String, List<CmdbV3ModuleCodeSetting>> groupMap = new HashMap<>();
        if (codeSettingList == null || codeSettingList.size() == 0) {
            return "";
        }
        for (CmdbV3ModuleCodeSetting setting : codeSettingList) {
            // 如果是设置为不显示的字段, 则跳过更新
            if (setting.getDisplay() == 1) {
                continue;
            }
            String ownerModuleId = setting.getOwnerModuleId();
            if (StringUtils.isEmpty(ownerModuleId)) {
                ownerModuleId = setting.getModuleId();
            }
            List<CmdbV3ModuleCodeSetting> list = new LinkedList<>();
            if (groupMap.containsKey(ownerModuleId)) {
                list = groupMap.get(ownerModuleId);
            }
            list.add(setting);
            groupMap.put(ownerModuleId, list);
        }
        // 开始组装SQL
        // 组装字段部分
        StringBuilder filedBuf = new StringBuilder();
        StringBuilder refFiledBuf = new StringBuilder();
        StringBuilder tableFiledBuf = new StringBuilder();
        String refModuleTableTemplate = "ref_tb_%s";
        String tableTableTemplate = "table_%s";
        String queryFiledTemplate = "`%s`.`%s`";
        String queryRefFiledTemplate = "`" + refModuleTableTemplate + "`.`%s` `%s_name`";
        String queryTableFiledTemplate = "`" + tableTableTemplate + "`.`value` `%s_value_name`";
        // 识别引用模型配置项
        Map<String, Map<String, Object>> refCodeMap = new HashMap<>();
        Map<String, Map<String, Object>> tableCodeMap = new HashMap<>();
        for (String temModuleId : groupMap.keySet()) {
            Module temModule = moduleService.getModuleDetail(temModuleId);
            if (temModule == null) {
                continue;
            }
            List<CmdbV3ModuleCodeSetting> temSettingList = groupMap.get(temModuleId);
            for (CmdbV3ModuleCodeSetting setting : temSettingList) {
                CmdbCode queryCode = new CmdbCode();
                queryCode.setCodeId(setting.getCodeId());
                CmdbCode code = cmdbCodeService.get(queryCode);
                if (code != null) {
                    if (StringUtils.isNotEmpty(filedBuf.toString())) {
                        filedBuf.append(",");
                    }
                    filedBuf.append(String.format(queryFiledTemplate, temModule.getCode(), code.getFiledCode()));
                    // 引用模型处理
                    if (code.getCodeBindSource() != null) {
                        if ("引用模型".equals(code.getCodeBindSource().getBindSourceType())) {
                            Map<String, Object> refMap = new HashMap<>();
                            refMap.put("ref_module_id", code.getCodeBindSource().getRefModuleId());
                            refMap.put("code", code);
                            refMap.put("owner_module_id", setting.getOwnerModuleId());
                            refMap.put("refAlias", String.format(refModuleTableTemplate, code.getFiledCode()));
                            refCodeMap.put(code.getFiledCode(), refMap);
                            String refCodeId = code.getCodeBindSource().getShowModuleCodeId();
                            CmdbSimpleCode refCode = cmdbCodeService.getSimpleCodeById(refCodeId);
                            if (StringUtils.isNotEmpty(refFiledBuf.toString())) {
                                refFiledBuf.append(",");
                            }
                            refFiledBuf.append(String.format(queryRefFiledTemplate, code.getFiledCode(), refCode.getFiledCode(), code.getFiledCode() + "_" + refCode.getFiledCode()));
                        }
                        // 处理数据表
                        if ("数据表".equals(code.getCodeBindSource().getBindSourceType())) {
                            Map<String, Object> tableMap = new HashMap<>();
                            tableMap.put("code", code);
                            tableMap.put("refAlias", String.format(tableTableTemplate, code.getFiledCode()));
                            tableMap.put("table_sql", code.getCodeBindSource().getTableSql());
                            tableCodeMap.put(code.getFiledCode(), tableMap);
                            if (StringUtils.isNotEmpty(tableFiledBuf.toString())) {
                                tableFiledBuf.append(",");
                            }
                            tableFiledBuf.append(String.format(queryTableFiledTemplate, code.getFiledCode(), code.getFiledCode()));
                        }
                    }
                }
            }
        }
        // 获取顶级节点
        CmdbV3ModuleCatalog topCatalog = catalogService.getTopCatalog(module.getCatalogId());
        if (topCatalog == null) {
            log.error("模型[{}]缺少顶级模型分组信息.", module.getId());
            return "";
        }
        // 根据模型分组ID获取模型信息
        Module topModule = moduleService.getModuleDetailByCatalogId(topCatalog.getId());
        if (topModule == null) {
            log.error("模型[{}]缺少顶级模型分组信息.", module.getId());
            return "";
        }
        // 组装Join部分
        StringBuilder joinBuf = new StringBuilder();
        for (String temModuleId : groupMap.keySet()) {
            // 自身模型或定义模型信息, 过滤
            if (temModuleId.equals(module.getId()) || temModuleId.equals(topModule.getId())) {
                continue;
            }
            Module temModule = moduleService.getModuleDetail(temModuleId);
            if (temModule == null) {
                continue;
            }
            String temModuleTable = temModule.getModuleCatalog().getCatalogCode();
            joinBuf.append(" left join ").append(temModuleTable).append(" `").append(temModule.getCode()).append("` on (")
                    .append("`").append(temModule.getCode()).append("`.`id`").append(" = ")
                    .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                    .append("`").append(temModule.getCode()).append("`.`is_delete`").append(" = 0 )");
        }
        // 组装引用模型部分
        StringBuilder refBuf = new StringBuilder();
        for (String filedCode : refCodeMap.keySet()) {
            String refModuleId = refCodeMap.get(filedCode).get("ref_module_id").toString();
            CmdbCode code = (CmdbCode) refCodeMap.get(filedCode).get("code");
            Module refModule = moduleService.getModuleDetail(refModuleId);
            String refAlias = refCodeMap.get(filedCode).get("refAlias").toString();
            refBuf.append("left join ").append(refModule.getModuleCatalog().getCatalogCode())
                    .append(" ").append(refAlias).append(" ").append("on (`tt`.`")
                    .append(code.getFiledCode()).append("`=`").append(refAlias).append("`.`id`")
                    .append("and `").append(refAlias).append("`.`is_delete` = '0')");
        }
        // 组织数据表查询部分
        StringBuilder tableBuf = new StringBuilder();
        for (String filedCode : tableCodeMap.keySet()) {
            CmdbCode code = (CmdbCode) tableCodeMap.get(filedCode).get("code");
            String tableAlias = tableCodeMap.get(filedCode).get("refAlias").toString();
            String tableSql = tableCodeMap.get(filedCode).get("table_sql").toString();
            tableBuf.append("left join ").append("(").append(tableSql).append(")")
                    .append(" ").append(tableAlias).append(" ").append("on (`tt`.`")
                    .append(code.getFiledCode()).append("`=`").append(tableAlias).append("`.`id`)");
        }
        String basicSql = "select m.ip cmdb_instance_manager_ip, c.* from cmdb_instance_ip_manager m ";
        basicSql += "inner join cmdb_instance c on m.instance_id = c.id where IFNULL(m.ip, '') != '' and c.is_delete='0' ";
        basicSql += Constants.INNER_LIMIT_STRING;
        //组装完整的SQL
        StringBuilder sqlBuffer = new StringBuilder();
        sqlBuffer.append("select ")
                .append("cmdb_instance_manager_ip,")
                .append(filedBuf)
                .append(" from ").append("(").append(basicSql).append(")").append(" `").append(module.getCode()).append("` ");
        if (!module.getId().equals(topModule.getId())) {
            sqlBuffer.append(" inner join ").append(topCatalog.getCatalogCode()).append(" `").append(topModule.getCode()).append("` on (")
                    .append("`").append(topModule.getCode()).append("`.`id`").append(" = ")
                    .append("`").append(module.getCode()).append("`.`id`").append(" and ")
                    .append("`").append(topModule.getCode()).append("`.`is_delete`").append(" = 0 ").append(" and ")
                    .append("`").append(topModule.getCode()).append("`.`module_id`").append("='").append(module.getId()).append("') ");
        }
        sqlBuffer.append(joinBuf);
        sqlBuffer.append(" where ").append(module.getCode()).append(".`is_delete` = 0 ");
        StringBuilder authSql = new StringBuilder();
        authSql.append("select * from (")
                .append(sqlBuffer)
                .append(") t where 1=1 ");
        String sql = "select tt.* ";
        if (StringUtils.isNotEmpty(refFiledBuf.toString())) {
            sql +=  "," + refFiledBuf;
        }
        if (StringUtils.isNotEmpty(tableFiledBuf.toString())) {
            sql += "," + tableFiledBuf;
        }
        sql += " from (" + authSql + ") tt " + refBuf + " " + tableBuf;
        return sql;
    }
}
