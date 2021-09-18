package com.aspire.mirror.alert.server.controller.monthReport;

import com.aspire.mirror.alert.api.service.monthReport.AlertsScheduleService;
import com.aspire.mirror.alert.server.biz.monthReport.impl.AlertScheduleBizImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AlertsScheduleController implements AlertsScheduleService {

    @Autowired
    private AlertScheduleBizImpl alertScheduleBiz;

    @Override
    public void alert(@RequestParam("startTime") String startTime,
                      @RequestParam("endTime") String endTime,
                      @RequestParam("month") String month) {
        alertScheduleBiz.alert(startTime,endTime,month);
    }

    @Override
    public void device(@RequestParam("startTime") String startTime,
                       @RequestParam("endTime") String endTime,
                       @RequestParam("month") String month) {
        alertScheduleBiz.device(startTime,endTime,month);
    }

    @Override
    public void alertIndex(@RequestParam("startTime") String startTime,
                           @RequestParam("endTime") String endTime,
                           @RequestParam("month") String month) {
        alertScheduleBiz.alertIndex(startTime,endTime,month);
    }

    @Override
    public void alertSum(@RequestParam("startTime") String startTime,
                         @RequestParam("endTime") String endTime,
                         @RequestParam("month") String month) {
        alertScheduleBiz.alertSum(startTime,endTime,month);
    }

    @Override
    public void synchronizeInstance() {
        alertScheduleBiz.synchronize();
    }

    @Override
    public void deviceInspectionByDay() {
        alertScheduleBiz.deviceInspectionByDay();
    }
}
