package com.aspire.mirror.alert.server.task.monthReport;

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
@ConditionalOnProperty(value = "alertMonReport.flag", havingValue = "normal")
public class AlertMonthReportSchedule {

    @Autowired
    private AlertsScheduleBiz alertScheduleBiz;

    @Scheduled(cron = "${alertMonReportIdc.cron}")
    void alert(){
        log.info("------运营月报告警分布定时任务开启------");
        alertScheduleBiz.alert(null,null,null);
        log.info("------运营月报告警分布定时任务关闭------");
    }

    @Scheduled(cron = "${alertMonReportDevice.cron}")
    void device(){
        log.info("------运营月报告警设备定时任务开启------");
        alertScheduleBiz.device(null,null,null);
        log.info("------运营月报告警设备定时任务关闭------");
    }

    @Scheduled(cron = "${alertMonReportIndex.cron}")
    void alertIndex(){
        log.info("------运营月报告警指标定时任务开启------");
        alertScheduleBiz.alertIndex(null,null,null);
        log.info("------运营月报告警指标定时任务关闭------");
    }

    @Scheduled(cron = "${alertMonReportSum.cron}")
    void alertCount(){
        log.info("------运营月报告警数量定时任务开启------");
        alertScheduleBiz.alertSum(null,null,null);
        log.info("------运营月报告警数量定时任务关闭------");
    }
}
