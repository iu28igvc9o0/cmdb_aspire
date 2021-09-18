package com.aspire.mirror.alert.server.task.leakScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@EnableScheduling
public class SecurityLeakScanShedule {
    @Autowired
    private SecurityLeakScanTask securityLeakScanTask;

    @Scheduled(cron = "${sls.cron: 0 */5 * * * ?}")
    public void execute () {
        securityLeakScanTask.scan(new Date());
    }
}
