package com.aspire.mirror.alert.server.biz.bpm;

import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderConfigDetailVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderConfigVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderLogVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderLogReqVo;
import com.aspire.mirror.common.entity.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AlertsAutoOrderConfigBiz {
    /**
     * 获取告警自动派单配置列表
     */
    PageResponse<AlertAutoOrderConfigDetailVo> getAlertAutoOrderConfigList(String configName,
                                                                           String isOpen,
                                                                           String startTime,
                                                                           String endTime,
                                                                           String orderType,
                                                                           String orderTimeInterval,
                                                                           Integer pageNum,
                                                                           Integer pageSize);
    /**
     * 创建告警自动派单配置
     */
    void createAlertAutoOrderConfig(AlertAutoOrderConfigVo request);
    /**
     * 校验配置名称
     */
    String checkName(String configName);
    /**
     * 修改告警自动派单配置
     */
    void updateAlertAutoOrderConfig(AlertAutoOrderConfigVo request);
    /**
     * 获取告警自动派单配置详情
     */
    AlertAutoOrderConfigDetailVo getAlertAutoOrderConfigDetail(String uuid);
    /**
     * 删除告警自动派单配置详情
     */
    void deleteAlertAutoOrderConfig(List<String> uuidList);
    /**
     * 更改告警自动派单配置状态
     */
    void updateAlertAutoOrderConfigStatus(List<String> uuidList,String configStatus);
    /**
     * 拷贝告警自动派单配置
     */
    void copyAlertAutoOrderConfig(String uuid,String userName);
    /**
     * 获取告警自动派单配置日志列表
     */
    PageResponse<AlertAutoOrderLogVo> getAlertAutoOrderLogList(AlertAutoOrderLogReqVo request);
    /**
     * 导出告警自动派单配置日志
     */
    List<Map<String, Object>> exportAlertAutoOrderLogList(AlertAutoOrderLogReqVo request);
    /**
     * 告警自动派单定时任务
     */
    ResponseEntity<String> alertAutoOrderSchedule();
}
