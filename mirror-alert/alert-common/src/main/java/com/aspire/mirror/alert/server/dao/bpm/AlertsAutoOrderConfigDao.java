package com.aspire.mirror.alert.server.dao.bpm;

import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderConfigDetailVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderConfigVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderLogVo;
import com.aspire.mirror.alert.server.vo.bpm.AlertAutoOrderLogReqVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlertsAutoOrderConfigDao {
    /**
     * 获取告警自动派单配置列表
     */
    List<AlertAutoOrderConfigDetailVo> getAlertAutoOrderConfigList(@Param("configName") String configName,
                                                                   @Param("isOpen") String isOpen,
                                                                   @Param("startTime") String startTime,
                                                                   @Param("endTime") String endTime,
                                                                   @Param("orderType") String orderType,
                                                                   @Param("orderTimeInterval") String orderTimeInterval,
                                                                   @Param("pageNum") Integer pageNum,
                                                                   @Param("pageSize") Integer pageSize);
    int getAlertAutoOrderConfigCount(@Param("configName") String configName,
                                    @Param("isOpen") String isOpen,
                                    @Param("startTime") String startTime,
                                    @Param("endTime") String endTime,
                                    @Param("orderType") String orderType,
                                    @Param("orderTimeInterval") String orderTimeInterval,
                                    @Param("pageNum") Integer pageNum,
                                    @Param("pageSize") Integer pageSize);
    /**
     * 创建告警自动派单配置
     */
    void createAlertAutoOrderConfig(AlertAutoOrderConfigVo request);
    /**
     * 校验配置名称
     */
    AlertAutoOrderConfigDetailVo checkName(@Param("configName") String configName);
    /**
     * 修改告警自动派单配置
     */
    void updateAlertAutoOrderConfig(AlertAutoOrderConfigVo request);
    /**
     * 获取告警自动派单配置详情
     */
    AlertAutoOrderConfigDetailVo getAlertAutoOrderConfigDetail(@Param("uuid") String uuid);
    /**
     * 删除告警自动派单配置详情
     */
    void deleteAlertAutoOrderConfig(List<String> uuidList);
    /**
     * 更改告警自动派单配置状态
     */
    void updateAlertAutoOrderConfigStatus(Map<String, Object> map);
    /**
     * 获取告警自动派单配置日志列表
     */
    List<AlertAutoOrderLogVo> getAlertAutoOrderLogList(AlertAutoOrderLogReqVo request);
    int getAlertAutoOrderLogCount(AlertAutoOrderLogReqVo request);
    /**
     * 导出告警自动派单配置日志
     */
    List<Map<String, Object>> exportAlertAutoOrderLogList(AlertAutoOrderLogReqVo request);
    /**
     * 根据过滤内容获取告警
     */
    List<Map<String, Object>> getAlertDataByFilter(Map<String, Object> map);

    /**
     * 添加派单日志
     */
    void insertAlertOrderConfigLog(AlertAutoOrderLogVo request);
    int getOrderLogCountByAlertIdAndOrderType(@Param("alertId") String alertId,
                                              @Param("orderType") String orderType);
    void updateOrderTime(@Param("alertId") String alertId,
                                              @Param("orderType") String orderType);
}
