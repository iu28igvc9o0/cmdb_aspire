package com.aspire.ums.cmdb.collect.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.CollectConst;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogEntity;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogListResp;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogShieldEntity;
import com.aspire.ums.cmdb.collect.mapper.AutoDiscoveryLogMapper;
import com.aspire.ums.cmdb.collect.mapper.AutoDiscoveryLogShieldMapper;
import com.aspire.ums.cmdb.collect.service.AutoDiscoveryLogService;
import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.Instance;
import com.aspire.ums.cmdb.maintain.entity.InstanceModel;
import com.aspire.ums.cmdb.maintain.mapper.FormValueMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceMapper;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AutoDiscoveryLogServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
@Transactional
public class AutoDiscoveryLogServiceImpl implements AutoDiscoveryLogService {

    @Autowired
    private AutoDiscoveryLogMapper logMapper;

    @Autowired
    private AutoDiscoveryLogShieldMapper logShieldMapper;

    @Autowired
    private InstanceMapper instanceMapper;

    @Autowired
    private FormValueMapper formValueMapper;


    @Override
    public void insertVO(AutoDiscoveryLogEntity logEntity) {
        logMapper.insertVO(logEntity);
    }

    @Override
    public List<AutoDiscoveryLogEntity> getListByRuleId(String ruleId) {
        return logMapper.getListByRuleId(ruleId);
    }

    @Override
    public List<String> getInstanceNameByRuleId(String ruleId) {
        return logMapper.getInstanceNameByRuleId(ruleId);
    }

    @Override
    public List<AutoDiscoveryLogListResp> getListByRuleIds(List<String> ruleIds, JSONObject queryData) {
        return logMapper.getListByRuleIds(ruleIds, queryData);
    }

    @Override
    public void updateVO(AutoDiscoveryLogEntity logEntity) {
        logMapper.updateVO(logEntity);
        if ("待处理".equals(logEntity.getStatus())) {
          List<String> instanceNames = logShieldMapper.getListByRuleId(logEntity.getRuleId());
          if (instanceNames.contains(logEntity.getInstanceName())) {
              logShieldMapper.delete(logEntity.getRuleId(), logEntity.getInstanceName());
          }
        }
    }

    @Override
    public void updateVOByBatch(Map<String, Object> updateMap) {
        logMapper.updateByBatch(updateMap);
    }


    @Override
    public void shieldIp(List<AutoDiscoveryLogEntity> discoveryLogs) {
        Map<String, Object> shieldMap = new HashMap<>();
        List<AutoDiscoveryLogShieldEntity> discoveryLogShields = new ArrayList<>();
        for (AutoDiscoveryLogEntity logEntity : discoveryLogs) {
            AutoDiscoveryLogShieldEntity logShieldEntity = new AutoDiscoveryLogShieldEntity();
            logShieldEntity.setId(UUIDUtil.getUUID());
            logShieldEntity.setInstanceName(logEntity.getInstanceName());
            logShieldEntity.setRuleId(logEntity.getRuleId());
            discoveryLogShields.add(logShieldEntity);
        }
        shieldMap.put("updateTime", new Date());
        shieldMap.put("status", CollectConst.SHIELD);
        shieldMap.put("logIds", discoveryLogs);
        // 批量更新发现数据状态为屏弊
        logMapper.updateByBatch(shieldMap);
        // 记录屏蔽的发现数据ip
        logShieldMapper.insertShields(discoveryLogShields);
    }

    @Override
    public AutoDiscoveryLogListResp getDetailById(String id) {
        return logMapper.getDetailById(id);
    }

    @Override
    public void bind(AutoDiscoveryLogEntity discoveryLog, String instanceId) {
        Instance instance = new Instance();
        FormValue formValue = new FormValue();
        // 更新实例名称
        instance.setName(discoveryLog.getInstanceName());
        instance.setId(instanceId);
        instanceMapper.update(instance);
        // 更新form_vlaue表code=ip数据
        formValue.setFormCode("ip");
        formValue.setFormValue(discoveryLog.getInstanceName());
        formValue.setInstanceId(instanceId);
        formValueMapper.update2(formValue);
        formValue.setFormCode("Y_name");
        formValue.setFormValue(discoveryLog.getInstanceName());
        formValue.setInstanceId(instanceId);
        formValueMapper.update2(formValue);
        // 更新发现数据状态为绑定（bind）
        discoveryLog.setUpdateTime(new Date());
        discoveryLog.setStatus(CollectConst.BIND);
        discoveryLog.setId(discoveryLog.getId());
        logMapper.updateVO(discoveryLog);
    }

    @Override
    public void maintain(String id, InstanceModel instanceModel) {
        // 添加实例
        Instance instance = new Instance();
        instance.setName(instanceModel.getName());
        instance.setId(UUIDUtil.getUUID());
        instance.setModuleId(instanceModel.getModuleId());
        instanceMapper.addInstance(instance);
        List<FormValue> formValues = instanceModel.getFormValues();
        if(formValues != null && formValues.size()>0){
            for(FormValue f: formValues){
                f.setId(UUIDUtil.getUUID());
                f.setInstanceId(instance.getId());
                if("Y_name".equals(f.getForm().getCode())){
                    f.setFormValue(instance.getName());
                }
            }
            formValueMapper.insert(formValues);
        }
        // 更新状态为已维护
        AutoDiscoveryLogEntity logEntity = new AutoDiscoveryLogEntity();
        logEntity.setId(id);
        logEntity.setStatus(CollectConst.MAINTAINED);
        logEntity.setUpdateTime(new Date());
        logMapper.updateVO(logEntity);
    }

    @Override
    public List<Instance> listInstanceByModulId(String moduleId) {
        return instanceMapper.getInstanceByModuleId(moduleId);
    }
}
