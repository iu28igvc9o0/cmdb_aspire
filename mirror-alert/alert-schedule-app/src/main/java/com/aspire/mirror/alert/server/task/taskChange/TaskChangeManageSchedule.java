package com.aspire.mirror.alert.server.task.taskChange;

import com.aspire.mirror.alert.server.biz.taskChange.TaskChangeManageBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
//@ConditionalOnProperty(value = "AlertSynchronizeCMDBInstance.flag", havingValue = "normal")
public class TaskChangeManageSchedule {

    @Autowired
    private TaskChangeManageBiz taskChangeManageBiz;

    @Scheduled(fixedDelay = 300000)
    void autoStopTask(){
        log.info("------计划变更管理自动结束定时任务开启------");
        taskChangeManageBiz.autoStopTask();
        log.info("------计划变更管理自动结束定时任务关闭------");
    }
}
