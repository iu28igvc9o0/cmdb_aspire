package com.aspire.ums.cmdb.collect.mapper;

import com.aspire.ums.cmdb.collect.entity.AutoDiscoveryLogShieldEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AutoDiscoveryLogShieldMapper
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface AutoDiscoveryLogShieldMapper {


    /**
     * 根据规则ID列表 获取所有被屏蔽的ip实例
     * @param ruleId
     * @return
     */
    List<String> getListByRuleId(@Param("ruleId") String ruleId);

    /**
     * 新增屏蔽实例
     * @param shieldEntities
     * @return
             */
    void insertShields(List<AutoDiscoveryLogShieldEntity> shieldEntities);

    /**
     * 解除屏蔽状态根据instancename删除数据
     * @param instanceName
     * @return
     */
    List<String> delete(@Param("ruleId") String ruleId, @Param("instanceName") String instanceName);

}
