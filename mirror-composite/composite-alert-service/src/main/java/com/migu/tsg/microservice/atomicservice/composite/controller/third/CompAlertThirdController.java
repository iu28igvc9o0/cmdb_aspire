package com.migu.tsg.microservice.atomicservice.composite.controller.third;

import com.aspire.mirror.alert.api.dto.third.ThirdCreateAlertReq;
import com.aspire.mirror.composite.payload.third.CompThirdCreateAlertReq;
import com.aspire.mirror.composite.service.third.ICompAlertThirdService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.third.AlertsThirdServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Slf4j
public class CompAlertThirdController implements ICompAlertThirdService {
    @Autowired
    private AlertsThirdServiceClient alertsThirdServiceClient;

    @Override
    public Object createdAlerts(@RequestBody CompThirdCreateAlertReq thirdCreateAlertReqList) {
        Object createdAlerts = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = dateFormat.format(new Date());
            thirdCreateAlertReqList.setAlertStartTime(date);
            thirdCreateAlertReqList.setCurMoniTime(date);
            createdAlerts = alertsThirdServiceClient.createdAlert(PayloadParseUtil.jacksonBaseParse( ThirdCreateAlertReq.class, thirdCreateAlertReqList));
        } catch (Exception e) {
            log.error("Create alert is error {}", e);
            throw new RuntimeException("Create alert is error {}", e);
        }
        return createdAlerts;
    }
}
