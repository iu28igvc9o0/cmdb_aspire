package com.aspire.mirror.alert.server.controller.bpm;

import com.aspire.mirror.alert.api.service.bpm.AlertBpmService;
import com.aspire.mirror.alert.server.biz.bpm.AlertBpmBiz;
import com.aspire.mirror.alert.server.biz.bpm.IAlertOrderHandleBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AlertBpmController implements AlertBpmService {

    @Autowired
    private AlertBpmBiz alertBpmBiz;
    @Autowired
    private IAlertOrderHandleBiz alertOrderHandleBiz;

    @Override
    public ResponseEntity<String> alertConfirmByOrderId(@RequestParam("username") String username,
                                                        @RequestParam("orderId") String orderId,
                                                        @RequestParam("content") String content) {
        log.info("call alertConfirmByOrderId username is {}, orderId is {}, content is {}", username, orderId, content);
        return alertBpmBiz.alertConfirmByOrderId(username,orderId,content);
    }

    @Override
    public ResponseEntity<String> alertRemoveByOrderId(@RequestParam("username") String username,
                                                       @RequestParam("orderId") String orderId,
                                                       @RequestParam("content") String content) {
        log.info("call alertRemoveByOrderId username is {}, orderId is {}, content is {}", username, orderId, content);
        return alertBpmBiz.alertRemoveByOrderId(username,orderId,content);
    }

    @Override
    public ResponseEntity<String> updateOrderByOrderId(@RequestParam("username") String username,
                                                       @RequestParam("oldOrderId") String oldOrderId,
                                                       @RequestParam("newOrderId") String newOrderId) {
        log.info("call updateOrderByOrderId username is {}, oldOrderId is {}, newOrderId is {}", username, oldOrderId, newOrderId);
        return alertBpmBiz.updateOrderByOrderId(username,oldOrderId,newOrderId);
    }

    /**
     * 同步工单处理状态
     *
     * @param orderId
     * @param execTime
     * @param status
     * @return
     */
    public ResponseEntity<String> updateOrderStatus(@PathVariable("orderId") String orderId,
                                                    @RequestParam(value = "execUser", required = false) String execUser,
                                                    @RequestParam(value = "account", required = false) String account,
                                                    @RequestParam("execTime") String execTime,
                                                    @RequestParam("status") String status) {
        log.info("call updateOrderStatus orderId is {}, execUser is {}, account is {}, execTime is {}, status is {}",orderId, execUser, account, execTime, status);
        return alertOrderHandleBiz.insert(orderId, execTime, status, execUser, account);
    }
}
