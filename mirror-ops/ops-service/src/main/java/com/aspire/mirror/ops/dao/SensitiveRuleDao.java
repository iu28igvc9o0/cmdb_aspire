package com.aspire.mirror.ops.dao;

import com.aspire.mirror.ops.api.domain.SensitiveRule;
import com.aspire.mirror.ops.api.domain.SensitiveRuleQueryModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.dao
 * 类名称:    SensitiveRuleDao.java
 * 类描述:
 * 创建人:    JinSu
 * 创建时间:  2020/2/17 11:53
 * 版本:      v1.0
 */
@Mapper
public interface SensitiveRuleDao {

    void insertBatchSensitiveRule(List<SensitiveRule> ruleList);

    void deleteSensitiveRuleByConfigId(@Param("sensitive_config_id") Long sensitiveConfigId);

    List<SensitiveRule> selectBySensitiveConfigId(@Param("sensitive_config_id") Long sensitiveConfigId);

    int updateStatusByRuleId(SensitiveRule sensitiveRule);

    List<SensitiveRule> selectPassedBySensitiveConfigId(@Param("sensitive_config_id") Long configId);

    void updateSensitiveRuleByRuleId(SensitiveRule sensitiveRule);

    void deleteSensitiveRuleByRuleIdArray(List<Long> deleteRuleIdList);

    void insertSensitiveRule(SensitiveRule sensitiveRule);

    Integer querySensitiveRuleListTotalSize(SensitiveRuleQueryModel sensitiveRuleQueryModel);

    List<SensitiveRule> querySensitiveRuleList(SensitiveRuleQueryModel sensitiveRuleQueryModel);
}
