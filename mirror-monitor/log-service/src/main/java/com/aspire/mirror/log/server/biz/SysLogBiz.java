package com.aspire.mirror.log.server.biz;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.*;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.log.server.biz
 * 类名称:    SysLogBiz.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/6/14 15:13
 * 版本:      v1.0
 */
public interface SysLogBiz {
    PageResponse<SysLogResponse> getLogData(SysLogSearchRequest request);
    /**
     * 创建日志过滤规则
     */
    String createLogFilterRule(CreateLogFilterRuleReq request);
    /**
     * 获取日志过滤规则详情
     */
    LogFilterRuleDetail getLogFilterRuleDetail(String uuid);
    /**
     * 获取日志过滤规则列表
     */
    List<LogFilterRuleDetail> getLogFilterRuleList(String ruleType);
    /**
     * 删除日志过滤规则
     */
    void deleteLogFilterRule(String uuid);

    /**
     * 根据过滤规则获取日志数据
     */
    PageResponse<SysLogResponse> getLogDataByFilterRule(String uuid, String pageNo, String pageSize);

    /**
     * 根据过滤规则名称获取日志过滤规则详情
     */
    LogFilterRuleDetail getLogFilterRuleDetailByName(String uuid);

    /**
     * 创建日志告警规则
     */
    String createLogAlertRule(CreateLogAlertRuleReq request);

    /**
     * 获取日志告警规则详情
     */
    LogAlertRuleDetail getLogAlertRuleDetail(String uuid);

    /**
     * 获取日志告警规则列表
     */
    PageResponse<LogAlertRuleDetail> getLogAlertRuleList(LogAlertRuleListReq request);

    /**
     * 删除日志告警规则规则
     */
    void deleteLogAlertRule(List<String> uuidList);

    /**
     * 根据告警规则名称获取日志告警规则详情
     */
    LogAlertRuleDetail getLogAlertRuleDetailByName(String uuid);

    /**
     * 根据uuid修改运行状态
     */
    void updateRunStatusByUuid(Map<String, Object> request);

    /**
     * 根据uuid修改规则
     */
    void updateLogAlertRule(CreateLogAlertRuleReq request);

    void insertLogAlertLinked(CreateLogAlertLinkedReq request);
}
