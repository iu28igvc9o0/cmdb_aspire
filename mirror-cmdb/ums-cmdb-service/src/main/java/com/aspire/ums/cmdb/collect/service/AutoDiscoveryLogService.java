package com.aspire.ums.cmdb.collect.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogEntity;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogListResp;
import com.aspire.ums.cmdb.maintain.entity.Instance;
import com.aspire.ums.cmdb.maintain.entity.InstanceModel;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AutoDiscoveryLogService
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface AutoDiscoveryLogService {

    /**
     * 新增实体
     * @param logEntity 实体对象
     */
    void insertVO(AutoDiscoveryLogEntity logEntity);

    /**
     * 修改实例
     * @param logEntity 实例对象
     */
    void updateVO(AutoDiscoveryLogEntity logEntity);

    /**
     * 根据规则ID列表 获取规则扫描到的所有实例
     * @param ruleId
     * @return
     */
    List<AutoDiscoveryLogEntity> getListByRuleId(String ruleId);

    /**
     * 根据规则ID 获取规则扫描到的所有实例
     * @param ruleId
     * @return
     */
    List<String> getInstanceNameByRuleId(String ruleId);


    /**
     * 根据规则ID 获取规则扫描到的所有实例
     * @param ruleIds
     * @return
     */
    List<AutoDiscoveryLogListResp> getListByRuleIds(List<String> ruleIds, JSONObject queryData);


    /**
     * 屏蔽新发现ip
     * @param discoveryLogs 实例对象
     */
    void shieldIp(List<AutoDiscoveryLogEntity> discoveryLogs);

    /**
     * 修改实例
     * @param updateMap 实例对象
     */
    void updateVOByBatch(Map<String, Object> updateMap);


    /**
     * 获取发现数据详情
     * @param id id
     */
    AutoDiscoveryLogListResp getDetailById (String id);

    /**
     * 新发现实例绑定（实例更新ip）
     * @param discoveryLog
     */
    void bind (AutoDiscoveryLogEntity discoveryLog, String instanceId);

    /**
     * 新发现实例维护（添加instance）
     * @param instanceModel
     */
    void maintain (String id, InstanceModel instanceModel);

    /**
     * 查询实例
     * @param moduleId
     */
    List<Instance> listInstanceByModulId (String moduleId);
}
