package com.aspire.mirror.alert.server.task.isolate;

import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.dao.isolate.AlertIsolateMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@EnableScheduling
@Component
@Slf4j
public class AlertAsolateReleaseTask {
    @Autowired
    private AlertIsolateMapper alertIsolateMapper;

    @Scheduled(cron="${schedule.release.cron:0 1 0/1 * * ?}")
    public void AutoRelease () {
        List<String> idList = alertIsolateMapper.listAutoRelease();
        if (CollectionUtils.isEmpty(idList)) {
            return;
        }
        log.info("auto release isolate ids is : {}", idList.toString());

        String[] ids =new String[idList.size()];
        alertIsolateMapper.status(Constants.ISOLATE_STATUS_STOP, idList.toArray(ids));
    }
}
