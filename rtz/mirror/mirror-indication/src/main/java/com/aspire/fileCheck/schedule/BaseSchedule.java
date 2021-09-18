package com.aspire.fileCheck.schedule;

import com.aspire.fileCheck.entity.FileIndicationJobManagerEntity;
import com.aspire.fileCheck.service.IFileIndicationJobManagerService;
import com.aspire.fileCheck.util.FileConst;
import com.aspire.fileCheck.util.TimeUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class BaseSchedule<T> {
    @Autowired
    private IFileIndicationJobManagerService jobManagerService;

    public void insertJobManager(String jobName) {
        Date date = new Date();
        String calcDate = TimeUtil.getCalcData(date);
        String period = TimeUtil.getCalcPeriod(date);
        FileIndicationJobManagerEntity entity = new FileIndicationJobManagerEntity();
        entity.setJobName(jobName);
        entity.setRunningDate(calcDate);
        entity.setRunningPeriod(period);
        entity.setLatestRunning(FileConst.JOB_LATEST_RUNNING_1);
        entity.setJobStatus(FileConst.JOB_STATUS_RUNNING);
        entity.setJobClass(getClass().getName());
        entity.setCreateTime(new Date());
        jobManagerService.insertEntity(entity);
    }

    public void updateJobManager(String jobName, Integer latestRunning, String runningStatus) {
        FileIndicationJobManagerEntity latestJobEntity = jobManagerService.getLatestJobManagerByJobName(jobName);
        if (latestJobEntity != null) {
            latestJobEntity.setLatestRunning(latestRunning);
            latestJobEntity.setJobStatus(runningStatus);
            Date date = new Date();
            String calcDate = TimeUtil.getCalcData(date);
            String period = TimeUtil.getCalcPeriod(date);
            latestJobEntity.setRunningDate(calcDate);
            latestJobEntity.setRunningPeriod(period);
            latestJobEntity.setModifyTime(new Date());
            jobManagerService.updateEntity(latestJobEntity);
        } else {
            insertJobManager(jobName);
        }
    }
}
