package com.aspire.mirror.alert.server.biz.bpm;

import org.springframework.http.ResponseEntity;

public interface AlertBpmBiz {
    /**
     * 根据工单号告警确认
     */
    ResponseEntity<String> alertConfirmByOrderId(String username,String orderId,String content);
    /**
     * 根据工单号告警清除
     */
    ResponseEntity<String> alertRemoveByOrderId(String username,String orderId,String content);
    /**
     * 更改工单类型
     */
    ResponseEntity<String> updateOrderByOrderId(String username,String oldOrderId,String newOrderId);
}
