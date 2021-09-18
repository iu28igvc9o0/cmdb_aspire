package com.aspire.mirror.alert.server.task.inspectionDaily;

import com.aspire.mirror.alert.server.biz.monthReport.AlertsScheduleBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
@ConditionalOnProperty(value = "InspectionByDaySchedule.flag", havingValue = "normal")
public class InspectionByDaySchedule {

    @Autowired
    private AlertsScheduleBiz alertAutoOrderSchedule;

    /**
     * 每日设备巡检
     */
//    @Scheduled(fixedDelay = 60000)
    @Scheduled(cron = "${InspectionByDaySchedule.cron}")
    void deviceInspectionByDay () {
        alertAutoOrderSchedule.deviceInspectionByDay();
    }
    /**
     * 每日业务巡检
     */
    @Scheduled(cron = "${InspectionByDaySchedule.cron}")
    void bizSystemInspectionByDay () {
        alertAutoOrderSchedule.bizSystemInspectionByDay();
    }
}
