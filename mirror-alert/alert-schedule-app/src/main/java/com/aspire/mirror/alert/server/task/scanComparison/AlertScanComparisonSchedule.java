package com.aspire.mirror.alert.server.task.scanComparison;

import com.aspire.mirror.alert.server.biz.scanComparision.AlertScanComparisionBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableScheduling
@Slf4j
public class AlertScanComparisonSchedule {

    @Autowired
    private AlertScanComparisionBiz alertScanComparisionBiz;

    @Scheduled(cron = "0 1 */1 * * ? ")
    public void init () {
        log.info("------告警扫描对账定时任务开启------");
        log.info("StartTime Of Creating Sys Log Alert is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(  )));
        alertScanComparisionBiz.alertScanComparisonSchedule();
        log.info("------告警扫描对账定时任务关闭------");
        log.info("EndTime Of Creating Sys Log Alert is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(  )));
    }

//    @Scheduled(cron = "0 2 6 * * ? ")
    @Scheduled(cron = "0 0 */1 * * ? ")
    public void compareCmdb () {
        alertScanComparisionBiz.compareCmdb();
    }
}
