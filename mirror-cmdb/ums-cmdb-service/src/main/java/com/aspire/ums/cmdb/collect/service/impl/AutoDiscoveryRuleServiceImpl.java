package com.aspire.ums.cmdb.collect.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryRuleEntity;
import com.aspire.ums.cmdb.collect.mapper.AutoDiscoveryRuleMapper;
import com.aspire.ums.cmdb.collect.schedule.DiscoverSchedule;
import com.aspire.ums.cmdb.collect.schedule.DiscoveryFromKafka;
import com.aspire.ums.cmdb.collect.service.AutoDiscoveryRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AutoDiscoveryRuleServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class AutoDiscoveryRuleServiceImpl implements AutoDiscoveryRuleService {

    @Autowired
    private AutoDiscoveryRuleMapper discoveryRuleMapper;

    @Autowired
    private DiscoverSchedule discoverSchedule;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void insertVO(AutoDiscoveryRuleEntity ruleEntity) {
        discoveryRuleMapper.insertVO(ruleEntity);
        if ("是".equals(ruleEntity.getEnable())) {
            discoverSchedule.addTriggerTask(ruleEntity);
        }
    }

    @Override
    public List<AutoDiscoveryRuleEntity> getRulesByModuleId(String moduleId, JSONObject param) {
        return discoveryRuleMapper.getRulesByModuleId(moduleId, param);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteVO(List<String> ruleIds) {
        if (ruleIds != null) {
            for (String ruleId : ruleIds) {
                AutoDiscoveryRuleEntity ruleEntity = new AutoDiscoveryRuleEntity();
                ruleEntity.setId(ruleId);
                discoveryRuleMapper.deleteVO(ruleEntity);
                discoverSchedule.cancelTriggerTask(ruleId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void updateVO(AutoDiscoveryRuleEntity ruleEntity) {
        discoveryRuleMapper.updateVO(ruleEntity);
        if ("是".equals(ruleEntity.getEnable())) {
            discoverSchedule.resetTriggerTask(ruleEntity);
        }
        if ("否".equals(ruleEntity.getEnable())) {
            discoverSchedule.cancelTriggerTask(ruleEntity.getId());
        }
    }

    @Override
    public List<AutoDiscoveryRuleEntity> getRulesByRuleName(String moduleId, String ruleName) {
        return discoveryRuleMapper.getRulesByRuleName(moduleId, ruleName);
    }

    @Override
    public List<AutoDiscoveryRuleEntity> listRules() {
        return discoveryRuleMapper.listRules();
    }

}
