package com.aspire.ums.cmdb.v3.module.event.publicFunc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.module.payload.SimpleModule;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.event.enums.ApprovalStatusEnum;
import com.aspire.ums.cmdb.v3.module.event.enums.EventTypeEnum;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: OperateLogHelper
 * Author:   hangfang
 * Date:     2020/5/26
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Component
public class OperateLogHelper {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbCodeService codeService;
    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ICmdbConfigService cmdbConfigService;

    public Map<String, Object> toLog(String logType, String moduleId, String instanceId, Map<String, Object> handleData) {
        log.info("-----------to start execute operate log event----------");
        log.info("----------- logType {} moduleId: {} instanceId: {} handleData: {}----------", logType, moduleId, instanceId, handleData);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            // 检验请求数据
            validRequest(moduleId, instanceId, handleData);
            // 获取审核完成列表
            List<CmdbCollectApproval> approvalList = (List<CmdbCollectApproval>)handleData.get("approvals");
            SimpleModule module = moduleService.getSimpleModuleDetail(moduleId);
            // 获取配置字段编码
            CmdbConfig cmdbConfigs = cmdbConfigService.getConfigByCode(logType);
            Map<String, String> configFiledMap= (Map<String, String>)JSONObject.parse(cmdbConfigs.getConfigValue());
            CmdbConfig queryConfig = new CmdbConfig();
            // 获取操作日志模型id
            queryConfig.setConfigCode("log_module_id");
            CmdbConfig logModuleConfig  = cmdbConfigService.get(queryConfig);
            //获取操作日志模型下所有字段
            Map<String, Object> instanceData = instanceService.getInstanceDetail(moduleId, instanceId);
            Map<String, Object> approvalData = new HashMap<>();
            for (CmdbCollectApproval approval: approvalList) {
                approvalData.put("module_id", logModuleConfig.getConfigValue());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                approvalData.put("operate_time", dateFormat.format(new Date()));
                approvalData.put("operate_obj_type", module.getName());
                approvalData.put("operate_result", ApprovalStatusEnum.getLabel(approval.getApprovalStatus()));
                approvalData.put("update_person", approval.getApprovalUser());
                approvalData.put("operate_account", approval.getApprovalUser());
                approvalData.put("operate_from", "自动生成操作日志");
                approvalData.put("operate_type", EventTypeEnum.getLabel(handleData.get("operateType").toString()));
                for (String key : configFiledMap.keySet()) {
                    approvalData.put(configFiledMap.get(key), instanceData.get(key));
                }
                if (StringUtils.isEmpty(approval.getInstanceId())) {
                    approvalData.put("operate_content", approval.getCurrValue());
                } else if ("手动删除".equals(approval.getOperaterType())) {
                    approvalData.put("operate_content", approval.getOldValue() + "-> 无数据");
                } else {
                    //如果更新审核
                    if (StringUtils.isEmpty(approval.getFiledName())) {
                        approval.setFiledName(codeService.getSimpleCodeById(approval.getCodeId()).getFiledName());
                    }
                    approvalData.put("operate_content", approval.getFiledName() + ":" + approval.getOldValue() + "->" + approval.getCurrValue());
                }
                instanceService.addInstance(approval.getApprovalUser(), approvalData, "自动生成操作日志");
            }
            resultMap.put("msg", "添加操作日志成功！");
            resultMap.put("flag", true);
        } catch (Exception e) {
            log.error("添加操作日志失败！error: {}", e.getMessage());
            resultMap.put("msg", "添加操作日志失败！error: " + e.getMessage());
            resultMap.put("flag", true);
        }
//

//        List<Map<String, Object>> returnList = approvalService.approve(null, CollectConst.APPROVE_STATUS_PASS, "自动通过", logApprovalList);
//        if (returnList != null && returnList.size() > 0) {
//            resultMap.put("errorInfo", returnList);
//
//        } else {
//            resultMap.put("msg", "添加操作日志成功！");
//            resultMap.put("flag", true);
//        }
        return resultMap;
    }
    private void validRequest(String moduleId, String instanceId, Map<String, Object> handleData) {
        if (StringUtils.isEmpty(moduleId)) {
            throw new RuntimeException("moduleId 不能为空");
        }
        if (StringUtils.isEmpty(instanceId)) {
            throw new RuntimeException("instanceId 不能为空");
        }
        if (!handleData.containsKey("approvals")) {
            throw new RuntimeException("处理数据未提供审核信息，无法记录操作结果");
        }
        if (!handleData.containsKey("operateType")) {
            throw new RuntimeException("处理数据未提供操作类型信息，无法记录操作结果");
        }

    }
}
