package com.aspire.mirror.alert.server.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.mirror.alert.server.biz.alert.AlertsBiz;
import com.aspire.mirror.alert.server.biz.alert.AlertsHisBiz;
import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsHis;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsHisVo;
import com.aspire.mirror.alert.server.util.StringUtils;

@RestController
public class AlertController {

    @Autowired
    private AlertsBiz alertsBiz;
    @Autowired
    private AlertsHisBiz alertsHisBiz;

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertController.class);
    


    @PutMapping(value = "/v1/alert/refresh")
    public void refreshAllAlerts(@RequestParam(value = "device_ip", required = false) String deviceIp,
                                 @RequestParam(value = "idc_type", required = false) String idcType,
                                 @RequestParam(value = "device_class", required = false) String deviceClass) {
        Alerts alerts = new Alerts();
        if (!StringUtils.isEmpty(deviceIp)) {
            alerts.setDeviceIp(deviceIp);
        }
        if (!StringUtils.isEmpty(idcType)) {
            alerts.setIdcType(idcType);
        }
        if (!StringUtils.isEmpty(deviceClass)) {
            alerts.setDeviceClass(deviceClass);
        }
        List<AlertsVo> alertsVoList = alertsBiz.select(alerts);
        alertsVoList.stream().forEach((AlertsVo alertsVo) -> {
            if (AlertsVo.OBJECT_TYPE_BIZ.equals(alertsVo.getObjectType())) {
                return;
            }
        });

    }

    @PutMapping(value = "/v1/alertHis/refresh")
    public void refreshAllAlertsHis (@RequestParam(value = "device_ip", required = false) String deviceIp,
                                     @RequestParam(value = "idc_type", required = false) String idcType,
                                     @RequestParam(value = "device_class", required = false) String deviceClass) {
        AlertsHis alertsHisQuery = new AlertsHis();
        if (!StringUtils.isEmpty(deviceIp)) {
            alertsHisQuery.setDeviceIp(deviceIp);
        }
        if (!StringUtils.isEmpty(idcType)) {
            alertsHisQuery.setIdcType(idcType);
        }
        if (!StringUtils.isEmpty(deviceClass)) {
            alertsHisQuery.setDeviceClass(deviceClass);
        }
        List<AlertsHisVo> alertsHisVoList = alertsHisBiz.select(alertsHisQuery);
        alertsHisVoList.stream().forEach((AlertsHisVo alertsHisVo) -> {
            if (AlertsVo.OBJECT_TYPE_BIZ.equals(alertsHisVo.getObjectType())) {
                return;
            }
        });

    }
    
}
