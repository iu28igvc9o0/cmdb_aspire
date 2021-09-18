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
public class FileMissingAndIntegrityCheckSchedule extends BaseSchedule<FileMissingAndIntegrityCheckSchedule> {

	 @Autowired
	 private IFileIndicationPeriodStateService iFileIndicationPeriodStateService;

    /**
     * 每15分钟检测一次
     */
	 @Scheduled(cron = "0 0/1 * * * ?")
	 private void check() {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
		log.info("文件缺失和完整度检测开始:"+DateFormatUtils.format(calendar, TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS));
		 updateJobManager("CHECK_FILE_MISSING_INTEGRITY", FileConst.JOB_LATEST_RUNNING_1, FileConst.JOB_STATUS_RUNNING);
		iFileIndicationPeriodStateService.check(calendar);
		 updateJobManager("CHECK_FILE_MISSING_INTEGRITY", FileConst.JOB_LATEST_RUNNING_1, FileConst.JOB_STATUS_FINISH);
		log.info("文件缺失和完整度检测结束:"+DateFormatUtils.format(calendar, TimeUtil.DATE_FORMAT_YYYYMMDDHHMMSS));
	 }
}
