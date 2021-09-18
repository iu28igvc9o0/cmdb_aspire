package com.aspire.mirror.alert.server.biz.bpm;

import com.aspire.mirror.alert.server.vo.bpm.AlertBpmCallBack;

import java.util.List;
import java.util.Map;

public interface IBpmTaskService {
    /**
     * 调用工单接口关闭告警工单
     * @param orderId
     * @return
     */
    String closeBpmInstance(String orderId, String content);

    /**
     * 自动调用工单接口关闭告警工单
     */
    void closeBpm(String orderId, String alertType);

    /**
     * 告警派单
     * @param alerts
     * @param orderType
     * @return
     */
    String alertHandleBpmResult(List<Map<String, Object>> alerts, String type, String user, Integer orderType);

    /**
     * 告警屏蔽日志派单
     * @param alerts
     * @param orderType
     * @return
     */
    String isolateHandleBpmResult(List<Map<String, Object>> alerts, String type, String user, Integer orderType);

    /**
     *
     * @param alert
     * @param orderTye
     * @return
     */
    AlertBpmCallBack callBpmFlowStart(String user, Map<String, Object> alert, String orderTye);

    /**
     * 校验工单是否关闭
     * @param orderList
     * @return
     */
    List<String> checkOrderIsEnd(List<String> orderList);
}