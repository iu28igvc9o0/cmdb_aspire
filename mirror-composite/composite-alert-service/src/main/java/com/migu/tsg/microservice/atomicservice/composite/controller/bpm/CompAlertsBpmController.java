package com.migu.tsg.microservice.atomicservice.composite.controller.bpm;

import com.aspire.mirror.composite.service.bpm.ICompAlertBpmService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.bpm.AlertBpmServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompAlertsBpmController implements ICompAlertBpmService {

    @Autowired
    private AlertBpmServiceClient alertBpmServiceClient;

    @Override
    public ResponseEntity<String> alertConfirmByOrderId(@RequestParam("username") String username,
                                                        @RequestParam("orderId") String orderId,
                                                        @RequestParam("content") String content) {
        return alertBpmServiceClient.alertConfirmByOrderId(username,orderId,content);
    }

    @Override
    public ResponseEntity<String> alertRemoveByOrderId(@RequestParam("username") String username,
                                                       @RequestParam("orderId") String orderId,
                                                       @RequestParam("content") String content) {
        return alertBpmServiceClient.alertRemoveByOrderId(username,orderId,content);
    }

    @Override
    public ResponseEntity<String> updateOrderByOrderId(@RequestParam("username") String username,
                                                       @RequestParam("oldOrderId") String oldOrderId,
                                                       @RequestParam("newOrderId") String newOrderId) {
        return alertBpmServiceClient.updateOrderByOrderId(username,oldOrderId,newOrderId);
    }

	@Override
	public ResponseEntity<String> updateOrderStatus(@PathVariable("orderId") String orderId,
            @RequestParam(value = "execUser", required = false) String execUser,
            @RequestParam(value = "account", required = false) String account,
            @RequestParam("execTime") String execTime,
            @RequestParam("status") String status) {
		return alertBpmServiceClient.updateOrderStatus(orderId, execUser,account, execTime, status);
	}

}
