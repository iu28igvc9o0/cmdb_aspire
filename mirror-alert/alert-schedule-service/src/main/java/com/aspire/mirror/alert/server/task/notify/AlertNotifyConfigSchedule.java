package com.aspire.mirror.alert.server.task.notify;

import com.aspire.mirror.alert.server.biz.notify.AlertNotifyConfigBiz;
import com.aspire.mirror.alert.server.biz.notify.AlertSubscribeRulesBiz;
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
public class AlertNotifyConfigSchedule {

    @Autowired
    private AlertNotifyConfigBiz alertNotifyConfigBiz;
    @Autowired
    private AlertSubscribeRulesBiz alertSubscribeRulesBiz;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void sendAlertNotifyConfig () {
        log.info("------告警通知发送定时任务开启------");
        log.info("StartTime Of Sending Alert Notify Config is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(  )));
        alertNotifyConfigBiz.sendAlertNotifyConfigNew();
        log.info("EndTime Of Sending Alert Notify Config is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(  )));
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void reSendAlertNotifyConfig () {
        log.info("------告警通知发送定时任务开启------");
        log.info("StartTime Of Resending Alert Notify Config is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        alertNotifyConfigBiz.resendAlertNotifyConfigNew();
        log.info("StartTime Of Resending Alert Notify Config is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void reSendAlertSubscribeConfig () {
        log.info("------告警订阅发送定时任务开启------");
        log.info("StartTime Of Resending Alert Subscribe Config is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        alertNotifyConfigBiz.resendAlertNotifyConfig();
        log.info("StartTime Of Resending Alert Subscribe Config is {}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }


}
