package com.aspire.mirror.log.server.dao;

import com.aspire.mirror.log.server.dao.po.CreateLogFilterRuleReqDTO;
import com.aspire.mirror.log.server.dao.po.LogFilterRuleDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogFilterRuleDao {

    /**
     * 创建日志过滤规则
     */
    void createLogFilterRule(CreateLogFilterRuleReqDTO request);

    /**
     * 获取日志过滤规则详情
     */
    LogFilterRuleDetailDTO getLogFilterRuleDetail(@Param("uuid") String uuid);
    /**
     * 删除日志过滤规则
     */
    void deleteLogFilterRule(@Param("uuid") String uuid);
    /**
     * 获取日志过滤规则列表
     */
    List<LogFilterRuleDetailDTO> getLogFilterRuleList(@Param("ruleType") String ruleType);

    /**
     * 根据过滤规则名称获取日志过滤规则详情
     */
    LogFilterRuleDetailDTO getLogFilterRuleDetailByName(@Param("name") String name);

    /**
     * 创建日志告警规则
     */
    void createLogAlertRule(CreateLogFilterRuleReqDTO request);
}
