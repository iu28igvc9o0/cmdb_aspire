package com.aspire.ums.cmdb.collect.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogEntity;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogListResp;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryRuleEntity;
import com.aspire.ums.cmdb.collect.entity.Page;
import com.aspire.ums.cmdb.collect.service.AutoDiscoveryLogService;
import com.aspire.ums.cmdb.collect.service.AutoDiscoveryRuleService;
import com.aspire.ums.cmdb.maintain.entity.Instance;
import com.aspire.ums.cmdb.maintain.entity.InstanceModel;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DiscoveryLogController
 * Author:   HANGFANG
 * Date:     2019/4/19 10:44
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@RestController
@RefreshScope
@Slf4j
@RequestMapping("/cmdb/discovery/log")
public class DiscoveryLogController {

    @Autowired
    private AutoDiscoveryRuleService ruleService;
    @Autowired
    private AutoDiscoveryLogService logService;

    /**
     * 获取指定moduleId的规则列表
     * @param moduleId 模型ID
     * @param pageNumber 当前页数
     * @param pageSize 每页数量5
     * @return 新发现数据列表
     * queryData {segment: 192.168.0,startIp: 1,endIp: 255}
     */
    @RequestMapping(value = "/list/{moduleId}", method = RequestMethod.POST)
    public JSONObject getLogList(@PathVariable("moduleId") String moduleId,
                                  @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                  @RequestBody(required = false) JSONObject queryData) {
        Page<AutoDiscoveryLogListResp> pageData = new Page<>(pageNumber, pageSize);
        // 获取当前模型下所有规则id
        JSONObject param = new JSONObject();
        if (null != queryData && queryData.containsKey("ruleName")) {
            param.put("ruleName", queryData.get("ruleName"));
        }
        List<AutoDiscoveryRuleEntity> ruleEntitys = ruleService.getRulesByModuleId(moduleId, param);
        List<String> ruleIds = new ArrayList<>();
        for (AutoDiscoveryRuleEntity rule : ruleEntitys) {
            ruleIds.add(rule.getId());
        }
        // 根据规则id去获取发现数据
        List<AutoDiscoveryLogListResp> logList = new ArrayList<>();
        if (ruleIds.size() > 0) {
            logList = logService.getListByRuleIds(ruleIds, queryData);
        }
        pageData.setDataList(logList);
        return (JSONObject) JSON.toJSON(pageData);
    }

    @RequestMapping(value = "/shield", method = RequestMethod.POST)
    public Map<String, Object> shieldIp(@RequestBody List<AutoDiscoveryLogEntity> discoveryLogs) {
        // 获取当前模型下所有规则id
        Map<String, Object> resultMap = new HashMap<>();
        try {
            logService.shieldIp(discoveryLogs);
            resultMap.put("code", "success");
            resultMap.put("msg", "屏蔽成功");
        } catch (Exception e) {
            log.error("shield discovery log error. discoveryLogs -> {}. cause -> {}",
                    JSONObject.toJSONString(discoveryLogs), e.getMessage(), e);
            resultMap.put("code", "error");
            resultMap.put("msg", "屏蔽失败");
        }
        return resultMap;
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public AutoDiscoveryLogListResp getLogDetailById(@PathVariable("id") String logId) {
        return logService.getDetailById(logId);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public  Map<String, Object> update(@PathVariable("id") String id,
                                       @RequestParam(value = "status") String status) {
        Map<String, Object> resultMap = new HashMap<>();
        AutoDiscoveryLogEntity logEntity = new AutoDiscoveryLogEntity();
        AutoDiscoveryLogListResp log = logService.getDetailById(id);
        try {
            if (null != log) {
                logEntity.setStatus(status);
                logEntity.setId(id);
                logEntity.setUpdateTime(new Date());
                logEntity.setRuleId(log.getRuleId());
                logService.updateVO(logEntity);
                resultMap.put("code", "success");
                resultMap.put("msg", "更新成功");
            } else {
                resultMap.put("code", "error");
                resultMap.put("msg", "数据不存在");
            }
        } catch (Exception e) {
            resultMap.put("code", "error");
            resultMap.put("msg", "更新失败");
        }
        return resultMap;
    }

    @RequestMapping(value = "/bind/{instanceId}", method = RequestMethod.POST)
    public Map<String, Object> bind(@PathVariable("instanceId") String instanceId,
                                    @RequestBody AutoDiscoveryLogEntity discoveryLog) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            logService.bind(discoveryLog, instanceId);
            resultMap.put("code", "success");
            resultMap.put("msg", "绑定成功");
        } catch (Exception e) {
            log.error("shield discovery log error. instanceId -> {}. discoverLog -> {}. cause -> {}",
                    instanceId, JSONObject.toJSONString(discoveryLog), e.getMessage(), e);
            resultMap.put("code", "error");
            resultMap.put("msg", "绑定失败");
        }
        return resultMap;
    }

    @RequestMapping(value = "/maintain", method = RequestMethod.POST)
    public Map<String, Object> maintain(@RequestParam(value = "id") String id,
                                        @RequestBody InstanceModel instanceModel) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            logService.maintain(id, instanceModel);
            resultMap.put("code", "success");
            resultMap.put("msg", "维护成功");
        } catch (Exception e) {
            log.error("shield discovery log error. id -> {}. instance -> {}. cause -> {}",
                    id, JSONObject.toJSONString(instanceModel), e.getMessage(), e);
            resultMap.put("code", "error");
            resultMap.put("msg", "维护失败");
        }
        return resultMap;
    }

    @RequestMapping(value = "/listInstance/{moduleId}", method = RequestMethod.GET)
    public JSONObject listInstanceByModulId(@PathVariable(value = "moduleId") String moduleId) {
        Page<Instance> pageData = new Page<>(null, null);
        List<Instance> instances = logService.listInstanceByModulId(moduleId);
        pageData.setDataList(instances);
        return (JSONObject) JSON.toJSON(pageData);
    }
}
