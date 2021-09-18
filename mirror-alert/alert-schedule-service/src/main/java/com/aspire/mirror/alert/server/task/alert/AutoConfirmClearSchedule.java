package com.aspire.mirror.alert.server.task.alert;

import com.aspire.mirror.alert.server.biz.alert.AutoConfirmClearScheduleBiz;
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
public class AutoConfirmClearSchedule {

    @Autowired
    private AutoConfirmClearScheduleBiz autoConfirmClearScheduleBiz;

    @Scheduled(cron = "0 1/2 * * * ? ")
    public void autoConfirm() {
        log.info("------自动确认定时任务开启------");
        log.info("StartTime Of Auto Confirm is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        autoConfirmClearScheduleBiz.autoConfirm();
        log.info("------自动确认定时任务关闭------");
        log.info("EndTime Of Auto Confirm is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @Scheduled(cron = "0 0/2 * * * ? ")
    public void autoClear() {
        log.info("------自动清除定时任务开启------");
        log.info("StartTime Of Auto Clear is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        autoConfirmClearScheduleBiz.autoClear();
        log.info("------自动清除定时任务关闭------");
        log.info("EndTime Of Auto Clear is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @Scheduled(fixedDelay = 60000)
    public void delete() {
        log.info("------自动清除规则定时任务开启------");
        log.info("StartTime Of Auto Clear Rule is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        autoConfirmClearScheduleBiz.delete();
        log.info("------自动清除规则定时任务关闭------");
        log.info("EndTime Of Auto Clear Rule is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
