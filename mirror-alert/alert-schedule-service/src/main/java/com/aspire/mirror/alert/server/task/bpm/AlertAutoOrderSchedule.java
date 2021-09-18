package com.aspire.mirror.alert.server.task.bpm;

import com.aspire.mirror.alert.server.biz.bpm.AlertsAutoOrderConfigBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
@ConditionalOnProperty(value = "alertAutoOrder.flag", havingValue = "normal")
public class AlertAutoOrderSchedule {

    @Autowired
    private AlertsAutoOrderConfigBiz biz;

    @Scheduled(fixedDelay = 60000)
    void alertAutoOrderSchedule () {
        log.info("------告警自动派单定时任务开启------");
        biz.alertAutoOrderSchedule();
        log.info("------告警自动派单定时任务关闭------");
    }

}
