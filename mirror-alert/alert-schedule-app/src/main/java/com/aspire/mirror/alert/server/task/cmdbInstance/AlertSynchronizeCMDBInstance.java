package com.aspire.mirror.alert.server.task.cmdbInstance;

import com.aspire.mirror.alert.server.biz.monthReport.AlertsScheduleBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

//@Component
//@EnableScheduling
@Slf4j
//@ConditionalOnProperty(value = "AlertSynchronizeCMDBInstance.flag", havingValue = "normal")
public class AlertSynchronizeCMDBInstance {

    @Autowired
    private AlertsScheduleBiz alertScheduleBiz;

//    @Scheduled(cron = "${AlertSynchronizeCMDBInstance.cron: 0 0 0 * * ?}")
    void synchronize(){
        log.info("------同步cmdb主机实例定时任务开启------");
        alertScheduleBiz.synchronize();
        log.info("------同步cmdb主机实例定时任务关闭------");
    }
}
