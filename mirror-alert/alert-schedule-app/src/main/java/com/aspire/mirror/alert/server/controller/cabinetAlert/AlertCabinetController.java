package com.aspire.mirror.alert.server.controller.cabinetAlert;

import com.aspire.mirror.alert.server.biz.cabinetAlert.AlertCabinetColumnBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AlertCabinetController {

    private static final Logger logger = LoggerFactory.getLogger(AlertCabinetController.class);
	@Autowired
	private AlertCabinetColumnBiz cabinetColumnBiz;
	 
	 @PostMapping(value = "/v1/alert/day/CabinetColumnTask") 
	 public void CabinetColumnTask() throws Exception {
		 cabinetColumnBiz.CabinetColumnTask();
	 }
}
