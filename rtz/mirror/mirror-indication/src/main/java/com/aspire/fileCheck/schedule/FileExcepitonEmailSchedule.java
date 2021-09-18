package com.aspire.fileCheck.schedule;

import com.aspire.fileCheck.service.IFileIndicationPeriodStateService;
import com.aspire.fileCheck.util.FileConst;
import com.aspire.fileCheck.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class FileExcepitonEmailSchedule extends BaseSchedule<FileExcepitonEmailSchedule> {

    @Autowired
    private IFileIndicationPeriodStateService iFileIndicationPeriodStateService;

    /**
     * 每天3/9/15/21点汇报文件缺失情况
     */
    @Scheduled(cron = "0 0 3,9,15,21 * * ?")
    private void checkPart() {
        updateJobManager("SEND_EXCEPTION_FILE_MAIL_3_HOUR", FileConst.JOB_LATEST_RUNNING_1, FileConst.JOB_STATUS_RUNNING);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, -FileConst.PERIOD_LAZY_HOUR);
        calendar.add(Calendar.HOUR, -FileConst.CALC_LAZY_HOUR);
        String checkDate = DateFormatUtils.format(calendar, TimeUtil.DATE_FORMAT_YYYYMMDD);
        log.info("文件缺失和完整度检测邮件发送开始:" + DateFormatUtils.format(calendar, TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS));
        iFileIndicationPeriodStateService.checkFullEmail("网关", checkDate, false);
        iFileIndicationPeriodStateService.checkFullEmail("机顶盒", checkDate, false);
        log.info("文件缺失和完整度检测邮件发送结束:" + DateFormatUtils.format(calendar, TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS));
        updateJobManager("SEND_EXCEPTION_FILE_MAIL_3_HOUR", FileConst.JOB_LATEST_RUNNING_1, FileConst.JOB_STATUS_FINISH);
    }

    /**
     * 每日凌晨3点 汇总上一日的缺失情况
     */
    @Scheduled(cron = "0 0 3 * * ?")
    private void checkFull() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        String checkDate = DateFormatUtils.format(calendar.getTime(), TimeUtil.DATE_FORMAT_YYYYMMDD);
        updateJobManager("SEND_EXCEPTION_FILE_MAIL", FileConst.JOB_LATEST_RUNNING_1, FileConst.JOB_STATUS_RUNNING);
        log.info("汇总{}文件缺失情况开始...", checkDate);
        iFileIndicationPeriodStateService.checkFullEmail("网关", checkDate, true);
        iFileIndicationPeriodStateService.checkFullEmail("机顶盒", checkDate, true);
        updateJobManager("SEND_EXCEPTION_FILE_MAIL", FileConst.JOB_LATEST_RUNNING_1, FileConst.JOB_STATUS_FINISH);
        log.info("汇总{}文件缺失情况结束.", checkDate);
    }

}
