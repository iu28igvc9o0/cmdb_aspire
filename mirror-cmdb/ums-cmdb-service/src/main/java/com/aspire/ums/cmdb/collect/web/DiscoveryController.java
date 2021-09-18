package com.aspire.ums.cmdb.collect.web;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryRuleEntity;
import com.aspire.ums.cmdb.collect.entity.InsertDiscoveryRuleEntity;
import com.aspire.ums.cmdb.collect.entity.Page;
import com.aspire.ums.cmdb.collect.service.AutoDiscoveryLogService;
import com.aspire.ums.cmdb.collect.service.AutoDiscoveryRuleService;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.module.service.ModuleService;
import com.aspire.ums.cmdb.util.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DiscoveryController
 * Author:   zhu.juwang
 * Date:     2019/4/1 15:11
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RefreshScope
@RestController
@Slf4j
@RequestMapping("/cmdb/discovery")
public class DiscoveryController {

    @Autowired
    private AutoDiscoveryRuleService ruleService;
    @Autowired
    private AutoDiscoveryLogService logService;
    @Autowired
    private ModuleService moduleService;

    /**
     * 查询模型列表, 返回模型下配置的新发现规则的列表
     * @return 模型列表
     */
    @RequestMapping(value = "/module/list", method = RequestMethod.GET)
    public JSONArray getModuleList() {
        //加载父模型
        List<Module> parentModule = moduleService.selectModule();
        JSONArray returnList = new JSONArray();
        if (parentModule != null && parentModule.size() >0) {
            for (Module parent : parentModule) {
                JSONObject parentObject = (JSONObject) JSON.toJSON(parent);
                JSONArray jsonArray = new JSONArray();
                if (parent.getChildModules() != null && parent.getChildModules().size() >0) {
                    for (Module child : parent.getChildModules()) {
                        JSONObject childObject = (JSONObject) JSON.toJSON(child);
                        List<AutoDiscoveryRuleEntity> mapList= ruleService.getRulesByModuleId(child.getId(), new JSONObject());
                        if (mapList != null && mapList.size() >0) {
                            childObject.put("rules", mapList);
                        } else {
                            childObject.put("rules", new ArrayList<>());
                        }
                        jsonArray.add(childObject);
                    }
                }
                parentObject.put("childModules", jsonArray);
                returnList.add(parentObject);
            }
        }
        return returnList;
    }

    /**
     * 获取指定moduleId的规则列表
     * @param moduleId 模型ID
     * @param pageNumber 当前页数
     * @param pageSize 每页数量
     * @return 规则列表
     */
    @RequestMapping(value = "/rule/list/{moduleId}", method = RequestMethod.POST)
    public JSONObject getRuleList(@PathVariable("moduleId") String moduleId,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestBody(required = false) JSONObject param) {
        Page<AutoDiscoveryRuleEntity> page = new Page<>(pageNumber, pageSize);
        List<AutoDiscoveryRuleEntity> ruleList = ruleService.getRulesByModuleId(moduleId, param);
        page.setDataList(ruleList);
        return (JSONObject) JSON.toJSON(page);
    }

    /**
     * 新增规则
     * @param moduleId 模型ID
     * @param ruleEntity 规则实体
     * @return
     */
    @RequestMapping(value = "/rule/insert/{moduleId}", method = RequestMethod.POST)
    public Map<String, String> insertRule(@PathVariable("moduleId") String moduleId,
                                           @RequestBody InsertDiscoveryRuleEntity ruleEntity) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            List<AutoDiscoveryRuleEntity> existsList = ruleService.getRulesByRuleName(moduleId, ruleEntity.getRuleName());
            if (existsList != null && existsList.size() >0) {
                returnMap.put("code", "error");
                returnMap.put("msg", "Already has the rule name [" + ruleEntity.getRuleName() + "].");
                return returnMap;
            }
            AutoDiscoveryRuleEntity entity = new AutoDiscoveryRuleEntity();
            entity.setId(UUIDUtil.getUUID());
            entity.setRuleName(ruleEntity.getRuleName());
            entity.setModuleId(moduleId);
            entity.setRoom(ruleEntity.getRoom());
            entity.setDiscoveryType(ruleEntity.getDiscoveryType());
            entity.setDiscoveryParam(ruleEntity.getDiscoveryParam() == null ? null : JSON.toJSONString(ruleEntity.getDiscoveryParam()));
            entity.setStartScanIp(ruleEntity.getStartScanIp());
            entity.setEndScanIp(ruleEntity.getEndScanIp());
            entity.setCollectCycle(ruleEntity.getCollectCycle());
            entity.setCycleUnit(ruleEntity.getCycleUnit());
            entity.setEnable(ruleEntity.getEnable());
            entity.setCreateTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            ruleService.insertVO(entity);
            returnMap.put("code", "success");
        } catch (Exception e) {
            log.error("Insert discovery rule error. entity -> {}. cause -> {}",
                    JSONObject.toJSONString(ruleEntity), e.getMessage(), e);
            returnMap.put("code", "error");
            returnMap.put("msg", e.getMessage());
        }
        return returnMap;
    }

    /**
     * 修改规则
     * @param moduleId 模型ID
     * @param ruleEntity 规则实体
     * @return
     */
    @RequestMapping(value = "/rule/update/{moduleId}/{ruleId}", method = RequestMethod.PUT)
    public Map<String, String> updateRule(@PathVariable("moduleId") String moduleId,
                                          @PathVariable("ruleId") String ruleId,
                                           @RequestBody InsertDiscoveryRuleEntity ruleEntity) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            AutoDiscoveryRuleEntity entity = new AutoDiscoveryRuleEntity();
            entity.setId(ruleId);
            entity.setModuleId(moduleId);
            entity.setRuleName(ruleEntity.getRuleName());
            entity.setDiscoveryType(ruleEntity.getDiscoveryType());
            entity.setDiscoveryParam(ruleEntity.getDiscoveryParam() == null ? null : JSON.toJSONString(ruleEntity.getDiscoveryParam()));
            entity.setStartScanIp(ruleEntity.getStartScanIp());
            entity.setEndScanIp(ruleEntity.getEndScanIp());
            entity.setCollectCycle(ruleEntity.getCollectCycle());
            entity.setCycleUnit(ruleEntity.getCycleUnit());
            entity.setEnable(ruleEntity.getEnable());
            entity.setCreateTime(ruleEntity.getCreateTime());
            ruleService.updateVO(entity);
            returnMap.put("code", "success");
        } catch (Exception e) {
            log.error("Update discovery rule error. entity -> {}. cause -> {}",
                    JSONObject.toJSONString(ruleEntity), e.getMessage(), e);
            returnMap.put("code", "error");
            returnMap.put("msg", e.getMessage());
        }
        return returnMap;
    }

    /**
     * 删除规则
     * @param moduleId 模型ID
     * @param ruleIds 删除实体
     * @return
     */
    @RequestMapping(value = "/rule/delete/{moduleId}/", method = RequestMethod.POST)
    public Map<String, String> deleteRule(@PathVariable("moduleId") String moduleId,
                                          @RequestBody List<String> ruleIds) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            ruleService.deleteVO(ruleIds);
            returnMap.put("code", "success");
        } catch (Exception e) {
            log.error("Delete discovery rule error. entity -> {}. cause -> {}", ruleIds.toString(), e.getMessage(), e);
            returnMap.put("code", "error");
            returnMap.put("msg", e.getMessage());
        }
        return returnMap;
    }

}
