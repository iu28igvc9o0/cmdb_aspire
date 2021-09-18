package com.aspire.ums.cmdb.collect.mapper;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryRuleEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
public interface AutoDiscoveryRuleMapper extends BaseMapper<AutoDiscoveryRuleEntity> {
    /**
     * 根据模型ID 获取模型下所有的规则
     * @param moduleId 模块ID
     * @param param 查询参数
     * @return
     */
    List<AutoDiscoveryRuleEntity> getRulesByModuleId(@Param("moduleId") String moduleId, @Param("param") JSONObject param);

    /**
     * 根据模型ID 规则名称 获取模型
     * @param moduleId 模块ID
     * @param ruleName 规则名称
     * @return
     */
    List<AutoDiscoveryRuleEntity> getRulesByRuleName(@Param("moduleId") String moduleId, @Param("ruleName") String ruleName);

    /**
     * 查询所有启用的规则
     */
    List<AutoDiscoveryRuleEntity> listRules ();
}
