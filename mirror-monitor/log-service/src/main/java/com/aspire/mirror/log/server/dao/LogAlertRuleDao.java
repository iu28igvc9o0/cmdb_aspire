package com.aspire.mirror.log.server.dao;

import com.aspire.mirror.log.server.dao.po.CreateLogAlertLinkedReqDTO;
import com.aspire.mirror.log.server.dao.po.CreateLogAlertRuleReqDTO;
import com.aspire.mirror.log.server.dao.po.LogAlertRuleDetailDTO;
import com.aspire.mirror.log.server.dao.po.LogAlertRuleListReqDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface LogAlertRuleDao {

    /**
     * 创建日志告警规则
     */
    void createLogAlertRule(CreateLogAlertRuleReqDTO request);

    /**
     * 获取日志告警规则详情
     */
    LogAlertRuleDetailDTO getLogAlertRuleDetail(@Param("uuid") String uuid);

    /**
     * 删除日志告警规则
     */
    void deleteLogAlertRule(List<String> uuidList);

    /**
     * 获取日志告警规则列表
     */
    int getLogAlertRuleListCount(LogAlertRuleListReqDTO request);
    List<LogAlertRuleDetailDTO> getLogAlertRuleListData(LogAlertRuleListReqDTO request);

    /**
     * 根据日志告警规则名称获取日志过滤规则详情
     */
    LogAlertRuleDetailDTO getLogAlertRuleDetailByName(@Param("name") String name);

    /**
     * 根据uuid修改运行状态
     */
    void updateRunStatusByUuid(Map<String, Object> request);

    /**
     * 修改日志告警规则
     */
    void updateLogAlertRule(CreateLogAlertRuleReqDTO request);

    void insertLogAlertLinked(CreateLogAlertLinkedReqDTO request);

    /**
     * 根据日志告警规则id删除日志告警关联表
     */
    void deleteLogAlertLinkedById(List<String> uuidList);

    /**
     * 根据日志告警规则id获取告警数量
     */
    int getAlertCount(@Param("uuid") String uuid);
}
