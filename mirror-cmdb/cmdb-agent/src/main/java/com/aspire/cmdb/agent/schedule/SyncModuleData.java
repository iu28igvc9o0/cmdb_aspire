package com.aspire.cmdb.agent.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aspire.cmdb.agent.util.StringUtils;
import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.BeansUtil;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.java2d.pipe.AAShapePipe;

import java.io.IOException;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SyncModuleData
 * Author:   hangfang
 * Date:     2020/12/10
 * Description: 一个模型同步另一个模型N字段
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@EnableScheduling
@Component
public class SyncModuleData {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbConfigService configService;
    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private ICmdbV3ModuleCodeSettingService codeSettingService;
    @Autowired
    private JDBCHelper jdbcHelper;

//    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void startToSync1() {
        //获取模型之间字段配置[{"toModule":{"id":"","relationKey":"owner_biz_system"},"syncFields":["department1","department2"],"fromModule": {"id":"","relationKey":"id"}}]
         CmdbConfig sourceConfig = configService.getConfigByCode("sync_from_other_module");
         if (sourceConfig == null) {
             log.error("未配置");
             return;
         }
        List<Map> configList = JSONArray.parseArray(sourceConfig.getConfigValue(), Map.class);
         for (Map config : configList) {
             // 从哪个模型中获取数据
             Map<String, Object> fromModuleInfo = JSONObject.parseObject(JSON.toJSONString(config.get("fromModule")), Map.class);
             String fromModuleId = fromModuleInfo.get("id").toString();
             String fromModuleRelationKey = fromModuleInfo.get("relationKey").toString();
             // 需要同步的字段
             List<Map> syncFields = JSONArray.parseArray(JSON.toJSONString(config.get("syncFields")), Map.class);
             // 目标模型信息
             Map<String, Object> toModuleInfo = JSONObject.parseObject(JSON.toJSONString(config.get("toModule")), Map.class);
             String toModuleId = toModuleInfo.get("id").toString();
             String toModuleRelationKey = toModuleInfo.get("relationKey").toString();
            if (syncFields == null || syncFields.size() == 0) {
                continue;
            }
             // 从目标模型查所有源模型主键
            String toBaseSql = moduleService.getModuleQuerySQL(toModuleId);
            String toSql = "select id, " + toModuleRelationKey + " from ( " + toBaseSql + ") target";
             CmdbSqlManage sqlManage = new CmdbSqlManage(toSql, null , Constants.UN_NEED_AUTH);
            List<Map<String, Object>> toKeyList = jdbcHelper.getQueryList(sqlManage, null, null, null, null);
            // 第个业务系统对应多少业务配额
            Map<String, List<String>> toKeyMap = new HashMap<>();
            if (null == toKeyList || toKeyList.size() == 0) {
                return;
            }
            // 例：一条业务系统下有多条业务配额，根据业务系统id进行整理
             for (Map<String,Object> keyMap: toKeyList) {
                 String targetKey = keyMap.get(toModuleRelationKey).toString();
                 if (!toKeyMap.containsKey(targetKey)) {
                     toKeyMap.put(targetKey, new ArrayList<>());
                 }
                 toKeyMap.get(targetKey).add(keyMap.get("id").toString());
             }
             // 源数据查询
             String fromBaseSql = moduleService.getModuleQuerySQL(fromModuleId);
             StringBuilder whereBuiler = new StringBuilder( "and " + fromModuleRelationKey + " in (");
             for (String tKey : toKeyMap.keySet()) {
                 whereBuiler.append("\"").append(tKey).append("\",");
             }
             String whereSql = whereBuiler.substring(0, whereBuiler.length() -1) + ")";
             sqlManage = new CmdbSqlManage(fromBaseSql, null , Constants.UN_NEED_AUTH);
             List<Map<String, Object>> sourceDataList = jdbcHelper.getQueryList(sqlManage, whereSql, null, null, null);

             for(Map<String, Object> sData : sourceDataList) {
                 List<CmdbCollectApproval> approvalList = new ArrayList<>();
                 //业务系统key
                String fKey = sData.get("id").toString();
                // 需要更新的实例列表
                List<String> tKeyList = toKeyMap.get(fKey);
                for (String tKey : tKeyList) {
                    Map<String, Object> params = new HashMap<>();
                    Map<String, Object> oldInstanceData = instanceService.getInstanceDetail(toModuleId, tKey);
                    params.put("module_id", toModuleId);
                    for (Map<String, String> fieldMap : syncFields) {
                        String toKey = fieldMap.get("toKey");
                        String curValue = StringUtils.isNotEmpty(fieldMap.get("fromKey")) ? sData.get(fieldMap.get("fromKey")).toString() : "";
                        String oldValue = StringUtils.isNotEmpty(oldInstanceData.get(toKey)) ? oldInstanceData.get(toKey).toString() : "";
                        if (curValue.equals(oldValue)) {
                            continue;
                        }
                        CmdbSimpleCode code = codeService.getSimpleCodeByCodeAndModuleId(toModuleId, toKey);
                        params.put(fieldMap.get("toKey"), sData.get(fieldMap.get("fromKey")));
                        CmdbCollectApproval approval = new CmdbCollectApproval();
                        approval.setModuleId(toModuleId);
                        approval.setOldValue(oldValue);
                        approval.setCurrValue(curValue);
                        approval.setCodeId(code.getCodeId());
                        approval.setResourceData(JSON.toJSONString(params));
                        approval.setOwnerModuleId(code.getOwnerModuleId());
                        approval.setApprovalType("update");
                        approval.setOperaterType("业务配额同步");
                        approval.setOperator("系统管理员");
                        approval.setOperatorTime(new Date());
                        approvalList.add(approval);
                    }
                    instanceService.update("系统管理员", approvalList);
                }
            }
         }


    }

    /**
     * 第三方
     * {
     *  "code":"",
     *  "update":[
     *     {
     *      "id":"",//源数据id
     *      "biz":"value1",
     *      "concat":"value2"
     *  }]
     * }
     *
     */
    public void startToSync(Map<String, Object> params) {
        if (!StringUtils.isNotEmpty(params.get("code"))) {
            throw new RuntimeException("未提供同步任务标识[code]");
        }
        String code  = params.get("code").toString();
        // 获取同步信息模型id
        CmdbConfig moduleInfo = configService.getConfigByCode("sync_data_module_id");
        if (moduleInfo == null) {
            throw new RuntimeException("未配置同步数据模型id");
        }
        String moduleId = moduleInfo.getConfigValue();
        // 获取同步信息列表
        String baseSql = moduleService.getModuleQuerySQL(moduleId);
        String querySql = "select * from (" + baseSql + ") res where sync_data_type = '" +  code + "' ";
        CmdbSqlManage sqlManage = new CmdbSqlManage(querySql, null, Constants.UN_NEED_AUTH);
        //配置信息列表
        List<Map<String, Object>> syncInfoList = jdbcHelper.getQueryList(sqlManage, null, null, null, null);
        for (Map<String, Object> info : syncInfoList) {
            // 获取同步源数据
            SyncModuleDataInfo syncInfo = new SyncModuleDataInfo();
//            BeanUtils.copyProperties(info, syncInfo);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                syncInfo = objectMapper.readValue(JSON.toJSONString(info), SyncModuleDataInfo.class);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("转换同步信息失败");
            }
            List<Map<String, Object>> sourceDataList = new ArrayList<>();
            if (params.containsKey("update")) {
                sourceDataList = JSON.parseObject(JSON.toJSONString(params.get("update")), new TypeReference<List<Map<String, Object>>>() {});
            } else {
                sourceDataList = toGetSourceList(syncInfo);
            }

                 // 根据源数据去更新目标数据(一个配置单个字段去更新)
            for (Map<String, Object> sourceData : sourceDataList) {
                // 如果源数据值为空则不进行更新
                if (!StringUtils.isNotEmpty(sourceData.get(syncInfo.getSourceFiled().trim()))) {
                    continue;
                }
                // 目标模型id
                String tModuleId = syncInfo.getTargetModule();
                // 目标模型关联字段
                String tRelationField = syncInfo.getTargetRelationFiled();
                // 目标模型字段
                String tFiled = syncInfo.getTargetFiled();
                // 是否审核
                String enableApprove = syncInfo.getEnableApprove();
                // 当前源字段值
                String currValue = sourceData.get(syncInfo.getSourceFiled().trim()).toString();
                Map<String, Object> instanceData = new HashMap<>();
                instanceData.put("module_id", tModuleId);
                instanceData.put(tFiled, sourceData.get(syncInfo.getSourceFiled().trim()));
                String tBaseSql = moduleService.getModuleQuerySQL(tModuleId);
                String tQuerySql = "select id, " + tFiled + " from ( " + tBaseSql + ") res where " + tRelationField + "='" + sourceData.get(syncInfo.getRelationFiled()) + "' ";
                CmdbSqlManage tSqlM = new CmdbSqlManage(tQuerySql, null,tModuleId, Constants.UN_NEED_AUTH);
                List<Map<String, Object>>  tList = jdbcHelper.getQueryList(tSqlM, null,null,null, null);
                for (Map<String, Object> t : tList) {
                    String tId = t.get("id").toString();
                    String oldValue = StringUtils.isNotEmpty(t.get(tFiled)) ? t.get(tFiled).toString() : "";
                    EventThreadUtils.NORMAL_POOL.execute(() -> {
                        // 如果需要审核
                        if(enableApprove.equals("11462")) {
                            instanceService.updateInstance(tId, "系统管理员" , instanceData, "同步模型数据");
                        } else {
                            // 不需要审核
                            List<CmdbCollectApproval> approvalList = new ArrayList<>();
                            CmdbSimpleCode tCode = codeService.getSimpleCodeByCodeAndModuleId(tModuleId, tFiled);
                            // 根据模型id和codeid获取归属模型
                            CmdbV3ModuleCodeSetting codeSetting = new CmdbV3ModuleCodeSetting();
                            codeSetting.setCodeId(tCode.getCodeId());
                            codeSetting.setModuleId(tModuleId);
                            codeSetting = codeSettingService.get(codeSetting);
                            CmdbCollectApproval approval = new CmdbCollectApproval();
                            approval.setApprovalStatus(1);
                            approval.setResourceData(JSON.toJSONString(instanceData));
                            approval.setApprovalType("");
                            approval.setApprovalStatus(1);
                            approval.setCurrValue(currValue);
                            approval.setOldValue(oldValue);
                            approval.setApprovalUser("系统管理员");
                            approval.setModuleId(tModuleId);
                            approval.setOperaterType("同步模型数据");
                            approval.setApprovalTime(new Date());
                            approval.setCodeId(tCode.getCodeId());
                            approval.setFiledName(tCode.getFiledName());
                            approval.setInstanceId(tId);
                            approval.setOwnerModuleId(codeSetting.getOwnerModuleId());
                            approvalList.add(approval);
                            instanceService.update("系统管理员",approvalList);
                        }
                    });
                }

            }
        }


    }
    /**
     * 定时同步业务系统级别
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void syncBizLevel() {
        Map<String, Object> syncInfo = new HashMap<>();
        syncInfo.put("code","sync_business_level");
        this.startToSync(syncInfo);
    }

    /**
     * 定时同步业务系统配额
     */
    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void syncBizQuota() {
        Map<String, Object> syncInfo = new HashMap<>();
        syncInfo.put("code","sync_biz_quota");
        this.startToSync(syncInfo);
    }
    private List<Map<String, Object>>  toGetSourceList(SyncModuleDataInfo syncInfo) {
        String sourceModule = syncInfo.getSourceModule();
        String sourceFiled = syncInfo.getSourceFiled();
        String sourceRFiled = syncInfo.getRelationFiled();
        String sBaseSql = moduleService.getModuleQuerySQL(sourceModule);
        String sQueryFileds = sourceFiled + "," + sourceRFiled;
        String sQuerySql = "select " + sQueryFileds  + " from (" + sBaseSql + ") res ";
        CmdbSqlManage sSqlM = new CmdbSqlManage(sQuerySql, null,sourceModule, Constants.UN_NEED_AUTH);
        return jdbcHelper.getQueryList(sSqlM, null, null, null,null);
    }
}
