package com.aspire.mirror.thirdparty.alertnotify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.thirdparty.alertnotify.api.IAlertsNotifyService;
import com.aspire.mirror.thirdparty.alertnotify.biz.RtzAlertNotifyBiz;
import com.aspire.mirror.thirdparty.alertnotify.domain.AlertNotifyWrap;


/**
* 告警通知    <br/>
* Project Name:rtz-alert-notify
* File Name:AlertsNotifyController.java
* Package Name:com.aspire.mirror.zabbixintegrate.controller
* ClassName: AlertsNotifyController <br/>
* date: 2019年1月11日 上午10:03:59 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
@RestController
public class AlertsNotifyController implements IAlertsNotifyService {
	@Autowired
	private RtzAlertNotifyBiz rtzAlertBiz;

	@Override
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void rtzAlertNotify(@RequestBody AlertNotifyWrap alertMsg) {
		rtzAlertBiz.doAlertNotify(alertMsg);
	}
}
