package com.aspire.mirror.alert.server.task.bpm;

import com.aspire.mirror.alert.server.dao.bpm.AlertOrderHandleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.schedule
 * @Author: baiwenping
 * @CreateTime: 2020-07-14 11:13
 * @Description: ${Description}
 */
@Component
@Slf4j
public class AlertOrderHandleSchedule {

    @Autowired
    private AlertOrderHandleMapper alertOrderHandleMapper;

    @Scheduled(cron = "0 10 1 * * ?")
    void run() {
        log.info("------告警自动清除未关联的工单处理记录开始------");
        alertOrderHandleMapper.deleteNotExist();
        log.info("------告警自动清除未关联的工单处理记录结束------");
    }
}
