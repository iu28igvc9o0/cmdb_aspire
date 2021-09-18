package com.aspire.ums.cmdb.collect.mapper;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogEntity;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogListResp;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryRuleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectService
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface AutoDiscoveryLogMapper extends BaseMapper<AutoDiscoveryLogEntity> {
    /**
     * 根据规则ID 获取规则扫描到的所有实例
     * @param ruleId
     * @return
     */
    List<AutoDiscoveryLogEntity> getListByRuleId(String ruleId);

    /**
     * 根据规则IDs 获取规则扫描到的所有实例
     * @param ruleIds
     * @return
     */
    List<AutoDiscoveryLogListResp> getListByRuleIds(@Param("ruleIds") List<String> ruleIds, @Param("queryData") JSONObject queryData);

    /**
            * 根据规则IDs 获取规则扫描到的所有实例
     * @param ruleId
     * @return
             */
    List<String> getInstanceNameByRuleId(@Param("ruleId") String ruleId);

    /**
     * 批量更新发现数据
     * @param updateMap
     * @return
     */
    void updateByBatch(Map<String, Object> updateMap);

    /**
     * 获取详情
     * @param id
     * @return
     */
    AutoDiscoveryLogListResp getDetailById(String id);

    /**
     * 新发现实例绑定（实例更新ip）
     * @param bindData
     */
    void updateInstanceAndFormValue (Map bindData);

}
